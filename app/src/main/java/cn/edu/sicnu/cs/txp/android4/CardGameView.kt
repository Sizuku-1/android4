package cn.edu.sicnu.cs.txp.android4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import cn.edu.sicnu.cs.txp.android4.model.Card
import cn.edu.sicnu.cs.txp.android4.model.CardMatchingGame

/**
 * 翻牌游戏的自定义视图（View 层）。
 * 负责把 Model 中的牌绘制到屏幕上，并处理点击坐标到牌的映射。
 */
class CardGameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var game: CardMatchingGame? = null

    private val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val backPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var cardWidth = 0f
    private var cardHeight = 0f
    private var horizontalSpacing = 0f
    private var verticalSpacing = 0f
    private var topOffset = 0f

    // 存储每张牌的绘制区域（用于点击命中检测）
    private val cardRects = mutableListOf<RectF>()

    private var onCardClickListener: ((Int) -> Unit)? = null

    init {
        cardPaint.color = Color.WHITE
        cardPaint.style = Paint.Style.FILL

        backPaint.color = Color.rgb(63, 81, 181)
        backPaint.style = Paint.Style.FILL

        textPaint.color = Color.BLACK
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isFakeBoldText = true

        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 4f
        borderPaint.color = Color.rgb(63, 81, 181)
    }

    fun setGame(game: CardMatchingGame) {
        this.game = game
        requestLayout()
        invalidate()
    }

    fun setOnCardClickListener(listener: (Int) -> Unit) {
        onCardClickListener = listener
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateLayout(w, h)
    }

    private fun calculateLayout(w: Int, h: Int) {
        val g = game ?: return
        val count = g.cardCount()
        if (count == 0) return

        // 采用 4 列布局，行数按牌数向上取整
        val cols = 4
        val rows = (count + cols - 1) / cols

        val padding = 16f
        val spacing = 12f

        val availableWidth = w - padding * 2 - spacing * (cols - 1)
        val availableHeight = h - padding * 2 - spacing * (rows - 1)

        cardWidth = availableWidth / cols
        cardHeight = cardWidth * 1.4f

        // 如果总高度超出可用高度，按高度反推宽度
        val totalHeight = cardHeight * rows + spacing * (rows - 1)
        if (totalHeight > availableHeight) {
            cardHeight = availableHeight / rows - spacing * (rows - 1) / rows
            cardWidth = cardHeight / 1.4f
        }

        horizontalSpacing = spacing
        verticalSpacing = spacing

        val totalWidth = cardWidth * cols + spacing * (cols - 1)
        val startX = (w - totalWidth) / 2
        val totalHeightFinal = cardHeight * rows + spacing * (rows - 1)
        topOffset = (h - totalHeightFinal) / 2

        cardRects.clear()
        for (i in 0 until count) {
            val row = i / cols
            val col = i % cols
            val left = startX + col * (cardWidth + spacing)
            val top = topOffset + row * (cardHeight + spacing)
            cardRects.add(RectF(left, top, left + cardWidth, top + cardHeight))
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val g = game ?: return

        for (i in 0 until g.cardCount()) {
            val rect = cardRects.getOrNull(i) ?: continue
            val card = g.cardAtIndex(i)
            drawCard(canvas, rect, card)
        }
    }

    private fun drawCard(canvas: Canvas, rect: RectF, card: Card) {
        // 圆角矩形
        val radius = 12f

        if (card.isMatched) {
            cardPaint.color = Color.rgb(76, 175, 80)
            canvas.drawRoundRect(rect, radius, radius, cardPaint)
            textPaint.color = Color.WHITE
            drawCenteredText(canvas, rect, "✓")
            return
        }

        if (card.isChosen) {
            // 正面（翻开）
            cardPaint.color = Color.WHITE
            canvas.drawRoundRect(rect, radius, radius, cardPaint)
            borderPaint.color = Color.rgb(255, 193, 7)
            canvas.drawRoundRect(rect, radius, radius, borderPaint)

            textPaint.color = if (card.suitValue == "♥" || card.suitValue == "♦") {
                Color.rgb(211, 47, 47)
            } else {
                Color.rgb(33, 33, 33)
            }
            drawCenteredText(canvas, rect, card.toString())
        } else {
            // 背面
            backPaint.color = Color.rgb(63, 81, 181)
            canvas.drawRoundRect(rect, radius, radius, backPaint)
            // 背面花纹
            textPaint.color = Color.WHITE
            textPaint.textSize = cardHeight * 0.3f
            drawCenteredText(canvas, rect, "?")
            textPaint.textSize = 40f
        }
    }

    private fun drawCenteredText(canvas: Canvas, rect: RectF, text: String) {
        val x = rect.centerX()
        val y = rect.centerY() - (textPaint.ascent() + textPaint.descent()) / 2
        canvas.drawText(text, x, y, textPaint)
    }

    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        if (event.action == android.view.MotionEvent.ACTION_DOWN) {
            val g = game ?: return true
            for (i in 0 until g.cardCount()) {
                val rect = cardRects.getOrNull(i) ?: continue
                if (rect.contains(event.x, event.y)) {
                    onCardClickListener?.invoke(i)
                    break
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }
}
