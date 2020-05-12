package com.oomall.kouclodelivery.ui.view.verify

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.oomall.kouclodelivery.R
import com.oomall.kouclodelivery.ui.view.verify.utils.Constant
import com.oomall.kouclodelivery.ui.base.BaseActivity
import com.oomall.kouclodelivery.ui.view.verify.utils.BitmapUtil
import com.oomall.kouclodelivery.utils.LogPrinter
import kotlinx.android.synthetic.main.activity_photo_preview.*

/**
 * 查看图片
 */
class PhotoPreviewActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.tvRestart -> {
                finish()
            }
            R.id.tvUse -> {
                val go = Intent(Constant.ACTION_VERIFY)
                go.putExtra("type", intent.extras!!.getInt("param1", Constant.REQUEST_FRONT_CODE))
                LocalBroadcastManager.getInstance(this@PhotoPreviewActivity).sendBroadcast(go)
                startActivity(IdentificationActivity::class.java)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_preview)
        setTitleVisibility(View.GONE)
        when(intent.extras!!.getInt("param1", Constant.REQUEST_FRONT_CODE)){
            Constant.REQUEST_FRONT_CODE -> {
                ivPreview.setImageBitmap(BitmapUtil.getBitmapFromFile(Constant.ID_CARD_FRONT))
            }
            Constant.REQUEST_BACK_CODE -> {
                ivPreview.setImageBitmap(BitmapUtil.getBitmapFromFile(Constant.ID_CARD_BACK))
            }
            Constant.REQUEST_HOLD_CODE -> {
                ivPreview.setImageBitmap(BitmapUtil.getBitmapFromFile(Constant.ID_CARD_HOLD))
            }
        }
        val a = intent.extras!!.getInt("param1", Constant.REQUEST_FRONT_CODE)
        LogPrinter.e("PhotoPreviewActivity", "----------$a")
        tvRestart.setOnClickListener(this)
        tvUse.setOnClickListener(this)
    }
}
