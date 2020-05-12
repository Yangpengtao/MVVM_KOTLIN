package com.oomall.kouclodelivery.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * 解决刷新控件与其他控件冲突问题
 */
class SlidingConfictSwipeRefreshLayout(context: Context, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs) {

    lateinit var mOnPreInterceptTouchEventDelegate: OnPreInterceptTouchEventDelegate


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var disallowIntercept = mOnPreInterceptTouchEventDelegate.shouldDisallowInterceptTouchEvent(ev!!)
        if (disallowIntercept) {
            return false
        }
        return super.onInterceptTouchEvent(ev)
    }


    interface OnPreInterceptTouchEventDelegate {
        fun shouldDisallowInterceptTouchEvent(ev: MotionEvent): Boolean
    }
}