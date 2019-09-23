package com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

internal interface FixedExecutor : ThingsExecutor

internal class FixedExecutorImpl(poolSize: Int) : ThingsExecutorImpl(), FixedExecutor {
    override val executorService: ScheduledExecutorService = Executors.newScheduledThreadPool(poolSize)
}