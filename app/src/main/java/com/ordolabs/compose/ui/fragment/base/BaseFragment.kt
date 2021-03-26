package com.ordolabs.compose.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    protected val b: B get() = _binding
    private lateinit var _binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseStartIntent()
        setUp()
        setViews()
    }

    /**
     * Parses `Intent`, that started this `Activity`.
     */
    protected open fun parseStartIntent() {
        // override me
    }

    /**
     * Configures non-view components.
     */
    abstract fun setUp()

    /**
     * Sets activity's views and configures them.
     */
    abstract fun setViews()

    /**
     * Specifies way of obtaining fragment's [ViewBinding]
     */
    abstract fun getViewBinding(i: LayoutInflater, c: ViewGroup?): B

    companion object {
        // extra keys and stuff
    }
}