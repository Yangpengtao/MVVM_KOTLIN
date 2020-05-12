package com.oomall.kouclodelivery.utils

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.InputType
import androidx.core.content.FileProvider
import com.oomall.kouclodelivery.KoucloApplication
import com.oomall.kouclodelivery.constant.Constant
import java.io.File
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.ActivityCompat.startActivityForResult
import java.text.SimpleDateFormat
import java.util.*


object IntentUtil {

    fun getDialIntent(phoneNumber: String): Intent {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }


    fun getAlumIntent(fileName: String, crop: Boolean, vararg parames: Int): Intent {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        setZoom(crop, intent, parames)
        val file = FileUtil.getFileImage(fileName)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        return intent
    }

    fun getCameraIntent(fileName: String): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        setZoom(crop, intent, parames)
        //添加两个权限让打开的裁剪应用拥有读取和修改这个URi的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getImageFileUri(fileName))
        return intent
    }

    fun getCropIntent(fileName: String, vararg parames: Int): Intent {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(FileUtil.getImageFileUri(fileName), "image/*")
        setZoom(true, intent, parames)
        // 图片格式
        intent.putExtra("outputFormat", "JPEG")
        return intent

    }

    private fun setZoom(crop: Boolean, intent: Intent, parames: IntArray) {
        if (crop) {
            intent.putExtra("noFaceDetection", true)
            intent.putExtra("crop", "true")
            if (parames.isNotEmpty()) {
                intent.putExtra("aspectX", parames[0])// 裁剪框比例
                intent.putExtra("aspectY", parames[1])
                if (parames.size > 2) {
                    intent.putExtra("outputY", parames[2])
                    intent.putExtra("outputX", parames[3])// 输出图片大小
                }
            }
        }
        //添加两个权限让打开的裁剪应用拥有读取和修改这个URi的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }


}
