package com.ordolabs.compose.ui.adapter.recycler.base

interface OnRecyclerItemClicksListener<T : Any> {

    fun onRecyclerItemClick(position: Int, item: T?) {
        // default empty implementation
    }

    fun onRecyclerItemLongClick(position: Int, item: T?) {
        // default empty implementation
    }
}