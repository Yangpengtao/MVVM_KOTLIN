package com.oomall.kouclodelivery.ui.view.history_details

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.data.bean.HistoryDetailsBean
import com.oomall.kouclodelivery.ui.adapter.base.BaseRefreshAdapter
import com.oomall.kouclodelivery.ui.adapter.base.BaseViewHolder
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view.history_details.dialog.SelectDateDialog
import com.oomall.kouclodelivery.ui.view_model.HistoryDetailsViewModel
import com.oomall.kouclodelivery.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_history_details.*
import java.util.*

/**
 * 历史明细
 */
class HistoryDetailsActivity : BaseActivity(), View.OnClickListener, BaseRefreshAdapter.OnLoadMoreListener,
    SelectDateDialog.ClickListener {

    lateinit var historyDetailsViewModel: HistoryDetailsViewModel
    private lateinit var baseAdapter: BaseRefreshAdapter<HistoryDetailsBean>
    private lateinit var selectDatePopup: SelectDateDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_details)
        historyDetailsViewModel = ViewModelProviders.of(this).get(HistoryDetailsViewModel::class.java)
        selectDatePopup = SelectDateDialog(this)
        selectDatePopup.setPopClickListener(this)
        selectDatePopup.setDateList(historyDetailsViewModel.getYearList(2015, 4))
        getLeftImg_().visibility = View.VISIBLE
        getRightText_().visibility = View.VISIBLE
        getTitle_().text = "历史订单"
        getRightText_().text = "历史"
        getRightText_().setTextColor(ContextCompat.getColor(this@HistoryDetailsActivity, R.color.colorAccent))
        getRightText_().setOnClickListener(this)
        initAdapter()
        setData()
        initRefresh()
    }

    private fun initRefresh() {
        swipe_refresh_layout.setColorSchemeColors(
            ContextCompat.getColor(
                this@HistoryDetailsActivity,
                R.color.colorAccent
            )
        )
        swipe_refresh_layout.setOnRefreshListener {
            //模拟网路请求
            historyDetailsViewModel.getHistoryDetails()
        }
    }

    private fun setData() {
        historyDetailsViewModel.historyDetails.observe(this@HistoryDetailsActivity, Observer {
            baseAdapter.setmData(it)
            swipe_refresh_layout.isRefreshing = false
        })
    }

    private fun initAdapter() {
        //设置支持上来加载
        baseAdapter = object : BaseRefreshAdapter<HistoryDetailsBean>(
            R.layout.list_item_history_details,
            historyDetailsViewModel.historyDetails.value
        ) {
            override fun onBindViewHolder(holder: BaseViewHolder, data: ArrayList<HistoryDetailsBean>?, position: Int) {

            }
        }
        baseAdapter.setLoadMore(true)
        baseAdapter.setLoadMoreListener(this)
        rv_history_detail.layoutManager = LinearLayoutManager(this@HistoryDetailsActivity)
        rv_history_detail.adapter = baseAdapter
    }

    /**
     * 列表加载失败的重新加载点击事件
     */
    override fun onErrorClick() {
    }

    override fun onLoadMore() {
        historyDetailsViewModel.getHistoryDetails()
    }

    /**
     * 选择时间的回调
     */
    override fun selected(year: Int, month: Int) {
        ToastUtil.show("您选择了 $year 年 $month 月")
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.title_right_text -> {
                selectDatePopup.show()
            }
        }
    }

}
