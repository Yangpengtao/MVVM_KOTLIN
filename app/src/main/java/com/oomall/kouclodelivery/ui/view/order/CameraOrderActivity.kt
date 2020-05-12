@file:Suppress("DEPRECATION")

package com.oomall.kouclodelivery.ui.view.order

import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import android.view.View
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseCameraActivity
import com.oomall.kouclodelivery.ui.view.order.utils.BitmapUtil
import kotlinx.android.synthetic.main.activity_camera_order.*

/**
 * 给我拍小票
 */
class CameraOrderActivity : BaseCameraActivity(), View.OnClickListener {
    private val TAG = CameraOrderActivity::class.java.name
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.vTakePhoto -> {
                pbTakePhoto.visibility = View.VISIBLE
                takePhoto()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pbTakePhoto.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_order)
        setTitleVisibility(View.GONE)
        surfaceView = takePhotoSurface
        vTakePhoto.setOnClickListener(this)
    }


    override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
        //截取选框中的图片，保存到本地
        val bitmap = BitmapUtil.getBitmap(data)
        BitmapUtil.cropBitmap(cropView, takePhotoSurface, bitmap!!, object : BitmapUtil.CropCallback {
            override fun cropSuccess(bmp: Bitmap) {
                BitmapUtil.rotateBitmap(90f, bmp, object : BitmapUtil.RotateCallback {
                    override fun rotateSuccess(bmp: Bitmap) {
                        BitmapUtil.saveBitmap2file(bmp, "order_number")
                        startActivity(PhotoPreviewOrderActivity::class.java)
                    }
                })
            }
        })
    }

}
