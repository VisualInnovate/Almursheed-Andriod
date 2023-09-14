package com.visualinnovate.almursheed.auth.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.visualinnovate.almursheed.auth.model.MessageResponse
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _validateOtpMutable: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val validateOtpLive: LiveData<ResponseHandler<MessageResponse?>> =
        _validateOtpMutable.toSingleEvent()

    fun validateOTP(email: String, code: String, type: String) {
        viewModelScope.launch {
            safeApiCall {
                val requestBody = createBodyRequest(email, code, type)
                // Make your API call here using Retrofit service or similar
                apiService.validateOTP(requestBody)
            }.collect {
                _validateOtpMutable.value = it
            }
        }
    }

    private fun createBodyRequest(email: String, code: String, type: String): RequestBody {
        val requestData = mapOf(
            "identifier" to email, // Enter your mail
            "otp" to code,
            "type" to type //  //0 for email , 2 for rese
        )
        val gson = Gson()
        val jsonData = gson.toJson(requestData)
        return jsonData.toRequestBody("application/json".toMediaTypeOrNull())
    }
}
