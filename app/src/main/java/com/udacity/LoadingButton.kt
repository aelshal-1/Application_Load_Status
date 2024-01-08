package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var loadingButtonColor = 0
    private var DownloadButtonColor =0
    private var animatingPosition = 0
    private var radiusPosition = 0.0f
    private var loadingAnimator = ValueAnimator()
    private var circularAnimator = ValueAnimator()
    private lateinit var displayedText :String
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->

        when(new){
            ButtonState.Clicked->{
                Log.d("TEST"," ButtonState.Clicked")
                loadingAnimator = ValueAnimator.ofInt(0,width)
                loadingAnimator.duration = 2000
                loadingAnimator.repeatCount = 1
                loadingAnimator.repeatMode = ValueAnimator.REVERSE
                setBackgroundColor(DownloadButtonColor)
                loadingAnimator.addUpdateListener {
                    animatingPosition = it.animatedValue as Int
                    invalidate()
                }

                circularAnimator = ValueAnimator.ofFloat(0f,360f)
                circularAnimator.duration= 1000
                circularAnimator.repeatCount = ValueAnimator.INFINITE
                circularAnimator.addUpdateListener {
                    radiusPosition = it.animatedValue as Float
                    this.invalidate()
                }
            }
            ButtonState.Loading->{
                Log.d("TEST"," ButtonState.Loading")
                displayedText = "WE ARE LOADING"
                loadingAnimator.start()
                circularAnimator.start()
            }
            ButtonState.Completed->{
                Log.d("TEST"," ButtonState.Completed")
                loadingAnimator.end()
                circularAnimator.end()
                animatingPosition =0
                radiusPosition =0f
                displayedText = "DOWNLOAD"
            }
        }
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = loadingButtonColor
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55f
        typeface= Typeface.create("",Typeface.BOLD)
    }

    init {
        isClickable = true
        displayedText = "DOWNLOAD"
        context.withStyledAttributes(attrs, R.styleable.LoadingButton){
            loadingButtonColor = getColor(R.styleable.LoadingButton_loadingColor1,0)
            DownloadButtonColor = getColor(R.styleable.LoadingButton_loadingColor2,0)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = loadingButtonColor
        canvas?.drawRect(0f, 0f, width.toFloat(),height.toFloat() ,paint)



        paint.color = DownloadButtonColor
        canvas?.drawRect(0f, 0f, width.toFloat() * animatingPosition/100,height.toFloat() ,paint)



        paint.color = Color.YELLOW
        val centerX = width * 0.8f
        val centerY = height / 2f
        val radius = minOf(width, height) * 0.3f
        canvas?.drawArc(centerX - radius, centerY - radius, centerX + radius, centerY + radius, 0f,radiusPosition,true,paint)

        paint.color = Color.WHITE
        canvas?.drawText(displayedText, width/2f,height/2f,paint)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}