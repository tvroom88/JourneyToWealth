package com.example.journeytowealth.core.base.main

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.journeytowealth.core.base.BaseFragment
import com.example.journeytowealth.ui.main.OnMainActivityUiControlListener

abstract class BaseMainFragment<VB : ViewBinding>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFragment<VB>(inflate) {

    protected var menuListener: OnMainActivityUiControlListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // MainActivity가 구현한 인터페이스를 여기서 공통으로 캐스팅
        if (context is OnMainActivityUiControlListener) {
            menuListener = context
        }
    }

    // 메뉴 닫기 로직을 공통 함수로 만들어둘 수 있습니다.
    protected fun checkMenuAndIntercept(e: MotionEvent): Boolean {
        if (menuListener?.isMenuOpen() == true && e.action == MotionEvent.ACTION_DOWN) {
            menuListener?.hideToolbarMenu()
            return true // 가로채기 성공
        }
        return false
    }
}