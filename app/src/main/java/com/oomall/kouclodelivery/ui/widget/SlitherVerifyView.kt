package com.oomall.kouclodelivery.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.utils.ScreenUtils
import kotlinx.android.synthetic.main.framlayout_slither_verify.view.*

/**
 * 滑动验证控件
 */
class SlitherVerifyView : RelativeLayout {


    /*滑动通过后通知activity*/
    var mCallBack: CallBack? = null
    /*滑动按下时的X*/
    private var downPointX: Float = 0f
    /*移动中的X*/
    private var movePointX: Float = 0f
    /*当前手机的密度*/
    private var mDensity: Float = 0f
    /*滑动按钮的宽度dp*/
    private var slitherBtnWidth: Int = 80
    /*是否通过*/
    var verify: Boolean = false
    private lateinit var leftLayoutParams: FrameLayout.LayoutParams
    private lateinit var btnLayoutParams: FrameLayout.LayoutParams


    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs, 0) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }


    fun initView() {
        LayoutInflater.from(context).inflate(R.layout.framlayout_slither_verify, this)
        leftLayoutParams = v_slither_left.layoutParams as FrameLayout.LayoutParams
        btnLayoutParams = v_slither_btn.layoutParams as FrameLayout.LayoutParams
        slitherBtnWidth = (ScreenUtils.getScreen(context).density * slitherBtnWidth).toInt()

        v_slither_btn_normal.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    downPointX = motionEvent.x
                    movePointX = motionEvent.x
                }
                MotionEvent.ACTION_MOVE -> {
                    movePointX = motionEvent.x
                    val diff = movePointX - downPointX
                    if (diff > 0) {
                        //如果移动到了最大距离则表示滑动通过
                        if (measuredWidth <= v_slither_left.width) {
                            verify = true
                        } else {
                            verify = false
                            this.leftLayoutParams.width = (diff + slitherBtnWidth).toInt()
                            v_slither_left.layoutParams = leftLayoutParams
                            //设置按钮的移动
                            this.btnLayoutParams.setMargins(diff.toInt(), 0, 0, 0)
                            v_slither_btn.layoutParams = btnLayoutParams
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    //未通过
                    if (!verify) {
                        reset()
                    } else {
                        v_slither_btn_normal.isEnabled = false
                        //验证通过
                        this.leftLayoutParams.width = measuredWidth
                        v_slither_left.layoutParams = leftLayoutParams
                        //设置按钮的移动
                        this.btnLayoutParams.setMargins(measuredWidth - v_slither_btn.width, 0, 0, 0)
                        v_slither_btn.layoutParams = btnLayoutParams

                        tv_slither_tip.setTextColor(Color.WHITE)
                        tv_slither_tip.text = context.resources.getString(R.string.login_slither_pass)
                        if (mCallBack == null) {
                            throw NullPointerException("you must be set callback!!!!!!!!!!!!!")
                        } else {
                            v_slither_btn.background =
                                ContextCompat.getDrawable(context, R.drawable.login_btn_verify_pass)
                            mCallBack!!.pass()
                        }
                    }
                }
            }

            true
        }
    }


    fun reset() {
        v_slither_btn.background = ContextCompat.getDrawable(context, R.drawable.login_btn_verify_unpass)
        v_slither_btn_normal.isEnabled = true
        this.downPointX = 0f
        this.verify = false
        this.leftLayoutParams.width = 0
        v_slither_left.layoutParams = leftLayoutParams
        //设置按钮的移动
        this.btnLayoutParams.setMargins(0, 0, 0, 0)
        v_slither_btn.layoutParams = btnLayoutParams
        tv_slither_tip.setTextColor(ContextCompat.getColor(context, R.color.color999))
        tv_slither_tip.text = context.resources.getString(R.string.login_slither_no)
    }


    fun setCallBack(callBack: CallBack) {
        this.mCallBack = callBack
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

    interface CallBack {
        fun pass()
    }

}
