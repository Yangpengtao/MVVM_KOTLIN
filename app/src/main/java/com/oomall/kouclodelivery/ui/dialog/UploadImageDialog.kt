package com.oomall.kouclodelivery.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.oomall.kouclodelivery.R
import java.lang.NullPointerException


/**
 * 上传图片
 */
class UploadImageDialog : Dialog {
    constructor(context: Context, themeId: Int) : super(context, themeId)
    constructor(context: Context) : super(context)

    private var clickListener: ClickListener? = null

    private var tvTakePhoto: TextView? = null
    private var tvAlum: TextView? = null
    private var tvCancel: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_upload_image)
        setParams()
        tvTakePhoto = findViewById(R.id.tvTakePhoto)
        tvAlum = findViewById(R.id.tvAlum)
        tvCancel = findViewById(R.id.tvCancel)
        tvTakePhoto!!.setOnClickListener(listener)
        tvAlum!!.setOnClickListener(listener)
        tvCancel!!.setOnClickListener(listener)
    }

    private fun setParams() {
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        val window = this.window!!
        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.PopBottom)

        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }

    private val listener: View.OnClickListener = View.OnClickListener {
        if (null == clickListener) {
            throw NullPointerException("you must be set PopClickListener")
        }
        when (it.id) {
            R.id.tvTakePhoto -> {
                clickListener!!.takePhoto()
                dismiss()
            }
            R.id.tvAlum -> {
                dismiss()
                clickListener!!.alum()
            }
            R.id.tvCancel -> {
                dismiss()

            }
        }
    }


    fun setPopClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun takePhoto()
        fun alum()
    }

}
