package com.oomall.kouclodelivery.camera2

import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import java.util.*
import kotlin.collections.ArrayList

object Constant {
    /**
     *  转换从屏幕旋转到JPEG方向。
     */
    val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)
    }


    /**
     * 摄像机状态：显示摄像机预览
     */
    const val STATE_PREVIEW = 0

    /**
     * 摄像机状态：等待对焦锁定。
     */
    const val STATE_WAITING_LOCK = 1

    /**
     * 相机状态：等待曝光处于预捕获状态。
     */
    const val STATE_WAITING_PRECAPTURE = 2

    /**
     * 相机状态：等待曝光状态为除了预捕捉之外的其他内容。
     */
    const val STATE_WAITING_NON_PRECAPTURE = 3

    /**
     * 相机状态:拍摄了图片。
     */
    const val STATE_PICTURE_TAKEN = 4

    /**
     * 由Camera2API保证的最大预览宽度
     */
    const val MAX_PREVIEW_WIDTH = 1920

    /**
     * 由Camera2API保证的最大预览高度
     */
    const val MAX_PREVIEW_HEIGHT = 1080

    /**
     * 给定由摄影机支持的“大小”的“选择”，
     * 请选择至少与相应纹理视图大小相同的最小大小，
     * 并且最多与相应的最大大小一样大，
     * 其纵横比与指定值匹配。
     * 如果不存在此大小，请选择最多与相应的最大*size大小相同的最大大小，并且其纵横比与指定值匹配。
     *
     * @param choices           摄像机支持的尺寸列表。
     * @param textureViewWidth  纹理视图相对于传感器坐标的宽度
     * @param textureViewHeight 相对于传感器坐标的纹理视图的高度。
     * @param maxWidth          可以选择的最大宽度。
     * @param maxHeight         可以选择的最大高度。
     * @param aspectRatio       纵横比
     * @return 最优的“Size”，如果没有足够大的话，则是任意的“Size”
     */
    @JvmStatic
    fun chooseOptimalSize(
            choices: Array<Size>,
            textureViewWidth: Int,
            textureViewHeight: Int,
            maxWidth: Int,
            maxHeight: Int,
            aspectRatio: Size
    ): Size {

        // 收集支持的分辨率，这些分辨率至少与预览曲面一样大。
        val bigEnough = ArrayList<Size>()
        // 收集比预览曲面小的受支持的分辨率。
        val notBigEnough = ArrayList<Size>()
        val w = aspectRatio.width
        val h = aspectRatio.height
        for (option in choices) {
            if (option.width <= maxWidth && option.height <= maxHeight &&
                    option.height == option.width * h / w) {
                if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                    bigEnough.add(option)
                } else {
                    notBigEnough.add(option)
                }
            }
        }

        // 选那些足够大的最小的。如果没有一个足够大的，选择最大的那些不够大。
        return when {
            bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
            notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
            else -> {
                Log.e(Constant::class.java.name, "Couldn't find any suitable preview size")
                choices[0]
            }
        }
    }
}