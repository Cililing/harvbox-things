package com.cililing.harvbox.thingsapp.common

val Any?.TAG: String
    get() {
        val className = this?.javaClass?.simpleName ?: "null"

        return className.substring(0, Math.min(className.length, 20))
    }