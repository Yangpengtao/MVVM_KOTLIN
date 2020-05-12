package com.oomall.kouclodelivery.ui.view_model

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oomall.kouclodelivery.data.bean.MainReceivedOrderList
import com.oomall.kouclodelivery.data.bean.MainWaitingOrderList
import com.oomall.kouclodelivery.ui.view.main.MainActivity
import com.oomall.kouclodelivery.ui.adapter.viewpager.ViewPagerAdapter
import com.oomall.kouclodelivery.ui.base.BaseViewModel
import com.oomall.kouclodelivery.ui.view.main.fragment.ReceivedOrderFragment
import com.oomall.kouclodelivery.ui.view.main.fragment.WaitingOrderFragment

/**
 * 主页的逻辑处理
 */
class MainViewModel : BaseViewModel() {


    lateinit var receivedOrderFragment: ReceivedOrderFragment
    lateinit var waitingOrderFragment: WaitingOrderFragment

    private val _waitingOrder = MutableLiveData<ArrayList<MainWaitingOrderList>>()
    private val _receivedOrder = MutableLiveData<ArrayList<MainReceivedOrderList>>()
    private val _bannerList = MutableLiveData<ArrayList<String>>()
    val waitingOrderList: LiveData<ArrayList<MainWaitingOrderList>> = _waitingOrder
    val receivedOrderList: LiveData<ArrayList<MainReceivedOrderList>> = _receivedOrder
    val bannerList: LiveData<ArrayList<String>> = _bannerList

    /*0选中待接单 1，选中已接单*/
    var cuurPosition = 0


    /**
     * 主页初始化待接单和已接单两个fragment
     */
    fun initAdapter(activity: MainActivity): ViewPagerAdapter {
        waitingOrderFragment = WaitingOrderFragment()
        receivedOrderFragment = ReceivedOrderFragment()
        val fragments = arrayOfNulls<Fragment>(2)
        fragments[0] = waitingOrderFragment
        fragments[1] = receivedOrderFragment
        return ViewPagerAdapter(activity.supportFragmentManager, fragments)
    }


    /**
     * 获取以及刷新待接单列表数据
     */
    fun getWaitingOrder() {
//        _get()
        val orderList = arrayListOf<MainWaitingOrderList>()
        for (i in 1..20) {
            orderList.add(MainWaitingOrderList("aaa"))
        }
        _waitingOrder.value = orderList
    }

    /**
     * 获取以及刷新待接单列表数据
     */
    fun getReceivedOrder() {
//        _get()
        val orderList = arrayListOf<MainReceivedOrderList>()
        for (i in 1..20) {
            orderList.add(MainReceivedOrderList("aaa"))
        }
        _receivedOrder.value = orderList
    }


    fun getBannerList(){
        val banner = arrayListOf<String>()
        banner.add("http://b-ssl.duitang.com/uploads/item/201508/09/20150809021146_5TJ3W.jpeg")
        banner.add("http://attachments.gfan.net.cn/forum/attachments2/201301/27/20251961x1xt010vs1x6sc.jpg")
        banner.add("http://img3.ph.126.net/redzAiFogChwrzrFOs5Wcg==/2608991559148818243.jpg")
        banner.add("http://b-ssl.duitang.com/uploads/blog/201412/27/20141227174553_auCHe.jpeg")
        _bannerList.value=banner
    }



}