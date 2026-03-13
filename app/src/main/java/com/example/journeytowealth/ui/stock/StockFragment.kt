package com.example.journeytowealth.ui.stock

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journeytowealth.core.base.main.BaseMainFragment
import com.example.journeytowealth.databinding.FragmentStockBinding
import com.example.journeytowealth.ui.main.MainViewModel
import com.example.journeytowealth.ui.state.UiState

class StockFragment : BaseMainFragment<FragmentStockBinding>(FragmentStockBinding::inflate) {

    private val mainRepository by lazy { createRepository() }
    private val marketIndexAdapter by lazy { MarketIndexAdapter() }
    private val stockAdapter by lazy { StockAdapter() }
    private val mainViewModel: MainViewModel by activityViewModels()

    // 현재 어떤 탭이 선택되어 있는지 상태 저장
    private var isCurrentIndexTab = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()

        // 초기 UI 상태 설정 (지수 탭 활성화)
        binding.root.post {
            updateTabUI(isIndexSelected = true, animate = false)
        }
    }

    private fun setupRecyclerView() {
        // 리사이클러뷰 공통 설정
        val touchListener = createTouchListener()

        binding.recyclerIndex.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = marketIndexAdapter
            addOnItemTouchListener(touchListener)
        }

        binding.recyclerStock.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = stockAdapter
            addOnItemTouchListener(touchListener)
        }

        // 탭 클릭 리스너 (상태가 다를 때만 작동하도록 최적화)
        binding.btnIndex.setOnClickListener {
            if (!isCurrentIndexTab) updateTabUI(isIndexSelected = true)
        }
        binding.btnStock.setOnClickListener {
            if (isCurrentIndexTab) updateTabUI(isIndexSelected = false)
        }
    }

    private fun observeData() {
        lifecycleScope.launchWhenStarted {
            mainRepository.observeDb().collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        mainViewModel.showLoading("데이터를 가져오는 중입니다.")
                    }

                    is UiState.Success -> {
                        marketIndexAdapter.submitList(state.data.indexes)
                        stockAdapter.submitList(state.data.stocks)
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

    /**
     * 핵심: 탭 전환 및 애니메이션 통합 관리
     */
    private fun updateTabUI(isIndexSelected: Boolean, animate: Boolean = true) {
        if (isCurrentIndexTab == isIndexSelected && animate) return
        isCurrentIndexTab = isIndexSelected

        val width = binding.root.width.toFloat()

        if (animate) {
            // 1. 상단 탭 배경 애니메이션: 버튼이 가는 방향 그대로!
            val targetX = if (isIndexSelected) 0f else binding.btnStock.x
            binding.viewSelector.animate()
                .translationX(targetX)
                .setDuration(1000)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()

            // 2. 하단 컨테이너 슬라이드: 탭이 가는 방향과 일치시킴
            if (isIndexSelected) {
                // [왼쪽(지수) 클릭] 탭이 왼쪽으로 이동 -> 본문도 왼쪽으로 이동하며 등장
                // 새로 나타날 지수 컨테이너: 왼쪽(-width)에서 중앙(0)으로
                animateSlide(binding.indexContainer, startX = -width / 4, endX = 0f, show = true)
                // 사라질 종목 컨테이너: 중앙(0)에서 왼쪽(-width)으로 퇴장
                animateSlide(binding.stockContainer, startX = 0f, endX = -width / 4, show = false)
            } else {
                // [오른쪽(종목) 클릭] 탭이 오른쪽으로 이동 -> 본문도 오른쪽으로 이동하며 등장
                // 새로 나타날 종목 컨테이너: 오른쪽(width)에서 중앙(0)으로
                animateSlide(binding.stockContainer, startX = width / 4, endX = 0f, show = true)
                // 사라질 지수 컨테이너: 중앙(0)에서 오른쪽(width)으로 퇴장
                animateSlide(binding.indexContainer, startX = 0f, endX = width / 4, show = false)
            }
        } else {
            // 초기 설정 (애니메이션 없음)
            binding.viewSelector.translationX = if (isIndexSelected) 0f else binding.btnStock.x
            binding.indexContainer.visibility = if (isIndexSelected) View.VISIBLE else View.GONE
            binding.stockContainer.visibility = if (isIndexSelected) View.GONE else View.VISIBLE
        }

        // 3. 텍스트 및 헤더 업데이트
        binding.apply {
            btnIndex.apply {
                setTextColor(if (isIndexSelected) Color.BLACK else Color.GRAY)
                setTypeface(null, if (isIndexSelected) Typeface.BOLD else Typeface.NORMAL)
            }
            btnStock.apply {
                setTextColor(if (isIndexSelected) Color.GRAY else Color.BLACK)
                setTypeface(null, if (isIndexSelected) Typeface.NORMAL else Typeface.BOLD)
            }
            indexHeader.tvTitleStock.text = "지수"
            stockHeader.tvTitleStock.text = "종목"
        }
    }

    private fun animateSlide(view: View, startX: Float, endX: Float, show: Boolean) {
        if (show) {
            view.visibility = View.VISIBLE
            view.translationX = startX
            view.alpha = 0f
            view.animate()
                .translationX(endX)
                .alpha(1f)
                .setDuration(1000)
                .setInterpolator(DecelerateInterpolator())
                .start()
        } else {
            view.animate()
                .translationX(endX)
                .alpha(0f)
                .setDuration(1000)
                .withEndAction {
                    view.visibility = View.GONE
                    view.translationX = 0f // 다음 사용을 위해 위치 리셋
                }
                .start()
        }
    }

    private fun createTouchListener() = object : RecyclerView.OnItemTouchListener {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return checkMenuAndIntercept(e)
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallow: Boolean) {}
    }
}
