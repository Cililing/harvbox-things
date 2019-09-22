package com.cililing.harvbox.thingsapp.core

import kotlinx.coroutines.*
import org.koin.core.parameter.DefinitionParameters
import org.koin.core.parameter.parametersOf
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class ProducerScheduler(
        private val interval: Long,
        private val initialDelay: Long?
) : CoroutineScope {

    companion object {
        fun createKoinParams(interval: Long, initialDelay: Long?): DefinitionParameters {
            return parametersOf(interval, initialDelay)
        }
    }

    private val job = Job()
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    override val coroutineContext: CoroutineContext
        get() = job + singleThreadExecutor.asCoroutineDispatcher()

    fun start(service: () -> Any?) = launch {
        initialDelay?.let {
            delay(it)
        }

        while (isActive) {
            service()
            delay(interval)
        }
    }

    fun stop() {
        job.cancel()
        singleThreadExecutor.shutdown()
    }
}