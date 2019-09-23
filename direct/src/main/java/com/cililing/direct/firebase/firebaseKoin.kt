package com.cililing.direct.firebase

import com.google.firebase.FirebaseApp
import org.koin.dsl.module

internal fun getFirebaseModule(
    firebaseApp: FirebaseApp
) = module {

    factory {
        FirebaseFailureHandlerImpl(get()) as FirebaseFailureHandler
    }

    single {
        FirebaseAppDatabaseImpl(
                firebaseApp,
                get(),
                get()
        ) as FirebaseAppDatabase
    }
}