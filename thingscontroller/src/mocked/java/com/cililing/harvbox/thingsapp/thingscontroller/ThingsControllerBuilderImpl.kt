package com.cililing.harvbox.thingsapp.thingscontroller

import android.content.Context

class ThingsControllerBuilderImpl : ThingsControllerBuilder {
    override fun build(context: Context): ThingsController {
        return ThingsControllerImpl(null)
    }
}