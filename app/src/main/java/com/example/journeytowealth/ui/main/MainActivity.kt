package com.example.journeytowealth.ui.main

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.journeytowealth.R
import com.example.journeytowealth.core.base.BaseActivity
import com.example.journeytowealth.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host)
                    as NavHostFragment

        val navController = navHostFragment.navController

        binding.bottomNavigationMain
            .setupWithNavController(navController)
    }
}