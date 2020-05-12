package com.oomall.kouclodelivery.ui.view.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.google.android.material.navigation.NavigationView
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BasePagerActivity
import com.oomall.kouclodelivery.ui.view.help_center.HelpCenterActivity
import com.oomall.kouclodelivery.ui.view.history_details.HistoryDetailsActivity
import com.oomall.kouclodelivery.ui.view.my_wallet.MyWalletActivity
import com.oomall.kouclodelivery.ui.view.popularize.PopularizeActivity
import com.oomall.kouclodelivery.ui.view.setting.SettingActivity
import com.oomall.kouclodelivery.ui.view_model.MainViewModel
import com.oomall.kouclodelivery.ui.view_model.MapViewModel
import com.oomall.kouclodelivery.utils.LogPrinter
import com.oomall.kouclodelivery.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 主页面
 * 主要功能：待接单，已接单,侧栏两个模块
 */
class MainActivity : BasePagerActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener, AMapLocationListener {

    lateinit var mViewViewModel: MainViewModel
    lateinit var mapViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        setTitleVisibility(View.GONE)
        vp_body.adapter = mViewViewModel.initAdapter(this)
        // 禁止手势滑动
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setListener()
        selectPosition(mViewViewModel.cuurPosition)
        mapViewModel.startLocation(this@MainActivity, false)
    }

    /**
     * 设置监听
     */
    private fun setListener() {
        vp_body.addOnPageChangeListener(this)
        tv_waiting_order.setOnClickListener(this)
        tv_received_order.setOnClickListener(this)
        nav_view.setNavigationItemSelectedListener(this)
        val imageView = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.imageView)
        imageView.setOnClickListener(this)
    }

    /**
     * 切换待接单&已接单
     */
    private fun selectPosition(position: Int) {
        if (position == 0) {
            tv_waiting_order.isSelected = true
            tv_received_order.isSelected = false
            vp_body.currentItem = 0
        } else {
            tv_waiting_order.isSelected = false
            tv_received_order.isSelected = true
            vp_body.currentItem = 1
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tv_waiting_order -> {
                mViewViewModel.cuurPosition = 0
                selectPosition(0)
            }
            R.id.tv_received_order -> {
                mViewViewModel.cuurPosition = 1
                selectPosition(1)
            }
            R.id.imageView -> {
                ToastUtil.show("点击头像")
                drawer_layout.closeDrawer(GravityCompat.START)
            }
        }
    }

    /**
     * 侧菜单栏点击事件
     */
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            /* R.id.main_menu_pickup_setting -> {
                 ToastUtil.show("main_menu_pickup_setting")
             }*/
            R.id.main_menu_historical_details -> {
                startActivity(HistoryDetailsActivity::class.java)
            }
            R.id.main_menu_my_wallet -> {
                startActivity(MyWalletActivity::class.java)
            }
            R.id.main_menu_help_center -> {
                startActivity(HelpCenterActivity::class.java)
            }
            /* R.id.main_menu_platform_Instruction -> {
                 ToastUtil.show("")
             }*/
            R.id.main_menu_popularize -> {
                startActivity(PopularizeActivity::class.java)
            }
            /* R.id.main_menu_value_service -> {
                 ToastUtil.show("")
             }*/
            R.id.main_menu_setting -> {
                startActivity(SettingActivity::class.java)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return false
    }

    fun View.openDrawer() {
        drawer_layout.openDrawer(nav_view)
    }

    fun View.openMsg() {
        ToastUtil.show("打开消息")
    }

    /*Viewpager 监听开始*/
    override fun onPageSelected(position: Int) {
        selectPosition(position)
    }
    /*Viewpager 监听结束*/


    /**
     * 定位回调
     */
    override fun onLocationChanged(p0: AMapLocation?) {
        if (p0 != null) {
            if (p0.errorCode == 0) {
                LogPrinter.e(localClassName, "维度：${p0.latitude} 经度${p0.longitude}")
            } else {
                LogPrinter.e(localClassName, "定位错误，错误信息${p0.adCode}")
                //定位错误
            }
        }

    }


}
