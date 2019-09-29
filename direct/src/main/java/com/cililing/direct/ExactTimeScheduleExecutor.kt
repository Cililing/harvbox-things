package com.cililing.direct

import java.util.Calendar
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

internal class ExactTimeScheduleExecutor {

    private var timer: Timer? = null

    fun release() {
        timer?.apply {
            cancel()
            purge()
        }
        timer = null
    }

    fun schedule(tasksList: List<ExactTimeScheduleTask>) {
        if (timer != null) {
            timer?.cancel()
            timer?.purge()
        }

        timer = Timer().also { timer ->
            tasksList.forEach { scheduleSingle(timer, it) }
        }
    }

    private fun scheduleSingle(timer: Timer, task: ExactTimeScheduleTask) {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, task.hour)
        today.set(Calendar.MINUTE, task.minute)
        today.set(Calendar.SECOND, 0)

        timer.schedule(
                task.what.asTimerTask(),
                today.time,
                TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        )
    }

    private fun (() -> Unit).asTimerTask() = object : TimerTask() {
        override fun run() {
            this@asTimerTask.invoke()
        }
    }
}