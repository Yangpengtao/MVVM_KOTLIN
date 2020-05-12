package com.oomall.kouclodelivery.ui.base

import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.TextureView
import com.oomall.kouclodelivery.camera2.CameraManager2
import com.oomall.kouclodelivery.ui.widget.AutoFitTextureView

abstract class BaseCamera2Activity : BaseActivity(), TextureView.SurfaceTextureListener {

    //记得在子activity里初始化
      private lateinit var cameraManager2: CameraManager2
      var textureView: AutoFitTextureView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        cameraManager2.openCamera(width, height)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        // Ignored, Camera does all the work for us
        cameraManager2.configureTransform(width, height)

    }

    override fun onSurfaceTextureDestroyed(texture: SurfaceTexture) = true

    override fun onSurfaceTextureUpdated(texture: SurfaceTexture) = Unit




    override fun onResume() {
        super.onResume()
        cameraManager2.startBackgroundThread()

        // 关闭屏幕并重新打开时，SurfaceTexture已经已经可用，
        // 且"OnSurfactureAvailable"将不被调用。在这种情况下，
        // 我们可以打开从此处开始相机并开始预览（否则，我们将等待直到表面就绪）SurfacettureListener)。
        if (textureView!!.isAvailable) {
            cameraManager2.openCamera(textureView!!.width, textureView!!.height)
        } else {
            textureView!!.surfaceTextureListener = this
        }
    }
    override fun onPause() {
        cameraManager2.closeCamera()
        cameraManager2.stopBackgroundThread()
        super.onPause()
    }

}