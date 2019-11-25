package com.cililing.harvbox.thingsapp.dashboard

import com.cililing.harvbox.common.StatusSnapshot
import com.cililing.harvbox.thingsapp.AppController
import com.cililing.harvbox.thingsapp.core.CurrentSnapshotProvider
import com.cililing.harvbox.thingsapp.core.LightStatusHandler
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class DashboardPresenterTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val viewMock: DashboardContract.View = mock()
    private val appControllerMock: AppController = mock()
    private val currentSnapshotProviderMock: CurrentSnapshotProvider<StatusSnapshot> = mock()
    private val lastPhotoProviderMock: CurrentSnapshotProvider<String> = mock()
    private val lightStatusHandlerMock: LightStatusHandler = mock()

    lateinit var instance: DashboardPresenter

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        instance = DashboardPresenter(
            viewMock,
            appControllerMock,
            currentSnapshotProviderMock,
            lastPhotoProviderMock,
            lightStatusHandlerMock
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun onRequestPhotoClicked_requestPhotoCalled() {
        instance.onRequestPhotoClicked()
        verify(appControllerMock).requestPhoto()
    }

    @Test
    fun onRequestPhotoClicked_resetCurrentPhoto() {
        runBlocking {
            instance.onRequestPhotoClicked()
        }
        verify(viewMock).onNewPhotoReceived(null)
    }
}