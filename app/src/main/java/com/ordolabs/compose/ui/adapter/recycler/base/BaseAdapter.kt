package com.ordolabs.compose.ui.adapter.recycler.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * An abstract implementation of [RecyclerView.Adapter].
 */
abstract class BaseAdapter<T : Any, VH : BaseViewHolder<T>>(
    var clicksListener: OnRecyclerItemClicksListener<T>
) : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val root = createItemView(parent, viewType)
        val holder = createViewHolder(root)
        setViewHolder(holder, viewType)
        return holder
    }

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
    }

    /**
     * Should be called to set data into `Adapter`.
     */
    abstract fun setItems(items: List<T>)

    /**
     * Specifies way of creating instances of [VH].
     */
    protected abstract fun createViewHolder(itemView: View): VH

    /**
     * Specifies layout ID res, according to [viewType].
     */
    @LayoutRes
    protected abstract fun getItemViewLayout(viewType: Int): Int

    /**
     * Specifies way of obtaining item [T] at [position].
     */
    protected abstract fun getItemAt(position: Int): T?

    /**
     * Configurates specified [holder] and its view.
     */
    @CallSuper
    protected open fun setViewHolder(holder: VH, viewType: Int) {
        holder.itemView.setOnClickListener { performOnItemViewClick(holder) }
        holder.itemView.setOnLongClickListener { performOnItemViewLongClick(holder) }
    }

    /**
     * Would be called on [VH]'s view click.
     */
    private fun performOnItemViewClick(holder: VH) {
        val position = holder.bindingAdapterPosition
        val item = getItemAt(position)
        onItemViewClick(holder)
        holder.onClick(holder.itemView)
        clicksListener.onRecyclerItemClick(position, item)
    }

    /**
     * Would be called on [VH]'s view long click.
     */
    private fun performOnItemViewLongClick(holder: VH): Boolean {
        val position = holder.bindingAdapterPosition
        val item = getItemAt(position)
        onItemViewLongClick(holder)
        val consumed = holder.onLongClick(holder.itemView)
        clicksListener.onRecyclerItemLongClick(position, item)
        return consumed
    }

    open fun onItemViewClick(holder: VH) {
        // default empty implementation
    }

    open fun onItemViewLongClick(holder: VH) {
        // default empty implementation
    }

    /**
     * Inflates [getItemViewLayout] view.
     */
    protected open fun createItemView(parent: ViewGroup, viewType: Int): View {
        val layout = getItemViewLayout(viewType)
        return LayoutInflater.from(parent.context).inflate(layout, parent, false)
    }

    /**
     * @return `Unit`, if specified [index] is in [getItemCount] range,
     * `null` otherwise.
     */
    protected fun ensureIndexInItemsRange(index: Int): Unit? {
        if (index in 0 until itemCount) return Unit
        return null
    }
}