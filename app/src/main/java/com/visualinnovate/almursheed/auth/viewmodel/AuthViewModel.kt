package com.visualinnovate.almursheed.auth.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.visualinnovate.almursheed.auth.model.DriverResponse
import com.visualinnovate.almursheed.auth.model.LoginResponse
import com.visualinnovate.almursheed.auth.model.MessageResponse
import com.visualinnovate.almursheed.auth.model.TouristResponse
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _loginMutableData: MutableLiveData<ResponseHandler<LoginResponse?>> =
        MutableLiveData()
    val loginLiveData: LiveData<ResponseHandler<LoginResponse?>> =
        _loginMutableData

    private val _registerTouristMutableData: MutableLiveData<ResponseHandler<TouristResponse?>> =
        MutableLiveData()
    val registerTouristLiveData: LiveData<ResponseHandler<TouristResponse?>> =
        _registerTouristMutableData

    private val _forgetPasswordMutableData: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val forgetPasswordLiveData: LiveData<ResponseHandler<MessageResponse?>> =
        _forgetPasswordMutableData.toSingleEvent()

    private val _validateOtpMutableData: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val validateOtpLiveData: LiveData<ResponseHandler<MessageResponse?>> =
        _validateOtpMutableData.toSingleEvent()

    private val _resetPasswordMutableData: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val resetPasswordLiveData: LiveData<ResponseHandler<MessageResponse?>> =
        _resetPasswordMutableData.toSingleEvent()

    private val _registerDriverMutableData: MutableLiveData<ResponseHandler<DriverResponse?>> =
        MutableLiveData()
    val registerDriverLiveData: LiveData<ResponseHandler<DriverResponse?>> =
        _registerDriverMutableData.toSingleEvent()

    fun registerDriver(name: String, email: String, password: String, gender: String, nationality: String, countryId: Int, cityId: Int, type: String) {
        val requestBody = createBodyRequest(name, email, password, gender, nationality, countryId, cityId, type)
        viewModelScope.launch {
            safeApiCall {
                apiService.registerDriver(requestBody)
            }.collect {
                _registerDriverMutableData.value = it
            }
        }
    }

    //
    private fun createBodyRequest(name: String, email: String, password: String, gender: String, nationality: String, countryId: Int, cityId: Int, type: String): RequestBody {
        val requestData = mapOf(
            "name" to name,
            "email" to email,
            "nationality" to nationality,
            "country_id" to countryId,
            "state_id" to cityId,
            "gender" to gender,
            "password" to password,
            "type" to type,
        )
        val gson = Gson()
        val jsonData = gson.toJson(requestData)
        return RequestBody.create("application/json".toMediaTypeOrNull(), jsonData)
    }

    fun login(email: String, password: String) {
        Log.d("login", "email $email password $password")
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.login(email, password)
            }.asLiveData().observeForever {
                _loginMutableData.value = it
            }
        }
    }

    fun registerTourist(
        username: String,
        email: String,
        nationalityName: String,
        password: String,
        file: File,
    ) {
        val countryId = "50"
        val stateId = "5"
        val genderId = "1"

        val requestFile = RequestBody.create("image/png".toMediaTypeOrNull(), file)
        val profilePicPart =
            MultipartBody.Part.createFormData("personal_pictures", file.name, requestFile)

        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), username)
        val country_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            countryId.toString(),
        )
        val state_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            stateId,
        )
        val gender = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            genderId,
        )
        val password =
            RequestBody.create("text/plain".toMediaTypeOrNull(), password)
        val email =
            RequestBody.create("text/plain".toMediaTypeOrNull(), email)

        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.registerTourist(
                    name,
                    country_id,
                    state_id,
                    gender,
                    password,
                    email,
                    profilePicPart,
                )
            }.asLiveData().observeForever {
                _registerTouristMutableData.value = it
            }
        }
    }

    fun forgetPassword(email: String) {
        Log.d("forgetPassword", "email $email")
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.forgetPassword(email)
            }.collect {
                _forgetPasswordMutableData.value = it
            }
        }
    }

    fun validateOTP(email: String, otpCode: String) {
        Log.d("forgetPassword", "email $email otpCode $otpCode")
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.validateOTP(email, otpCode)
            }.collect {
                _validateOtpMutableData.value = it
            }
        }
    }

    fun resetPassword(email: String, otp: String, newPassword: String, confirmPassword: String) {
        Log.d("forgetPassword", "email $email otpCode $otp newPassword $otp confirmPassword $otp")
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.resetPassword(otp, email, newPassword, confirmPassword)
            }.collect {
                _resetPasswordMutableData.value = it
            }
        }
    }
}
