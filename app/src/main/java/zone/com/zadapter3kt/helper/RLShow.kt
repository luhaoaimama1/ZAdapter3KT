package zone.com.zadapter3kt.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 *[2019/4/19] by Zone
 */
class RLShow :RelativeLayout{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val first= Rect()
    val end= Rect()
    val paint=getStrokePaint(Paint.Style.STROKE,5F).apply {
        color= Color.RED
    }

    fun getStrokePaint(style: Paint.Style, strokeWidth: Float): Paint {
        val mPaint2 = Paint()
        mPaint2.isAntiAlias = true
        mPaint2.style = style
        mPaint2.strokeWidth = strokeWidth
        return mPaint2
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.drawRect(first,paint)
        canvas?.drawRect(end,paint)
    }
}