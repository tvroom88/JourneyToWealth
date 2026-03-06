package com.example.journeytowealth.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.journeytowealth.R
import com.example.journeytowealth.core.base.BaseActivity
import com.example.journeytowealth.data.local.MarketIndexLocalDataSource
import com.example.journeytowealth.data.local.StockLocalDataSource
import com.example.journeytowealth.data.local.database.AppDatabase
import com.example.journeytowealth.data.remote.ExcelRemoteDataSource
import com.example.journeytowealth.data.repository.MainRepository
import com.example.journeytowealth.databinding.ActivityMainBinding
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Design Pattern : MVVM Pattern
 * click ui(button) -> request data -> get data -> change ui
 *
 * TableView
 * https://github.com/evrencoskun/TableView
 *
 * Todo:
 * (1) 원격으로 가져오는 내용 정리 완료하기
 * (2) Room DB 추가하기
 * (3) Room DB에 대이터 넣기
 * (4) RecyclerView로 표처럼 보여주기
 */
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val mainRepository by lazy { createRepository() }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(mainRepository)
    }
    private var currentToolbarStatus = false

    // Google Sign-In
    private lateinit var googleSignInClient: GoogleSignInClient
    private var googleToken: String? = null
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleGoogleSignInResult(result.data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupToolbar() // toolbar 세팅
        setupMainFragmentContainer() // Fragment Container 부분 세팅
        setupBottomNavigation() // bottomNavigation 세팅
        observeData()
        setupGoogleLogin(this)
    }

    /** Toolbar UI & Action 설정 */
    private fun setupToolbar() {
        binding.toolbarMain.ivMenuToolbar.setOnClickListener {
            toggleToolbarMenu()
        }

        val tempAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (tempAccount != null) {
            binding.tvLoginToolbarMain.text = "Logout"
        }

        binding.tvLoginToolbarMain.setOnClickListener {
            // 로그인 버튼 클릭 처리
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account == null) {
                googleLogin()
            } else {
                googleLogout()
            }
        }

        binding.tvLoadingData.setOnClickListener {
            mainViewModel.loadExcel(googleToken)
            if (googleToken == null) {
                Log.d("MainActivity", "googleToken이 없습니다.")
            }
        }
    }

    private fun toggleToolbarMenu() {
        currentToolbarStatus = !currentToolbarStatus
        binding.menuMain.visibility = if (currentToolbarStatus) View.VISIBLE else View.GONE
    }

    /** MainFragment 설정 */
    private fun setupMainFragmentContainer() {
        binding.navHost.setOnClickListener {
            if (currentToolbarStatus) {
                toggleToolbarMenu()
            }
        }
    }

    /** BottomNavigation 세팅 */
    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        binding.bottomNavigationMain.setupWithNavController(navHostFragment.navController)
    }

    /** ViewModel 데이터 관찰 */
    private fun observeData() {

        // 로딩관련 내용을 관리한다.
        lifecycleScope.launch {
            mainViewModel.loading.collect { loadingData ->
                showLoading(loadingData.isLoading, loadingData.message)
            }
        }
    }

    /** Google 로그인 세팅 */
    private fun setupGoogleLogin(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope("https://www.googleapis.com/auth/drive.readonly"))
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)

        // 로그인이 되어있다면
        val account = GoogleSignIn.getLastSignedInAccount(this)
        account?.account?.let { googleAccount ->
            CoroutineScope(Dispatchers.IO).launch {
                val scope = "oauth2:https://www.googleapis.com/auth/drive.readonly"

                googleToken = GoogleAuthUtil.getToken(
                    context,
                    googleAccount,
                    scope
                )
            }
        }
    }

    private fun googleLogin() {
        showLoading(true, "구글 로그인 중...")
        launcher.launch(googleSignInClient.signInIntent)
    }

    private fun googleLogout() {
        showLoading(true, "구글 로그아웃 중...")
        googleSignInClient.signOut()
            .addOnCompleteListener { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    googleToken = null
                    changeGoogleButton(false)
                } else {
                    Toast.makeText(this, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun changeGoogleButton(isTryLogin: Boolean) {
        runOnUiThread {
            if (isTryLogin) {
                binding.tvLoginToolbarMain.text = "Logout"
            } else {
                binding.tvLoginToolbarMain.text = "Login"
            }
        }
    }

    /** Google Sign-In 결과 처리 */
    private fun handleGoogleSignInResult(data: android.content.Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val token = GoogleAuthUtil.getToken(
                            this@MainActivity,
                            account.account!!,
                            "oauth2:https://www.googleapis.com/auth/drive.readonly"
                        )
                        googleToken = token

                        // UI 업데이트는 main thread에서
                        withContext(Dispatchers.Main) {
                            showLoading(false)   // 로그인 완료 시 로딩 숨김
                            changeGoogleButton(true)
                            Log.d("MainActivity", "GoogleSignIn success: ${account.email}")
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            showLoading(false)   // 실패 시에도 로딩 숨김
                        }
                        Log.e("MainActivity", "Token error: ${e.message}")
                    }
                }
            } else {
                // 계정 null인 경우 로딩 숨기기
                showLoading(false)
            }
        } catch (e: ApiException) {
            showLoading(false)
            Log.d("MainActivity", "GoogleSignIn failed: ${e.message}")
        }
    }

    private fun showLoading(show: Boolean, message: String = "로딩중") {
        binding.flLoadingContainerMain.visibility = if (show) View.VISIBLE else View.GONE
        binding.tvLoadingText.text = message
    }

    private fun createRepository(): MainRepository {

        val db = AppDatabase.getInstance(applicationContext)
        return MainRepository(
            excelRemoteDataSource = ExcelRemoteDataSource(),
            stockLocalDataSource = StockLocalDataSource(db.stockDao()),
            marketIndexLocalDataSource =
                MarketIndexLocalDataSource(db.marketIndexDao())
        )
    }
}