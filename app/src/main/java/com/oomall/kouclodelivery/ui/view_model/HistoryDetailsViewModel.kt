package com.oomall.kouclodelivery.ui.view_model

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oomall.kouclodelivery.data.bean.HistoryDetailsBean
import com.oomall.kouclodelivery.ui.base.BaseViewModel
import java.time.Month
import java.util.*
import java.util.logging.LogRecord
import kotlin.collections.ArrayList


/**
 * 历史明细
 */
class HistoryDetailsViewModel : BaseViewModel() {

    private val _historyDetails = MutableLiveData<ArrayList<HistoryDetailsBean>>()
    val historyDetails: LiveData<ArrayList<HistoryDetailsBean>> = _historyDetails
    var historyData = arrayListOf<HistoryDetailsBean>()
    var datelist = arrayListOf<YearBean>()


    fun getHistoryDetails() {
//        _get()
        mHandler.postDelayed(Runnable {
            mHandler.sendEmptyMessage(1)
        }, 1000)
    }

    var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            for (i in 1..3) {
                historyData.add(HistoryDetailsBean("aa"))
            }
            historyData.addAll(historyData)
            _historyDetails.value = historyData
        }

    }

    fun getYearList(startYear: Int, startMonth: Int): ArrayList<YearBean> {
        datelist.clear()
        val mCalendar: Calendar = Calendar.getInstance()
        val currMonth = mCalendar.get(Calendar.MONTH) + 1
        val currYear = mCalendar.get(Calendar.YEAR)
        for (i in currYear downTo startYear) {
            val monthList = arrayListOf<MonthBean>()
            when (i) {
                currYear -> for (j in 1..currMonth) {
                    monthList.add(MonthBean(j))
                }
                startYear -> for (c in startMonth..12) {
                    monthList.add(MonthBean(c))
                }
                else -> for (k in 1..12) {
                    monthList.add(MonthBean(k))
                }
            }
            datelist.add(YearBean(i, monthList))
        }
        return datelist
    }


    data class YearBean(
        val year: Int,
        val monthList: ArrayList<MonthBean>
    )

    data class MonthBean(
        val month: Int
    )

}