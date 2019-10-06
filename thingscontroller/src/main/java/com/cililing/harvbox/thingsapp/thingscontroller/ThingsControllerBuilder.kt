package com.cililing.harvbox.thingsapp.thingscontroller

import android.content.Context

interface ThingsControllerBuilder {
    fun build(context: Context): ThingsController
}