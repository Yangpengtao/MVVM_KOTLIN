package com.oomall.kouclodelivery.ui.view.setting.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.utils.IntentUtil
import kotlinx.android.synthetic.main.dialog_setting_bind_phone.*

class BindPhoneDialog(context: Context) : Dialog(context), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvIKnow -> {
                dismiss()
            }
            R.id.tvPhoneNumber -> {
                context.startActivity(IntentUtil.getDialIntent(tvPhoneNumber.text.toString()))
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_setting_bind_phone)
        val tvPhoneNumber = findViewById<TextView>(R.id.tvPhoneNumber)
        tvPhoneNumber.setOnClickListener(this)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
    }
}