package com.oomall.kouclodelivery.ui.adapter.base

import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * viewholder父类
 */
class BaseViewHolder
/**
 * 构造函数
 * @param itemView  布局
 */
    (itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * view储存器
     */
    private val mViews: SparseArray<View> = SparseArray()


    /**
     * 根据资源id得到view
     * @param resId  控件id
     * @param <T>   view
     * @return   view
    </T> */
    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(resId: Int): T {
        var v: View? = mViews.get(resId)
        if (v == null) {
            v = itemView.findViewById(resId)
            mViews.put(resId, v)
        }
        return v as T
    }

    /**
     * 直接设置textview的text
     * @param resId  控件id
     * @param text   显示内容
     * @return     本类
     */
    fun setText(resId: Int, text: String): BaseViewHolder {
        var v: View? = mViews.get(resId)
        if (v == null) {
            v = itemView.findViewById(resId)
            mViews.put(resId, v)
        }
        (v as TextView).text = text
        return this
    }

    /**
     * 直接设置ImageView的src
     * @param resId  控件id
     * @param drawableId  图片id
     * @return     本类
     */
    fun setImage(resId: Int, drawableId: Int): BaseViewHolder {
        var v: View? = mViews.get(resId)
        if (v == null) {
            v = itemView.findViewById(resId)
            mViews.put(resId, v)
        }
        (v as ImageView).setImageResource(drawableId)
        return this
    }
}
