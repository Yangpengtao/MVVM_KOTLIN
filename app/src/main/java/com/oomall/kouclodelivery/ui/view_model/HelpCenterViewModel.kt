package com.oomall.kouclodelivery.ui.view_model

import androidx.fragment.app.Fragment
import com.oomall.kouclodelivery.ui.adapter.viewpager.ViewPagerAdapter
import com.oomall.kouclodelivery.ui.base.BaseViewModel
import com.oomall.kouclodelivery.ui.view.help_center.HelpCenterActivity
import com.oomall.kouclodelivery.ui.view.help_center.fragment.ExceptionHandlingFragment
import com.oomall.kouclodelivery.ui.view.help_center.fragment.PlatformInstructionsFragment
import com.oomall.kouclodelivery.ui.view.help_center.fragment.RuleAnalysisFragment

/**
 * 帮助中心
 */
class HelpCenterViewModel : BaseViewModel() {
    lateinit var platformInstructionsFragment: PlatformInstructionsFragment
    lateinit var ruleAnalysisFragment: RuleAnalysisFragment
    lateinit var exceptionHandlingFragment: ExceptionHandlingFragment
    val tabLable = arrayOf("平台须知", "规则解析", "异常处理")


    /**
     * 主页初始化待接单和已接单两个fragment
     */
    fun initAdapter(activity: HelpCenterActivity): ViewPagerAdapter {
        platformInstructionsFragment = PlatformInstructionsFragment()
        ruleAnalysisFragment = RuleAnalysisFragment()
        exceptionHandlingFragment = ExceptionHandlingFragment()
        val fragments = arrayOfNulls<Fragment>(3)
        fragments[0] = platformInstructionsFragment
        fragments[1] = ruleAnalysisFragment
        fragments[2] = exceptionHandlingFragment
        return ViewPagerAdapter(activity.supportFragmentManager, fragments)
    }
}