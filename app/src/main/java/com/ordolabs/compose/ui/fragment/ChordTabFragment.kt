package com.ordolabs.compose.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.ordolabs.compose.databinding.FragmentChordTabBinding
import com.ordolabs.compose.ui.fragment.base.BaseFragment

class ChordTabFragment : BaseFragment<FragmentChordTabBinding>() {

    override fun setUp() {

    }

    override fun setViews() {

    }

    override fun getViewBinding(i: LayoutInflater, c: ViewGroup?): FragmentChordTabBinding {
        return FragmentChordTabBinding.inflate(i, c, false)
    }

    companion object {
        fun new(): ChordTabFragment {
            return ChordTabFragment()
        }
    }
}