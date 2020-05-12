@file:Suppress("DEPRECATION")

package com.oomall.kouclodelivery.ui.view.verify

import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseCameraActivity
import com.oomall.kouclodelivery.ui.view.verify.utils.BitmapUtil
import com.oomall.kouclodelivery.ui.view.verify.utils.Constant
import com.oomall.kouclodelivery.ui.view_model.VerifyViewModel
import kotlinx.android.synthetic.main.activity_camera.*


/**
 * 废话少说，照相就完了
 */
class CameraActivity : BaseCameraActivity(), View.OnClickListener {
    private val TAG = CameraActivity::class.java.name

    lateinit var verifyViewModel: VerifyViewModel
    private var idType: String = "0"

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.vTakePhoto -> {
                pbTakePhoto.visibility = View.VISIBLE
                takePhoto()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        verifyViewModel = ViewModelProviders.of(this).get(VerifyViewModel::class.java)
        setTitleVisibility(View.GONE)
        surfaceView = takePhotoSurface
        vTakePhoto.setOnClickListener(this)
        when (intent.extras!!.getInt("param1", Constant.REQUEST_FRONT_CODE)) {
            Constant.REQUEST_FRONT_CODE -> {
                setType(1)
            }
            Constant.REQUEST_BACK_CODE -> {
                setType(2)
            }
            Constant.REQUEST_HOLD_CODE -> {
                setType(3)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pbTakePhoto.visibility = View.GONE
    }

    /**
     * 设置类型
     */
    private fun setType(type: Int) {
        when (type) {
            1 -> rlFront.visibility = View.VISIBLE
            else -> rlFront.visibility = View.GONE
        }
        when (type) {
            2 -> rlBack.visibility = View.VISIBLE
            else -> rlBack.visibility = View.GONE
        }
        when (type) {
            3 -> rlHold.visibility = View.VISIBLE
            else -> rlHold.visibility = View.GONE
        }
    }


    override fun onPictureTaken(data: ByteArray?, camera: Camera?) {

        showToast("-------$data")

        //截取选框中的图片，保存到本地
        val bitmap = BitmapUtil.getBitmap(data)
        BitmapUtil.cropBitmap(cropView, takePhotoSurface, bitmap!!, object : BitmapUtil.CropCallback {
            override fun cropSuccess(bmp: Bitmap) {
                //子线程
                when (intent.extras!!.getInt("param1", Constant.REQUEST_FRONT_CODE)) {
                    Constant.REQUEST_FRONT_CODE -> {
                        BitmapUtil.saveBitmap2file(bmp, Constant.ID_CARD_FRONT)
                    }
                    Constant.REQUEST_BACK_CODE -> {
                        BitmapUtil.saveBitmap2file(bmp, Constant.ID_CARD_BACK)
                    }
                    Constant.REQUEST_HOLD_CODE -> {
                        BitmapUtil.rotateBitmap(90f,bmp,object:BitmapUtil.RotateCallback{
                            override fun rotateSuccess(bmp: Bitmap) {
                                BitmapUtil.saveBitmap2file(bmp, Constant.ID_CARD_HOLD)
                            }
                        })
                    }
                }
                runOnUiThread {
                    startActivityInt(
                        PhotoPreviewActivity::class.java,
                        intent.extras!!.getInt("param1", Constant.REQUEST_FRONT_CODE)
                    )
                }
            }
        })
    }

}
