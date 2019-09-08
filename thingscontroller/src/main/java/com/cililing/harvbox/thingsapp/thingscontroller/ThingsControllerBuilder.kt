package com.cililing.harvbox.thingsapp.thingscontroller

class ThingsControllerBuilder {

    private var isDebug: Boolean = false
    private var configMap: ControllerConfigMap = ControllerConfigMapDefault
    private var proximityCallback: ((Double) -> Unit)? = null

    fun isDebug(isDebug: Boolean) = apply { this.isDebug = isDebug }
    fun configMap(configMap: ControllerConfigMap) = apply { this.configMap = configMap }

    fun withProximityListener(callback: (Double) -> Unit) = apply { proximityCallback = callback}

    fun build(): ThingsController {
        return ThingsController(
                configMap = configMap,
                debug = isDebug
        ).apply {
            proximityCallback?.let {
                this.proximityController.distanceCallback = it
            }

        }
    }
}