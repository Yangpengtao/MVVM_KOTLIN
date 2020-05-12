package com.oomall.kouclodelivery.ui.view.setting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view.setting.dialog.BindPhoneDialog
import com.oomall.kouclodelivery.ui.view.setting.utils.Constant
import com.oomall.kouclodelivery.ui.view_model.SettingViewModel
import com.oomall.kouclodelivery.ui.dialog.UploadImageDialog
import com.oomall.kouclodelivery.utils.FileUtil
import com.oomall.kouclodelivery.utils.IntentUtil
import com.oomall.kouclodelivery.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * 设置
 */
class SettingActivity : BaseActivity(), UploadImageDialog.ClickListener, View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.llHeadPhoto -> {
                uploadDialog.show()
            }
            R.id.llPhoneNumber -> {
                bindPhoneDailog.show()
            }
        }
    }


    override fun takePhoto() {
        val cameraIntent = IntentUtil.getCameraIntent(Constant.HEAD_PHOTO)
//        val cameraIntent = IntentUtil.aaaa(Constant.HEAD_PHOTO )
        startActivityForResult(
            cameraIntent,
            Constant.REQUEST_CAMERA
        )
    }

    override fun alum() {
        startActivityForResult(
            IntentUtil.getAlumIntent(Constant.HEAD_PHOTO, true, 9999, 9998, 300, 300),
            Constant.REQUEST_ALUM
        )
    }

    lateinit var uploadDialog: UploadImageDialog
    lateinit var bindPhoneDailog: BindPhoneDialog
    lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        settingViewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)
        getTitle_().text = "设置"
        getLeftImg_().visibility = View.VISIBLE
        uploadDialog  =
            UploadImageDialog(this, R.style.AppTheme_FullScreen_DialogBg)
        bindPhoneDailog = BindPhoneDialog(this)
        uploadDialog.setPopClickListener(this)
        llHeadPhoto.setOnClickListener(this)
        llPhoneNumber.setOnClickListener(this)
        settingViewModel.headImg.observe(this, Observer {
            if (it.code == 1) {
                ToastUtil.show("上传成功")
            } else {
                ToastUtil.show("上传失败")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constant.REQUEST_CAMERA -> {
//                    settingViewModel.uploadHead(FileUtil.getFileImage(Constant.HEAD_PHOTO))
                    startActivityForResult(
                        IntentUtil.getCropIntent(Constant.HEAD_PHOTO, 9999, 9998, 300, 300),
                        Constant.REQUEST_CROP
                    )
                }
                Constant.REQUEST_CROP -> {
                    settingViewModel.uploadHead(FileUtil.getFileImage(Constant.HEAD_PHOTO))
                }
                Constant.REQUEST_ALUM -> {
                    settingViewModel.uploadHead(FileUtil.getFileImage(Constant.HEAD_PHOTO))
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
