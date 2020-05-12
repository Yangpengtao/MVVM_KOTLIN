package com.oomall.kouclodelivery.tools.thread

import com.oomall.kouclodelivery.tools.thread.interfaces.IThreadProcessor
import java.util.concurrent.*

/**
 * 线程池单例
 */
object ThreadPoolProcessor   : IThreadProcessor {
    //corePoolSize：核心线程数
    private val NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors()
    //keepAliveTime:非核心线程闲置超时时间
    private val KEEP_ALIVE_TIME = 5
    //Unit:非核心线程闲置超时时间单位
    private val KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS

    private var executorService: ThreadPoolExecutor


    init {
        val taskQueue: BlockingQueue<Runnable> = LinkedBlockingQueue<Runnable>()
        executorService= ThreadPoolExecutor(NUMBER_OF_CORES,NUMBER_OF_CORES*2,KEEP_ALIVE_TIME.toLong(),KEEP_ALIVE_TIME_UNIT,taskQueue)
     /*   executorService = ThreadPoolExecutor(
            NUMBER_OF_CORES, //corePoolSize：核心线程数
            NUMBER_OF_CORES * 2, //maximumPoolSize：线程池最大线程数
            KEEP_ALIVE_TIME.toLong(), //keepAliveTime:非核心线程闲置超时时间
            KEEP_ALIVE_TIME_UNIT, //Unit:非核心线程闲置超时时间单位
            taskQueue, //workQueue:任务队列
            BackgroundThreadFactory(), //threadFactory:线程工厂
            DefaultRejectedExecutionHandler()
        ) //handler：表示当拒绝处理任务时的策略*/

    }





    override fun _submit(runnable: Runnable): Future<*> {
        return executorService.submit(runnable) as Future<*>
    }

    override fun _shutdown() {
        executorService.shutdown()
    }


    override fun _execute(runnable: Runnable) {
        executorService.execute(runnable)
    }

}