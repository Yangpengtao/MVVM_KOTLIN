package com.oomall.kouclodelivery.ui.view.popularize

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.data.bean.InvitationPeopleBean
import com.oomall.kouclodelivery.ui.adapter.base.BaseAdapter
import com.oomall.kouclodelivery.ui.adapter.base.BaseViewHolder
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view_model.PopularizeViewModel
import kotlinx.android.synthetic.main.activity_my_invitation.*

/**
 * 我的邀请
 */
class MyInvitationActivity : BaseActivity() {
    lateinit var listAdapter: BaseAdapter<InvitationPeopleBean>
    lateinit var popularizeViewModel: PopularizeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_invitation)
        popularizeViewModel = ViewModelProviders.of(this).get(PopularizeViewModel::class.java)
        getTitle_().text = "我的邀请"
        getLeftImg_().visibility = View.VISIBLE
        setAdapter()
        popularizeViewModel.getInvitation()
        popularizeViewModel.invitationList.observe(this, Observer {
            listAdapter.setmData(it)
        })
    }


    private fun setAdapter() {
        rvInvitationPeople.layoutManager = LinearLayoutManager(this)
        listAdapter = object : BaseAdapter<InvitationPeopleBean>(
            R.layout.list_item_my_invitation,
            popularizeViewModel.invitationList.value
        ) {
            override fun onBindViewHolder(
                holder: BaseViewHolder,
                data: ArrayList<InvitationPeopleBean>?,
                position: Int
            ) {
            }
        }
        rvInvitationPeople.layoutManager = LinearLayoutManager(this)
        rvInvitationPeople.adapter = listAdapter
    }
}
