package com.example.journeytowealth.ui.checklist

import android.os.Bundle
import android.view.View
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.databinding.FragmentCheckListBinding

class CheckListFragment : BaseFragment<FragmentCheckListBinding>(FragmentCheckListBinding::inflate)  {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = CheckListFragment()
    }
}
