package com.oomall.kouclodelivery.tools.map.overlay;

import android.content.Context;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.*;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideStep;
import com.oomall.kouclodelivery.R;
import com.oomall.kouclodelivery.tools.map.utils.AMapUtil;

import java.util.List;

/**
 * 从当前位置到商家的骑行路线图层类。
 */
public class ToShopOverlay extends RouteOverlay {

    private PolylineOptions mPolylineOptions;

    private BitmapDescriptor rideStationDescriptor = null;

    private RidePath ridePath;

    /**
     * 通过此构造函数创建骑行路线图层。
     *
     * @param context 当前activity。
     * @param amap    地图对象。
     * @param path    骑行路线规划的一个方案。详见搜索服务模块的路径查询包     、
     * @param start   起点。详见搜索服务模块的核心基础包
     * @param end     终点。详见搜索服务模块的核心基础包
     * @since V3.5.0
     */
    public ToShopOverlay(Context context, AMap amap, RidePath path,
                         LatLonPoint start, LatLonPoint end) {
        super(context);
        this.mAMap = amap;
        this.ridePath = path;
        startPoint = AMapUtil.convertToLatLng(start);
        endPoint = AMapUtil.convertToLatLng(end);
    }

    /**
     * 添加骑行路线到地图中。
     *
     * @since V3.5.0
     */
    public void addToMap() {
        initPolylineOptions();
        try {
            List<RideStep> ridePaths = ridePath.getSteps();
            mPolylineOptions.add(startPoint);
            for (int i = 0; i < ridePaths.size(); i++) {
                RideStep rideStep = ridePaths.get(i);
                //添加路线上的骑车标识
                /*LatLng latLng = AMapUtil.convertToLatLng(rideStep
                        .getPolyline().get(0));
                addRideStationMarkers(rideStep, latLng);*/
                addRidePolyLines(rideStep);
            }
            mPolylineOptions.add(endPoint);
            //设置当前位置
            addCurrMarker();

            //绘制路线
            showPolyline();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    /**
     * @param rideStep
     */
    private void addRidePolyLines(RideStep rideStep) {
        mPolylineOptions.addAll(AMapUtil.convertArrList(rideStep.getPolyline()));
    }

    /**
     * 在每个节点添加骑行标识
     *
     * @param rideStep
     * @param position
     */
    private void addRideStationMarkers(RideStep rideStep, LatLng position) {
        addStationMarker(new MarkerOptions()
                .position(position)
                .title("\u65B9\u5411:" + rideStep.getAction()
                        + "\n\u9053\u8DEF:" + rideStep.getRoad())
                .snippet(rideStep.getInstruction()).visible(nodeIconVisible)
                .anchor(0.5f, 0.5f).icon(rideStationDescriptor));
    }

    /**
     * 初始化线段属性
     */
    private void initPolylineOptions() {
        rideStationDescriptor = getRideBitmapDescriptor();
        if (rideStationDescriptor == null) {
            rideStationDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.amap_ride);
        }
        mPolylineOptions = null;
        mPolylineOptions = new PolylineOptions();
        mPolylineOptions.color(getToShopColor()).width(getRouteWidth());
    }

    private void showPolyline() {
        addPolyLine(mPolylineOptions);
    }
}
