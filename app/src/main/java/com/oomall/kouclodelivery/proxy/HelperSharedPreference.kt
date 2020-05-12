package com.oomall.kouclodelivery.proxy

import com.oomall.kouclodelivery.tools.sp.interfaces.ISharedPreferenceProcessor
import com.oomall.kouclodelivery.tools.sp.SharedPreferenceProcessor

/**
 * sp的代理
 */
object HelperSharedPreference :
    ISharedPreferenceProcessor {


    override fun _putData(key: String, value: Any) {
        SharedPreferenceProcessor._putData(key, value)
    }

    override fun _getData(key: String, dataType: Int): Any {
        return SharedPreferenceProcessor._getData(key, dataType)
    }

    override fun _getData(key: String, defaulrValue: Any, dataType: Int): Any {
        return SharedPreferenceProcessor._getData(key, defaulrValue, dataType)
    }

    override fun _getBoolean(key: String, defaulrValue: Boolean): Boolean {
        return SharedPreferenceProcessor._getBoolean(key, defaulrValue)
    }

    override fun _getInt(key: String, defaulrValue: Int): Int {
        return SharedPreferenceProcessor._getInt(key, defaulrValue)
    }

    override fun _getFloat(key: String, defaulrValue: Float): Float {
        return SharedPreferenceProcessor._getFloat(key, defaulrValue)
    }

    override fun _getLong(key: String, defaulrValue: Long): Long {
        return SharedPreferenceProcessor._getLong(key, defaulrValue)
    }

    override fun _getString(key: String, defaulrValue: String): String {
        return SharedPreferenceProcessor._getString(key, defaulrValue)
    }

    override fun _getBoolean(key: String): Boolean {
        return SharedPreferenceProcessor._getBoolean(key)
    }

    override fun _getInt(key: String): Int {
        return SharedPreferenceProcessor._getInt(key)
    }

    override fun _getFloat(key: String): Float {
        return SharedPreferenceProcessor._getFloat(key)
    }

    override fun _getLong(key: String): Long {
        return SharedPreferenceProcessor._getLong(key)
    }

    override fun _getString(key: String): String {
        return SharedPreferenceProcessor._getString(key)
    }

}