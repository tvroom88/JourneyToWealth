package com.example.journeytowealth.ui.mystock.stock

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.journeytowealth.core.base.main.BaseMainFragment
import com.example.journeytowealth.databinding.FragmentStockBinding
import com.example.journeytowealth.databinding.LayoutStockPagerItemBinding
import com.example.journeytowealth.ui.main.MainViewModel
import com.example.journeytowealth.ui.mystock.state.UiState
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StockFragment : BaseMainFragment<FragmentStockBinding>(FragmentStockBinding::inflate) {

    private val marketIndexAdapter by lazy { MarketIndexAdapter() }
    private val stockAdapter by lazy { StockAdapter() }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val mainRepository by lazy { createRepository() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 새로고침 버튼 클릭 시
        binding.btnRefresh.setOnClickListener {
            // ViewModel에 데이터 갱신 요청 (예시)
            val account = GoogleSignIn.getLastSignedInAccount(mContext)

            account?.account?.let { googleAccount ->
                CoroutineScope(Dispatchers.IO).launch {
                    val scope = "oauth2:https://www.googleapis.com/auth/drive.readonly"

                    val googleToken = GoogleAuthUtil.getToken(
                        mContext,
                        googleAccount,
                        scope
                    )
                    mainViewModel.loadExcel(googleToken)
                }
            }


            // 클릭 시 햅틱 피드백(진동)을 주면 훨씬 고급스럽습니다.
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

        }
        setupViewPager()
        observeData()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = StockPagerAdapter()

        // 페이지 변경 콜백 (실시간 위치 동기화 핵심)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            // position: 현재 페이지 index
            // positionOffset: 현재 페이지에서 다음 페이지로 넘어간 비율 (0.0 ~ 1.0)
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                // 1. 이동할 거리 계산
                // 지수(0)에서 종목(1) 버튼까지의 거리
                val travelDistance = binding.btnStock.x - binding.btnIndex.x

                // 2. 실시간 좌표 계산
                // (현재 페이지 위치 * 전체 거리) + (움직인 비율 * 전체 거리)
                val translationX = (position * travelDistance) + (positionOffset * travelDistance)

                // 3. 탭 배경 위치 즉시 반영
                binding.viewSelector.translationX = translationX
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 페이지가 완전히 멈췄을 때 텍스트 스타일 최종 업데이트
                updateTextStyle(position == 0)
            }
        })

        // 탭 클릭 시 이동
        binding.btnIndex.setOnClickListener { binding.viewPager.currentItem = 0 }
        binding.btnStock.setOnClickListener { binding.viewPager.currentItem = 1 }
    }

    // 텍스트 스타일만 변경해주는 함수 (애니메이션 로직 제외)
    private fun updateTextStyle(isIndexSelected: Boolean) {
        binding.btnIndex.apply {
            setTextColor(if (isIndexSelected) Color.BLACK else Color.GRAY)
            setTypeface(null, if (isIndexSelected) Typeface.BOLD else Typeface.NORMAL)
        }
        binding.btnStock.apply {
            setTextColor(if (isIndexSelected) Color.GRAY else Color.BLACK)
            setTypeface(null, if (isIndexSelected) Typeface.NORMAL else Typeface.BOLD)
        }
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            mainRepository.observeDb().collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        setLoadingState(true)
                    }

                    is UiState.Success -> {
                        // ... 데이터 제출 로직 ...
                        marketIndexAdapter.submitList(state.data.indexes)
                        stockAdapter.submitList(state.data.stocks)
                        setLoadingState(false)
                    }

                    is UiState.Error -> {
                        setLoadingState(false)
                    }
                }
            }
        }
    }

    /**
     * 로딩 상태에 따른 Cool한 애니메이션 제어
     */
    private fun setLoadingState(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                // 아이콘을 숨기고 프로그레스바 표시
                ivRefresh.visibility = View.GONE
                pbLoading.visibility = View.VISIBLE
                // 버튼 자체에 부드러운 스케일 애니메이션 (움찔하는 효과)
                btnRefresh.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start()
            } else {
                ivRefresh.visibility = View.VISIBLE
                pbLoading.visibility = View.GONE

                // 버튼 복구 및 아이콘 회전 애니메이션 (성공 피드백)
                btnRefresh.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                ivRefresh.animate().rotationBy(360f).setDuration(500).start()
            }
        }
    }

    // ViewPager2를 위한 내부 어댑터
    inner class StockPagerAdapter : RecyclerView.Adapter<StockPagerAdapter.PagerViewHolder>() {

        // ViewBinding을 사용하는 ViewHolder
        inner class PagerViewHolder(val binding: LayoutStockPagerItemBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
            val binding = LayoutStockPagerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return PagerViewHolder(binding)
        }

        override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
            holder.binding.apply {
                // include된 레이아웃의 ID가 pager_header라면 아래와 같이 접근 가능합니다.
                pagerHeader.tvTitleStock.text = if (position == 0) "지수" else "종목"

                recyclerViewInner.layoutManager = LinearLayoutManager(mContext)
                recyclerViewInner.adapter = if (position == 0) marketIndexAdapter else stockAdapter
            }
        }

        override fun getItemCount(): Int = 2
    }
}
