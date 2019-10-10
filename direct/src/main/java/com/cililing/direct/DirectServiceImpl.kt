package com.cililing.direct

import android.content.Context
import com.cililing.direct.elastic.ElasticSearch
import com.cililing.direct.elastic.getElasticModule
import com.cililing.direct.firebase.FirebaseAppDatabase
import com.cililing.direct.firebase.FirebaseAppStorage
import com.cililing.direct.firebase.getFirebaseModule
import com.cililing.harvbox.common.DateTimeParser
import com.cililing.harvbox.common.Logger
import com.cililing.harvbox.common.SemiblockValueReporter
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

internal class DirectServiceImpl(
    context: Context,
    private val firebaseApp: FirebaseApp,
    cooldown: Long,
    private val isDebug: Boolean,
    private val dateTimeParser: DateTimeParser = DateTimeParser
) : DirectService, StandaloneKoinCompontent {

    init {
        StandaloneKoinContext.koinApplication = koinApplication {
            if (isDebug) printLogger()

            modules(listOf(
                getDirectKoinModule(context, isDebug, this@DirectServiceImpl::processPhoto),
                getFirebaseModule(firebaseApp),
                getElasticModule(cooldown)
            ))
        }
    }

    private val thingsController: ThingsController by inject()
    private val firebaseAppDatabase: FirebaseAppDatabase by inject {
        parametersOf(this)
    }
    private val elasticSearch: ElasticSearch by inject()
    private val logger: Logger by inject()
    private val firebaseAppStorage: FirebaseAppStorage by inject()

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(
        parentJob + Dispatchers.Default
    )

    private val exactTimeSchedulerExecutor = ExactTimeScheduleExecutor()

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

    override fun setElasticCooldown(cooldown: Long) {
        elasticSearch.setCooldown(cooldown)
    }

    override fun scheduleTasks(tasks: List<ExactTimeScheduleTask>) {
        logger.i("Scheduling tasks: $tasks")
        exactTimeSchedulerExecutor.schedule(tasks)
    }

    override fun request(actionRequest: ThingsActionRequest) {
        logger.i("Requesting: $actionRequest")
        coroutineScope.launch {
            when (actionRequest) {
                is ThingsActionRequest.Light1 -> {
                    withContext(Dispatchers.IO) {
                        light1Reporter.value = actionRequest.isOn
                        thingsController.setState(actionRequest.isOn, null)
                    }
                }
                is ThingsActionRequest.Light2 -> {
                    withContext(Dispatchers.IO) {
                        light2Reporter.value = actionRequest.isOn
                        thingsController.setState(null, actionRequest.isOn)
                    }
                }
                is ThingsActionRequest.Photo -> {
                    thingsController.requestPhoto()
                }
            }
        }
    }

    override fun release() {
        parentJob.cancel()
        exactTimeSchedulerExecutor.release()
    }

    private suspend fun reportCurrentStatusToElastic(snapshot: StatusSnapshot) {
        elasticSearch.reportSnapshot(snapshot)
    }

    private fun reportCurrentStatusToFirebase(statusSnapshot: StatusSnapshot) {
        firebaseAppDatabase.post(statusSnapshot)
    }

    private fun generateThingsSnapshot(): StatusSnapshot {
        val snapshot = thingsController.getSnapshot().toFirebaseThingsSnapshot(
            dateTimeParser.getFormattedDate()
        )
        return snapshot.copy(
            light1PowerOn = light1Reporter.obtainValueAndRelease(snapshot.light1PowerOn),
            light2PowerOn = light2Reporter.obtainValueAndRelease(snapshot.light2PowerOn)
        )
    }

    private fun processPhoto(byteArray: ByteArray) {
        firebaseAppStorage.putPhoto(byteArray) {
            // When done just put newest url to fb rt db
            firebaseAppDatabase.newPhotoAvailable(it)
        }
    }
}