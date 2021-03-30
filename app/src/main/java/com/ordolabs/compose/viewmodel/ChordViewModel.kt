package com.ordolabs.compose.viewmodel

import com.ordolabs.compose.model.ui.ChordNoteItem
import com.ordolabs.compose.util.struct.Note
import com.ordolabs.compose.viewmodel.base.BaseViewModel

class ChordViewModel : BaseViewModel() {

    fun makeChordNoteItem(note: Note): ChordNoteItem {
        return ChordNoteItem(note)
    }
}