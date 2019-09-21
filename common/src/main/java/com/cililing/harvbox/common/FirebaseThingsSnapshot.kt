package com.cililing.harvbox.common

import com.google.gson.annotations.SerializedName

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
        @SerializedName("light1")
        val light1PowerOn: PowerOn = PowerOn(),
        @SerializedName("light2")
        val light2PowerOn: PowerOn = PowerOn(),
        @SerializedName("proximity")
        val proximityValue: Value<Double> = Value(0.0),
        @SerializedName("humidity")
        val humidityValue: Value<Int> = Value(0),
        @SerializedName("temp")
        val tempValue: Value<Int> = Value(0)
)