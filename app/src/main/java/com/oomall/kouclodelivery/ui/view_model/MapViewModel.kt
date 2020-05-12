package com.oomall.kouclodelivery.ui.view_model

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.RidePath
import com.amap.api.services.route.RideRouteResult
import com.amap.api.services.route.RouteSearch
import com.oomall.kouclodelivery.tools.map.overlay.ToBuyerOverlay
import com.oomall.kouclodelivery.tools.map.overlay.ToShopOverlay
import com.oomall.kouclodelivery.ui.base.BaseMapActivity
import com.oomall.kouclodelivery.ui.base.BaseMapLocationActivity
import com.oomall.kouclodelivery.ui.base.BaseViewModel

/**
 * 关于地图的数据处理
 */
class MapViewModel : BaseViewModel() {


    companion object {
        const val QUERY_TO_SHOP = 1
        const val QUERY_TO_BUYER = 2
        const val LOCATION_INTERVAL=15000L
    }

    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null

    var aMap: AMap? = null

    var currQueryRouteLine = QUERY_TO_SHOP
    var mRouteSearch: RouteSearch? = null


    /**
     * 开始定位
     * @param context
     * @param 是否单次定位
     */
    fun startLocation(context: BaseMapLocationActivity, isOnce : Boolean) {
        //初始化定位
        mLocationClient = AMapLocationClient(context)
        //初始化定位参数
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption!!.isOnceLocation = isOnce
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        if (!isOnce) mLocationOption!!.interval = LOCATION_INTERVAL
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption!!.isOnceLocationLatest = true

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption!!.isNeedAddress = false
        if (null != mLocationClient) {
            mLocationClient!!.setLocationOption(mLocationOption)
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient!!.stopLocation()
            mLocationClient!!.startLocation()
        }
        //设置定位回调监听
        mLocationClient!!.setLocationListener(context)
    }


    /**
     * 请求当前位置到店铺的
     * @param lat 当前维度
     * @param long 当前精度
     */
    fun calculateToShopRouteLIne(lat: Double, long: Double) {
        val fromAndTo = RouteSearch.FromAndTo(LatLonPoint(lat, long), LatLonPoint(39.917337, 116.397056))
        val query = RouteSearch.RideRouteQuery(fromAndTo )
        mRouteSearch!!.calculateRideRouteAsyn(query)
        currQueryRouteLine = QUERY_TO_SHOP
    }

    /**
     * 绘制当前位置到店铺的
     */
    fun drawToShopRouteLIne(context: BaseMapActivity, ridePath: RidePath, result: RideRouteResult) {
        val rideRouteOverlay = ToShopOverlay(
            context, aMap, ridePath,
            result.startPos,
            result.targetPos
        )
        rideRouteOverlay.removeFromMap()
        rideRouteOverlay.addToMap()
        rideRouteOverlay.zoomToSpan()
        calculateToBuerRouteLIne(1.0,1.0)
    }

    /**
     * 请求 店铺到买家的
     * @param lat 当前维度
     * @param long 当前精度
     */
    private fun calculateToBuerRouteLIne(lat: Double, long: Double) {
        val fromAndTo1 = RouteSearch.FromAndTo(LatLonPoint(39.917337, 116.397056), LatLonPoint(40.041986, 116.414496))
        val query1 = RouteSearch.RideRouteQuery(fromAndTo1 )
        mRouteSearch!!.calculateRideRouteAsyn(query1)
        currQueryRouteLine = QUERY_TO_BUYER
    }

    /**
     * 绘制到买家的
     */
    fun drawToBuerRouteLIne(context: BaseMapActivity, ridePath: RidePath, result: RideRouteResult) {
        val rideRouteOverlay = ToBuyerOverlay(
            context, aMap, ridePath,
            result.startPos,
            result.targetPos
        )
        rideRouteOverlay.addToMap()
    }


    internal var p1 = LatLng(39.993266, 116.473193)//首开广场
    internal var p2 = LatLng(39.917337, 116.397056)//故宫博物院
    internal var p3 = LatLng(39.904556, 116.427231)//北京站
    internal var p4 = LatLng(39.773801, 116.368984)//新三余公园(南5环)
    internal var p5 = LatLng(40.041986, 116.414496)//立水桥(北5环)
}