package com.cililing.harvbox.thingsapp.core

interface ConnectivityChecker {
    suspend fun isNetworkOk(): Boolean
}