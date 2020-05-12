package com.oomall.kouclodelivery.ui.view.my_wallet

import android.os.Bundle
import android.view.View
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_my_wallet.*

/**
 * 我的钱包
 */
class MyWalletActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)
        getTitle_().text = "我的钱包"
        getLeftImg_().visibility = View.VISIBLE
        setLisener()
    }

    private fun setLisener() {
        tvWithdrawDeposit.setOnClickListener(this)
        tvAccountDetails.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvWithdrawDeposit -> {
                startActivity(WithdrawDepositActivity::class.java)
            }
            R.id.tvAccountDetails -> {
                startActivity(AccountDetailsActivity::class.java)
            }
        }
    }

}
