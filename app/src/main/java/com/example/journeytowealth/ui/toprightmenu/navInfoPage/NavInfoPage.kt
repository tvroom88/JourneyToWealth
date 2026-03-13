package com.example.journeytowealth.ui.toprightmenu.navInfoPage

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.example.journeytowealth.R
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.databinding.FragmentNavInfoPageBinding


class NavInfoPage : BaseFragment<FragmentNavInfoPageBinding>(
    FragmentNavInfoPageBinding::inflate
),
    View.OnClickListener {
    // 버튼 제목 배열
    val buttonNames = arrayOf("거시경제지표", "화면2", "화면3", "화면4")

    val destinations = arrayOf(
        R.id.action_navPage_to_macro,
        R.id.action_navPage_to_macro,
        R.id.action_navPage_to_macro,
        R.id.action_navPage_to_macro

    )

    private val buttonIds = mutableListOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View가 다시 생성될 때마다 리스트를 초기화하고 새로 그려야 합니다.
        buttonIds.clear()
        setUi()
    }

    fun setUi() {
        val layout = binding.root
        var lastRowAnchorId = binding.tvTitleNav.id

        buttonNames.forEachIndexed { index, name ->
            val isLeft = index % 2 == 0
            val button = Button(requireContext()).apply {
                text = name
                id = View.generateViewId()
                setOnClickListener(this@NavInfoPage)

                // 디자인: 둥근 모서리 & 색상
                val shape = GradientDrawable().apply {
                    cornerRadius = 20f
                    setColor(if (isLeft) Color.parseColor("#6C63FF") else Color.parseColor("#FF6584"))
                }
                background = shape
                setTextColor(Color.WHITE)
                // 텍스트 정렬 중앙 고정 (미세한 위치 차이 방지)
                gravity = Gravity.CENTER
            }

            // 너비 0dp (MATCH_CONSTRAINT)
            val params = ConstraintLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT)

            if (isLeft) {
                // [왼쪽 버튼: 기준점 잡기]
                params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                params.topToBottom = lastRowAnchorId
                params.topMargin = 32 // 줄 간격
                params.marginStart = 40
                params.marginEnd = 12
                params.horizontalWeight = 1f

                // 너비가 너무 커지지 않게 제한 (선택 사항)
                params.matchConstraintMaxWidth = 400

            } else {
                // [오른쪽 버튼: 왼쪽 버튼에 완전히 고정]
                val leftButtonId = buttonIds[index - 1]

                params.startToEnd = leftButtonId
                params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

                // ★ 핵심: 왼쪽 버튼의 위/아래에 완전히 정렬 (수직 중앙 일치)
                params.topToTop = leftButtonId
                params.bottomToBottom = leftButtonId

                params.marginStart = 12
                params.marginEnd = 40
                params.horizontalWeight = 1f

                // 가로 체인 완성 (왼쪽 버튼의 end를 현재 버튼에 연결)
                val leftButton = layout.findViewById<Button>(leftButtonId)
                val leftParams = leftButton.layoutParams as ConstraintLayout.LayoutParams
                leftParams.endToStart = button.id
                leftButton.layoutParams = leftParams

                // 다음 줄을 위해 앵커 갱신
                lastRowAnchorId = leftButtonId
            }

            button.layoutParams = params
            layout.addView(button)
            buttonIds.add(button.id)
        }
    }

    override fun onClick(v: View?) {
        v ?: return
        val index = buttonIds.indexOf(v.id)
        if (index != -1) {
            // destination 이동
            findNavController().navigate(destinations[index])
        }
    }
}