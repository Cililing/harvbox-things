package com.cililing.harvbox.common

import java.util.Calendar

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

    fun currentHour(): Int {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    }

    fun currentMin(): Int {
        return Calendar.getInstance().get(Calendar.MINUTE)
    }
}

class RealClock : Clock