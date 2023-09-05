package com.visualinnovate.almursheed.auth.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.auth.model.DriverResponse
import com.visualinnovate.almursheed.auth.model.RegisterDriverRequest
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
class DriverViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application
) : BaseApiResponse(application) {

    var registerDriverRequest = RegisterDriverRequest()
    val imagesList = ArrayList<MultipartBody.Part>()

    private val _registerDriverMutableData: MutableLiveData<ResponseHandler<DriverResponse?>> =
        MutableLiveData()
    val registerDriverLiveData: LiveData<ResponseHandler<DriverResponse?>> =
        _registerDriverMutableData

    private fun createMultiPartToImage(image: String, name: String): MultipartBody.Part {
        val file = File(image) //
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData(name, file.name, requestFile) // "image"
        Log.d("createMultiPartToImage", "body $body")
        return body
    }

    fun createDriverMultiPart() {
        for (carImagePath in registerDriverRequest.carImagesList!!) {
            val carImageFile = File(carImagePath)
            val carImageRequestBody =
                RequestBody.create("image/*".toMediaTypeOrNull(), carImageFile)
            val carImagePart = MultipartBody.Part.createFormData(
                "car_photos",
                carImageFile.name,
                carImageRequestBody
            )
            imagesList.add(carImagePart)
        }

        val profilePicFile = File(registerDriverRequest.personal_pictures!!)
        val profilePicRequestBody =
            RequestBody.create("image/*".toMediaTypeOrNull(), profilePicFile)

        // yaaaaaaaaaaaaaaaaaaaaaad
        val profilePicPart = MultipartBody.Part.createFormData(
            "personal_pictures",
            profilePicFile.name,
            profilePicRequestBody
        )
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), registerDriverRequest.name!!)
        val country_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            registerDriverRequest.country_id!!.toString()
        )
        val state_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            registerDriverRequest.state_id!!.toString()
        )
        val gender = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            registerDriverRequest.gender!!.toString()
        )
        val password =
            RequestBody.create("text/plain".toMediaTypeOrNull(), registerDriverRequest.password!!)
        val email =
            RequestBody.create("text/plain".toMediaTypeOrNull(), registerDriverRequest.email!!)
        val phone =
            RequestBody.create("text/plain".toMediaTypeOrNull(), registerDriverRequest.phone!!)
        val bio = RequestBody.create("text/plain".toMediaTypeOrNull(), registerDriverRequest.bio!!)
        val car_number =
            RequestBody.create("text/plain".toMediaTypeOrNull(), registerDriverRequest.car_number!!)
        val driver_licence_number = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            registerDriverRequest.driver_licence_number!!
        )
        val gov_id =
            RequestBody.create("text/plain".toMediaTypeOrNull(), registerDriverRequest.gov_id!!)
        val languagesList = ArrayList<RequestBody>()
        for (i in registerDriverRequest.languages!!) {
            val lang = RequestBody.create("text/plain".toMediaTypeOrNull(), i.toString())
            languagesList.add(lang)
        }

        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.registerDriver(
                    name,
                    country_id,
                    state_id,
                    gender,
                    password,
                    email,
                    phone,
                    bio,
                    car_number,
                    driver_licence_number,
                    gov_id,
                    imagesList,
                    profilePicPart,
                    registerDriverRequest.languages!!
                )
            }.asLiveData().observeForever {
                _registerDriverMutableData.value = it
            }
        }
    }
}
