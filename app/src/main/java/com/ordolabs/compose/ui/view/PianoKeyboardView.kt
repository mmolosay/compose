package com.ordolabs.compose.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.ordolabs.compose.R

class PianoKeyboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var octaveCount: Int = OCTAVE_COUNT_DEFAULT

    private val underneathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = KEYS_COLOR_UNDERNEATH
    }

    private val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = WHITE_KEYS_COLOR_DEFAULT
    }

    private val blackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = BLACK_KEYS_COLOR_DEFAULT
    }

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.PianoKeyboardView)
        parseWhiteKeysColorAttr(typed)
        parseBlackKeysColorAttr(typed)
        parseOctaveCountAttr(typed)
        typed.recycle()
    }

    private fun parseWhiteKeysColorAttr(typed: TypedArray) {
        whitePaint.color =
            typed.getColor(R.styleable.PianoKeyboardView_whiteKeysColor, WHITE_KEYS_COLOR_DEFAULT)
    }

    private fun parseBlackKeysColorAttr(typed: TypedArray) {
        blackPaint.color =
            typed.getColor(R.styleable.PianoKeyboardView_blackKeysColor, BLACK_KEYS_COLOR_DEFAULT)
    }

    private fun parseOctaveCountAttr(typed: TypedArray) {
        octaveCount =
            typed.getInteger(R.styleable.PianoKeyboardView_octaveCount, OCTAVE_COUNT_DEFAULT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // if layout_width="wrap_content", set width to one octave exactly
        val widthSpec = MeasureSpec.getMode(widthMeasureSpec)
        if (widthSpec == MeasureSpec.AT_MOST || widthSpec == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(
                computeOctavesWidth(octaveCount).toInt(),
                MeasureSpec.getSize(heightMeasureSpec)
            )
        }
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
        val (keyWidth, keyHeight) = computeWhiteKeySize()
        val key = RectF(0f, 0f, keyWidth, keyHeight).apply {
            offset(computeOctavesWidth(octave), 0f)
        }
        for (i in 0 until 7) {
            c.drawRoundRect(key, roundness, roundness, whitePaint)
            c.drawRect(key.left, key.top, key.right, roundness, whitePaint)
            key.offset(keyWidth + WHITE_KEYS_SPACING, 0f)
        }
    }

    private fun drawBlackKeys(c: Canvas, octave: Int) {
        val roundness = BLACK_KEYS_ROUNDNESS
        val wKeyWidth = computeWhiteKeySize().first
        val (bKeyWidth, bKeyHeight) = computeBlackKeySize()
        val wKeyWidthTotal = (wKeyWidth + WHITE_KEYS_SPACING)
        val startOffset = wKeyWidth + (WHITE_KEYS_SPACING / 2) - (bKeyWidth / 2)
        val key = RectF(startOffset, 0f, startOffset + bKeyWidth, bKeyHeight).apply {
            offset(computeOctavesWidth(octave), 0f)
        }
        for (i in 0 until 6) {
            if (i != 2) {
                c.drawRoundRect(key, roundness, roundness, blackPaint)
                c.drawRect(key.left, key.top, key.right, roundness, blackPaint)
            }
            key.offset(wKeyWidthTotal, 0f)
        }
    }

    private fun computeWhiteKeySize(): Pair<Float, Float> {
        return Pair(
            getViewHeight() / WHITE_KEY_HEIGHT * WHITE_KEY_WIDTH,
            getViewHeight()
        )
    }

    private fun computeBlackKeySize(): Pair<Float, Float> {
        return Pair(
            getViewHeight() / WHITE_KEY_HEIGHT * BLACK_KEY_WIDTH,
            getViewHeight() / WHITE_KEY_HEIGHT * BLACK_KEY_HEIGHT
        )
    }

    private fun computeOctaveSize(): Pair<Float, Float> {
        computeWhiteKeySize().let {
            return Pair(
                (it.first * 7) + (WHITE_KEYS_SPACING * 6),
                it.second
            )
        }
    }

    private fun computeOctavesWidth(octaveCount: Int): Float {
        return (computeOctaveSize().first + WHITE_KEYS_SPACING) * octaveCount
    }

    private fun getViewHeight(): Float {
        return (this.height.takeIf { it != 0 } ?: this.measuredHeight).toFloat()
    }

    companion object {
        private const val WHITE_KEYS_COLOR_DEFAULT = Color.LTGRAY
        private const val BLACK_KEYS_COLOR_DEFAULT = Color.DKGRAY
        private const val KEYS_COLOR_UNDERNEATH = Color.BLACK

        private const val OCTAVE_COUNT_DEFAULT = 2

        private const val WHITE_KEYS_ROUNDESS = 10f
        private const val WHITE_KEYS_SPACING = 4f
        private const val BLACK_KEYS_ROUNDNESS = 5f

        private const val WHITE_KEY_WIDTH = 2.2f
        private const val BLACK_KEY_WIDTH = 1.2f
        private const val WHITE_KEY_HEIGHT = 14.8f
        private const val BLACK_KEY_HEIGHT = 9.6f
    }
}