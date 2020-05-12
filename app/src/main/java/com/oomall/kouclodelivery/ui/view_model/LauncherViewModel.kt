package com.oomall.kouclodelivery.ui.view_model

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oomall.kouclodelivery.constant.SpConstant
import com.oomall.kouclodelivery.proxy.HelperSharedPreference
import com.oomall.kouclodelivery.ui.base.BaseViewModel

/**
 * 启动页 的数据处理
 * 权限
 * 启动倒计时
 */
class LauncherViewModel : BaseViewModel() {

    private val mMillisInfuture: Long = 1000
    private val mCountDownInterval: Long = 1000

    //保存及时结束的状态
    private val _timerState = MutableLiveData<Boolean>()
    val timerState: LiveData<Boolean> = _timerState
    //保存及时结束的状态
    private val _keyState = MutableLiveData<Boolean>()
    val keyState: LiveData<Boolean> = _keyState

    fun startTimer() {
        myTimer.start()
    }

    fun getKey() {
//        _get()
         if (HelperSharedPreference ._getString(SpConstant.KEY).isNotBlank()){
            _keyState.value=true
         }else{
//             _get()
             _keyState.value=true
         }
    }


    private val myTimer: CountDownTimer = object : CountDownTimer(mMillisInfuture, mCountDownInterval) {
        override fun onFinish() {
            _timerState.value = true
        }

        override fun onTick(p0: Long) {
        }
    }

}