package com.oomall.kouclodelivery.ui.view_model

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.oomall.kouclodelivery.data.bean.Basebean
import com.oomall.kouclodelivery.network.HttpCallback
import com.oomall.kouclodelivery.constant.ApiConstant
import com.oomall.kouclodelivery.data.bean.MainWaitingOrderList
import com.oomall.kouclodelivery.ui.base.BaseViewModel
import com.oomall.kouclodelivery.utils.ToastUtil
import java.util.*

/**
 * 用户信息类逻辑处理
 */
class AccountViewModel : BaseViewModel() {

    //登陆页的滑动验证
    var slitherVeriry: Boolean = false

    private val _loginForm = MutableLiveData<LoginResult>()
    val loginFormState: LiveData<LoginResult> = _loginForm

    fun loginForCode(username: String, code: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginResult(success = false)
            ToastUtil.show("请输入正确手机号")
        } else if (!isCodeValid(code)) {
            _loginForm.value = LoginResult(success = false)
            ToastUtil.show("请输入正确验证码")
        } else {
            val map = TreeMap<String, Any>()
            map.put("user_name", username)
            map.put("password", code)
            map.put("cityname", "北京")
            _post(ApiConstant.LOGIN, map, object : HttpCallback<Basebean>() {
                override fun onSuccess(objResult: Basebean) {
                    _loginForm.value = LoginResult(success = true, data = objResult)
                }

                override fun onFailure(e: String) {
                    ToastUtil.show("-------$e")
                }
            })
        }
    }


    private fun isUserNameValid(username: String): Boolean {
        return if (username.length == 11) {
            Patterns.PHONE.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isCodeValid(code: String): Boolean {
        return code.isNotBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }


    data class LoginResult(
        val data: Basebean? = null,
        val success: Boolean = false
    )

}
