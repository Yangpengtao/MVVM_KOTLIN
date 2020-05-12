package com.oomall.kouclodelivery.tools.image.interfaces

import android.net.Uri
import android.widget.ImageView

interface IImageLoaderProcessor {



    /**
     * 加载默认图片
     */
    fun loadNormalImage(imageView: ImageView, url: String)
    /**
     * 加载默认图片
     */
    fun loadNormalImage(imageView: ImageView, uri: Uri)
    /**
     * 加载圆形图片
     */
    fun loadCircleImage(imageView: ImageView, url: String)

    /**
     * 加载圆角图片
     */
    fun loadRoundImage(imageView: ImageView, url: String)
}