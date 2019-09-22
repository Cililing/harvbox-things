package com.cililing.harvbox.thingsapp

import com.cililing.direct.getDirectService
import com.cililing.harvbox.common.StatusSnapshot
import com.google.firebase.FirebaseApp

class AppControlerImpl : AppController {

    private val directService by lazy {
        getDirectService(FirebaseApp.getInstance(), AppController.isDebug)
    }

    override suspend fun getData(): StatusSnapshot {
        return directService.getAndProcess()
    }
}