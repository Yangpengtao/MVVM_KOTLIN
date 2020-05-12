package com.oomall.kouclodelivery.ui.adapter.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

/**
 * 适配器父类
 * Created by ypt on 2017/9/6.
 */
abstract class BaseAdapter<T>
/**
 * 构造器
 *
 * @param resId layout资源id
 * @param data  数据
 */
    (
    /**
     * layout资源id
     */
    private val layoutResId: Int,
    /**
     * 数据
     */
    var mData: ArrayList<T>?
) : RecyclerView.Adapter<BaseViewHolder>() {

    fun setmData(data: ArrayList<T>?) {
        if (data != null) {
            this.mData = data
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return BaseViewHolder(view)
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        onBindViewHolder(holder, mData, position)
    }

    override fun getItemCount(): Int {
        return if (mData == null) 0 else mData!!.size
    }

    abstract fun onBindViewHolder(holder: BaseViewHolder, data: ArrayList<T>?, position: Int)
}
