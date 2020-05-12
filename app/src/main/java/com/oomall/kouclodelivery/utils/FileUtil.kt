package com.oomall.kouclodelivery.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.oomall.kouclodelivery.KoucloApplication
import com.oomall.kouclodelivery.constant.Constant
import java.io.File
import java.io.IOException
import java.math.BigDecimal

object FileUtil {


    /**
     * 获取图片uri
     */
    fun getImage(fileName: String): Bitmap? {
        val filePath = Constant.baseImageDir + fileName
        return BitmapFactory.decodeFile(filePath)
    }


    /**
     * 获取图片uri
     */
    fun getImageFileUri(fileName: String): Uri? {
        val filePath = Constant.baseImageDir + fileName
        if (FileUtil.createOrExistsFile(filePath)) {
            val file = FileUtil.getFileImage(fileName)
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Uri.fromFile(file)
            } else {
                FileProvider.getUriForFile(
                    KoucloApplication.init()!!,
                    "com.oomall.kouclodelivery.fileprovider", file
                )
            }
        }
        return null
    }

    /**
     * 获取apk uri
     */
    fun getApkFileUri(fileName: String): Uri? {
        val filePath = Constant.baseApkDir + fileName
        if (FileUtil.createOrExistsFile(filePath)) {
            val file = FileUtil.getFileImage(fileName)
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Uri.fromFile(file)
            } else {
                FileProvider.getUriForFile(
                    KoucloApplication.init()!!,
                    "com.oomall.kouclodelivery.fileprovider", file
                )
            }
        }
        return null
    }

    /**
     * 判断文件jia是否存在，不存在则判断是否创建成功
     */
    fun createOrExitstDir(filePath: String) {
        val file = File(filePath)
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsFile(filePath: String): Boolean {
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

    fun getFileImage(fileName: String): File {
        val imgPath = File(Constant.baseImageDir)
        LogPrinter.e("FileUtil", "-----${File(imgPath, fileName)}")
        return File(imgPath, fileName)
    }

    fun getFileApk(fileName: String): File {
        val imgPath = File(Constant.baseApkDir)
        return File(imgPath, fileName)
    }

    fun getFileCrashLog(fileName: String): File {
        val imgPath = File(Constant.baseCrashDir)
        return File(imgPath, fileName)
    }


    /**
     * 清除图片磁盘缓存
     */
    fun clearImageDiskCache(context: Context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Thread(Runnable {
                    //删除glide
                    Glide.get(context).clearDiskCache()
                    //删除自己缓存的
                    deleteFolderFile(com.oomall.kouclodelivery.constant.Constant.baseImageDir, false)
                    // BusUtil.getBus().post(new GlideCacheClearSuccessEvent());
                }).start()
            } else {
                Glide.get(context).clearDiskCache()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath filePath   目录地址
     * @param deleteThisPath deleteThisPath   是否删除父目录
     */
    private fun deleteFolderFile(filePath: String, deleteThisPath: Boolean) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                val file = File(filePath)
                if (file.isDirectory) {
                    val files = file.listFiles()
                    for (file1 in files!!) {
                        deleteFolderFile(file1.absolutePath, true)
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory) {
                        file.delete()
                    } else {
                        if (file.listFiles()!!.isEmpty()) {
                            file.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 获取缓存大小
     */
    fun getCacheSize(context: Context): String {
        try {
            return getFormatSize(getFolderSize(File(context.cacheDir.toString())).toDouble())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     */
    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList!!) {
                size += if (aFileList.isDirectory) {
                    getFolderSize(aFileList)
                } else {
                    aFileList.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private fun getFormatSize(size: Double): String {

        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "Byte"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

}