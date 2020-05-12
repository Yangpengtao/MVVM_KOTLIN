package com.oomall.kouclodelivery.ui.view_model

import androidx.lifecycle.MutableLiveData
import com.oomall.kouclodelivery.data.bean.UploadBean
import com.oomall.kouclodelivery.network.HttpCallback
import com.oomall.kouclodelivery.ui.base.BaseViewModel
import com.oomall.kouclodelivery.utils.LogPrinter
import java.io.File

/**
 * 身份认证
 */
class VerifyViewModel : BaseViewModel() {
    var frontImg = MutableLiveData<UploadBean>()
    var backImg = MutableLiveData<UploadBean>()
    var holderImg = MutableLiveData<UploadBean>()


    @Synchronized
    fun uploadFront(file: File) {
        LogPrinter.e("", "-----$file")

        _file(file, object : HttpCallback<UploadBean>() {
            override fun onSuccess(objResult: UploadBean) {
                if (objResult.code == 0) {
                    frontImg.value = UploadBean(code = 1, url = objResult.url)
                } else {
                    frontImg.value = UploadBean(code = 2, url = "0")
                }
            }

            override fun onFailure(e: String) {
                frontImg.value = UploadBean(code = 2, url = "0")
                super.onFailure(e)
            }
        })
    }

    @Synchronized
    fun uploadBack(file: File) {
        LogPrinter.e("", "-----$file")
        _file(file, object : HttpCallback<UploadBean>() {
            override fun onSuccess(objResult: UploadBean) {
                if (objResult.code == 0) {
                    backImg.value = UploadBean(code = 1, url = objResult.url)
                } else {
                    backImg.value = UploadBean(code = 2, url = "0")
                }
            }

            override fun onFailure(e: String) {
                backImg.value = UploadBean(code = 2, url = "0")
                super.onFailure(e)
            }
        })
    }

    @Synchronized
    fun uploadHold(file: File) {
        LogPrinter.e("", "-----$file")
        _file(file, object : HttpCallback<UploadBean>() {
            override fun onSuccess(objResult: UploadBean) {
                if (objResult.code == 0) {
                   holderImg.value = UploadBean(code = 1, url = objResult.url)
                } else {
                    holderImg.value = UploadBean(code = 2, url = "0")
                }
            }

            override fun onFailure(e: String) {
                holderImg.value = UploadBean(code = 2, url = "0")
                super.onFailure(e)
            }
        })
    }


}