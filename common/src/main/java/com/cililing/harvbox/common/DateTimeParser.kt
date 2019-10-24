package com.cililing.harvbox.common

import java.text.SimpleDateFormat
import java.time.Instant.ofEpochMilli
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateTimeParser {

    private val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US)

    fun getFormattedDate(): String {
        val calendar: Calendar = Calendar.getInstance()
        val currentLocalTime: Date = calendar.time

        return date.format(currentLocalTime)
    }

    fun getDate(source: String): Date? {
        return date.parse(source)
    }
}

fun StatusSnapshot.getDate(): Date? {
    return this.timestamp?.let { DateTimeParser.getDate(it) }
}

fun Long.milisToDate(): LocalDateTime? {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this

    val date = ofEpochMilli(this)
    val utc = LocalDateTime.ofInstant(date, ZoneOffset.UTC)

    return utc
}