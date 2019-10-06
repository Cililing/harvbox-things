package com.cililing.direct

import android.content.Context
import com.cililing.harvbox.common.Clock
import com.cililing.harvbox.common.DebugLogger
import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.common.RealClock
import com.cililing.harvbox.common.ReleaseLogger
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsController
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsControllerBuilderImpl
import com.google.gson.Gson
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.KoinComponent
import org.koin.dsl.module

internal object StandaloneKoinContext {
    lateinit var koinApplication: KoinApplication
}

internal interface StandaloneKoinCompontent : KoinComponent {
    override fun getKoin(): Koin {
        return StandaloneKoinContext.koinApplication.koin
    }
}

internal fun getDirectKoinModule(
    context: Context,
    isDebug: Boolean,
    photoListener: (ByteArray) -> Unit
) = module {

    single {
        if (isDebug) DebugLogger() else ReleaseLogger() as Logger
    }

    single {
        Gson() as Gson
    }

    single {
        RealClock() as Clock
    }

    single {
        ThingsControllerBuilderImpl()
                .build(context, photoListener) as ThingsController
    }
}