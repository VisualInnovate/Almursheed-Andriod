package com.visualinnovate.almursheed.auth.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.auth.model.MessageResponse
import com.visualinnovate.almursheed.auth.model.UserResponse
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _loginMutableData: MutableLiveData<ResponseHandler<UserResponse?>> =
        MutableLiveData()
    val loginLiveData: LiveData<ResponseHandler<UserResponse?>> =
        _loginMutableData

    private val _forgetPasswordMutableData: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val forgetPasswordLiveData: LiveData<ResponseHandler<MessageResponse?>> =
        _forgetPasswordMutableData.toSingleEvent()

    private val _resetPasswordMutableData: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val resetPasswordLiveData: LiveData<ResponseHandler<MessageResponse?>> =
        _resetPasswordMutableData.toSingleEvent()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.login(email, password)
            }.collect {
                _loginMutableData.value = it
            }
        }
    }

    fun forgetPassword(email: String, type: String) {
        Log.d("forgetPassword", "email $email")
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.forgetPassword(email, type)
            }.collect {
                _forgetPasswordMutableData.value = it
            }
        }
    }


    fun resetPassword(email: String, otp: String, newPassword: String, confirmPassword: String) {
        Log.d("forgetPassword", "email $email otpCode $otp newPassword $otp confirmPassword $otp")
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.resetPassword(email, otp, newPassword, confirmPassword)
            }.collect {
                _resetPasswordMutableData.value = it
            }
        }
    }
}
