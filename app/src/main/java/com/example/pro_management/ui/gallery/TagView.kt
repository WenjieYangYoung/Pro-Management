package com.example.pro_management.ui.gallery

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import com.example.pro_management.R

class TagView(context: Context): ViewGroup(context){
    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        var left: Int = paddingLeft
        val right: Int = width - paddingRight
        var top: Int = paddingTop
        val bottom: Int = height - paddingBottom
        var lp: MarginLayoutParams
        var cw: Int
        var ch: Int

        for (index in 0..childCount - 1) {
            var view = getChildAt(index)
            lp = view.layoutParams as MarginLayoutParams
            cw = view.measuredWidth + lp.leftMargin + lp.rightMargin
            ch = view.measuredHeight + lp.topMargin + lp.bottomMargin

            //该换行了
            if (left + cw > right) {
                left = paddingLeft
                top += ch
            }
            //如果高度超出了范围就退出绘制
            if (top >= bottom) break

            view.layout(left + lp.leftMargin, top + lp.topMargin, left + cw, top + ch)
            left += cw

        }
    }

    val TAG = "TagView"

    var mBackgroundDrawable: Drawable? = null


    constructor(context: Context, attrs: AttributeSet):this(context){
        val ta : TypedArray = context.obtainStyledAttributes(attrs, R.styleable.TagView)
        mBackgroundDrawable = ta.getDrawable((R.styleable.TagView_android_background))

        ta.recycle()

        if(mBackgroundDrawable != null){
            setBackgroundDrawable(mBackgroundDrawable)
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val suggestWidth = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val suggestHeight = MeasureSpec.getSize(heightMeasureSpec)

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var cWidth : Int
        var cHeight : Int
        var lineWidth : Int = paddingLeft + paddingRight
        var lineMaxWidth : Int = lineWidth
        var lineHeight : Int = paddingBottom + paddingTop
        var childlPara : MarginLayoutParams
        var resultW : Int = suggestWidth
        var resultH : Int = suggestHeight

        for ( index in 0..childCount - 1) {
            val view = getChildAt(index)
            childlPara = view.layoutParams as MarginLayoutParams
            // 子 View 的实际宽高包含它们的 margin
            cWidth = view.measuredWidth + childlPara.leftMargin + childlPara.rightMargin
            cHeight = view.measuredHeight + childlPara.topMargin + childlPara.bottomMargin

            if (widthMode == MeasureSpec.AT_MOST) {
                // 如果此次排列后，这一行的宽度超过 parent 提供的 size 就表明要换行了
                if ( lineWidth + cWidth > suggestWidth ) {
                    // 换行后需要重置 lineWidth
                    lineWidth = paddingLeft + paddingRight + cWidth
                    lineHeight += cHeight

                } else {
                    // lineWidth 对子 View 宽度进行累加
                    lineWidth += cWidth
                }

                if ( lineWidth > lineMaxWidth ) {
                    lineMaxWidth = lineWidth
                }
            }
        }

        if (widthMode == MeasureSpec.AT_MOST) {
            resultW = lineMaxWidth
        }

        if ( heightMode == MeasureSpec.AT_MOST) {
            resultH = lineHeight
            if (resultH > suggestHeight ) {
                resultH = suggestHeight
            }
        }

        setMeasuredDimension(resultW,resultH)

    }

}