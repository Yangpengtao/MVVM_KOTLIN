package com.oomall.kouclodelivery.ui.view.order

import android.os.Bundle
import android.view.View
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view.verify.utils.BitmapUtil
import kotlinx.android.synthetic.main.activity_photo_preview_order.*

class PhotoPreviewOrderActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvRestart -> {
                finish()
            }
            R.id.btnUpload -> {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_preview_order)
        tvRestart.setOnClickListener(this)
        btnUpload.setOnClickListener(this)
        ivPreview.setImageBitmap(BitmapUtil.getBitmapFromFile("order_number"))
    }
}
