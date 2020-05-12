@file:Suppress("UNCHECKED_CAST")

package com.oomall.kouclodelivery.network


import com.google.gson.Gson

import java.lang.reflect.ParameterizedType

import com.oomall.kouclodelivery.network.interfaces.ICallback
import com.oomall.kouclodelivery.utils.LogPrinter
import com.oomall.kouclodelivery.utils.ToastUtil


/**
 * 统一数据处理类
 */
abstract class HttpCallback<Result> : ICallback {

    override fun onSuccess(result: String) {
        val gson = Gson()
        val clz = analysisClassInfo(this)
        val objResult = gson.fromJson(result, clz) as Result
        onSuccess(objResult)
        LogPrinter.e("TAG", objResult.toString())
    }

    abstract fun onSuccess(objResult: Result)

    override fun onFailure(e: String) {
        ToastUtil.show("联网失败！")
    }

    private fun analysisClassInfo(obj: Any): Class<*> {
        val getType = obj.javaClass.genericSuperclass
        val params = (getType as ParameterizedType).actualTypeArguments
        return params[0] as Class<*>
    }
}
