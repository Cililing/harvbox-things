package com.cililing.harvbox.thingsapp

import com.cililing.direct.ExactTimeScheduleTask
import com.cililing.direct.getDirectService
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.cililing.harvbox.thingsapp.model.LightTrigger
import com.cililing.harvbox.thingsapp.model.TriggerType
import com.cililing.harvbox.thingsapp.settings.SettingsContract
import com.google.firebase.FirebaseApp

class AppControllerImpl : AppController {

    private val directService by lazy {
        getDirectService(FirebaseApp.getInstance(), AppController.isDebug)
    }

    override suspend fun getData(): StatusSnapshot {
        return directService.getAndProcess()
    }

    override fun request(actionRequest: ThingsActionRequest) {
        directService.request(actionRequest)
    }

    override fun newLightSettingsReceived(light1: Set<LightTrigger>, light2: Set<LightTrigger>) {
        directService.scheduleTasks(
                light1.map {
                    ExactTimeScheduleTask(it.hour, it.minute) {
                        request(it.toThingsActionRequest(SettingsContract.LightId.LIGHT_1))
                    }
                } + light2.map {
                    ExactTimeScheduleTask(it.hour, it.minute) {
                        request(it.toThingsActionRequest(SettingsContract.LightId.LIGHT_2))
                    }
                }
        )
    }

    private fun LightTrigger.toThingsActionRequest(lightId: SettingsContract.LightId): ThingsActionRequest {
        val isOn = this.type == TriggerType.ON
        return when (lightId) {
            SettingsContract.LightId.LIGHT_1 -> ThingsActionRequest.Light1(isOn)
            SettingsContract.LightId.LIGHT_2 -> ThingsActionRequest.Light2(isOn)
        }
    }
}