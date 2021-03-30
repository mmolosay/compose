package com.ordolabs.compose.ui.adapter.recycler

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.ordolabs.compose.R
import com.ordolabs.compose.model.ui.ChordNoteItem
import com.ordolabs.compose.ui.adapter.recycler.base.BaseAdapter
import com.ordolabs.compose.ui.adapter.recycler.base.BaseViewHolder
import com.ordolabs.compose.ui.adapter.recycler.base.OnRecyclerItemClicksListener
import com.ordolabs.compose.util.viewId

class ChordNotesAdapter(
    l: OnRecyclerItemClicksListener<ChordNoteItem>
) : BaseAdapter<ChordNoteItem, ChordNotesAdapter.ChordNoteViewHolder>(l) {

    private val items = arrayListOf<ChordNoteItem>()

    fun addOrRemoveNote(item: ChordNoteItem): Boolean {
        return if (items.contains(item)) removeNote(item)
        else addNote(item)
    }

    private fun addNote(item: ChordNoteItem): Boolean {
        if (items.size == ITEMS_MAX) return false
        val position = items.size
        items.add(item)
        notifyItemInserted(position)
        return true
    }

    private fun removeNote(item: ChordNoteItem): Boolean {
        val position = items.indexOf(item).takeIf { it != -1 } ?: return false
        items.removeAt(position)
        notifyItemRemoved(position)
        return true
    }

    override fun setItems(items: List<ChordNoteItem>) {
        this.items.clear()
        this.items.addAll(items)
    }

    override fun onBindViewHolder(holder: ChordNoteViewHolder, position: Int) {
        val item = items[position]
        holder.onBind(item)
    }

    override fun onItemViewClick(holder: ChordNoteViewHolder) {
        val index = holder.bindingAdapterPosition.takeIf { it != -1 } ?: return
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun createViewHolder(itemView: View): ChordNoteViewHolder {
        return ChordNoteViewHolder(itemView)
    }

    override fun getItemViewLayout(viewType: Int): Int {
        return R.layout.item_chord_note
    }

    override fun getItemAt(position: Int): ChordNoteItem? {
        return items.getOrNull(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ChordNoteViewHolder(root: View) : BaseViewHolder<ChordNoteItem>(root) {

        private val note by root.viewId<AppCompatTextView>(R.id.chordNote)

        override fun populate(item: ChordNoteItem) {
            note.text = item.note.key.name
        }
    }

    companion object {
        private const val ITEMS_MAX = 6
    }
}