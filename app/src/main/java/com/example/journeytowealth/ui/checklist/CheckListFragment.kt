package com.example.journeytowealth.ui.checklist

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.databinding.FragmentCheckListBinding
import com.example.journeytowealth.ui.checklist.CHECKLISTTYPE

/**
 * 체크리스트에는 추가 / 수정 / 보기 이렇게 3개로
 * 추가 부분 부터 추가예정
 */
class CheckListFragment :
    BaseFragment<FragmentCheckListBinding>(FragmentCheckListBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivAddChecklist.setOnClickListener {
            // 1. 액션과 데이터를 조합한 방향(direction) 생성
            val action = CheckListFragmentDirections
                .actionChecklistToDetail(type = CHECKLISTTYPE.ADD)

            // 2. NavController를 통해 이동
            findNavController().navigate(action)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = CheckListFragment()
    }
}

