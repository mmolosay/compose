package com.ordolabs.compose.util.struct

data class Key(
    val name: String
) {

    fun simplify(): String {
        if (name.length == 1) return name
        val base = name.substring(0, 1)
        val sharps = name.count { it == Theory.SHARP }
        val flats = name.count { it == Theory.FLAT }
        val pitch = sharps - flats
        val baseIndex = Theory.ChromaticScale.keys.indexOfFirst { it.name == base }
        val resultIndex = baseIndex + pitch
        return Theory.ChromaticScale.getAt(resultIndex).name
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Key) return false
        return (other.simplify() == this.simplify())
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
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

enum class Mode {
    IONIAN, // Major
    DORIAN,
    PHRYGIAN,
    LYDIAN,
    MIXOLYDIAN,
    AEOLIAN, // Minor
    LOCRIAN
}

enum class ModeModern {
    MAJOR,
    MINOR
}

data class Note(
    val key: Key,
    val octave: Int
)

data class Scale(
    val key: Key,
    val mode: Mode,
    val degrees: Array<ScaleDegree>
)

data class ScaleDegree(
    val degree: Degree,
    val key: Key
)

object Theory {

    const val SHARP = '#'
    const val FLAT = 'b'

    const val OCTAVE_KEY_COUNT = 12

    object Whites {

        const val COUNT = 7

        val keys: Array<Key> get() = ChromaticScale.whites

        fun getAt(index: Int): Key {
            val octaveIndex = if (index < 0) keys.size + (index % COUNT)
            else index % COUNT
            return keys[octaveIndex]
        }

        fun indexOf(key: Key): Int? {
            return this.keys.indexOf(key).takeIf { it != -1 }
        }
    }

    object Blacks {

        const val COUNT = 5

        val keys: Array<Key> get() = ChromaticScale.blacks

        fun getAt(index: Int): Key {
            val octaveIndex = if (index < 0) keys.size + (index % COUNT)
            else index % COUNT
            return keys[octaveIndex]
        }

        fun indexOf(key: Key): Int? {
            return keys.indexOf(key).takeIf { it != -1 }
        }
    }

    object ChromaticScale {

        val keys = arrayOf(
            Key("C"),
            Key("C#"),
            Key("D"),
            Key("D#"),
            Key("E"),
            Key("F"),
            Key("F#"),
            Key("G"),
            Key("G#"),
            Key("A"),
            Key("A#"),
            Key("B")
        )
        val whites = arrayOf(
            Key("C"),
            Key("D"),
            Key("E"),
            Key("F"),
            Key("G"),
            Key("A"),
            Key("B")
        )
        val blacks = arrayOf(
            Key("C#"),
            Key("D#"),
            Key("F#"),
            Key("G#"),
            Key("A#")
        )

        fun getAt(index: Int): Key {
            val octaveIndex = if (index < 0) keys.size + (index % OCTAVE_KEY_COUNT)
            else index % OCTAVE_KEY_COUNT
            return keys[octaveIndex]
        }

        fun indexOf(key: Key): Int {
            return keys.indexOf(key)
        }

        fun byOrdinal(ordinal: Int, isWhite: Boolean): Key {
            return if (isWhite) Whites.getAt(ordinal) else Blacks.getAt(ordinal)
        }

        fun fromOrdinalToIndex(ordinal: Int, isWhite: Boolean): Int {
            val key = if (isWhite) Whites.getAt(ordinal) else Blacks.getAt(ordinal)
            return indexOf(key)
        }
    }
}