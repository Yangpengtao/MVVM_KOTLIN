package com.oomall.kouclodelivery.proxy

import com.oomall.kouclodelivery.network.interfaces.ICallback
import com.oomall.kouclodelivery.network.interfaces.IHttpProcessor
import java.io.File

import java.io.UnsupportedEncodingException
import java.net.URLEncoder


/**
 * 网络代理类
 * 可通过public static void init(IHttpProcessor httpProcessor)
 * 在application里面初始化自己要用的网络框架，可随意更改
 */
object HelperHttp : IHttpProcessor {


    override fun _post(url: String, params: Map<String, Any>, callback: ICallback) {
        //        final String finalUrl =appendParams(url,params);
        mHttpProcessor!!._post(url, params, callback)
    }

    override fun _get(url: String, params: Map<String, Any>, callback: ICallback) {
        val finalUrl = appendParams(url, params)
        mHttpProcessor!!._get(finalUrl, params, callback)
    }

    override fun _file(file: File, callback: ICallback) {
        mHttpProcessor!!._file(file, callback)

    }

    private fun appendParams(url: String, params: Map<String, Any>?): String {
        if (params == null || params.isEmpty()) return url

        val urlBuilder = StringBuilder(url)
        if (url.indexOf("?") <= 0)
            urlBuilder.append("?")
        else {
            if (!urlBuilder.toString().endsWith("?")) {
                urlBuilder.append("&")
            }
        }
        for ((key, value) in params) {
            urlBuilder.append(key).append("=").append(encode(value.toString()))

        }
        return urlBuilder.toString()

    }

    private var mHttpProcessor: IHttpProcessor? = null

    fun init(httpProcessor: IHttpProcessor) {
        mHttpProcessor = httpProcessor
    }


    //URL不允许有空格等字符，如果参数值有空格，需要此方法转换
    private fun encode(str: String): String {
        try {
            return URLEncoder.encode(str, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException(e)
        }

    }

}
