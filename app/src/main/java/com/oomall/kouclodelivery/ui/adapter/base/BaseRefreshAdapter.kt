package com.oomall.kouclodelivery.ui.adapter.base

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oomall.kouclodelivery.R


import java.util.ArrayList

/**
 * 适配器父类
 * Created by ypt on 2017/9/6.
 */
abstract class BaseRefreshAdapter<T>
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
    private var mData: ArrayList<T>?
) : RecyclerView.Adapter<BaseViewHolder>() {
    /*可以加载更多的viewType*/
    private val LOAD_TYPE = -1
    /*不可以加载更多的viewType*/
    private val NORMAL_TYPE = -2

    private var LOAD_MORE = "加载更多"
    private var LOADIND = "正在加载..."
    private var LOAD_NO_MORE = "没有更多内容了"
    private var LOAD_ERROR = "加载失败，点击重试"
    /*下拉刷新的状态1，加载更多2，显示正在加载3，显示没有更多内容了*/
    private var loadType = 1
    /**
     * 是否开启加载更多
     */
    private var isLoadMore = false
    /**
     * 加载更多回调参数
     */
    private var onLoadMoreListener: OnLoadMoreListener? = null
    /**
     * 设置底部的回调
     */
    private var onSetBottomListener: OnSetBottomListener? = null
    /**
     * 正在刷新
     */
    /**
     * 上拉刷新的时候设置为true，不然会出错.刷新完成设置为false
     *
     * @param refreshing
     */
    var isRefreshing = false
    /**
     * 父布局
     */
    private var parent: ViewGroup? = null

    /**
     * 底部布局
     */
    private var footer: View? = null

    /**
     * 设置布局显示文本
     *
     * @param loadType
     */
    fun setLoadType(loadType: Int) {
        this.loadType = loadType
    }

    fun setmData(data: ArrayList<T>?) {
        if (data != null) {
            this.mData = ArrayList()
            this.mData!!.addAll(data)
            val handler1 = Handler()
            val r = Runnable { notifyDataSetChanged() }
            handler1.post(r)
        }
    }

    /**
     * 设置支持加载更多
     *
     * @param loadMore
     */
    fun setLoadMore(loadMore: Boolean) {
        this.isLoadMore = loadMore
    }

    /**
     * 设置底部布局
     * 自定义布局的view_id必须和默认的id名称一致
     *
     * @param layoutResId
     */
    fun setFooter(layoutResId: Int) {
        footer = LayoutInflater.from(parent!!.context).inflate(layoutResId, parent, false)
    }


    /**
     * 加载完成时调用
     */
    fun showComplete() {
        isRefreshing = false
        setLoadType(LOADE_MORE_TYPE)
        val handler1 = Handler()
        val r = Runnable { notifyDataSetChanged() }
        handler1.post(r)
    }

    /**
     * 没有更多
     */
    fun showNoMore() {
        isRefreshing = false
        setLoadType(LOAD_NO_MORE_TYPE)
        val handler1 = Handler()
        val r = Runnable { notifyDataSetChanged() }
        handler1.post(r)
    }

    /**
     * 显示错误。点击重试
     */
    fun showError() {
        isRefreshing = false
        setLoadType(LOAD_ERROR_TYPE)
        val handler1 = Handler()
        val r = Runnable { notifyDataSetChanged() }
        handler1.post(r)
    }

    /**
     * 设置加载更多文本
     *
     * @param LOAD_MORE
     */
    fun setLoadMore(LOAD_MORE: String) {
        this.LOAD_MORE = LOAD_MORE
    }

    /**
     * 设置没有更多内容了文本
     *
     * @param LOAD_NO_MORE
     */
    fun setLoadNoMore(LOAD_NO_MORE: String) {
        this.LOAD_NO_MORE = LOAD_NO_MORE
    }

    /**
     * 设置加载中文本
     *
     * @param LOADIND
     */
    fun setLoading(LOADIND: String) {
        this.LOADIND = LOADIND
    }


    /**
     * 设置加载失败的文本
     *
     * @param LOAD_ERROR
     */
    fun setLoadError(LOAD_ERROR: String) {
        this.LOAD_ERROR = LOAD_ERROR
    }

    fun setLoadMoreListener(loadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = loadMoreListener
    }

    fun setOnSetBottomListener(onSetBottomListener: OnSetBottomListener) {
        this.onSetBottomListener = onSetBottomListener
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoadMore) {
            if (position == this.mData!!.size) { //如果支持加载更多，并且是最后一个
                LOAD_TYPE
            } else {
                getViewType(position)
            }
        } else {
            getViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        this.parent = parent
        var view: View? = null
        when (viewType) {
            NORMAL_TYPE -> view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
            LOAD_TYPE -> view = onCreateView(parent, viewType)
        }
        return BaseViewHolder(view!!)
    }

    /**
     * 重写此方法和onCreateView可实现viewtype功能
     *
     * @param position
     * @return
     */
    private fun getViewType(position: Int): Int {
        when (position) {
            0 -> {

            }
            else -> {

            }
        }
        return NORMAL_TYPE
    }

    /**
     * 重写此方法和getViewType可实现viewtype功能
     *
     * @param parent
     * @param viewType
     * @return
     */
    private fun onCreateView(parent: ViewGroup, viewType: Int): View {
        when (viewType) {
            1 -> {

            }
        }
        return LayoutInflater.from(parent.context).inflate(R.layout.base_adapter_normal_footer, parent, false)
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        //如果加载更多开关开启&&没有在刷新中&&绘制下标和是最后一个item。则刷新
        if (!isRefreshing && getItemViewType(position) == LOAD_TYPE) {
            isRefreshing = true
            if (onLoadMoreListener == null) {
                throw NullPointerException("you must set OnLoadMoreListener")
            } else {
                if (loadType == LOADE_MORE_TYPE) {
                    setLoadType(LOADIND_TYPE)
                }
            }
        }
        when (getItemViewType(position)) {
            LOAD_TYPE -> if (this.onSetBottomListener == null) {
                when (loadType) {
                    LOADE_MORE_TYPE -> {
                        holder.setText(R.id.tv_load_prompt, LOAD_MORE)
                        holder.getView<View>(R.id.progress).visibility = View.GONE
                        holder.getView<View>(R.id.tv_load_prompt).setOnClickListener(null)
                    }
                    LOADIND_TYPE -> {
                        holder.setText(R.id.tv_load_prompt, LOADIND)
                        holder.getView<View>(R.id.progress).visibility = View.VISIBLE
                        setLoadType(LOADIND_TYPE) //基本没有显示
                        onLoadMoreListener!!.onLoadMore()
                        holder.getView<View>(R.id.tv_load_prompt).setOnClickListener(null)
                    }
                    LOAD_NO_MORE_TYPE -> {
                        holder.setText(R.id.tv_load_prompt, LOAD_NO_MORE)
                        holder.getView<View>(R.id.progress).visibility = View.GONE
                        holder.getView<View>(R.id.tv_load_prompt).setOnClickListener(null)
                    }
                    LOAD_ERROR_TYPE -> {
                        holder.setText(R.id.tv_load_prompt, LOAD_ERROR)
                        holder.getView<View>(R.id.progress).visibility = View.GONE
                        holder.getView<View>(R.id.tv_load_prompt).setOnClickListener {
                            holder.setText(R.id.tv_load_prompt, LOADIND)
                            holder.getView<View>(R.id.progress).visibility = View.VISIBLE
                            setLoadType(LOADIND_TYPE) //基本没有显示
                            holder.getView<View>(R.id.tv_load_prompt).setOnClickListener(null)
                            onLoadMoreListener!!.onErrorClick()
                        }
                    }
                }
            } else {
                when (loadType) {
                    LOADE_MORE_TYPE -> this.onSetBottomListener!!.onLoadMore(holder, position)
                    LOADIND_TYPE -> this.onSetBottomListener!!.onLoading(holder, position)
                    LOAD_NO_MORE_TYPE -> this.onSetBottomListener!!.onNoMore(holder, position)
                    LOAD_ERROR_TYPE -> this.onSetBottomListener!!.onLoadError(holder, position)
                }
            }
            NORMAL_TYPE -> onBindViewHolder(holder, mData, position)
        }
    }

    override fun getItemCount(): Int {
        return if (isLoadMore) {
            if (mData == null) 0 else mData!!.size + 1
        } else {
            if (mData == null) 0 else mData!!.size
        }
    }

    abstract fun onBindViewHolder(holder: BaseViewHolder, data: ArrayList<T>?, position: Int)

    /**
     * 用户上啦加载和失败点击重新加载
     */
    interface OnLoadMoreListener {
        fun onLoadMore()

        fun onErrorClick()
    }

    /**
     * 用户可以定制自定义底部后如果，底部布局变化非常大。可以实现自定义回调
     */
    interface OnSetBottomListener {
        fun onLoadMore(holder: BaseViewHolder, position: Int)

        fun onLoading(holder: BaseViewHolder, position: Int)

        fun onNoMore(holder: BaseViewHolder, position: Int)

        fun onLoadError(holder: BaseViewHolder, position: Int)
    }

    companion object {
        val LOADE_MORE_TYPE = 1
        val LOADIND_TYPE = 2
        val LOAD_NO_MORE_TYPE = 3
        val LOAD_ERROR_TYPE = 4
    }
}
