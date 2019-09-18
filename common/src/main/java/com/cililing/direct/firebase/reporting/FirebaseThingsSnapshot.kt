package com.cililing.direct.firebase.reporting

data class PowerOn constructor(
        val isOn: Boolean = false
)

data class Value<T> constructor(
        val value: T
)

fun <T> valueOf(value: T?, default: T): Value<T> {
    return if (value != null) Value(value) else Value(default)
}

data class FirebaseThingsSnapshot constructor(
        val light1PowerOn: PowerOn = PowerOn(),
        val light2PowerOn: PowerOn = PowerOn(),
        val proximityValue: Value<Double> = Value(0.0),
        val humidityValue: Value<Int> = Value(0),
        val tempValue: Value<Int> = Value(0)
)