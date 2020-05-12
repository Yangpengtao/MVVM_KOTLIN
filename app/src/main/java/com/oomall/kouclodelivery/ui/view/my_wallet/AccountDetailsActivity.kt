package com.oomall.kouclodelivery.ui.view.my_wallet

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.amap.api.location.AMapLocation
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.data.bean.MainWaitingOrderList
import com.oomall.kouclodelivery.ui.adapter.base.BaseAdapter
import com.oomall.kouclodelivery.ui.adapter.base.BaseViewHolder
import com.oomall.kouclodelivery.ui.base.BasePagerActivity
import com.oomall.kouclodelivery.ui.view_model.WalletViewModel
import kotlinx.android.synthetic.main.activity_account_details.*
import kotlinx.android.synthetic.main.fragment_waiting_order.*
import java.util.ArrayList

/**
 * 账户明细
 */
class AccountDetailsActivity : BasePagerActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {


    lateinit var walletViewModel: WalletViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_details)
        walletViewModel = ViewModelProviders.of(this).get(WalletViewModel::class.java)
        getTitle_().text = "账目明细"
        getLeftImg_().visibility = View.VISIBLE
        vpBody.adapter = walletViewModel.initAdapter(this)
        vpBody.addOnPageChangeListener(this)
        tvSzDetails.setOnClickListener(this)
        tvWithdrawHis.setOnClickListener(this)
        setPosition(0)
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvSzDetails -> {
                setPosition(0)
                vpBody.currentItem = 0
            }
            R.id.tvWithdrawHis -> {
                setPosition(1)
                vpBody.currentItem = 1
            }
        }
    }


    override fun onPageSelected(position: Int) {
        setPosition(position)
    }

    private fun setPosition(position: Int) {
        when (position) {
            0 -> {
                tvSzDetails.isSelected = true
                tvWithdrawHis.isSelected = false
            }
            1 -> {
                tvSzDetails.isSelected = false
                tvWithdrawHis.isSelected = true
            }
        }
    }


}
