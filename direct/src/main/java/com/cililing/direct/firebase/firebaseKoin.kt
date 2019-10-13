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
        FirebaseRealtimeDatabaseImpl(
                firebaseApp,
                get(),
                FirebaseNewDataCallbackImpl(params[0]),
                get()
        ) as FirebaseRealtimeDatabase
    }

    single {
        FirebaseCloudDatabaseImpl(
            firebaseApp,
            get()
        ) as FirebaseCloudDatabase
    }
}