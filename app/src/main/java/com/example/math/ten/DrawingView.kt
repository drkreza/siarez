package com.example.math.ten

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttrs: Int = 0) :
    View(context, attrs, defStyleAttrs) {
    private val mPaint: Paint = Paint()
    private val mPath: Path
    private var onTouchView: OnTouchView? = null

    var isLock = false
    var canDraw = false

    init {
        mPaint.color = Color.parseColor("#ff587a9f")
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.strokeWidth = 20f
        mPath = Path()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(canDraw)
            canvas?.drawPath(mPath, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(isLock)
            return false

        if(!canDraw)
            return false
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                mPath.moveTo(event.x, event.y)
                onTouchView?.touchStart(event.x, event.y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                mPath.lineTo(event.x, event.y)
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
               /* if(!canDraw)
                    resetLine()*/
                onTouchView?.touchEnd(event.x, event.y)
                return true
            }
        }
        return true
    }

    fun setOnTouchView(onTouchView: OnTouchView) {
        this.onTouchView = onTouchView
    }

    fun changeLineColor(hexColor:String) {
        mPaint.color = Color.parseColor(hexColor)
        invalidate()
    }

    fun resetLine(){
        mPath.reset()
        mPaint.color = Color.parseColor("#ff587a9f")
        canDraw = false
        invalidate()
    }
}

interface OnTouchView {
    fun touchStart(x: Float, y: Float)
    fun touchEnd(x: Float, y: Float)
}