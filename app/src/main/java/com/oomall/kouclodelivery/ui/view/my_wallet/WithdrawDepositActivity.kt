package com.oomall.kouclodelivery.ui.view.my_wallet

import android.os.Bundle
import android.view.View
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseActivity

/**
 * 提现
 */
class WithdrawDepositActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw_deposit)
        getTitle_().text = "提现"
        getLeftImg_().visibility = View.VISIBLE
        getRightText_().visibility = View.VISIBLE
        getRightText_().text = "银行卡"
        getRightText_().setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.title_right_text -> {
                startActivity(CreateBankActivity::class.java)
            }
        }
    }


}
