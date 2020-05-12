package com.oomall.kouclodelivery.ui.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.view.account.LoginActivity
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view_model.LauncherViewModel
import com.oomall.kouclodelivery.utils.PermissionUtils
import com.oomall.kouclodelivery.utils.ToastUtil

/**
 * 启动页
 */
class LauncherActivity : BaseActivity() {

    lateinit var mViewHolder: LauncherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        setTitleVisibility(View.GONE)
        mViewHolder = ViewModelProviders.of(this).get(LauncherViewModel::class.java)
        PermissionUtils.init()!!.requestSystemPermissiont(this, this)

        mViewHolder.keyState.observe(this, Observer {
            if (it) {
                mViewHolder.startTimer()
            }
        })
        mViewHolder.timerState.observe(this, Observer {
            if (it) {
                finish()
                startActivity(LoginActivity::class.java)
            }
        })
    }

    override fun permissionSuccess() {
        mViewHolder.getKey()
    }

    override fun permissionFailed() {
        // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
        ToastUtil.show("您取消了权限，请到设置里打开，否则无法使用")
        finish()
        startActivity(LoginActivity::class.java)
    }


}
