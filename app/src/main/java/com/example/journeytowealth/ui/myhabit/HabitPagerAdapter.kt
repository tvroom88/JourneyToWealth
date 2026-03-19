package com.example.journeytowealth.ui.myhabit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HabitPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 7 // 일주일

    override fun createFragment(position: Int): Fragment {
//        return HabitListFragment.newInstance(position)
    }
}