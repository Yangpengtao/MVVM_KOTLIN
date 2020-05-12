package com.oomall.kouclodelivery.ui.view.account

import android.os.Bundle
import android.view.View
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_set_password.*


/**
 * 设置登陆密码
 * 1，第一次通过验证码注册，会提示他“设置登陆密码”
 * 2，忘记密码，通过[PhoneNumberVerifyActivity]验证通过后，进入，提示“设置新密码”
 */
class SetPasswordActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnFinish -> {
                startActivity(MainActivity::class.java)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_password)
        setRemark()
        btnFinish.setOnClickListener(this)
    }

    private fun setRemark() {
//        when (intent.extras!!.getInt("param1", 1)) {
        when (intent.getIntExtra("param1", 2)) {
            1 -> {  //设置新密码
                tvRemark.text = "设置新密码"
                tvRemark2.text = "输入新密码，密码将会被重置"
                getLeftImg_().visibility = View.VISIBLE
            }
            2 -> {  //设置登陆密码
                tvRemark.text = "设置登录密码"
                tvRemark2.text = "设置成功后可用账号密码登录"
                getRightText_().text = "跳过"
                getRightText_().visibility = View.VISIBLE
            }
        }
    }
}
