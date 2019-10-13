package com.cililing.client

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.google.firebase.FirebaseApp
import org.koin.core.inject
import org.koin.dsl.koinApplication

class ClientServiceImpl(
    firebaseApp: FirebaseApp,
    debug: Boolean
) : ClientService, StandaloneKoinComponent {

    init {
        StandaloneKoinComponent.StandaloneKoinContext.application = koinApplication {
            modules(getClientKoin(firebaseApp, debug))
        }
    }

    private val mFirebaseRelatimeDb: FirebaseRelatimeDatabase by inject()

    override fun request(actionRequest: ThingsActionRequest) {
        when (actionRequest) {
            is ThingsActionRequest.Light1 -> mFirebaseRelatimeDb.setLight1(actionRequest.isOn)
            is ThingsActionRequest.Light2 -> mFirebaseRelatimeDb.setLight2(actionRequest.isOn)
            is ThingsActionRequest.Photo -> mFirebaseRelatimeDb.triggerPhoto()
        }
    }

    override fun getCurrentSnapshot(): StatusSnapshot {
        return mFirebaseRelatimeDb.getSnapshot() ?: StatusSnapshot()
    }
}