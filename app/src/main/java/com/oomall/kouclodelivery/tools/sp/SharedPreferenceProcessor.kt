package com.oomall.kouclodelivery.tools.sp

import android.content.Context
import android.content.SharedPreferences
import com.oomall.kouclodelivery.KoucloApplication
import com.oomall.kouclodelivery.tools.sp.interfaces.ISharedPreferenceProcessor

/**
 * sp 生成器
 */
object SharedPreferenceProcessor   :
    ISharedPreferenceProcessor {


    private var sharedPreferences: SharedPreferences? = null


    init {
        sharedPreferences = KoucloApplication .init()!!.getSharedPreferences("koucloDelivery", Context.MODE_PRIVATE)
    }


    override fun _putData(key: String, value: Any) {
        when (value) {
            is String -> {
                sharedPreferences!!.edit().putString(key, value).apply()
            }
            is Boolean -> {
                sharedPreferences!!.edit().putBoolean(key, value).apply()
            }
            is Float -> {
                sharedPreferences!!.edit().putFloat(key, value).apply()
            }
            is Int -> {
                sharedPreferences!!.edit().putInt(key, value).apply()
            }
            is Long -> {
                sharedPreferences!!.edit().putLong(key, value).apply()
            }
            is Set<*> -> {
            }
        }
    }

    override fun _getData(key: String, dataType: Int): Any {
        return _getData(key, 0, dataType)
    }

    override fun _getData(key: String, defaulrValue: Any, dataType: Int): Any {
        when (dataType) {
            SpType._STRING -> {
                return sharedPreferences!!.getString(key, defaulrValue as String) as String
            }
            SpType._BOOLEAD -> {
                return sharedPreferences!!.getBoolean(key, defaulrValue as Boolean)
            }
            SpType._FLOAT -> {
                return sharedPreferences!!.getFloat(key, defaulrValue as Float)
            }
            SpType._INT -> {
                return sharedPreferences!!.getInt(key, defaulrValue as Int)
            }
            SpType._LONG -> {
                return sharedPreferences!!.getLong(key, defaulrValue as Long)
            }
        }
        return 0
    }

    override fun _getBoolean(key: String, defaulrValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defaulrValue)
    }

    override fun _getInt(key: String, defaulrValue: Int): Int {
        return sharedPreferences!!.getInt(key, defaulrValue)
    }

    override fun _getFloat(key: String, defaulrValue: Float): Float {
        return sharedPreferences!!.getFloat(key, defaulrValue)
    }

    override fun _getLong(key: String, defaulrValue: Long): Long {
        return sharedPreferences!!.getLong(key, defaulrValue)
    }

    override fun _getString(key: String, defaulrValue: String): String {
        return sharedPreferences!!.getString(key, defaulrValue).toString()
    }

    override fun _getBoolean(key: String): Boolean {
        return sharedPreferences!!.getBoolean(key, false)
    }

    override fun _getInt(key: String): Int {
        return sharedPreferences!!.getInt(key, 0)
    }

    override fun _getFloat(key: String): Float {
        return sharedPreferences!!.getFloat(key, 0F)
    }

    override fun _getLong(key: String): Long {
        return sharedPreferences!!.getLong(key, 0L)
    }

    override fun _getString(key: String): String {
        return sharedPreferences!!.getString(key, "").toString()
    }
}


