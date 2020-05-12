package com.oomall.kouclodelivery.utils

import android.widget.Toast
import com.oomall.kouclodelivery.KoucloApplication

/**
 * Toast工具类
 */
object ToastUtil {
    private var toast: Toast? = null

    fun show(str: String) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(KoucloApplication.init()!!, str, Toast.LENGTH_SHORT)
        toast!!.show()
    }
}
