@file:Suppress("DEPRECATION")

package com.oomall.kouclodelivery.ui.base

import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.oomall.kouclodelivery.camera.CameraManager
import java.io.IOException

/**
 * 相机的父类
 */
abstract class BaseCameraActivity : BaseActivity(), SurfaceHolder.Callback, Camera.PictureCallback {

    lateinit var cameraManager: CameraManager
    private var isHasSurface = false
    lateinit var surfaceView: SurfaceView


    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) = Unit

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        isHasSurface = false
    }


    override fun onResume() {
        super.onResume()
        cameraManager = CameraManager(this)
        if (isHasSurface) {
            initCamera(surfaceView.holder)
        } else {
            surfaceView.holder.addCallback(this)
        }
    }

    override fun onPause() {
        cameraManager.stopPreview()
        cameraManager.closeDriver()
        if (!isHasSurface) {
            surfaceView.holder.removeCallback(this)
        }
        super.onPause()
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        if (!isHasSurface) {
            isHasSurface = true
            initCamera(p0)
        }
    }

    private fun initCamera(surfaceHolder: SurfaceHolder?) {
        if (surfaceHolder == null) {
            throw IllegalStateException("No SurfaceHolder provided")
        }
        if (cameraManager.isOpen) {
            return
        }
        try {
            cameraManager.openDriver(surfaceHolder)
            cameraManager.startPreview()

        } catch (ioe: IOException) {
        } catch (e: RuntimeException) {
        }
    }

    fun takePhoto() {
        cameraManager.takePhoto(this)
    }

    override fun onPictureTaken(data: ByteArray?, camera: Camera?) = Unit
}