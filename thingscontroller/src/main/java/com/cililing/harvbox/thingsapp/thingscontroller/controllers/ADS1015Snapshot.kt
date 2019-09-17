package com.cililing.harvbox.thingsapp.thingscontroller.controllers

import java.io.Serializable

data class ADS1015Snapshot(
        private val a0: ADS1015Controller.ADS1015PinSnapshot,
        private val a1: ADS1015Controller.ADS1015PinSnapshot
) : Serializable