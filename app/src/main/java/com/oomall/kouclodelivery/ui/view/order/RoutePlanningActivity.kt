package com.oomall.kouclodelivery.ui.view.order

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.amap.api.location.AMapLocation
import com.amap.api.services.route.RideRouteResult
import com.amap.api.services.route.RouteSearch
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseMapActivity
import com.oomall.kouclodelivery.ui.view_model.MapViewModel
import com.oomall.kouclodelivery.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_route_planning.*

/**
 * 路线规划
 */
class RoutePlanningActivity : BaseMapActivity(), View.OnClickListener {


    lateinit var mapViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_planning)
        setTitleVisibility(View.GONE)
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        mMapView = route_map_view
        mMapView!!.onCreate(savedInstanceState)
        mapSetting()
        mapViewModel.startLocation(this,true)
        btn_go_navi.setOnClickListener(this)
    }


    private fun mapSetting() {
        mapViewModel.aMap = route_map_view.map
        mapViewModel.aMap!!.uiSettings.isZoomControlsEnabled = false
        //c初始化骑行线路规划
        mapViewModel.mRouteSearch = RouteSearch(this)
        mapViewModel.mRouteSearch!!.setRouteSearchListener(this)
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
                ToastUtil.show("定位错误")
            }
        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_go_navi -> {
                ToastUtil.show("去导航")
            }
        }
    }

}
