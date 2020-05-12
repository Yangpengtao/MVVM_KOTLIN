package com.oomall.kouclodelivery.camera2

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import com.oomall.kouclodelivery.ui.base.BaseCamera2Activity
import com.oomall.kouclodelivery.ui.widget.AutoFitTextureView
import java.io.File
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.TimeUnit

class CameraManager2(private val activity: BaseCamera2Activity, private val textureView: AutoFitTextureView,private val picName:String) {


    /**
     * 一个[信号量]，以防止应用程序退出之前，关闭相机。
     */
    private val cameraOpenCloseLock = Semaphore(1)
    /**
     * 用于运行不应该阻止UI的任务的附加线程。
     */
    private var backgroundThread: HandlerThread? = null

    /**
     * 用于在后台运行任务的[Handler]。
     */
    private var backgroundHandler: Handler? = null

    /**
     * 当前的ID[CameraDevice]。
     */
    private lateinit var cameraId: String

    /**
     * 对打开的[CameraDevice]的引用。
     */
    private var cameraDevice: CameraDevice? = null

    /**
     * 处理静态图像捕获的[ImageReader]。
     */
    private var imageReader: ImageReader? = null


    /**
     * 一种处理静止图像捕捉的[ImageReader].[android.util.ize]的照相机预览。
     */
    private lateinit var previewSize: Size


    /**
     * 这是我们图片的输出文件。
     */
    private var file: File = File(activity.getExternalFilesDir(null), picName)

    /**
     * 相机传感器的定位
     */
    private var sensorOrientation = 0


    /**
     * 当前的相机设备是否支持Flash。
     */
    private var flashSupported = false


    /**
     * [CaptureRequest.Builder]用于照相机预览
     */
    private lateinit var previewRequestBuilder: CaptureRequest.Builder

    /**
     * 照相机预览用的[CameraCaptureSession]
     */
    private var captureSession: CameraCaptureSession? = null

    /**
     * [.previewRequestBuilder]生成的[CaptureRequest]
     */
    private lateinit var previewRequest: CaptureRequest

    /**
     * 用于拍照的摄像机状态的当前状态。
     *
     * @see .captureCallback
     */
    private var state = Constant.STATE_PREVIEW

