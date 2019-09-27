package com.cililing.direct

import com.cililing.direct.elastic.ElasticSearch
import com.cililing.direct.elastic.getElasticModule
import com.cililing.direct.firebase.FirebaseAppDatabase
import com.cililing.direct.firebase.getFirebaseModule
import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.common.ThingsActionRequest
import com.cililing.harvbox.thingsapp.thingscontroller.ThingsController
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

internal class DirectServiceImpl(
        private val firebaseApp: FirebaseApp,
        private val isDebug: Boolean
) : DirectService, StandaloneKoinCompontent {

    init {
        StandaloneKoinContext.koinApplication = koinApplication {
            if (isDebug) printLogger()

            modules(listOf(
                    getDirectKoinModule(isDebug),
                    getFirebaseModule(firebaseApp),
                    getElasticModule()
            ))
        }
    }

    private val thingsController: ThingsController by inject()
    private val cloudDatabase: FirebaseAppDatabase by inject {
        parametersOf(this)
    }
    private val elasticSearch: ElasticSearch by inject()
    private val logger: Logger by inject()

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
            parentJob + Dispatchers.Default
    )

    private val light1Reporter = SemiblockValueReporter<Boolean>()
    private val light2Reporter = SemiblockValueReporter<Boolean>()

    /**
     * This will return snapshot do provider and report process in background.
     */
    override suspend fun getAndProcess(): StatusSnapshot {
        logger.i("Status snaphot requested!")
        return generateThingsSnapshot().also {
            // Other things do async to return result faster
            withContext(Dispatchers.Default) {
                reportCurrentStatusToFirebase(it)
                withContext(Dispatchers.IO) {
                    reportCurrentStatusToElastic(it)
                }
            }
        }
    }

    override fun request(actionRequest: ThingsActionRequest) {
        logger.i("Requesting: $actionRequest")
        coroutineScope.launch {
            val action = when (actionRequest) {
                is ThingsActionRequest.Light1 -> {
                    {
                        light1Reporter.value = actionRequest.isOn
                        thingsController.setState(actionRequest.isOn, null)
                    }
                }
                is ThingsActionRequest.Light2 -> {
                    {
                        light2Reporter.value = actionRequest.isOn
                        thingsController.setState(null, actionRequest.isOn)
                    }
                }
            }
            withContext(Dispatchers.IO) {
                action()
            }
        }
    }

    override fun release() {
        parentJob.cancel()
    }

    private suspend fun reportCurrentStatusToElastic(snapshot: StatusSnapshot) {
        elasticSearch.reportSnapshot(snapshot)
    }

    private fun reportCurrentStatusToFirebase(statusSnapshot: StatusSnapshot) {
        cloudDatabase.post(statusSnapshot)
    }

    private fun generateThingsSnapshot(): StatusSnapshot {
        val calendar: Calendar = Calendar.getInstance()
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US)
        val currentLocalTime: Date = calendar.time
        val localTime = date.format(currentLocalTime)

        val snapshot = thingsController.getSnapshot().toFirebaseThingsSnapshot(localTime)

        return snapshot.copy(
                light1PowerOn = light1Reporter.obtainValueAndRelease(snapshot.light1PowerOn),
                light2PowerOn = light2Reporter.obtainValueAndRelease(snapshot.light2PowerOn)
        )
    }
}