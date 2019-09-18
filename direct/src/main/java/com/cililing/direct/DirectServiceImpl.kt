package com.cililing.direct

import com.cililing.direct.firebase.reporting.FirebaseThingsSnapshot
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsController
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsControllerBuilderImpl
import kotlinx.coroutines.*

internal class DirectServiceImpl : DirectService {

    private val thingsController by lazy {
        ThingsControllerBuilderImpl().build()
    }

    private suspend fun generateThingsSnapshot(): FirebaseThingsSnapshot {
        return thingsController.getSnapshotAsync().toFirebaseThingsSnapshot()
    }

    override suspend fun get(listener: (FirebaseThingsSnapshot) -> Unit) {
        val snapshot = generateThingsSnapshot()
        withContext(Dispatchers.IO) {
            reportToFirebase(snapshot)
        }
        listener.invoke(snapshot)
    }

    private suspend fun reportToFirebase(firebaseThingsSnapshot: FirebaseThingsSnapshot) {
        delay(1000)
    }
}