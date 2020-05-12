package com.oomall.kouclodelivery.tools.image

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.oomall.kouclodelivery.tools.image.interfaces.IImageLoaderProcessor
import com.oomall.kouclodelivery.tools.image.utils.GlideCircleTransform
import com.oomall.kouclodelivery.tools.image.utils.GlideRoundTransform

object GlideProcessor : IImageLoaderProcessor {
    override fun loadNormalImage(imageView: ImageView, uri: Uri) {
        Glide.with(imageView.context)
            .load(uri)
            // .error(R.drawable.error_bitmap)
            //.placeholder(R.drawable.bitmap)
            .into(imageView)
    }

    override fun loadNormalImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load(url)
            // .error(R.drawable.error_bitmap)
            //.placeholder(R.drawable.bitmap)
            .into(imageView)
    }

    override fun loadCircleImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load(url)
//            .error(R.drawable.logo)
//            .placeholder(R.drawable.logo)
            .transform(GlideCircleTransform(imageView.context))
            .into(imageView)
    }

    override fun loadRoundImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load(url)
            //                .error(R.drawable.error_bitmap)
//            .placeholder(R.drawable.bitmap)
            .transform(GlideRoundTransform(imageView.context, 10))
            .into(imageView)
    }


}