package com.cililing.client

import com.google.firebase.FirebaseApp
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.KoinComponent
import org.koin.dsl.module

internal interface StandaloneKoinComponent : KoinComponent {
    object StandaloneKoinContext {
        lateinit var application: KoinApplication
    }

    override fun getKoin(): Koin {
        return StandaloneKoinContext.application.koin
    }
}

internal fun getClientKoin(firebaseApp: FirebaseApp, isDebug: Boolean) = module {
    factory {
        FirebaseDatabaseServiceImpl(
                firebaseApp
        ) as FirebaseDatabaseService
    }
}