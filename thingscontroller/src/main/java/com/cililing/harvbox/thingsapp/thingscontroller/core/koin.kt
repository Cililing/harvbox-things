package com.cililing.harvbox.thingsapp.thingscontroller.core

import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.FixedExecutor
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.FixedExecutorImpl
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.SerialExecutor
import com.cililing.harvbox.thingsapp.thingscontroller.core.multithreading.SerialExecutorImpl
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

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

        loadKoinModules(getThingsModule())
    }
}