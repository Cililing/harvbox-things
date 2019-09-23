package com.cililing.harvbox.common

import com.google.gson.Gson
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

data class StatusSnapshot constructor(
    @SerializedName("timestamp")
    val timestamp: String? = null,
    @SerializedName("light1")
    val light1PowerOn: PowerOn = PowerOn(),
    @SerializedName("light2")
    val light2PowerOn: PowerOn = PowerOn(),
    @SerializedName("proximity")
    val proximityValue: Value<Double> = Value(0.0),
    @SerializedName("humidity")
    val humidityValue: Value<Double> = Value(0.0),
    @SerializedName("temp")
    val tempValue: Value<Double> = Value(0.0)
)

fun StatusSnapshot.toJson(gson: Gson): String {
    return gson.toJson(this)
}

data class StatusSnapshotValues(
    @SerializedName("timestamp")
    val timestamp: String? = null,
    @SerializedName("light1")
    val light1PowerOn: Boolean,
    @SerializedName("light2")
    val light2PowerOn: Boolean,
    @SerializedName("proximity")
    val proximityValue: Double,
    @SerializedName("humidity")
    val humidityValue: Double,
    @SerializedName("temp")
    val tempValue: Double
)

fun StatusSnapshot.toValues() = StatusSnapshotValues(
        timestamp = this.timestamp,
        light1PowerOn = this.light1PowerOn.isOn,
        light2PowerOn = this.light2PowerOn.isOn,
        proximityValue = this.proximityValue.value,
        humidityValue = this.humidityValue.value,
        tempValue = this.tempValue.value
)

fun StatusSnapshot.toValueJson(gson: Gson): String {
        return gson.toJson(this.toValues())
}