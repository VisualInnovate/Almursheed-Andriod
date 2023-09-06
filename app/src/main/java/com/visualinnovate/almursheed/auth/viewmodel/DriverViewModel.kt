package com.visualinnovate.almursheed.auth.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.visualinnovate.almursheed.auth.model.DriverResponse
import com.visualinnovate.almursheed.auth.model.RegisterDriverRequest
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.Constant.ROLE_DRIVER
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    var registerDriverRequest = RegisterDriverRequest()
    val imagesList = ArrayList<MultipartBody.Part>()

    private val _registerDriverMutableData: MutableLiveData<ResponseHandler<DriverResponse?>> =
        MutableLiveData()
    val registerDriverLiveData: LiveData<ResponseHandler<DriverResponse?>> =
        _registerDriverMutableData


    fun registerDriver(name: String, email: String, password: String , gender:String ,  nationality:String , countryId:Int , cityId:Int , type:String) {
       val requestBody =  createBodyRequest(name, email, password, gender,nationality, countryId, cityId, type)
        viewModelScope.launch {
            safeApiCall {
                apiService.registerDriver(requestBody)
            }.collect {
                _registerDriverMutableData.value = it
            }
        }
    }


    private fun createBodyRequest(name: String, email: String, password: String , gender:String ,  nationality:String , countryId:Int , cityId:Int , type:String): RequestBody {
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
}
