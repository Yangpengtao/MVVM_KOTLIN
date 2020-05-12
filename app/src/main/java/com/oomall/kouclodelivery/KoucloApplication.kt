package com.oomall.kouclodelivery

import android.app.Application
import com.oomall.kouclodelivery.constant.Constant
import com.oomall.kouclodelivery.tools.image.GlideProcessor
import com.oomall.kouclodelivery.network.okhttp.OKHttpProcessor
import com.oomall.kouclodelivery.proxy.HelperHttp
import com.oomall.kouclodelivery.proxy.HelperImageLoader
import com.oomall.kouclodelivery.utils.FileUtil

class KoucloApplication : Application() {

    //測試地址开关 true线下  false线上
    var isDebug = false


    companion object {

        private var koucloApplication: KoucloApplication? = null

        fun init(): KoucloApplication? {
            if (koucloApplication == null) {
                koucloApplication = KoucloApplication()
            }
            return koucloApplication
        }
    }


    override fun onCreate() {
        super.onCreate()
        koucloApplication = this
        HelperHttp.init(OKHttpProcessor)
        HelperImageLoader.init(GlideProcessor)
        FileUtil.createOrExitstDir(Constant.baseCrashDir)
        FileUtil.createOrExitstDir(Constant.baseImageDir)
        FileUtil.createOrExitstDir(Constant.baseApkDir)
        FileUtil.createOrExitstDir(Constant.baseHttpDir)
    }
}
