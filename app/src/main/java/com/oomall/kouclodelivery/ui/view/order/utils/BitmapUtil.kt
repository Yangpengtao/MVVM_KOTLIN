package com.oomall.kouclodelivery.ui.view.order.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.SurfaceView
import android.view.View
import com.oomall.kouclodelivery.constant.Constant
import com.oomall.kouclodelivery.proxy.HelperThreadPool
import com.oomall.kouclodelivery.utils.LogPrinter
import java.io.*


object BitmapUtil {


    fun getBitmap(data: ByteArray?): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(data, 0, data!!.size, options)
        options.inSampleSize = calculateInSampleSize(options, 500, 500)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(data, 0, data.size, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val outHeight = options.outHeight
        val outWidth = options.outWidth
        var inSampleSize = 1
        if (outHeight > reqHeight || outWidth > reqWidth) {
            val halfHeight = outHeight / 2
            val halfWidth = outWidth / 2
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }
        LogPrinter.e("com.oomall.kouclodelivery.ui.view.order.utils.BitmapUtil", "------$inSampleSize")
        return inSampleSize
    }


    fun rotateBitmap(orientationDegree: Float, bmp: Bitmap, callBack: RotateCallback) {
        HelperThreadPool._submit(Runnable {
            val m = Matrix()
            m.setRotate(orientationDegree, bmp.width.toFloat(), bmp.height.toFloat())

            try {
                callBack.rotateSuccess(Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, m, true))
            } catch (ex: OutOfMemoryError) {
            }

        })
    }


    fun cropBitmap(cropView: View, takePhotoSurface: SurfaceView, bmp: Bitmap, callback: CropCallback) {
        HelperThreadPool. _submit(Runnable {
            val width: Int = ((bmp.width.toFloat() / takePhotoSurface.height) * cropView.height).toInt()
            val height: Int = ((bmp.width.toFloat() / takePhotoSurface.height) * cropView.width).toInt()
            val x: Int = (bmp.width - width) / 2
            val y: Int = (bmp.height - height) / 2
            LogPrinter.e("VerifyViewModel", ":$x----$y-----$width-----$height")
            //因为做了旋转，按横的来看
            val cropBmp = Bitmap.createBitmap(bmp, x, y, width, height)
            callback.cropSuccess(cropBmp)
        })
    }

    /**
     * 保存图片
     */
    fun saveBitmap2file(bmp: Bitmap, filename: String) {
        val format = Bitmap.CompressFormat.JPEG
        var stream: OutputStream? = null
        val imgFilePath = Constant.baseImageDir + filename
        LogPrinter.e("BitmapUtil.saveBitmap2file", "------------$imgFilePath")
//        if (!createOrExistsFile(imgFilePath)) {
//            ToastUtil.show("请去设置给与应用存储权限")
//            return
//        }
        try {
            stream = FileOutputStream(imgFilePath)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        bmp.compress(format, 100, stream)
        try {
            stream!!.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     *
     * 读取文件为Bitmap
     * @param filename
     * @return
     * @throws FileNotFoundException
     */
    fun getBitmapFromFile(filename: String): Bitmap? {
        try {
            val imgFilePath = Constant.baseImageDir + filename
            LogPrinter.e("BitmapUtil.getBitmapFromFile", "------------$imgFilePath")
            return BitmapFactory.decodeStream(
                FileInputStream(imgFilePath)
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    private fun createOrExistsFile(filePath: String): Boolean {
        val file = File(filePath)
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile
        if (!createOrExistsDir(file.parentFile)) return false
        return try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }

    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    private fun createOrExistsDir(file: File?): Boolean {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    interface CropCallback {
        fun cropSuccess(bmp: Bitmap)
    }

    interface RotateCallback {
        fun rotateSuccess(bmp: Bitmap)
    }


}