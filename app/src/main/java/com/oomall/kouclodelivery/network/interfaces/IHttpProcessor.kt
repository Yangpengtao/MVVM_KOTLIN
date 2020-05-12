package com.oomall.kouclodelivery.network.interfaces

import java.io.File

/**
 * 网络请求所用到的方法
 */
interface IHttpProcessor {
    //网络访问：POST,GET,DELETE,PUT,UPDATE
    fun _post(url: String, params: Map<String, Any>, callback: ICallback)

    fun _get(url: String, params: Map<String, Any>, callback: ICallback)
    fun _file(file: File, callback: ICallback)
}
