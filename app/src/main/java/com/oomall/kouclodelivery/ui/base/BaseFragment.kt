package com.oomall.kouclodelivery.ui.base

import android.content.Intent
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {


    fun startActivity(clazz: Class<*>) {
        val intent = Intent(context, clazz)
        super.startActivity(intent)
    }
}