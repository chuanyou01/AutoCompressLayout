package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout

/**
 * @author zengchuan
 * @date 2023/11/15
 * AutoCompressLayout
 */
class AutoCompressLayout : FrameLayout {

    /**
     * constructors
     */
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * only one child can be added, change the scale of child
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount != 1) {
            throw IllegalStateException("Only one child can be added")
        }
        val subView = getChildAt(0)
        //reset the scale
        subView.scaleX = 1.0f
        subView.scaleY = 1.0f
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        subView?.let {
            val scale = it.measuredWidth * 1.0f / measuredWidth
            if (scale > 1.0f) {
                it.pivotX = getChildPiovtX(it);
                it.pivotY = getChildPiovtY(it);
                it.scaleX = 1 / scale
                it.scaleY = 1 / scale
            }
        }
    }

    /**
     * get the scale pivotX
     */
    private fun getChildPiovtX(subView: View): Float {
        val lp = subView.layoutParams as FrameLayout.LayoutParams
        var gravity: Int = lp.gravity
        if (gravity == -1) {
            gravity = Gravity.TOP or Gravity.START
        }

        val layoutDirection = layoutDirection
        val absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection)

        return when (absoluteGravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.CENTER_HORIZONTAL -> subView.measuredWidth * 1.0f / 2

            Gravity.RIGHT -> {
                subView.measuredWidth * 1.0f
            }

            Gravity.LEFT -> 0f
            else -> 0f
        }
    }

    /**
     * get the scale pivotX
     */
    private fun getChildPiovtY(subView: View): Float {
        val lp = subView.layoutParams as FrameLayout.LayoutParams
        var gravity: Int = lp.gravity
        if (gravity == -1) {
            gravity = Gravity.TOP or Gravity.START
        }

        val verticalGravity = gravity and Gravity.VERTICAL_GRAVITY_MASK

        return when (verticalGravity) {
            Gravity.TOP -> 0f
            Gravity.CENTER_VERTICAL -> subView.measuredHeight * 1.0f / 2

            Gravity.BOTTOM -> subView.measuredHeight * 1.0f
            else -> 0f
        }
    }

    /**
     * set parent width screenWidth*20, then the child can get the max width that can be calc
     */
    override fun measureChildWithMargins(child: View?, parentWidthMeasureSpec: Int, widthUsed: Int, parentHeightMeasureSpec: Int, heightUsed: Int) {
        val newParentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(context.resources.displayMetrics.widthPixels * 20, MeasureSpec.EXACTLY);
        super.measureChildWithMargins(child, newParentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed)
    }

}