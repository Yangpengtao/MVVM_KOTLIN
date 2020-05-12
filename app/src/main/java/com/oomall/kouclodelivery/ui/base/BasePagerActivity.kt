package com.oomall.kouclodelivery.ui.base

import androidx.viewpager.widget.ViewPager

abstract class BasePagerActivity : BaseMapLocationActivity(), ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }
}