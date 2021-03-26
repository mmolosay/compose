package com.ordolabs.compose.ui.adapter.pager.base

import androidx.fragment.app.Fragment

interface TabsEnum {
    val titleRes: Int
    fun newFragment(): Fragment
}