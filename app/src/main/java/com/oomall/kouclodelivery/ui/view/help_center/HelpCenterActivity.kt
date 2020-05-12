package com.oomall.kouclodelivery.ui.view.help_center

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view_model.HelpCenterViewModel
import kotlinx.android.synthetic.main.activity_help_center.*

/**
 * 帮助中心
 */
class HelpCenterActivity : BaseActivity() {


    lateinit var helpCenterViewModel: HelpCenterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_center)
        helpCenterViewModel = ViewModelProviders.of(this).get(HelpCenterViewModel::class.java)
        getTitle_().text = "帮助中心"
        getLeftImg_().visibility = View.VISIBLE
        vpBody.adapter = helpCenterViewModel.initAdapter(this@HelpCenterActivity)
        tabMode.setupWithViewPager(vpBody)
        for (i in 0..2) {
            tabMode.getTabAt(i)!!.text = helpCenterViewModel.tabLable[i]
        }
    }

}
