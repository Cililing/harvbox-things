package com.cililing.harvbox.common

interface Clock {
    fun nano(): Long {
        return System.nanoTime()
    }

    fun micro(): Long {
        return System.currentTimeMillis() * 1000
    }

    fun milis(): Long {
        return System.currentTimeMillis()
    }
}

class RealClock : Clock