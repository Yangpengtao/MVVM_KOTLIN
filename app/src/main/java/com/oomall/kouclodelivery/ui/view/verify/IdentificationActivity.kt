package com.oomall.kouclodelivery.ui.view.verify

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.proxy.HelperImageLoader
import com.oomall.kouclodelivery.ui.view.verify.utils.Constant
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view_model.VerifyViewModel
import com.oomall.kouclodelivery.ui.dialog.UploadImageDialog
import com.oomall.kouclodelivery.utils.FileUtil
import com.oomall.kouclodelivery.utils.IntentUtil
import com.oomall.kouclodelivery.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_identification.*

/**
 * 身份认证
 */
class IdentificationActivity : BaseActivity(), View.OnClickListener, UploadImageDialog.ClickListener {

    private val TAG: String = IdentificationActivity::class.java.name

    private var verifyReceiver: VerifyReceiver? = null
    lateinit var verifyViewModel: VerifyViewModel
    lateinit var uploadDialog: UploadImageDialog
    //1 正面 2，反面 3，手持
    private var photoType = 1


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.ivFrontPhoto -> {
                uploadDialog.show()
                photoType = 1
            }
            R.id.ivBackPhoto -> {
                photoType = 2
                uploadDialog.show()
            }
            R.id.ivHoldPhoto -> {
                photoType = 3
                uploadDialog.show()
            }
        }
    }

    override fun takePhoto() {
        when (photoType) {
            1 -> {
                startActivityInt(CameraActivity::class.java, Constant.REQUEST_FRONT_CODE)
            }
            2 -> {
                startActivityInt(CameraActivity::class.java, Constant.REQUEST_BACK_CODE)
            }
            3 -> {
                startActivityInt(CameraActivity::class.java, Constant.REQUEST_HOLD_CODE)
            }
        }
    }

    override fun alum() {
        when (photoType) {
            1 -> {
                startActivityForResult(
                    IntentUtil.getAlumIntent(Constant.ID_CARD_FRONT, true),
                    Constant.REQUEST_FRONT_CODE
                )
            }
            2 -> {
                startActivityForResult(
                    IntentUtil.getAlumIntent(Constant.ID_CARD_BACK, true),
                    Constant.REQUEST_BACK_CODE
                )
            }
            3 -> {
                startActivityForResult(
                    IntentUtil.getAlumIntent(Constant.ID_CARD_HOLD, true),
                    Constant.REQUEST_HOLD_CODE
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identification)
        verifyViewModel = ViewModelProviders.of(this).get(VerifyViewModel::class.java)
        uploadDialog = UploadImageDialog(this)
        uploadDialog.setPopClickListener(this)
        getTitle_().text = "身份认证"
        title_left_img.visibility = View.VISIBLE
        setListener()
        initBroadcast()
        verifyViewModel.frontImg.observe(this, Observer {
            if (it.code == 1) {
                ToastUtil.show("上传成功")
                ivFrontPhoto.setImageBitmap(FileUtil.getImage(Constant.ID_CARD_FRONT))
                ivFrontPhoto.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        })
        verifyViewModel.backImg.observe(this, Observer {
            if (it.code == 1) {
                ToastUtil.show("上传成功")
                HelperImageLoader.loadNormalImage(ivBackPhoto, FileUtil.getImageFileUri(Constant.ID_CARD_BACK)!!)
                ivBackPhoto.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        })
        verifyViewModel.holderImg.observe(this, Observer {
            if (it.code == 1) {
                ToastUtil.show("上传成功")
                HelperImageLoader.loadNormalImage(ivHoldPhoto, FileUtil.getImageFileUri(Constant.ID_CARD_HOLD)!!)
                ivHoldPhoto.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            upload(requestCode)
        }
    }

    private fun upload(requestCode: Int) {
        when (requestCode) {
            Constant.REQUEST_FRONT_CODE -> {
                verifyViewModel.uploadFront(FileUtil.getFileImage(Constant.ID_CARD_FRONT))
            }
            Constant.REQUEST_BACK_CODE -> {
                verifyViewModel.uploadBack(FileUtil.getFileImage(Constant.ID_CARD_BACK))
            }
            Constant.REQUEST_HOLD_CODE -> {
                verifyViewModel.uploadHold(FileUtil.getFileImage(Constant.ID_CARD_HOLD))
            }
        }
    }

    /**
     * 初始化通知
     */
    private fun initBroadcast() {
        // 动态注册广播
        val filter = IntentFilter()
        filter.addAction(Constant.ACTION_VERIFY)
        verifyReceiver = VerifyReceiver()
        LocalBroadcastManager.getInstance(this).registerReceiver(verifyReceiver!!, filter)
    }

    override fun onDestroy() {
        if (verifyReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(verifyReceiver!!)
            verifyReceiver = null
        }
        super.onDestroy()
    }

    /**
     * 定义广播接收器（内部类）
     */
    private inner class VerifyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            upload(intent.getIntExtra("type", Constant.REQUEST_FRONT_CODE))
        }
    }

    private fun setListener() {
        ivFrontPhoto.setOnClickListener(this)
        ivBackPhoto.setOnClickListener(this)
        ivHoldPhoto.setOnClickListener(this)
    }
}
