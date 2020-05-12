package com.oomall.kouclodelivery.ui.view.my_wallet.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.data.bean.SzDetailsBean
import com.oomall.kouclodelivery.ui.adapter.base.BaseAdapter
import com.oomall.kouclodelivery.ui.adapter.base.BaseViewHolder
import com.oomall.kouclodelivery.ui.view_model.WalletViewModel
import kotlinx.android.synthetic.main.fragment_sz_details.*
import java.util.*


/**
 * 收支明细
 */
class SzDetailsFragment : Fragment() {
    lateinit var walletViewModel: WalletViewModel
    lateinit var listAdapter: BaseAdapter<SzDetailsBean>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sz_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        walletViewModel = ViewModelProviders.of(activity!!).get(WalletViewModel::class.java)
        setRefreshLayoutStyle()
        setAdapter()
        walletViewModel.szDetails.observe(this, androidx.lifecycle.Observer {
            listAdapter.setmData(it)
            swipe_refresh_layout.isRefreshing = false
        })
    }


    /**
     * 设置适配器
     */
    private fun setAdapter() {
        listAdapter = object : BaseAdapter<SzDetailsBean>(
            R.layout.list_item_sz_details,
            walletViewModel.szDetails.value
        ) {
            override fun onBindViewHolder(
                holder: BaseViewHolder,
                data: ArrayList<SzDetailsBean>?,
                position: Int
            ) {
            }


        }
        rvBody.layoutManager = LinearLayoutManager(context)
        rvBody.adapter = listAdapter
    }

    /**
     * 设置刷新控件样式
     */
    private fun setRefreshLayoutStyle() {
        swipe_refresh_layout.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.colorAccent))
        swipe_refresh_layout.setOnRefreshListener {
            //获取数据
            walletViewModel.getSzDetails()
        }
    }


}
