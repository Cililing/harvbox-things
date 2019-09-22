package com.cililing.harvbox.thingsapp.thingscontroller.core

import com.cililing.harvbox.common.*
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.FixedExecutor
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.FixedExecutorImpl
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.SerialExecutor
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.SerialExecutorImpl
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.KoinComponent
import org.koin.core.module.Module
import org.koin.dsl.module

internal object StandaloneKoinContext {
    lateinit var koinApplication: KoinApplication
}

internal interface StandaloneKoinCompontent : KoinComponent {
    override fun getKoin(): Koin {
        return StandaloneKoinContext.koinApplication.koin
    }
}

internal fun getKoinModule(debug: Boolean): Module {
    return module {
        single {
            if (debug) DebugLogger() else ReleaseLogger() as Logger
        }

        factory {
            RealClock() as Clock
        }

        // Executors
        factory {
            SerialExecutorImpl() as SerialExecutor
        }

        factory { (poolSize: Int) ->
            FixedExecutorImpl(poolSize) as FixedExecutor
        }

        plus(getThingsModule())
    }
}