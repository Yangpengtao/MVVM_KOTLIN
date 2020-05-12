package com.oomall.kouclodelivery.ui.view.account

import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.view.main.MainActivity
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view_model.AccountViewModel
import com.oomall.kouclodelivery.ui.widget.SlitherVerifyView
import com.oomall.kouclodelivery.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登陆模块
 */
class LoginActivity : BaseActivity(), View.OnClickListener, SlitherVerifyView.CallBack,
    CompoundButton.OnCheckedChangeListener {


    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setTitleVisibility(View.GONE)
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        btnLogin.setOnClickListener(this)
        tvGoPasswordLogin.setOnClickListener(this)
        tvGoVerifyCodeLogin.setOnClickListener(this)
        tvForgetPassword.setOnClickListener(this)
        v_slither_verify.setCallBack(this)
        cbPassword.setOnCheckedChangeListener(this)

        accountViewModel.loginFormState.observe(this, Observer {
            if (it.success) {
                ToastUtil.show("登陆成功---${it.data!!.code}")
                startActivity(MainActivity::class.java)
            } else {
                ToastUtil.show("登陆失败")
                startActivity(MainActivity::class.java)
            }
        })
    }

    /**
     * 滑动控件通过后回调
     */
    override fun pass() {
        accountViewModel.slitherVeriry = true
        ToastUtil.show("验证通过")
    }

    /**
     * 控件点击监听
     */
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnLogin -> {
                accountViewModel.loginForCode(etUserPhone.text.toString(), etCode.text.toString())
            }
            R.id.tvGoPasswordLogin -> {
                showPasswordLogin()
            }
            R.id.tvGoVerifyCodeLogin -> {
                showVerifyLogin()
            }
            R.id.tvForgetPassword -> {
                startActivity(PhoneNumberVerifyActivity::class.java)
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0!!.id) {
            R.id.cbPassword -> {
                if (p1) {
                    etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
    }

    /**
     * 显示密码登陆
     */
    private fun showPasswordLogin() {
        etPassword.visibility = View.VISIBLE
        cbPassword.visibility = View.VISIBLE
        rlPassLogin.visibility = View.VISIBLE
        etCode.visibility = View.GONE
        tvGetVerifyCode.visibility = View.GONE
        tvGoPasswordLogin.visibility = View.GONE
        btnLogin.text = getString(R.string.login_login)
    }

    private fun showVerifyLogin() {
        etPassword.visibility = View.GONE
        cbPassword.visibility = View.GONE
        rlPassLogin.visibility = View.GONE
        etCode.visibility = View.VISIBLE
        tvGetVerifyCode.visibility = View.VISIBLE
        tvGoPasswordLogin.visibility = View.VISIBLE
        btnLogin.text = getString(R.string.login_login_register)
    }


}