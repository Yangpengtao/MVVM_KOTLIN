package com.oomall.kouclodelivery.proxy

import com.oomall.kouclodelivery.tools.thread.ThreadPoolProcessor
import com.oomall.kouclodelivery.tools.thread.interfaces.IThreadProcessor
import java.util.concurrent.Future

/**
 * 线程池
 */
object HelperThreadPool : IThreadProcessor {


    override fun _execute(runnable: Runnable) {
        ThreadPoolProcessor._execute(runnable)
    }

    override fun _submit(runnable: Runnable): Future<*> {
        return ThreadPoolProcessor._submit(runnable)
    }

    override fun _shutdown() {
        return ThreadPoolProcessor._shutdown()
    }

}
