package com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading

import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

internal interface ThingsExecutor {
    fun executeOnce(what: () -> Unit)
    fun startExecuting(howOften: Long, delayOnStart: Boolean = false, what: () -> Unit)
    fun awaitTermination(howLong: Long)

    fun startExecuting(howOften: Long, delayOnStart: Boolean = false, what: Runnable) {
        startExecuting(howOften, delayOnStart) {
            what.run()
        }
    }
}

internal abstract class ThingsExecutorImpl : ThingsExecutor {

    protected abstract val executorService: ScheduledExecutorService

    override fun executeOnce(what: () -> Unit) {
        executorService.execute(what)
    }

    override fun startExecuting(howOften: Long, delayOnStart: Boolean, what: () -> Unit) {
        executorService.scheduleAtFixedRate(
                what,
                howOften,
                if (delayOnStart) howOften else 0,
                TimeUnit.MILLISECONDS
        )
    }

    override fun awaitTermination(howLong: Long) {
        executorService.shutdown()
        try {
            if (!executorService.awaitTermination(howLong, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow()
            }
        } catch (e: InterruptedException) {
            executorService.shutdownNow()
        }
    }
}