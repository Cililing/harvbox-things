package com.cililing.direct

import com.cililing.harvbox.common.FirebaseThingsSnapshot
import com.google.firebase.FirebaseApp

interface DirectService {
    suspend fun getAndProcess(): FirebaseThingsSnapshot
}

fun getDirectService(
        firebaseApp: FirebaseApp,
        isDebug: Boolean
): DirectService {
    return DirectServiceImpl(firebaseApp, isDebug)
}
