package com.cililing.harvbox.thingsapp.thingscontroller.core

internal interface Clock {
    fun nano(): Long {
        return System.nanoTime()
    }

    fun micro(): Long {
        return System.currentTimeMillis() * 1000
    }
}

internal class RealClock : Clock