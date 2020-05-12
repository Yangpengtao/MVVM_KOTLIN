package com.oomall.kouclodelivery.ui.view.order

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import com.amap.api.location.AMapLocation
import com.amap.api.services.route.RideRouteResult
import com.amap.api.services.route.RouteSearch
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseMapActivity
import com.oomall.kouclodelivery.ui.view_model.MapViewModel
import com.oomall.kouclodelivery.ui.view_model.OrderViewModel
import com.oomall.kouclodelivery.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.layout_order_detail_nav.*


/**
 * 订单详情
 */
class OrderDetailsActivity : BaseMapActivity(), View.OnClickListener {


    lateinit var orderViewModel: OrderViewModel
    lateinit var mapViewModel: MapViewModel

    private var llRouteCalculate: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)
        setTitle()
        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel::class.java)
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        /*到时候根绝订单状态初始化相应布局*/
        loadView()
        mMapView = llRouteCalculate!!.findViewById(R.id.map_view)
        mMapView!!.onCreate(savedInstanceState)
        mapSetting()
        setLisener()
        mapViewModel.startLocation(this,true)
    }

    private fun setLisener() {
        iv_go_route_planing.setOnClickListener(this)
    }


    private fun loadView() {
        llRouteCalculate = vs_route_calculate.inflate() as LinearLayout
        vs_order_dispatching.inflate()
        vs_goods_details.inflate()
        vs_order_details.inflate()
        vs_earning.inflate()
    }

    private fun mapSetting() {
        mapViewModel.aMap = map_view.map
        mapViewModel.aMap!!.uiSettings.isZoomControlsEnabled = false
        //c初始化骑行线路规划
        mapViewModel.mRouteSearch = RouteSearch(this)
        mapViewModel.mRouteSearch!!.setRouteSearchListener(this)
        //解决滑动冲突
        mapContainer.setScrollView(scrollView)
    }


    /**
     * 骑车路线规划成功
     */
    override fun onRideRouteSearched(result: RideRouteResult?, errorCode: Int) {
        super.onRideRouteSearched(result, errorCode)
        if (result != null && result.paths != null) {
            if (result.paths.size > 0) {
                val ridePath = result.paths[0]
                when (mapViewModel.currQueryRouteLine) {
                    MapViewModel.QUERY_TO_SHOP -> {
                        mapViewModel.drawToShopRouteLIne(this, ridePath, result)
                    }
                    MapViewModel.QUERY_TO_BUYER -> {
                        mapViewModel.drawToBuerRouteLIne(this, ridePath, result)
                    }
                }
            }
        }
    }

    /**
     * 定位回调
     */
    override fun onLocationChanged(p0: AMapLocation?) {
        if (p0 != null) {
            if (p0.errorCode == 0) {
                mapViewModel.calculateToShopRouteLIne(p0.latitude, p0.longitude)
            } else {
                //定位错误
            }
        }

    }

    private fun setTitle() {
        getLeftImg_().visibility = View.VISIBLE
        getTitle_().text = "订单详情"
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.image11111 -> {
                ToastUtil.show("fffff")
            }
            R.id.iv_go_route_planing -> {
                startActivity(RoutePlanningActivity::class.java)
            }
        }
    }

}
