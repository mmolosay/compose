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
import android.widget.Toast
import com.ordolabs.compose.R

class PianoKeyboardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var octaveCount: Int = OCTAVE_COUNT_DEFAULT
    private var shouldCoerceInWidth: Boolean = false

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
        parseShouldCoerceInWidthAttr(typed)
        typed.recycle()
    }

    private fun parseWhiteKeysColorAttr(typed: TypedArray) {
        whitePaint.color = typed.getColor(
            R.styleable.PianoKeyboardView_whiteKeysColor,
            WHITE_KEYS_COLOR_DEFAULT
        )
    }

    private fun parseBlackKeysColorAttr(typed: TypedArray) {
        blackPaint.color = typed.getColor(
            R.styleable.PianoKeyboardView_blackKeysColor,
            BLACK_KEYS_COLOR_DEFAULT
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

    fun setWhiteKeysColor(color: Int) {
        whitePaint.color = color
    }

    fun setBlackKeysColor(color: Int) {
        blackPaint.color = color
    }

    fun setOctaveCout(count: Int) {
        octaveCount = count
    }

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
        val black = computeBlackKeySize()
        val whiteWidthTotal = white.width + WHITE_KEYS_SPACING
        val whiteTouched = (event.x / whiteWidthTotal).toInt()

        if (event.y > black.height) {
            toastWhite(whiteTouched)
            return true
        }

        val ordinal = whiteTouched % 7

        // check black key at left of touched white
        if (ordinal != 0 || ordinal != 3) {
            val dx =
                whiteTouched * whiteWidthTotal - (WHITE_KEYS_SPACING / 2) - (black.width / 2)
            RectF(0f, 0f, black.width, black.height).run {
                offset(dx, 0f)
                if (contains(event.x, event.y)) {
                    toastBlack(whiteTouched, "left")
                }
            }
        }

        // check black key at right of touched white
        if (ordinal != 2 || ordinal != 6) {
            val dx =
                (whiteTouched + 1) * whiteWidthTotal - (WHITE_KEYS_SPACING / 2) - (black.width / 2)
            RectF(0f, 0f, black.width, black.height).run {
                offset(dx, 0f)
                if (contains(event.x, event.y)) {
                    toastBlack(whiteTouched, "right")
                }
            }
        }

        return true
    }

    private fun toastWhite(i: Int) {
        Toast.makeText(context, "Touched #$i white key", Toast.LENGTH_SHORT).show()
    }

    private fun toastBlack(i: Int, side: String) {
        Toast.makeText(context, "Touched black key at $side of #$i white one", Toast.LENGTH_SHORT)
            .show()
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
            c.drawRoundRect(key, roundness, roundness, whitePaint)
            c.drawRect(key.left, key.top, key.right, roundness, whitePaint)
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
                c.drawRoundRect(key, roundness, roundness, blackPaint)
                c.drawRect(key.left, key.top, key.right, roundness, blackPaint)
            }
            key.offset(wKeyWidthTotal, 0f)
        }
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

    companion object {
        private const val WHITE_KEYS_COLOR_DEFAULT = Color.LTGRAY
        private const val BLACK_KEYS_COLOR_DEFAULT = Color.DKGRAY
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
}