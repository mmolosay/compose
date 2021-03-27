package com.ordolabs.compose.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.ordolabs.compose.databinding.FragmentChordTabBinding
import com.ordolabs.compose.ui.fragment.base.BaseFragment
import com.ordolabs.compose.ui.view.PianoKeyboardView
import com.ordolabs.compose.util.struct.Note

class ChordTabFragment : BaseFragment<FragmentChordTabBinding>(),
    PianoKeyboardView.OnKeyboardNoteTouchListener {

    override fun setUp() {

    }

    override fun setViews() {
        setKeyboard()
    }

    private fun setKeyboard() {
        b.chordKeyboard.listener = this
    }

    override fun onKeboardKeyTouched(note: Note) {
        Toast.makeText(context, note.name, Toast.LENGTH_SHORT).show()
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