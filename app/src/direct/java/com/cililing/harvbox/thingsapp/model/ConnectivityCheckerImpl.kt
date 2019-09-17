package com.cililing.harvbox.thingsapp.model

import kotlinx.coroutines.delay

class ConnectivityCheckerImpl : ConnectivityChecker {
    override suspend fun isNetworkOk(): Boolean {
        delay(2500)
        return true
    }
}

