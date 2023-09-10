package com.visualinnovate.almursheed.home.view.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.UpdateDriverResponse
import com.visualinnovate.almursheed.home.view.ProfileData
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
class ProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    // var updateDriveItem = UpdateDriveItem()
    private val imagesList = ArrayList<MultipartBody.Part>()

    private val _updateDriverMutableData: MutableLiveData<ResponseHandler<UpdateDriverResponse?>> =
        MutableLiveData()
    val updateDriverLiveData: LiveData<ResponseHandler<UpdateDriverResponse?>> =
        _updateDriverMutableData.toSingleEvent()


    fun updateDriverMultiPart(
        profileData: ProfileData?,
        governmentID: String,
        licenseNumber: String,
        carNumber: String,
        desc: String,
        carImagesList: ArrayList<String>,
        languagesIdsList: ArrayList<Int>
    ) {
        for (car_photos in carImagesList) {
            val carImageFile = File(car_photos)
            val carImageRequestBody =
                RequestBody.create("image/*".toMediaTypeOrNull(), carImageFile)
            val carImagePart = MultipartBody.Part.createFormData(
                "car_photos",
                carImageFile.name,
                carImageRequestBody
            )
            imagesList.add(carImagePart)
        }

        val profilePicFile = profileData?.personal_pictures?.let { File(it) }
        val profilePicRequestBody =
            profilePicFile?.let { RequestBody.create("image/*".toMediaTypeOrNull(), it) }

        val profilePicPart = profilePicRequestBody?.let {
            MultipartBody.Part.createFormData(
                "personal_pictures",
                profilePicFile.name,
                it
            )
        }
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), profileData?.userName ?: "")
        val country_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            profileData?.countryId.toString()
        )
        val state_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            profileData?.cityId.toString()
        )
        val gender = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            profileData?.gender.toString()
        )
        val email =
            RequestBody.create("text/plain".toMediaTypeOrNull(), profileData?.email ?: "")
        val phone =
            RequestBody.create("text/plain".toMediaTypeOrNull(), profileData?.phoneNumber ?: "")
        val bio = RequestBody.create("text/plain".toMediaTypeOrNull(), desc)
        val car_number =
            RequestBody.create("text/plain".toMediaTypeOrNull(), carNumber)
        val car_type =
            RequestBody.create("text/plain".toMediaTypeOrNull(), "car_type")
        val car_brand_name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), "car_brand_name")
        val car_manufacturing_date =
            RequestBody.create("text/plain".toMediaTypeOrNull(), "2023-09-11")
        val driver_licence_number = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            licenseNumber
        )
        val gov_id =
            RequestBody.create("text/plain".toMediaTypeOrNull(), governmentID)
        val languagesList = ArrayList<RequestBody>()
        for (i in languagesIdsList) {
            val lang = RequestBody.create("text/plain".toMediaTypeOrNull(), i.toString())
            languagesList.add(lang)
        }

        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.updateDriver(
                    name,
                    country_id,
                    state_id,
                    // gender,
                    phone,
                    bio,
                    gov_id,
                    // languagesList,
                    // profilePicPart!!,
                    driver_licence_number,
                    // imagesList,
                    car_number,
                    car_type,
                    car_brand_name,
                    car_manufacturing_date,
                )
            }.collect {
                _updateDriverMutableData.value = it
            }
        }
    }

}