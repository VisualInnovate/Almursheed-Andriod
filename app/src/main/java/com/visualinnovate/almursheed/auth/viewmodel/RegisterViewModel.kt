package com.visualinnovate.almursheed.auth.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.visualinnovate.almursheed.auth.model.UserResponse
import com.visualinnovate.almursheed.common.SharedPreference
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
class RegisterViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _registerUserMutable: MutableLiveData<ResponseHandler<UserResponse?>> =
        MutableLiveData()
    val registerUserLive: LiveData<ResponseHandler<UserResponse?>> =
        _registerUserMutable.toSingleEvent()

    fun registerGuide(name: String, email: String, password: String, nationality: String, countryId: Int, cityId: Int, type: String) {
        val requestBody = createBodyRequestDriverOrGuide(name, email, password, nationality, countryId, cityId, type)
        viewModelScope.launch {
            safeApiCall {
                apiService.registerGuide(requestBody)
            }.collect {
                saveCurrentUser(it)
                _registerUserMutable.value = it
            }
        }
    }

    fun registerDriver(name: String, email: String, password: String, nationality: String, countryId: Int, cityId: Int, type: String) {
        val requestBody = createBodyRequestDriverOrGuide(name, email, password, nationality, countryId, cityId, type)
        viewModelScope.launch {
            safeApiCall {
                apiService.registerDriver(requestBody)
            }.collect {
                saveCurrentUser(it)
                _registerUserMutable.value = it
            }
        }
    }

    fun registerTourist(name: String, email: String, password: String, nationality: String, cityId: Int, type: String) {
        val requestBody = createBodyRequestTourist(name, email, password, nationality, cityId, type)
        viewModelScope.launch {
            safeApiCall {
                apiService.registerTourist(requestBody)
            }.collect {
                saveCurrentUser(it)
                _registerUserMutable.value = it
            }
        }
    }

    private fun saveCurrentUser(response: ResponseHandler<UserResponse?>) {
        when (response) {
            is ResponseHandler.Success -> {
                SharedPreference.saveUser(response.data?.user)
                SharedPreference.saveUserToken(response.data?.token)
            }

            else -> {}
        }
    }

    private fun createBodyRequestDriverOrGuide(name: String, email: String, password: String, nationality: String, countryId: Int, cityId: Int, type: String): RequestBody {
        val requestData = mapOf(
            "name" to name,
            "email" to email,
            "nationality" to nationality,
            "country_id" to countryId,
            "state_id" to cityId,
            "password" to password,
            "type" to type,
        )
        val gson = Gson()
        val jsonData = gson.toJson(requestData)
        return jsonData.toRequestBody("application/json".toMediaTypeOrNull())
    }

    private fun createBodyRequestTourist(name: String, email: String, password: String, nationality: String, cityId: Int, type: String): RequestBody {
        val requestData = mapOf(
            "name" to name,
            "email" to email,
            "nationality" to nationality,
            "dest_city_id" to cityId,
            "password" to password,
            "type" to type,
        )
        val gson = Gson()
        val jsonData = gson.toJson(requestData)
        return jsonData.toRequestBody("application/json".toMediaTypeOrNull())
    }
}
