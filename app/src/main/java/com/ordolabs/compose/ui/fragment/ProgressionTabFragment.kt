package com.ordolabs.compose.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ordolabs.compose.databinding.FragmentProgressionTabBinding
import com.ordolabs.compose.ui.fragment.base.BaseFragment

class ProgressionTabFragment : BaseFragment<FragmentProgressionTabBinding>() {

    override fun setUp() {

    }

    override fun setViews() {

    }

    override fun getViewBinding(i: LayoutInflater, c: ViewGroup?): FragmentProgressionTabBinding {
        return FragmentProgressionTabBinding.inflate(i, c, false)
    }

    companion object {
        fun new(): ProgressionTabFragment {
            return ProgressionTabFragment()
        }
    }
}