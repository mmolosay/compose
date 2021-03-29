package com.ordolabs.compose.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.ColorUtils
import com.ordolabs.compose.R
import com.ordolabs.compose.util.struct.Note
import com.ordolabs.compose.util.struct.Theory

class PianoKeyboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var listener: OnKeyboardNoteTouchListener? = null

    private var octaveCount: Int = OCTAVE_COUNT_DEFAULT
    private var shouldCoerceInWidth: Boolean = false

    private val selected = arrayListOf<SelectedKey>()

    private val underneathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = KEYS_COLOR_UNDERNEATH
    }

    private val keysPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = KEYS_COLOR_UNDERNEATH
    }

    private var whitesColor = WHITE_KEYS_COLOR_DEFAULT
    private var blacksColor = BLACK_KEYS_COLOR_DEFAULT
    private var selectedColor = SELECTED_KEYS_COLOR_DEFAULT

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.PianoKeyboardView)
        parseWhiteKeysColorAttr(typed)
        parseBlackKeysColorAttr(typed)
        parseSelectedKeysColorAttr(typed)
        parseOctaveCountAttr(typed)
        parseShouldCoerceInWidthAttr(typed)
        typed.recycle()
    }

    private fun parseWhiteKeysColorAttr(typed: TypedArray) {
        whitesColor = typed.getColor(
            R.styleable.PianoKeyboardView_whiteKeysColor,
            WHITE_KEYS_COLOR_DEFAULT
        )
    }

    private fun parseBlackKeysColorAttr(typed: TypedArray) {
        blacksColor = typed.getColor(
            R.styleable.PianoKeyboardView_blackKeysColor,
            BLACK_KEYS_COLOR_DEFAULT
        )
    }

    private fun parseSelectedKeysColorAttr(typed: TypedArray) {
        selectedColor = typed.getColor(
            R.styleable.PianoKeyboardView_selectedKeysColor,
            SELECTED_KEYS_COLOR_DEFAULT
        )
    }

    private fun parseOctaveCountAttr(typed: TypedArray) {
        octaveCount = typed.getInteger(
            R.styleable.PianoKeyboardView_octaveCount,
            OCTAVE_COUNT_DEFAULT
        )
    }

    private fun parseShouldCoerceInWidthAttr(typed: TypedArray) {
        shouldCoerceInWidth = typed.getBoolean(
            R.styleable.PianoKeyboardView_coerceInWidth,
            SHOULD_COERCE_IN_WIDTH_DEFAULT
        )
    }

    @Suppress("unused")
    fun setWhiteKeysColor(color: Int) {
        whitesColor = color
    }

    @Suppress("unused")
    fun setBlackKeysColor(color: Int) {
        blacksColor = color
    }

    @Suppress("unused")
    fun setSelectedKeysColor(color: Int) {
        selectedColor = color
    }

    @Suppress("unused")
    fun setOctaveCout(count: Int) {
        octaveCount = count
    }

    @Suppress("unused")
    fun setShouldCoerceInWidth(state: Boolean) {
        shouldCoerceInWidth = state
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (shouldCoerceInWidth) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            setMeasuredDimension(
                width,
                getViewHeightFromWidth(width)
            )
        } else {
            // if layout_width="wrap_content", set width to fit all octaves
            val widthSpec = MeasureSpec.getMode(widthMeasureSpec)
            if (widthSpec == MeasureSpec.AT_MOST || widthSpec == MeasureSpec.UNSPECIFIED) {
                setMeasuredDimension(
                    computeOctavesWidth(octaveCount).toInt(),
                    MeasureSpec.getSize(heightMeasureSpec)
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.actionMasked == MotionEvent.ACTION_DOWN) return true
        if (event == null || event.actionMasked != MotionEvent.ACTION_UP) return false

        val white = computeWhiteKeySize()
        val whiteWidthTotal = white.width + WHITE_KEYS_SPACING

        val index = (event.x / whiteWidthTotal).toInt()

        // could check here if (event.y > black.height) then white, but I will not :>

        checkBlackAside(index, onLeft = true, event).let { found -> if (found) return true }
        checkBlackAside(index, onLeft = false, event).let { found -> if (found) return true }

        val note = Theory.Whites.getOn(index)
        if (listener?.onKeboardKeyTouched(note) == true) {
            val ordinal = index % Theory.Whites.COUNT
            val octave = index / Theory.Whites.COUNT
            val key = SelectedKey(ordinal, octave, isWhite = true)
            selectKey(key)
        }
        return true
    }

    private fun checkBlackAside(anchorWhite: Int, onLeft: Boolean, event: MotionEvent): Boolean {
        val octaveIndex = anchorWhite % 7
        if (onLeft && (octaveIndex == 0 || octaveIndex == 3)) return false
        if (!onLeft && (octaveIndex == 2 || octaveIndex == 6)) return false
        val white = computeWhiteKeySize()
        val black = computeBlackKeySize()
        val whiteWidthTotal = white.width + WHITE_KEYS_SPACING

        val whiteToLeft = anchorWhite + if (onLeft) 0 else 1
        val dx = whiteToLeft * whiteWidthTotal - (WHITE_KEYS_SPACING / 2) - (black.width / 2)
        RectF(0f, 0f, black.width, black.height).run {
            offset(dx, 0f)
            if (!contains(event.x, event.y)) return false
        }

        val whiteNote = Theory.Whites.getOn(anchorWhite)
        val whiteIndex = Theory.ChromaticScale.indexOf(whiteNote)
        val blackIndex = whiteIndex + if (onLeft) -1 else +1

        val note = Theory.ChromaticScale.notes[blackIndex]
        if (listener?.onKeboardKeyTouched(note) == true) {
            val ordinal = Theory.Blacks.indexOf(note)!!
            val octave = anchorWhite / Theory.Whites.COUNT
            val key = SelectedKey(ordinal, octave, isWhite = false)
            selectKey(key)
        }
        return true
    }

    private fun selectKey(key: SelectedKey) {
        selected.add(key)
        invalidate()
    }

    override fun onDraw(c: Canvas?) {
        super.onDraw(c)
        c ?: return
        drawUnderneath(c)
        for (octave in 0 until octaveCount) {
            drawWhiteKeys(c, octave)
            drawBlackKeys(c, octave)
        }
    }

    private fun drawUnderneath(c: Canvas) {
        val (octaveWidth, octaveHeight) = computeOctaveSize()
        val width = octaveWidth * octaveCount
        val height = octaveHeight - WHITE_KEYS_ROUNDESS
        c.drawRect(0f, 0f, width, height, underneathPaint)
    }

    private fun drawWhiteKeys(c: Canvas, octave: Int) {
        val roundness = WHITE_KEYS_ROUNDESS
        val white = computeWhiteKeySize()
        val key = RectF(0f, 0f, white.width, white.height).apply {
            offset(computeOctavesWidth(octave), 0f)
        }
        for (i in 0 until 7) {
            val isSelected = selected.find { it.hasAll(i, octave, isWhite = true) } != null
            keysPaint.color = getKeyColor(whitesColor, isSelected)

            c.drawRoundRect(key, roundness, roundness, keysPaint)
            c.drawRect(key.left, key.top, key.right, roundness, keysPaint)
            key.offset(white.width + WHITE_KEYS_SPACING, 0f)
        }
    }

    private fun drawBlackKeys(c: Canvas, octave: Int) {
        val roundness = BLACK_KEYS_ROUNDNESS
        val white = computeWhiteKeySize()
        val black = computeBlackKeySize()
        val wKeyWidthTotal = (white.width + WHITE_KEYS_SPACING)
        val startOffset = white.width + (WHITE_KEYS_SPACING / 2) - (black.width / 2)
        val key = RectF(startOffset, 0f, startOffset + black.width, black.height).apply {
            offset(computeOctavesWidth(octave), 0f)
        }
        for (i in 0 until 6) {
            if (i != 2) {
                val isSelected = selected.find { it.hasAll(i, octave, isWhite = false) } != null
                keysPaint.color = getKeyColor(blacksColor, isSelected)

                c.drawRoundRect(key, roundness, roundness, keysPaint)
                c.drawRect(key.left, key.top, key.right, roundness, keysPaint)
            }
            key.offset(wKeyWidthTotal, 0f)
        }
    }

    private fun getKeyColor(base: Int, isSelected: Boolean): Int {
        if (!isSelected) return base
        return ColorUtils.compositeColors(selectedColor, base)
    }

    private fun computeWhiteKeySize(): KeySize {
        return KeySize(
            getViewHeight() / WHITE_KEY_HEIGHT * WHITE_KEY_WIDTH,
            getViewHeight()
        )
    }

    private fun computeBlackKeySize(): KeySize {
        return KeySize(
            getViewHeight() / WHITE_KEY_HEIGHT * BLACK_KEY_WIDTH,
            getViewHeight() / WHITE_KEY_HEIGHT * BLACK_KEY_HEIGHT
        )
    }

    private fun computeOctaveSize(): KeySize {
        computeWhiteKeySize().let { whiteKey ->
            return KeySize(
                (whiteKey.width * 7) + (WHITE_KEYS_SPACING * 6),
                whiteKey.height
            )
        }
    }

    private fun computeOctavesWidth(octaveCount: Int): Float {
        return (computeOctaveSize().width + WHITE_KEYS_SPACING) * octaveCount
    }

    private fun getViewHeight(): Float {
        return (this.height.takeIf { it != 0 } ?: this.measuredHeight).toFloat()
    }

    private fun getViewHeightFromWidth(newWidth: Int): Int {
        val whites = octaveCount * 7
        val whiteWidth = (newWidth - (WHITE_KEYS_SPACING * (whites - 1))) / whites
        return (whiteWidth / WHITE_KEY_WIDTH * WHITE_KEY_HEIGHT).toInt()
    }

    interface OnKeyboardNoteTouchListener {
        /**
         * @return Is [note] was consumed.
         */
        fun onKeboardKeyTouched(note: Note): Boolean
    }

    companion object {
        private const val WHITE_KEYS_COLOR_DEFAULT = Color.LTGRAY
        private const val BLACK_KEYS_COLOR_DEFAULT = Color.DKGRAY
        private const val SELECTED_KEYS_COLOR_DEFAULT = 0x400000ff // 25% blue
        private const val KEYS_COLOR_UNDERNEATH = Color.BLACK

        private const val OCTAVE_COUNT_DEFAULT = 2
        private const val SHOULD_COERCE_IN_WIDTH_DEFAULT = false

        private const val WHITE_KEYS_ROUNDESS = 10f
        private const val WHITE_KEYS_SPACING = 4f
        private const val BLACK_KEYS_ROUNDNESS = 5f

        private const val WHITE_KEY_WIDTH = 2.2f
        private const val BLACK_KEY_WIDTH = 1.2f
        private const val WHITE_KEY_HEIGHT = 14.8f
        private const val BLACK_KEY_HEIGHT = 9.6f
    }

    private data class KeySize(
        val width: Float,
        val height: Float
    )

    private data class SelectedKey(
        val ordinal: Int,
        val octave: Int,
        val isWhite: Boolean
    ) {
        fun hasAll(ordinal: Int, octave: Int, isWhite: Boolean): Boolean {
            return (this.ordinal == ordinal && this.octave == octave && this.isWhite == isWhite)
        }
    }
}