    /**
     * 启动后台线程及其[Handler]。
     */
    fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread!!.looper)
    }


    /**
     * 打开[ CameraId]指定的摄像机。
     */
    @SuppressLint("MissingPermission")
    fun openCamera(width: Int, height: Int) {
        setUpCameraOutputs(width, height)
        configureTransform(width, height)
        val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // 等待相机打开-2.5秒就足够了
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw RuntimeException("Time out waiting to lock camera opening.")
            }
            manager.openCamera(cameraId, stateCallback, backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e("", e.toString())
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }
    }

    /**
     * 关闭相机
     */
    fun closeCamera() {
        try {
            cameraOpenCloseLock.acquire()
            captureSession?.close()
            captureSession = null
            cameraDevice?.close()
            cameraDevice = null
            imageReader?.close()
            imageReader = null
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera closing.", e)
        } finally {
            cameraOpenCloseLock.release()
        }
    }

    /**
     * 停止后台线程及其[Handler]。
     */
    fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Log.e(CameraManager2::class.java.name, e.toString())
        }

    }

    /**
     * 将必要的[android.Graphics.Matrix]转换配置为“textureView”。
     * 在setUpCameraOutput中确定摄像机预览大小以及“textureView”的大小固定之后，应该调用此方法。
     *
     * @param viewWidth  textureView`的宽度
     * @param viewHeight  textureView`的高度
     * @param textureView  textureView
     */
    fun configureTransform(viewWidth: Int, viewHeight: Int) {
        val rotation = activity.windowManager.defaultDisplay.rotation
        val matrix = Matrix()
        val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect = RectF(0f, 0f, previewSize.height.toFloat(), previewSize.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()

        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
            val scale = Math.max(
                    viewHeight.toFloat() / previewSize.height,
                    viewWidth.toFloat() / previewSize.width)
            with(matrix) {
                setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
                postScale(scale, scale, centerX, centerY)
                postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
            }
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180f, centerX, centerY)
        }
        textureView.setTransform(matrix)
    }

    /**
     * 设置与照相机相关的成员变量。
     * @param width  摄像机预览可用大小的宽度
     * @param height 摄像机预览可用大小的高度
     * @param textureView  textureView
     */
    private fun setUpCameraOutputs(width: Int, height: Int) {
        val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            for (cameraId in manager.cameraIdList) {
                val characteristics = manager.getCameraCharacteristics(cameraId)

                //  我们不使用正面摄像头。
                val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (cameraDirection != null &&
                        cameraDirection == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue
                }

                val map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: continue

                // 对于静止图像捕获，我们使用最大可用大小。
                val largest = Collections.max(
                        Arrays.asList(*map.getOutputSizes(ImageFormat.JPEG)),
                        CompareSizesByArea())
                imageReader = ImageReader.newInstance(largest.width, largest.height,
                        ImageFormat.JPEG, /*maxImages*/ 2).apply {
                    setOnImageAvailableListener(onImageAvailableListener, backgroundHandler)
                }

                // 了解是否需要交换尺寸以获取相对于传感器
                // coordinate.
                val displayRotation = activity.windowManager.defaultDisplay.rotation

                sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
                val swappedDimensions = areDimensionsSwapped(displayRotation)

                val displaySize = Point()
                activity.windowManager.defaultDisplay.getSize(displaySize)
                val rotatedPreviewWidth = if (swappedDimensions) height else width
                val rotatedPreviewHeight = if (swappedDimensions) width else height
                var maxPreviewWidth = if (swappedDimensions) displaySize.y else displaySize.x
                var maxPreviewHeight = if (swappedDimensions) displaySize.x else displaySize.y

                if (maxPreviewWidth > Constant.MAX_PREVIEW_WIDTH) maxPreviewWidth = Constant.MAX_PREVIEW_WIDTH
                if (maxPreviewHeight > Constant.MAX_PREVIEW_HEIGHT) maxPreviewHeight = Constant.MAX_PREVIEW_HEIGHT

                // 尝试使用太大的预览大小可能会超过摄像机。
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
                // garbage capture data.
                previewSize = Constant.chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java),
                        rotatedPreviewWidth, rotatedPreviewHeight,
                        maxPreviewWidth, maxPreviewHeight,
                        largest)

                // 我们适合的宽比的textureView和大小的预览我们选择。
                if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    textureView.setAspectRatio(previewSize.width, previewSize.height)
                } else {
                    textureView.setAspectRatio(previewSize.height, previewSize.width)
                }

                // 检查是否支持闪存。
                flashSupported =
                        characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true

                this.cameraId = cameraId

                // 我们找到了一个可行的相机并完成了成员变量的设置，
                // 因此，我们不需要迭代通过其他可用的相机。
                return
            }
        } catch (e: CameraAccessException) {
            Log.e("", e.toString())
        } catch (e: NullPointerException) {

        }

    }

    /**
     * 这是[ImageReader]的回调对象。“onImageAvailable”将在静止图像准备保存时调用。
     */
    private val onImageAvailableListener = ImageReader.OnImageAvailableListener {
        backgroundHandler?.post(ImageSaver(it.acquireNextImage(), file))
    }

    /**
     * 确定在手机当前旋转的情况下是否交换了尺寸。
     * @param displayRotation 显示器的当前旋转
     * @return 如果尺寸被交换，则为true，否则为false。
     */
    private fun areDimensionsSwapped(displayRotation: Int): Boolean {
        var swappedDimensions = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 -> {
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    swappedDimensions = true
                }
            }
            Surface.ROTATION_90, Surface.ROTATION_270 -> {
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    swappedDimensions = true
                }
            }
            else -> {
                Log.e(CameraManager2::class.java.name, "Display rotation is invalid: $displayRotation")
            }
        }
        return swappedDimensions
    }


    /**
     * [CameraDevice.StateCallback]在[CameraDevice]更改其状态时被调用。
     */
    private val stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            this@CameraManager2.cameraDevice = cameraDevice
            createCameraPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraOpenCloseLock.release()
            cameraDevice.close()
            this@CameraManager2.cameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            onDisconnected(cameraDevice)
            activity.finish()
        }

    }

    /**
     * 为摄像机预览创建一个新的[CameraCaptureSession]。
     */
    private fun createCameraPreviewSession() {
        try {
            val texture = textureView.surfaceTexture

            // 我们将默认缓冲区的大小配置为所需的摄像机预览大小。
            texture.setDefaultBufferSize(previewSize.width, previewSize.height)

            // 这是我们需要开始预览的输出面。
            val surface = Surface(texture)

            //我们使用输出曲面设置CaptureRequest.Builder。
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(
                    CameraDevice.TEMPLATE_PREVIEW
            )
            previewRequestBuilder.addTarget(surface)

            // 在这里，我们创建一个CameraCaptureSession相机预览。
            cameraDevice?.createCaptureSession(Arrays.asList(surface, imageReader?.surface),
                    object : CameraCaptureSession.StateCallback() {

                        override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                            // 摄像机已经关闭了
                            if (cameraDevice == null) return

                            // 会话准备就绪后，我们将开始显示预览。
                            captureSession = cameraCaptureSession
                            try {
                                // 自动对焦应该是连续的相机预览。
                                previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                // Flash is automatically enabled when necessary.
                                setAutoFlash(previewRequestBuilder)

                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder.build()
                                captureSession?.setRepeatingRequest(previewRequest,
                                        captureCallback, backgroundHandler)
                            } catch (e: CameraAccessException) {
                                Log.e("", e.toString())
                            }

                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) {

                        }
                    }, null)
        } catch (e: CameraAccessException) {
            Log.e("", e.toString())
        }

    }

    private fun setAutoFlash(requestBuilder: CaptureRequest.Builder) {
        if (flashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
        }
    }

    /**
     * 处理与JPEG捕获相关的事件
     */
    private val captureCallback = object : CameraCaptureSession.CaptureCallback() {

        private fun process(result: CaptureResult) {
            when (state) {
                Constant.STATE_PREVIEW -> Unit // 当摄像机预览正常工作时，不要做任何事情。
                Constant.STATE_WAITING_LOCK -> capturePicture(result)
                Constant.STATE_WAITING_PRECAPTURE -> {
                    // CONTROL_AE_STATE can be null on some devices
                    val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        state = Constant.STATE_WAITING_NON_PRECAPTURE
                    }
                }
                Constant.STATE_WAITING_NON_PRECAPTURE -> {
                    // CONTROL_AE_STATE can be null on some devices
                    val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        state = Constant.STATE_PICTURE_TAKEN
                        captureStillPicture()
                    }
                }
            }
        }

        @Synchronized
        private fun capturePicture(result: CaptureResult) {
            /* val afState = result.get(CaptureResult.CONTROL_AF_STATE)
             Log.e("tag","afState------$afState")
             if (afState == null) {
                 captureStillPicture()
             } else if (afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED
                     || afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                 // CONTROL_AE_STATE can be null on some devices
                 val aeState = result.get(CaptureResult.CONTROL_AE_STATE)
                 if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                     state = Constant.STATE_PICTURE_TAKEN
                     captureStillPicture()
                 } else {
                     runPrecaptureSequence()
                 }
             }*/
            captureStillPicture()


        }


        /**
         * 当图像捕获进行部分正向进度时，调用此方法；一些（但不是所有）来自图像捕获的结果可用。
         */
        override fun onCaptureProgressed(session: CameraCaptureSession,
                                         request: CaptureRequest,
                                         partialResult: CaptureResult) {
            process(partialResult)
        }


        /**
         * 当照相机设备无法为请求生成CaptureResult时，调用此方法
         */
        override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
            super.onCaptureFailed(session, request, failure)
        }

        /**
         * 当图像捕获完全完成并且所有结果元数据都可用时，将调用此方法。
         */
        override fun onCaptureCompleted(session: CameraCaptureSession,
                                        request: CaptureRequest,
                                        result: TotalCaptureResult) {
            process(result)
            Log.e("tag", "----${result.get(CaptureResult.CONTROL_AF_STATE)}")
            Log.e("tag", "----$result ")
            Log.e("tag", "----$state ")
        }
    }

    /**
     *  捕捉一张静止的照片。当我们从[.lockFocus]获得[.captureCallback]中的响应时，应该调用此方法。
     */
    private fun captureStillPicture() {
        try {
            if (cameraDevice == null) return
            val rotation = activity.windowManager.defaultDisplay.rotation

            // This is the CaptureRequest.Builder that we use to take a picture.
            val captureBuilder = cameraDevice?.createCaptureRequest(
                    CameraDevice.TEMPLATE_STILL_CAPTURE)?.apply {
                addTarget(imageReader?.surface!!)

                // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
                // We have to take that into account and rotate JPEG properly.
                // For devices with orientation of 90, we return our mapping from ORIENTATIONS.
                // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
                set(CaptureRequest.JPEG_ORIENTATION,
                        (Constant.ORIENTATIONS.get(rotation) + sensorOrientation + 270) % 360)

                // Use the same AE and AF modes as the preview.
                set(CaptureRequest.CONTROL_AF_MODE,
                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            }?.also { setAutoFlash(it) }

            val captureCallback = object : CameraCaptureSession.CaptureCallback() {

                override fun onCaptureCompleted(session: CameraCaptureSession,
                                                request: CaptureRequest,
                                                result: TotalCaptureResult) {
                    activity.showToast("Saved: $file")
                    Log.d("TAG", file.toString())
                    unlockFocus()
                }

                override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
                    Log.d("TAG", "onCaptureFailed----${failure.reason}")
                    super.onCaptureFailed(session, request, failure)
                }

                override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult) {

                    Log.d("TAG", "onCaptureProgressed----$partialResult")
                    super.onCaptureProgressed(session, request, partialResult)
                }


            }

            captureSession?.apply {
                stopRepeating()
                abortCaptures()
                capture(captureBuilder?.build()!!, captureCallback, null)
            }
        } catch (e: CameraAccessException) {
            Log.e("", e.toString())
        }
    }

    /**
     * 打开焦点。当静态图像捕获序列完成时，应该调用该方法。
     */
    private fun unlockFocus() {
        try {
            // Reset the auto-focus trigger
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
            setAutoFlash(previewRequestBuilder)
            captureSession?.capture(previewRequestBuilder.build(), captureCallback,
                    backgroundHandler)
            // After this, the camera will go back to the normal state of preview.
            state = Constant.STATE_PREVIEW
            captureSession?.setRepeatingRequest(previewRequest, captureCallback,
                    backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e("TAG", e.toString())
        }

    }

    /**
     * 锁定焦点，作为静止图像捕获的第一步。
     */
    fun lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START)
            // Tell #captureCallback to wait for the lock.
            state = Constant.STATE_WAITING_LOCK
            captureSession?.capture(previewRequestBuilder.build(), captureCallback,
                    backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e("TAG", e.toString())
        }

    }

    /**
     * 运行捕获前序列以捕获静止图像。当我们从[.lockFocus]获得[.captureCallback]中的响应时，应该调用此方法。
     */
    private fun runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            previewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START)
            // Tell #captureCallback to wait for the precapture sequence to be set.
            state = Constant.STATE_WAITING_PRECAPTURE
            captureSession?.capture(previewRequestBuilder.build(), captureCallback,
                    backgroundHandler)
        } catch (e: CameraAccessException) {
            Log.e("TAG", e.toString())
        }

    }

}