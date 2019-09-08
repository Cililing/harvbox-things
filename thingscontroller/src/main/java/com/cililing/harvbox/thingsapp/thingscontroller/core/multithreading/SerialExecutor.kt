package com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

internal interface SerialExecutor : ThingsExecutor

internal class SerialExecutorImpl : ThingsExecutorImpl(), SerialExecutor {

    override val executorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
}