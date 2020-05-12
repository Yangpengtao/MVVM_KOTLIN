package com.oomall.kouclodelivery.ui.view_model

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oomall.kouclodelivery.data.bean.SzDetailsBean
import com.oomall.kouclodelivery.data.bean.WithdrawHistoryBean
import com.oomall.kouclodelivery.ui.adapter.viewpager.ViewPagerAdapter
import com.oomall.kouclodelivery.ui.base.BaseViewModel
import com.oomall.kouclodelivery.ui.view.my_wallet.AccountDetailsActivity
import com.oomall.kouclodelivery.ui.view.my_wallet.fragment.SzDetailsFragment
import com.oomall.kouclodelivery.ui.view.my_wallet.fragment.WithdrawHistoryFragment

/**
 * 钱包 数据、逻辑处理
 */
class WalletViewModel : BaseViewModel() {
    lateinit var szDetailsFragment: SzDetailsFragment
    lateinit var withdrawHistoryFragment: WithdrawHistoryFragment

    private val _szDetails = MutableLiveData<ArrayList<SzDetailsBean>>()
    private val _withdrawHistory = MutableLiveData<ArrayList<WithdrawHistoryBean>>()
    val szDetails: LiveData<ArrayList<SzDetailsBean>> = _szDetails
    val withdrawHistory: LiveData<ArrayList<WithdrawHistoryBean>> = _withdrawHistory


    /**
     * 主页初始化待接单和已接单两个fragment
     */
    fun initAdapter(activity: AccountDetailsActivity): ViewPagerAdapter {
        szDetailsFragment = SzDetailsFragment()
        withdrawHistoryFragment = WithdrawHistoryFragment()
        val fragments = arrayOfNulls<Fragment>(2)
        fragments[0] = szDetailsFragment
        fragments[1] = withdrawHistoryFragment
        return ViewPagerAdapter(activity.supportFragmentManager, fragments)
    }

    /**
     * 获取收支明细
     */
    fun getSzDetails() {
//        _get()
        var orderList = arrayListOf<SzDetailsBean>()
        for (i in 1..20) {
            orderList.add(SzDetailsBean("aaa"))
        }
        _szDetails.value = orderList
    }

    /**
     * 获取提现记录
     */
    fun getWithdtawHistory() {
//        _get()
        var orderList = arrayListOf<WithdrawHistoryBean>()
        for (i in 1..20) {
            orderList.add(WithdrawHistoryBean("aaa"))
        }
        _withdrawHistory.value = orderList
    }


}