package com.oomall.kouclodelivery.ui.view.my_wallet

import android.os.Bundle
import android.view.View
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseActivity

/**
 * 新增银行看
 */
class CreateBankActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_confirm -> {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_bank)
        getTitle_().text = "新增银行卡"
        getLeftImg_().visibility = View.VISIBLE
    }
}
