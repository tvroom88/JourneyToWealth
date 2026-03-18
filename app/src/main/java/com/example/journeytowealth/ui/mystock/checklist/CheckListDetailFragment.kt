package com.example.journeytowealth.ui.mystock.checklist

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.databinding.FragmentCheckListDetailBinding

class CheckListDetailFragment :
    BaseFragment<FragmentCheckListDetailBinding>(FragmentCheckListDetailBinding::inflate) {

    // 1. 위임 프로퍼티를 사용하여 전달받은 인자 가져오기
    private val args: CheckListDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2. args에서 데이터 꺼내 쓰기
        val type = args.type

//        when(type == CHECKLISTTYPE.ADD)
        when(type){
            CHECKLISTTYPE.ADD ->{}
            CHECKLISTTYPE.MODIFY -> {}
            CHECKLISTTYPE.CONTENT -> {}
            else ->{}
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = CheckListDetailFragment()
    }
}
