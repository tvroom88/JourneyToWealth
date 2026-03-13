package com.example.journeytowealth.ui.portfolio

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.databinding.FragmentPortfolioBinding
import com.example.journeytowealth.ui.main.MainViewModel
import com.example.journeytowealth.ui.state.UiState

class PortfolioFragment :
    BaseFragment<FragmentPortfolioBinding>(FragmentPortfolioBinding::inflate) {

    private val mainRepository by lazy { createRepository() }
    private val portfolioAdapter by lazy { PortfolioAdapter() }
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerPortfolio.layoutManager = LinearLayoutManager(mContext)
        binding.recyclerPortfolio.adapter = portfolioAdapter

        lifecycleScope.launchWhenStarted {
            mainRepository.observeDb().collect { state ->
                when (state) {

                    is UiState.Loading -> {
                        mainViewModel.showLoading("DB에서 데이터를 가져오는 중입니다.")
                    }

                    is UiState.Success -> {
                        val dbData = state.data
                        portfolioAdapter.submitList(dbData.portfolios)
                        mainViewModel.hideLoading()
                    }

                    is UiState.Error -> {
                        mainViewModel.hideLoading()
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = PortfolioFragment()
    }
}
