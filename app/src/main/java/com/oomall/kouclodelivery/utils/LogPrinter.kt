package com.oomall.kouclodelivery.utils

import android.util.Log

/**
 * 日志输出类，统一管理日志输出
 */
object LogPrinter {

    private const val OUTPUT_LOG = true


    /**
     * @param msg 日志
     */
    fun i(Tag: String, msg: String) {
        if (OUTPUT_LOG) {
            Log.i(Tag, msg)
        }
    }


    /**
     * @param msg 日志
     */
    fun d(Tag: String, msg: String) {
        if (OUTPUT_LOG) {
            Log.d(Tag, msg)
        }
    }


    /**
     * @param msg 日志
     */
    fun e(Tag: String, msg: String) {
        if (OUTPUT_LOG) {
            Log.e(Tag, msg)
        }
    }

}
