package com.cililing.direct

import com.cililing.direct.firebase.reporting.FirebaseThingsSnapshot

interface DirectService {
    suspend fun get(listener: (FirebaseThingsSnapshot) -> Unit)
}

fun getDirectService(): DirectService {
    return DirectServiceImpl()
}
