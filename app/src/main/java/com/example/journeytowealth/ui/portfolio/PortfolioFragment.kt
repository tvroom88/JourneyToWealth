package com.example.journeytowealth.ui.portfolio

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.databinding.FragmentPortfolioBinding
import com.example.journeytowealth.ui.main.MainViewModel
import com.example.journeytowealth.ui.state.PortfolioUi
import com.example.journeytowealth.ui.state.UiState
import com.example.journeytowealth.ui.stock.MarketIndexAdapter
import kotlin.getValue

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
                        // 로딩 표시
                        Log.d("DB_DATA", "Loading...")
                        mainViewModel.showLoading("DB에서 데이터를 가져오는 중입니다.")
                    }

                    is UiState.Success -> {
                        val dbData = state.data

                        val accountTypes = dbData.portfolios.filterIsInstance<PortfolioUi.AccountType>()

                        // 2. 이름들 확인---------------------------
                        // dbData가 생성된 곳에서 아래 코드를 실행하세요.
                        val portfolios = dbData.portfolios

                        Log.d("CheckCheck", "================ [포트폴리오 구조 분석 시작] ================")
                        Log.d("CheckCheck", "전체 UI 아이템 개수: ${portfolios.size}개")

// 1. 어떤 계좌 타입들이 들어있는지 확인
                        val allAccountTypes = portfolios.filterIsInstance<PortfolioUi.AccountType>()
                        Log.d("CheckCheck", "▶ 발견된 계좌 종류 (${allAccountTypes.size}개): ${allAccountTypes.map { it.name }}")

// 2. 각 섹션별 데이터 구성 확인 (순서대로 출력)
                        var currentAccountName = "알 수 없음"
                        var itemCount = 0

                        for (uiModel in portfolios) {
                            when (uiModel) {
                                is PortfolioUi.AccountType -> {
                                    // 새로운 계좌 섹션 시작
                                    Log.d("CheckCheck", "------------------------------------------")
                                    Log.d("CheckCheck", "● 계좌명 발견: [${uiModel.name}]")
                                    currentAccountName = uiModel.name
                                    itemCount = 0 // 아이템 개수 초기화
                                }
                                is PortfolioUi.Header -> {
                                    Log.d("CheckCheck", "   └ [헤더] 추가됨")
                                }
                                is PortfolioUi.Item -> {
                                    itemCount++
                                    // 아이템이 너무 많으면 로그가 지저분하므로 개수만 세거나 핵심만 출력
                                     Log.d("CheckCheck", "   └ [아이템 $itemCount] ${uiModel.data.etfName}")
                                }
                            }
                        }

                        Log.d("CheckCheck", "================ [포트폴리오 구조 분석 종료] ================")
                        // 2. 이름들 확인---------------------------

                        portfolioAdapter.submitList(dbData.portfolios)
                        mainViewModel.hideLoading()
                    }

                    is UiState.Error -> {
                        Log.e("DB_DATA", "Error: ${state.message}")
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
