package com.cililing.harvbox.thingsapp

import com.cililing.direct.firebase.reporting.FirebaseThingsSnapshot

interface AppController {
    suspend fun getData(listener: (FirebaseThingsSnapshot) -> Unit)
}