package com.oomall.kouclodelivery.ui.view.main.fragment


import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.data.bean.MainWaitingOrderList
import com.oomall.kouclodelivery.ui.view.verify.IdentificationActivity
import com.oomall.kouclodelivery.ui.view.order.OrderDetailsActivity
import com.oomall.kouclodelivery.ui.adapter.base.BaseAdapter
import com.oomall.kouclodelivery.ui.adapter.base.BaseViewHolder
import com.oomall.kouclodelivery.ui.base.BaseFragment
import com.oomall.kouclodelivery.ui.view_model.MainViewModel
import com.oomall.kouclodelivery.ui.widget.SlidingConfictSwipeRefreshLayout.OnPreInterceptTouchEventDelegate
import com.oomall.kouclodelivery.utils.ToastUtil
import kotlinx.android.synthetic.main.fragment_waiting_order.*
import java.util.ArrayList
import com.oomall.kouclodelivery.tools.image.imageloaders.GlideImageLoader



/**
 * 待接单fragment
 * 和已接单各自处理自己的逻辑，数据处理全部在mainViewModel里
 */
class WaitingOrderFragment : BaseFragment(), View.OnClickListener {

    lateinit var mViewModel: MainViewModel
    lateinit var listAdapter: BaseAdapter<MainWaitingOrderList>
    lateinit var verifyStub: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_waiting_order, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //  获取view model对象
        mViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        // 设置为列表布局
        //设置滑动冲突
        setSlidingConfict()
        setRefreshLayoutStyle()
        setAdapter()
        btn_refresh.setOnClickListener(this)
        verifyStub = vs_verify.inflate() as LinearLayout
        verifyStub.findViewById<Button>(com.oomall.kouclodelivery.R.id.btn_verify).setOnClickListener(this)
        mViewModel.waitingOrderList.observe(this, Observer {
            swipe_refresh_layout.isRefreshing = false
            listAdapter.setmData(it)
        })
        mViewModel.bannerList.observe(this, Observer {
            topBanner.setImages(it).setImageLoader(GlideImageLoader()).start()
        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btn_refresh -> {
                mViewModel.getWaitingOrder()
                showOrderList()
            }
            R.id.btn_verify -> {
                ToastUtil.show("立即验证")
                startActivity(IdentificationActivity::class.java)
            }
            R.id.ll_waiting_order_content -> {
                startActivity(OrderDetailsActivity::class.java)
            }
        }
    }


    /**
     * 设置适配器
     */
    private fun setAdapter() {
        listAdapter = object : BaseAdapter<MainWaitingOrderList>(
            R.layout.list_item_main_waiting_order,
            mViewModel.waitingOrderList.value
        ) {
            override fun onBindViewHolder(
                holder: BaseViewHolder,
                data: ArrayList<MainWaitingOrderList>?,
                position: Int
            ) {
                holder.getView<LinearLayout>(R.id.ll_waiting_order_content)
                    .setOnClickListener(this@WaitingOrderFragment)
            }


        }
        rv_body.layoutManager = LinearLayoutManager(context)
        rv_body.adapter = listAdapter
    }

    /**
     * 设置刷新控件样式
     */
    private fun setRefreshLayoutStyle() {
        swipe_refresh_layout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorAccent))
        swipe_refresh_layout.setOnRefreshListener {
            //获取数据
            mViewModel.getWaitingOrder()
            mViewModel.getBannerList()
            showOrderList()
        }
    }

    private fun showOrderList() {
        verifyStub.visibility = View.GONE
        ll_content.visibility = View.VISIBLE
    }

    /**
     * 设置滑动冲突
     * 标明app_bar滚出布局后，拦截刷新事件
     */
    private fun setSlidingConfict() {
        swipe_refresh_layout.mOnPreInterceptTouchEventDelegate = object :
            OnPreInterceptTouchEventDelegate {
            override fun shouldDisallowInterceptTouchEvent(ev: MotionEvent): Boolean {
                return app_bar.top < 0
            }
        }
    }


    //如果你需要考虑更好的体验，可以这么操作
    override fun onStart() {
        super.onStart()
        //开始轮播
        topBanner .startAutoPlay()
    }

      override fun onStop() {
        super.onStop()
        //结束轮播
          topBanner.stopAutoPlay()
    }
}


