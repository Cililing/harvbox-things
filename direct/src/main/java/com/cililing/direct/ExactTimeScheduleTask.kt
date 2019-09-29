package com.cililing.direct

data class ExactTimeScheduleTask(
    val hour: Int,
    val minute: Int,
    val what: () -> Unit
)