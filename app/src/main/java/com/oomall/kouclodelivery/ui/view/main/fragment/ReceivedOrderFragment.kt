package com.oomall.kouclodelivery.ui.view.main.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.data.bean.MainReceivedOrderList
import com.oomall.kouclodelivery.ui.adapter.base.BaseAdapter
import com.oomall.kouclodelivery.ui.adapter.base.BaseViewHolder
import com.oomall.kouclodelivery.ui.base.BaseFragment
import com.oomall.kouclodelivery.ui.view.order.CameraOrderActivity
import com.oomall.kouclodelivery.ui.view_model.MainViewModel
import kotlinx.android.synthetic.main.fragment_received_order.*
import kotlinx.android.synthetic.main.fragment_waiting_order.rv_body
import java.util.*


/**
 * 已接单fragment
 * 和待接单各自处理自己的逻辑，数据处理全部在mainViewModel里
 */
class ReceivedOrderFragment : BaseFragment(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnGoTakePhoto -> {
                startActivity(CameraOrderActivity::class.java)
            }
        }
    }

    lateinit var mViewModel: MainViewModel
    lateinit var listAdapter: BaseAdapter<MainReceivedOrderList>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_received_order, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //  获取view model对象
        mViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        // 设置为列表布局
        rv_body.layoutManager = LinearLayoutManager(context)
        setRefreshLayoutStyle()
        setAdapter()
        mViewModel.receivedOrderList.observe(this, Observer {
            swipe_refresh_layout1.isRefreshing = false
            listAdapter.setmData(it)
        })
    }

    /**
     * 设置适配器
     */
    private fun setAdapter() {
        listAdapter = object : BaseAdapter<MainReceivedOrderList>(
            R.layout.list_item_main_received_order,
            mViewModel.receivedOrderList.value
        ) {
            override fun onBindViewHolder(
                holder: BaseViewHolder,
                data: ArrayList<MainReceivedOrderList>?,
                position: Int
            ) {
                holder.getView<Button>(R.id.btnGoTakePhoto).setOnClickListener(this@ReceivedOrderFragment)
            }


        }
        rv_body.adapter = listAdapter
    }

    /**
     * 设置刷新控件样式
     */
    private fun setRefreshLayoutStyle() {
        swipe_refresh_layout1.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorAccent))
        swipe_refresh_layout1.setOnRefreshListener {
            //获取数据
            mViewModel.getReceivedOrder()
        }
    }

}
