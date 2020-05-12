package com.oomall.kouclodelivery.constant

import com.oomall.kouclodelivery.KoucloApplication

/**
 * 接口常量
 */
object ApiConstant {
    val BASE_URL = getBaseUrl()
    const val UPLOAD_URL = "http://file.oomall.com/api/upload"

    const val LOGIN = "checkLogin"    //登录

    private fun getBaseUrl(): String {
        return if (KoucloApplication.init()!!.isDebug) {
            "http://api-canteen.oomall.com/v265/api/"
        } else {
            "http://api-canteen.oomall.com/v265/api/"
        }
    }
}
