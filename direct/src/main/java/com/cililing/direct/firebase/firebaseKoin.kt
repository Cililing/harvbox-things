package com.cililing.direct.firebase

import com.google.firebase.FirebaseApp
import org.koin.dsl.module

internal fun getFirebaseModule(
    firebaseApp: FirebaseApp
) = module {

    factory {
        FirebaseFailureHandlerImpl(get()) as FirebaseFailureHandler
    }

    single { params -> // params: DirectService
        FirebaseAppDatabaseImpl(
                firebaseApp,
                get(),
                FirebaseNewDataCallbackImpl(params[0]),
                get()
        ) as FirebaseAppDatabase
    }

    single {
        FirebaseAppStorageImpl(
            firebaseApp,
            get()
        ) as FirebaseAppStorage
    }
}