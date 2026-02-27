package com.example.journeytowealth.ui.main

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.journeytowealth.R
import com.example.journeytowealth.core.base.BaseActivity
import com.example.journeytowealth.databinding.ActivityMainBinding


/**
 * Todo:
 * 1. MainActivity :
 * 1) Toolbar :
 * 1-1) 추후 시간 될 경우 RecyclerView로 menu 추가하기 (Toolbar UI 개선)
 */
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    var currentToolbarStatus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // 1. Toolbar 부분 세팅
        setToolbarUiAndAction()

        // 2. BottomNavigation 세팅
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationMain.setupWithNavController(navController)
    }

    fun setToolbarUiAndAction() {
        binding.toolbarMain.ivMenuToolbar.setOnClickListener {// 메뉴 클릭시
            currentToolbarStatus = !currentToolbarStatus
            if (currentToolbarStatus) {
                binding.menu1Main.visibility = View.VISIBLE
            } else {
                binding.menu1Main.visibility = View.GONE
            }

        }
    }
}