package com.oomall.kouclodelivery.ui.view.popularize

import android.os.Bundle
import android.view.View
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseActivity

/**
 * 推广赚钱
 */
class PopularizeActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.title_right_text -> {
                startActivity(MyInvitationActivity::class.java)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popularize)
        getTitle_().text = "推广赚钱"
        getLeftImg_().visibility = View.VISIBLE
        getRightText_().visibility = View.VISIBLE
        getRightText_().text = "我的邀请"
        getRightText_().setOnClickListener(this)
    }

}
