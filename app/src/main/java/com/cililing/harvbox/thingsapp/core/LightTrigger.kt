package com.cililing.harvbox.thingsapp.core

enum class TriggerType {
    ON, OFF
}

data class LightTrigger(
    val hour: Int,
    val minute: Int,
    var type: TriggerType
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LightTrigger

        if (hour != other.hour) return false
        if (minute != other.minute) return false

        return true
    }

    override fun hashCode(): Int {
        var result = hour
        result = 31 * result + minute
        return result
    }

    fun changeType() {
        type = if (type == TriggerType.ON) TriggerType.OFF else TriggerType.ON
    }
}