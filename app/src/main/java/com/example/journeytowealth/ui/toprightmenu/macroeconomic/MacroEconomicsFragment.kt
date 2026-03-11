package com.example.journeytowealth.ui.toprightmenu.macroeconomic

import android.os.Bundle
import android.view.View
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.databinding.FragmentPortfolioBinding
import com.example.journeytowealth.ui.portfolio.PortfolioFragment

/**
 * FedWatch
 */
class MacroEconomicsFragment :
    BaseFragment<FragmentPortfolioBinding>(FragmentPortfolioBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = PortfolioFragment()
    }
}
