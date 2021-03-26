package com.ordolabs.compose.ui.adapter.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.ordolabs.compose.ui.activity.HomeActivity
import com.ordolabs.compose.ui.adapter.pager.base.BasePagerAdapter

class HomePagerAdapter(fm: FragmentManager, lc: Lifecycle) : BasePagerAdapter(fm, lc) {

    override fun createFragment(pos: Int): Fragment {
        return makeEnumTabFragment<HomeActivity.Tabs>(pos)
    }

    override fun getItemCount(): Int {
        return enumValues<HomeActivity.Tabs>().size
    }
}