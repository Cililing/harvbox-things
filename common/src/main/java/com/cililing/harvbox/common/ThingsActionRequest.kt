package com.cililing.harvbox.common

sealed class ThingsActionRequest {
    data class Light1(val isOn: Boolean) : ThingsActionRequest()
    data class Light2(val isOn: Boolean) : ThingsActionRequest()
}