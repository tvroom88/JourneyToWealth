package com.example.journeytowealth.ui.stock

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
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
import com.example.journeytowealth.ui.state.UiState

class StockFragment : BaseMainFragment<FragmentStockBinding>(FragmentStockBinding::inflate) {

    private val marketIndexAdapter by lazy { MarketIndexAdapter() }
    private val stockAdapter by lazy { StockAdapter() }
    private val mainViewModel: MainViewModel by activityViewModels()
    private val mainRepository by lazy { createRepository() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        observeData()
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = StockPagerAdapter()

        // 페이지 변경 콜백 (실시간 위치 동기화 핵심)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            // position: 현재 페이지 index
            // positionOffset: 현재 페이지에서 다음 페이지로 넘어간 비율 (0.0 ~ 1.0)
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
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

    private fun updateTabUI(isIndexSelected: Boolean) {
        // 탭 배경 이동 애니메이션
        val targetX = if (isIndexSelected) 0f else binding.btnStock.x
        binding.viewSelector.animate()
            .translationX(targetX)
            .setDuration(250)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        // 텍스트 스타일 변경
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
                if (state is UiState.Success) {
                    marketIndexAdapter.submitList(state.data.indexes)
                    stockAdapter.submitList(state.data.stocks)
                    mainViewModel.hideLoading()
                }
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
