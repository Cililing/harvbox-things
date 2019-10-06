package com.cililing.harvbox.thingsapp.thingscontroller.core

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.media.ImageReader
import android.os.Handler

// Implementation is based on official example of doorbell with camera.
// Source can be found here: https://github.com/androidthings/doorbell/blob/master/app/src/main/java/com/example/androidthings/doorbell/DoorbellCamera.java
internal object CameraAdapter {
    private const val IMAGE_WIDTH = 640
    private const val IMAGE_HEIGHT = 480
    private const val CONCURRENT_PHOTOS = 1

    private lateinit var imageReader: ImageReader

    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null

    @SuppressLint("MissingPermission") // So what? :D
    fun initialize(
        context: Context,
        cameraHandler: Handler,
        onImageAccessibleListener: ImageReader.OnImageAvailableListener
    ) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val camIds = try {
            cameraManager.cameraIdList
        } catch (ex: CameraAccessException) {
            return
        }

        val cameraId = camIds.first()

        imageReader = ImageReader.newInstance(
            IMAGE_WIDTH,
            IMAGE_HEIGHT,
            ImageFormat.JPEG,
            CONCURRENT_PHOTOS
        )
        imageReader.setOnImageAvailableListener(onImageAccessibleListener, cameraHandler)

        cameraManager.openCamera(cameraId, stateCallback, cameraHandler)
    }

    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
        }

        override fun onError(camera: CameraDevice, error: Int) {
            cameraDevice?.close()
        }

        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice?.close()
        }

        override fun onClosed(camera: CameraDevice) {
            cameraDevice = null
        }
    }

    fun takePicture(): Boolean {
        if (cameraDevice == null) return false

        cameraDevice?.createCaptureSession(
            listOf(imageReader.surface),
            sessionCallback,
            null
        )
        return true
    }

    private val sessionCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigureFailed(session: CameraCaptureSession) {
        }

        override fun onConfigured(session: CameraCaptureSession) {
            if (cameraDevice == null) return
            captureSession = session
            triggerImageCapture()
        }
    }

    fun triggerImageCapture() {
        cameraDevice?.createCaptureRequest(
            CameraDevice.TEMPLATE_STILL_CAPTURE
        )?.apply {
            addTarget(imageReader.surface)
            set(
                CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_ON
            )
        }?.build()
            ?.let {
                captureSession?.capture(it, captureCallback, null)
            }
    }

    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            session.close()
            captureSession = null
        }
    }

    fun shutDown() {
        cameraDevice?.close()
    }
}