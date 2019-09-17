package com.cililing.direct

import com.cililing.harvbox.thingsapp.thingscontroller.ThingsControllerBuilderImpl
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsSnapshot
import kotlinx.coroutines.delay

class FirebaseService {
    private val controller = ThingsControllerBuilderImpl().build()

    suspend fun getSnapshotAsync(): String {
        delay(500)
        return controller.getSnapshotAsync().toString()
    }
}