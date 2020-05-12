package com.oomall.kouclodelivery.ui.base

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.oomall.kouclodelivery.KoucloApplication
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_base.*

/**
 * 基础类
 */
abstract class BaseActivity : AppCompatActivity(), PermissionUtils.PermissiontCallBack {


    override fun setContentView(layoutResID: Int) {
        val viewRoot = LayoutInflater.from(this).inflate(R.layout.activity_base, null, false)
        val contentView = LayoutInflater.from(this).inflate(layoutResID, null, false)
        val params =
            RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        contentView.layoutParams = params
        val mContainer = viewRoot.findViewById<FrameLayout>(R.id.content_view)
        mContainer.addView(contentView)
        window.setContentView(viewRoot)
    }

    /**
     * 设置标题栏显示状态
     *
     * @param type 显示类型
     */
    fun setTitleVisibility(type: Int) {
        title_parent.visibility = type
    }

    /**
     * 设置标题栏显示状态
     *
     * @param color 颜色
     */
    fun setTitleBgCorlor(color: Int) {
        title_parent.setBackgroundColor(color)
    }


    /**
     * 得到左边text
     *
     * @return
     */
    fun getLeftText_(): TextView {
        return title_left_text
    }

    /**
     * 得到左边img
     *
     * @getRightText_
     */
    fun getLeftImg_(): ImageView {
        return title_left_img
    }

    /**
     * 得到title
     *
     * @return
     */
    fun getTitle_(): TextView {
        return title_content
    }

    /**
     * 得到右侧text
     *
     * @return
     */
    fun getRightText_(): TextView {
        return title_right_text
    }

    /**
     * 得到右侧img
     *
     * @return
     */
    fun getRightImg_(): ImageView {
        return title_right_img
    }


    /**
     * 显示内容
     */
    fun showContent() {
        tv_empty.visibility = View.GONE
        tv_network_error.visibility = View.GONE
        pgb_loading.visibility = View.GONE
    }

    /**
     * 显示加载中
     */
    fun showLoading() {
        tv_empty.visibility = View.GONE
        tv_network_error.visibility = View.GONE
        pgb_loading.visibility = View.VISIBLE
    }

    /**
     * 显示空布局
     */
    fun showEmpty() {
        tv_empty.visibility = View.VISIBLE
        tv_network_error.visibility = View.GONE
        pgb_loading.visibility = View.GONE
    }

    /**
     * 显示空布局
     */
    fun showNetworkError() {
        tv_empty.visibility = View.GONE
        tv_network_error.visibility = View.VISIBLE
        pgb_loading.visibility = View.GONE
    }

    fun startActivity(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        super.startActivity(intent)
    }

    fun startActivityString(clazz: Class<*>, vararg arg: String) {
        val intent = Intent(this, clazz)
        var i = 1
        val bundle: Bundle = Bundle()
        for (o: String in arg) {
            bundle.putString("param$i", o)
            i++
        }
        intent.putExtras(bundle)
        super.startActivity(intent)
    }

    fun startActivityInt(clazz: Class<*>, vararg arg: Int) {
        val intent = Intent(this, clazz)
        var i = 1
        val bundle: Bundle = Bundle()
        for (o: Int in arg) {
            bundle.putInt("param$i", o)
            i++
        }
        intent.putExtras(bundle)
        super.startActivity(intent)
    }

    fun View.backPage() {
        finish()
    }

    private var toast: Toast? = null
    fun showToast(str: String) {
        runOnUiThread {
            if (toast != null) {
                toast!!.cancel()
            }
            toast = Toast.makeText(KoucloApplication.init()!!, str, Toast.LENGTH_SHORT)
            toast!!.show()
        }
    }

    override fun permissionSuccess() {
    }

    override fun permissionFailed() {
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtils.init()!!.permissionAllRequestCode) {
            var isAllGranted = true
            for (grant: Int in grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false
                    break
                }
            }
            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码
                permissionSuccess()
            } else {
                permissionFailed()
            }
        }
    }

}