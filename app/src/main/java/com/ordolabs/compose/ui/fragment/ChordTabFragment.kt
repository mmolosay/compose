package com.ordolabs.compose.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.ordolabs.compose.databinding.FragmentChordTabBinding
import com.ordolabs.compose.model.ui.ChordNoteItem
import com.ordolabs.compose.ui.adapter.recycler.ChordNotesAdapter
import com.ordolabs.compose.ui.adapter.recycler.base.OnRecyclerItemClicksListener
import com.ordolabs.compose.ui.fragment.base.BaseFragment
import com.ordolabs.compose.ui.view.PianoKeyboardView
import com.ordolabs.compose.util.struct.Note
import com.ordolabs.compose.viewmodel.ChordViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChordTabFragment : BaseFragment<FragmentChordTabBinding>(),
    PianoKeyboardView.OnKeyboardNoteTouchListener,
    OnRecyclerItemClicksListener<ChordNoteItem> {

    private val chordVM: ChordViewModel by viewModel()

    private val notesAdapter = ChordNotesAdapter(this)

    override fun setUp() {

    }

    override fun setViews() {
        setKeyboard()
        setNotesRecycler()
    }

    private fun setKeyboard() {
        b.chordKeyboard.listener = this
    }

    private fun setNotesRecycler() {
        val lm = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP)
        b.chordNotes.layoutManager = lm
        b.chordNotes.adapter = notesAdapter
    }

    override fun onKeboardKeyClick(note: Note): Boolean {
        val item = chordVM.makeChordNoteItem(note)
        return notesAdapter.addOrRemoveNote(item)
    }

    override fun onRecyclerItemClick(position: Int, item: ChordNoteItem?) {
        item ?: return
        b.chordKeyboard.unselectNote(item.note)
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