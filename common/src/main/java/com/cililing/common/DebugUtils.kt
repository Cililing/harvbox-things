package com.cililing.common

val Any?.TAG: String
    get() {
        return this.toString().substring(20)
    }