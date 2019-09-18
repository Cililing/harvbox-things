package com.cililing.harvbox.thingsapp

import com.cililing.direct.firebase.reporting.FirebaseThingsSnapshot
import com.cililing.direct.getDirectService

class AppControlerImpl : AppController {

    private val directService by lazy {
        getDirectService()
    }

    override suspend fun getData(listener: (FirebaseThingsSnapshot) -> Unit) {
        return directService.get(listener)
    }
}