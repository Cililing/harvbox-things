package com.cililing.direct

import com.cililing.direct.firebase.FirebaseAppDatabase
import com.cililing.direct.firebase.getFirebaseModule
import com.cililing.harvbox.common.FirebaseThingsSnapshot
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsController
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.inject
import org.koin.dsl.koinApplication

internal class DirectServiceImpl(
        private val firebaseApp: FirebaseApp,
        private val isDebug: Boolean
) : DirectService, StandaloneKoinCompontent {

    init {
        StandaloneKoinContext.koinApplication = koinApplication {
            if (isDebug) printLogger()

            modules(listOf(
                    getDirectKoinModule(isDebug),
                    getFirebaseModule(firebaseApp)
            ))
        }
    }

    private val thingsController: ThingsController by inject()
    private val cloudDatabase: FirebaseAppDatabase by inject()

    /**
     * This will return snapshot do provider and report process in background.
     */
    override suspend fun getAndProcess(): FirebaseThingsSnapshot {
        return generateThingsSnapshot().also {
            // Other things do async to return result faster
            withContext(Dispatchers.Default) {
                reportCurrentStatusToFirebase(it)
            }
        }
    }

    private fun reportCurrentStatusToFirebase(firebaseThingsSnapshot: FirebaseThingsSnapshot) {
        cloudDatabase.post(firebaseThingsSnapshot)
    }

    private fun generateThingsSnapshot(): FirebaseThingsSnapshot {
        return thingsController.getSnapshot().toFirebaseThingsSnapshot()
    }
}