package com.oomall.kouclodelivery.constant

import com.oomall.kouclodelivery.KoucloApplication
import java.io.File

object Constant {
    val baseImageDir: String =
        KoucloApplication.init()!!.externalCacheDir!!.path + File.separator + "images_cache" + File.separator
    val baseHttpDir: String =
        KoucloApplication.init()!!.cacheDir!!.path + File.separator + "http_cache" + File.separator
    val baseCrashDir: String =
        KoucloApplication.init()!!.externalCacheDir!!.path + File.separator + "crash_log" + File.separator
    val baseApkDir: String =
        KoucloApplication.init()!!.externalCacheDir!!.path + File.separator + "apk" + File.separator

}