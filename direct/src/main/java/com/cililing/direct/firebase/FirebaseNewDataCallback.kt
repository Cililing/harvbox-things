package com.cililing.direct.firebase

import com.cililing.direct.DirectService
import com.cililing.harvbox.common.ThingsActionRequest

interface FirebaseNewDataCallback {
    fun onNewLight1(value: Boolean)
    fun onNewLight2(value: Boolean)
    fun onPhotoRequested()
}

class FirebaseNewDataCallbackImpl(
    private val directService: DirectService
) : FirebaseNewDataCallback {
    override fun onNewLight1(value: Boolean) {
        directService.request(ThingsActionRequest.Light1(value))
    }

    override fun onNewLight2(value: Boolean) {
        directService.request(ThingsActionRequest.Light2(value))
    }

    override fun onPhotoRequested() {
        directService.request(ThingsActionRequest.Photo)
    }
}
