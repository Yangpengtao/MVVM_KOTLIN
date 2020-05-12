package com.oomall.kouclodelivery.ui.base

import androidx.lifecycle.ViewModel
import com.oomall.kouclodelivery.constant.ApiConstant
import com.oomall.kouclodelivery.network.interfaces.ICallback
import com.oomall.kouclodelivery.network.interfaces.IHttpProcessor
import com.oomall.kouclodelivery.proxy.HelperHttp
import java.io.File

abstract class BaseViewModel : ViewModel(), IHttpProcessor {
    override fun _file(file: File, callback: ICallback) {
        HelperHttp ._file(file, callback)
    }

    override fun _post(url: String, params: Map<String, Any>, callback: ICallback) {
        HelperHttp ._post(ApiConstant.BASE_URL + url, params, callback)
    }

    override fun _get(url: String, params: Map<String, Any>, callback: ICallback) {
        HelperHttp ._get(ApiConstant.BASE_URL + url, params, callback)
    }

}