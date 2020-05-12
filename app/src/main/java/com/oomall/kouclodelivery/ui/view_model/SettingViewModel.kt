package com.oomall.kouclodelivery.ui.view_model

import androidx.lifecycle.MutableLiveData
import com.oomall.kouclodelivery.constant.SpConstant
import com.oomall.kouclodelivery.data.bean.UploadBean
import com.oomall.kouclodelivery.network.HttpCallback
import com.oomall.kouclodelivery.proxy.HelperSharedPreference
import com.oomall.kouclodelivery.ui.base.BaseViewModel
import com.oomall.kouclodelivery.utils.LogPrinter
import java.io.File

class SettingViewModel : BaseViewModel() {


    var headImg = MutableLiveData<UploadBean>()


    @Synchronized
    fun uploadHead(file: File) {
        val length = file.length()
        LogPrinter.e("SettingViewModel.uploadHead", "-----$length")
        _file(file, object : HttpCallback<UploadBean>() {
            override fun onSuccess(objResult: UploadBean) {
                if (objResult.code == 0) {
                    headImg.value = UploadBean(code = 1, url = objResult.url)
                    HelperSharedPreference._putData(SpConstant.HEAD_PHOTO, objResult.url)
                } else {
                    headImg.value = UploadBean(code = 2, url = "")
                }
            }

            override fun onFailure(e: String) {
                headImg.value = UploadBean(code = 2, url = "")
            }
        })
    }
}