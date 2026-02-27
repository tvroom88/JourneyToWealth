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
import com.example.journeytowealth.core.result.HttpResult
import com.example.journeytowealth.data.remote.ExcelRemoteDataSource
import com.example.journeytowealth.data.repository.MainRepository
import com.example.journeytowealth.databinding.ActivityMainBinding
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(MainRepository(ExcelRemoteDataSource()))
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

        setupToolbar()
        setupBottomNavigation()
        observeExcelData(this)
        setupGoogleLogin(this)
    }

    /** Toolbar UI & Action 설정 */
    private fun setupToolbar() {
        binding.toolbarMain.ivMenuToolbar.setOnClickListener {
            toggleToolbarMenu()
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
    }

    private fun toggleToolbarMenu() {
        currentToolbarStatus = !currentToolbarStatus
        binding.menu1Main.visibility = if (currentToolbarStatus) View.VISIBLE else View.GONE
    }

    /** BottomNavigation 세팅 */
    private fun setupBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        binding.bottomNavigationMain.setupWithNavController(navHostFragment.navController)
    }

    /** ViewModel 데이터 관찰 */
    private fun observeExcelData(context: Context) {
        lifecycleScope.launchWhenStarted {
            viewModel.excelData.collect { result ->
                when (result) {
                    is HttpResult.Success -> {
                        Log.d("MainActivity", "ExcelData loaded successfully")
                        // TODO: RecyclerView에 데이터 전달
                    }

                    is HttpResult.Error -> {
                        Toast.makeText(
                            context,
                            result.message ?: "Error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is HttpResult.Loading -> {
                        // TODO: ProgressBar 표시
                    }
                }
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
        binding.llLoadingContainerMain.visibility = if (show) View.VISIBLE else View.GONE
        binding.tvLoadingText.text = message
    }
}