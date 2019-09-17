package com.cililing.harvbox.thingsapp.thingscontroller

class ThingsControllerBuilderImpl : ThingsControllerBuilder {
    override fun build(): ThingsController {
        return ThingsControllerImpl(null)
    }
}