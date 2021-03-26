package com.ordolabs.compose.ui.adapter.pager.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class BasePagerAdapter(fm: FragmentManager, lc: Lifecycle) :
    FragmentStateAdapter(fm, lc) {

    protected inline fun <reified E : TabsEnum> makeEnumTabFragment(position: Int): Fragment {
        return E::class.java.enumConstants[position].newFragment()
    }
}