package com.example.journeytowealth.ui.myhabit.mind

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.databinding.FragmentTheMindHabitBinding
import com.example.journeytowealth.ui.myhabit.HabitItem

/**
 * 1. Habit 내용 그 자체 
 * 2. Habit을 매일 했는지 체크 하는 부분 (여기에는 배열로 넣기???)
 */
class TheMindHabitFragment :
    BaseFragment<FragmentTheMindHabitBinding>(FragmentTheMindHabitBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val habitList = mutableListOf(
            HabitItem(1, "목표를 소리 내어 하루 3번씩 말하기"),
            HabitItem(2, "손으로 하루 1번씩 100일간 쓰기"),
            HabitItem(3, "상상으로 시각화하기"),
            HabitItem(4, "감사하기"),
            HabitItem(5, "노트에 목표를 적고 취침 전후로 읽어보기"),
            HabitItem(6, "10번씩 90일간 말하기"),
            HabitItem(7, "선불 감사 하기"),
            HabitItem(8, "셀프 하이파이브 하기"),
            HabitItem(9, "작은 끌어당김 경험하기"),
            HabitItem(10, "아침에 일어나서 이불 정리하기")
        )

        val adapter = TheMindHabitAdapter(habitList)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = TheMindHabitFragment()
    }
}