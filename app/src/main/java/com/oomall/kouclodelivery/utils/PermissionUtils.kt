package com.oomall.kouclodelivery.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * 权限请求
 * 还差 相机的单个请求方法
 */
class PermissionUtils private constructor() {
    val permissionAllRequestCode = 1001

    companion object {
        private var instance: PermissionUtils? = null
        fun init(): PermissionUtils? {
            synchronized(PermissionUtils::class.java) {
                if (instance == null) {
                    instance = PermissionUtils()
                }
            }
            return instance
        }
    }

    init {

    }


    fun requestSystemPermissiont(activity: AppCompatActivity, permissiontCallBack: PermissiontCallBack) {
        /**
         * 第 1 步: 检查是否有相应的权限
         */
        val isAllGranted = checkPermissionAllGranted(
            activity,
            arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        )
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            permissiontCallBack.permissionSuccess()
            return
        }
        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            ),
            permissionAllRequestCode
        )
    }

    fun requestPermissiong(activity: AppCompatActivity, permission: String, permissiontCallBack: PermissiontCallBack) {
        var isGranted = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            permissiontCallBack.permissionSuccess()
            return
        }
        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                permission
            ),
            permissionAllRequestCode
        )
    }


    /**
     * 检查是否拥有指定的所有权限
     */
    private fun checkPermissionAllGranted(activities: Activity, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activities, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false
            }
        }
        return true
    }


    interface PermissiontCallBack {
        fun permissionSuccess()

        fun permissionFailed()
    }

}
