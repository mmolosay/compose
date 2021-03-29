package com.ordolabs.compose.util.struct

enum class Note(val displayName: String) {
    C("C"),
    C_SHARP("C#"), D_FLAT("Cb"),
    D("D"),
    D_SHARP("D#"), E_FLAT("Db"),
    E("E"),
    F("F"),
    F_SHARP("F#"), G_FLAT("Fb"),
    G("G"),
    G_SHARP("G#"), A_FLAT("Gb"),
    A("A"),
    A_SHARP("A#"), B_FLAT("Ab"),
    B("B")
}

enum class Degree(val pos: Int) {
    TONIC(1),
    SUPERTONIC(2),
    MEDIANT(3),
    SUBDOMINANT(4),
    DOMINANT(5),
    SUBMEDIANT(6),
    LEADING(7)
}

// TODO: should be not notes, but degrees (?)
enum class Mode(val generative: Note) {
    IONIAN(Note.C), // Major
    DORIAN(Note.D),
    PHRYGIAN(Note.E),
    LYDIAN(Note.F),
    MIXOLYDIAN(Note.G),
    AEOLIAN(Note.A), // Minor
    LOCRIAN(Note.B)
}

enum class ModeModern(val generative: Note) {
    MAJOR(Note.C),
    MINOR(Note.A)
}

data class Scale(
    val key: Note,
    val mode: Mode,
    val degrees: Array<ScaleDegree>
)

data class ScaleDegree(
    val degree: Degree,
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