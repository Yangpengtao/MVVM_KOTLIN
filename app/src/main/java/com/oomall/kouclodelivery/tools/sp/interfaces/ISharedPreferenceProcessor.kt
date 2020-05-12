package com.oomall.kouclodelivery.tools.sp.interfaces

interface ISharedPreferenceProcessor {
    fun _putData(key: String, value: Any)
    fun _getData(key: String, dataType: Int): Any
    fun _getData(key: String, defaulrValue: Any, dataType: Int): Any

    fun _getBoolean(key: String, defaulrValue: Boolean): Boolean
    fun _getInt(key: String, defaulrValue: Int): Int
    fun _getFloat(key: String, defaulrValue: Float): Float
    fun _getLong(key: String, defaulrValue: Long): Long
    fun _getString(key: String, defaulrValue: String): String

    fun _getBoolean(key: String): Boolean
    fun _getInt(key: String): Int
    fun _getFloat(key: String): Float
    fun _getLong(key: String): Long
    fun _getString(key: String): String

}