package com.oomall.kouclodelivery.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oomall.kouclodelivery.data.bean.InvitationPeopleBean
import com.oomall.kouclodelivery.ui.base.BaseViewModel

class PopularizeViewModel : BaseViewModel() {

    private val _invitationList = MutableLiveData<ArrayList<InvitationPeopleBean>>()
    val invitationList: LiveData<ArrayList<InvitationPeopleBean>> = _invitationList


    fun getInvitation() {
//        _get()
        var orderList = arrayListOf<InvitationPeopleBean>()
        for (i in 1..20) {
            orderList.add(InvitationPeopleBean("aaa"))
        }
        _invitationList.value = orderList
    }


}