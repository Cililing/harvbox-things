package com.cililing.harvbox.thingsapp.common

val Any?.TAG: String
    get() {
        return this.toString().substring(20)
    }