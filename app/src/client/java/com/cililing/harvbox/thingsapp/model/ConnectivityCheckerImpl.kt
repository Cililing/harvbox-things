package com.cililing.harvbox.thingsapp.model

import com.cililing.harvbox.thingsapp.core.ConnectivityChecker
import kotlinx.coroutines.delay

class ConnectivityCheckerImpl : ConnectivityChecker {

    override suspend fun isNetworkOk(): Boolean {
        delay(1000)
        return true
    }
}