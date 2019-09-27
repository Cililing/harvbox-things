package com.cililing.harvbox.common

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class StatusSnapshot constructor(
    @SerializedName("timestamp")
    val timestamp: String? = null,
    @SerializedName("light1")
    val light1PowerOn: Boolean = false,
    @SerializedName("light2")
    val light2PowerOn: Boolean = false,
    @SerializedName("proximity")
    val proximityValue: Double = 0.0,
    @SerializedName("humidity")
    val humidityValue: Double = 0.0,
    @SerializedName("temp")
    val tempValue: Double = 0.0
)

fun StatusSnapshot.toJson(gson: Gson): String {
    return gson.toJson(this)
}

fun StatusSnapshot.toValueJson(gson: Gson): String {
        return gson.toJson(this)
}