package com.oomall.kouclodelivery.ui.view.account

import android.os.Bundle
import android.view.View
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_phone_number_verify.*

/**
 * 手机号验证
 */
class PhoneNumberVerifyActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnNext -> {
                startActivity(SetPasswordActivity::class.java)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number_verify)
        getLeftImg_().visibility = View.VISIBLE
        btnNext.setOnClickListener(this)
    }
}
