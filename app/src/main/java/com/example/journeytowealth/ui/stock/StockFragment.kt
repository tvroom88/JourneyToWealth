package com.example.journeytowealth.ui.stock

import android.os.Bundle
import android.view.View
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.databinding.FragmentStockBinding

class StockFragment :
    BaseFragment<FragmentStockBinding>(FragmentStockBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = StockFragment()
    }
}
