package com.cililing.harvbox.thingsapp.model

interface ConnectivityChecker {
    suspend fun isNetworkOk(): Boolean
}