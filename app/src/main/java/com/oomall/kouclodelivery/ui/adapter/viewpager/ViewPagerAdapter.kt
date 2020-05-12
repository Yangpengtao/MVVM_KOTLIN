package com.oomall.kouclodelivery.ui.adapter.viewpager


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * viewPager通用适配器
 */
class ViewPagerAdapter(fm: FragmentManager, private val mFragments: Array<Fragment?>) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment = mFragments[position]!!
    override fun getCount(): Int = mFragments.size
}
