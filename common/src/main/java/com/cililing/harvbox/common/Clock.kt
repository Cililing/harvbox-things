package com.cililing.harvbox.common

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale

interface Clock {
    companion object {
        private val firebaseFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US)
        private val dateTimeFormat = DateFormat.getDateTimeInstance()

        fun parseFirebaseDate(dateString: String): String {
            val date = firebaseFormat.parse(dateString)
            return dateTimeFormat.format(date)
        }

        fun parseLocalDateTime(localDateTime: LocalDateTime?): String? {
            val datetime = Date.from(localDateTime?.toInstant(ZoneOffset.UTC))
            return dateTimeFormat.format(datetime)
        }
    }

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
