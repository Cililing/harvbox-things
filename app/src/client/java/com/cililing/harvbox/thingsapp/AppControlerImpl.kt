package com.cililing.harvbox.thingsapp

import com.cililing.client.clientSnapshotMock
import com.cililing.direct.firebase.reporting.FirebaseThingsSnapshot

class AppControlerImpl : AppController {

    override suspend fun getData(listener: (FirebaseThingsSnapshot) -> Unit) {
        listener.invoke(clientSnapshotMock)
    }

}
