package com.oomall.kouclodelivery.ui.view.history_details.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.adapter.base.BaseAdapter
import com.oomall.kouclodelivery.ui.adapter.base.BaseViewHolder
import com.oomall.kouclodelivery.ui.view_model.HistoryDetailsViewModel
import java.util.*


/**
 * 选择日期
 */
class SelectDateDialog(context: Context) : Dialog(context) {

    private var callback: ClickListener? = null
    private var year: Int = 0
    private var month: Int = 0

    /*年月*/
    private var tvYear: TextView? = null
    private var tvMonth: TextView? = null
    /*年月*/
    private var rvYear: RecyclerView? = null
    private var rvMonth: RecyclerView? = null

    private val yearAdapter =
        object : BaseAdapter<HistoryDetailsViewModel.YearBean>(R.layout.list_item_pop_date, ArrayList()) {
            override fun onBindViewHolder(
                holder: BaseViewHolder,
                data: ArrayList<HistoryDetailsViewModel.YearBean>?,
                position: Int
            ) {
                holder.setText(R.id.tv_name, data!![position].year.toString() + "年")
                holder.getView<View>(R.id.tv_name).setOnClickListener {
                    rvMonth!!.visibility = View.VISIBLE
                    rvYear!!.visibility = View.GONE
                    tvYear!!.text = data[position].year.toString()
                    tvYear!!.visibility = View.VISIBLE
                    year = data[position].year
                    monthAdapter.setmData(data[position].monthList)
                }
            }
        }
    private val monthAdapter =
        object : BaseAdapter<HistoryDetailsViewModel.MonthBean>(R.layout.list_item_pop_date, ArrayList()) {
            override fun onBindViewHolder(
                holder: BaseViewHolder,
                data: ArrayList<HistoryDetailsViewModel.MonthBean>?,
                position: Int
            ) {
                holder.setText(R.id.tv_name, data!![position].month.toString() + "月")
                holder.getView<View>(R.id.tv_name).setOnClickListener {
                    month = data[position].month
                    callback!!.selected(year, month)
                    tvYear!!.visibility = View.GONE
                    rvMonth!!.visibility = View.GONE
                    rvYear!!.visibility = View.VISIBLE
                    dismiss()
                }
            }
        }

    private val listener = View.OnClickListener { view ->
        when (view.id) {
            R.id.tv_year -> {
                rvYear!!.visibility = View.VISIBLE
                rvMonth!!.visibility = View.GONE
                tvMonth!!.visibility = View.GONE

            }
            R.id.iv_close -> dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_date)
        setParams()

        tvYear = findViewById(R.id.tv_year)
        tvMonth = findViewById(R.id.tv_month)
        rvYear = findViewById(R.id.rv_year)
        rvMonth = findViewById(R.id.rv_month)
        val iv_close = findViewById<ImageView>(R.id.iv_close)
        rvYear!!.layoutManager = LinearLayoutManager(context)
        rvYear!!.adapter = yearAdapter
        rvMonth!!.layoutManager = LinearLayoutManager(context)
        rvMonth!!.adapter = monthAdapter
        iv_close.setOnClickListener(listener)
        tvYear!!.setOnClickListener(listener)
    }

    private fun setParams() {
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        val window = this.window!!
        window.setGravity(Gravity.BOTTOM)
        window.setWindowAnimations(R.style.PopBottom)

        val params = window.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = params
    }




    fun setDateList(dateList: ArrayList<HistoryDetailsViewModel.YearBean>) {
        yearAdapter.mData = dateList
    }

    fun setPopClickListener(callback: ClickListener) {
        this.callback = callback
    }

    interface ClickListener {
        fun selected(year: Int, month: Int)
    }

}
