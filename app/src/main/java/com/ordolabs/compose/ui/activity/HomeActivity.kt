package com.ordolabs.compose.ui.activity

import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ordolabs.compose.R
import com.ordolabs.compose.databinding.ActivityHomeBinding
import com.ordolabs.compose.ui.activity.base.BaseActivity
import com.ordolabs.compose.ui.adapter.pager.HomePagerAdapter
import com.ordolabs.compose.ui.adapter.pager.base.TabsEnum
import com.ordolabs.compose.ui.fragment.ChordTabFragment
import com.ordolabs.compose.ui.fragment.ProgressionTabFragment
import com.ordolabs.compose.util.getFrom

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun setUp() {

    }

    override fun setViews() {
        setPager()
    }

    private fun setPager() {
        b.homePager.adapter = HomePagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(b.homeTabs, b.homePager, ::configureTab).attach()
    }

    private fun configureTab(tab: TabLayout.Tab, pos: Int) {
        tab.setText(getFrom<Tabs>(pos).titleRes)
        val ripple = ContextCompat.getDrawable(this, R.drawable.ripple_rounded_dark)
        tab.view.background = ripple
    }

    override fun getViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    enum class Tabs : TabsEnum {
        CHORD {
            override val titleRes = R.string.home_chord_tab
            override fun newFragment() = ChordTabFragment.new()
        },
        PROGRESSION {
            override val titleRes = R.string.home_progression_tab
            override fun newFragment() = ProgressionTabFragment.new()
        };
    }
}