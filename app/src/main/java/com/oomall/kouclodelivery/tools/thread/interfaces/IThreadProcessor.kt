package com.oomall.kouclodelivery.tools.thread.interfaces

import java.util.concurrent.Future

interface IThreadProcessor {
    fun _execute(runnable: Runnable)
    fun _submit(runnable: Runnable): Future<*>


    fun _shutdown()

}