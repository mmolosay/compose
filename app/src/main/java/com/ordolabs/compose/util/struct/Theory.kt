package com.ordolabs.compose.util.struct

enum class Note {
    C,
    C_SHARP, D_FLAT,
    D,
    D_SHARP, E_FLAT,
    E,
    F,
    F_SHARP, G_FLAT,
    G,
    G_SHARP, A_FLAT,
    A,
    A_SHARP, B_FLAT,
    B
}

// TODO: should be not notes, but degrees (?)
enum class Mode(generative: Note) {
    IONIAN(Note.C), // Major
    DORIAN(Note.D),
    PHRYGIAN(Note.E),
    LYDIAN(Note.F),
    MIXOLYDIAN(Note.G),
    AEOLIAN(Note.A), // Minor
    LOCRIAN(Note.B)
}

data class Scale(
    val key: Note,
    val mode: Mode,
    val degrees: Array<ScaleDegree>
)

data class ScaleDegree(
    val degree: Int,
    val note: Note
)

object Theory {

    object ChromaticScale {

        val notes = arrayOf(
            Note.C, Note.C_SHARP, Note.D, Note.D_SHARP,
            Note.E, Note.F, Note.F_SHARP, Note.G,
            Note.G_SHARP, Note.A, Note.A_SHARP, Note.B
        )
        val whites = arrayOf(
            Note.C, Note.D, Note.E, Note.F, Note.G, Note.A, Note.B
        )
        val blacks = arrayOf(
            Note.C_SHARP, Note.D_SHARP, Note.F_SHARP, Note.G_SHARP, Note.A_SHARP
        )
    }
}