package com.ordolabs.compose.ui.adapter.recycler.base

interface OnRecyclerItemClicksListener {

    fun onRecyclerItemClick(position: Int) {
        // default empty implementation
    }

    fun onRecyclerItemLongClick(position: Int) {
        // default empty implementation
    }
}