package com.visualinnovate.almursheed.commonView.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.UpdateResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {


    // var updateDriveItem = UpdateDriveItem()
    private val imagesList = ArrayList<MultipartBody.Part>()

    private val _updateDriverMutableData: MutableLiveData<ResponseHandler<UpdateResponse?>> =
        MutableLiveData()
    val updateDriverLiveData: LiveData<ResponseHandler<UpdateResponse?>> =
        _updateDriverMutableData.toSingleEvent()

    private val _updateTouristMutableData: MutableLiveData<ResponseHandler<UpdateResponse?>> =
        MutableLiveData()
    val updateTouristLiveData: LiveData<ResponseHandler<UpdateResponse?>> =
        _updateTouristMutableData.toSingleEvent()

    private val _updateGuideMutableData: MutableLiveData<ResponseHandler<UpdateResponse?>> =
        MutableLiveData()
    val updateGuideLiveData: LiveData<ResponseHandler<UpdateResponse?>> =
        _updateGuideMutableData.toSingleEvent()

    fun updateDriver(currentUser: User) {
        /*for (car_photos in carImagesList) {
            val carImageFile = File(car_photos)
            val carImageRequestBody =
                RequestBody.create("image/*".toMediaTypeOrNull(), carImageFile)
            val carImagePart = MultipartBody.Part.createFormData(
                "car_photos",
                carImageFile.name,
                carImageRequestBody
            )
            imagesList.add(carImagePart)
        }*/
         */

        val profilePicFile = currentUser.personalPhoto?.let { File(it) }
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
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.name.toString())
        val country_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            currentUser.countryId.toString()
        )
        val state_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            currentUser.stateId.toString()
        )
        val gender = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            currentUser.gender.toString()
        )
        val phone =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.phone.toString())
        val car_number =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.carNumber.toString())
        val car_type =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.carType.toString())
        val car_brand_name =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentUser.carBrandName.toString()
            )
        val car_manufacturing_date =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentUser.carManufacturingDate.toString()
            )
        val driver_licence_number = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            currentUser.driverLicenceNumber.toString()
        )
        val gov_id =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.govId.toString())
        /*val languagesList = ArrayList<RequestBody>()
        for (i in languagesIdsList) {
            val lang = RequestBody.create("text/plain".toMediaTypeOrNull(), i.toString())
            languagesList.add(lang)
        }*/

        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.updateDriver(
                    name,
                    country_id,
                    state_id,
                    gender,
                    phone,
                    // bio,
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

    fun updateTourist(currentUser: User) {
        Log.d("updateTourist", "currentUser: $currentUser")
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.name.toString())
        // val des_city_id =
        // RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.desCityId.toString().toInt())

        val gender =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.gender.toString())
        val nationalityPart =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.nationality.toString())

        val profilePicFile = currentUser.personalPhoto?.let { File(it) }
        val profilePicRequestBody =
            profilePicFile?.asRequestBody("image/*".toMediaTypeOrNull())

        val profilePicPart = profilePicRequestBody?.let {
            MultipartBody.Part.createFormData(
                "personal_pictures", // personal_pictures
                profilePicFile.name,
                it
            )
        }
        // val requestBody = createBodyRequestDriverOrGuide(currentUser)
        viewModelScope.launch {
            safeApiCall {
                apiService.updateTourist(
                    name, currentUser.desCityId!!,
                    gender,
                    nationalityPart
                    // profilePicPart!!
                )
                // apiService.updateTourist(requestBody)
            }.collect {
                _updateTouristMutableData.value = it
            }
        }
    }

    private fun createBodyRequestDriverOrGuide(currentUser: User): RequestBody {
        val requestData = mapOf(
            "name" to currentUser.name,
            "dest_city_id" to currentUser.desCityId,
            "gender" to currentUser.gender,
            "nationality" to currentUser.nationality,
        )
        val gson = Gson()
        val jsonData = gson.toJson(requestData)
        return jsonData.toRequestBody("application/json".toMediaTypeOrNull())
    }

    fun updateGuide(currentUser: User) {
        Log.d("updateTourist", "currentUser: $currentUser")
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.name.toString())
        val phone =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(), currentUser.phone.toString()
            )
        val bio =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(), currentUser.bio.toString()
            )

        val gender =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.gender.toString())
        val nationalityPart =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.nationality.toString())

        val profilePicFile = currentUser.personalPhoto?.let { File(it) }
        val profilePicRequestBody =
            profilePicFile?.asRequestBody("image/*".toMediaTypeOrNull())

        val profilePicPart = profilePicRequestBody?.let {
            MultipartBody.Part.createFormData(
                "personal_pictures", // personal_pictures
                profilePicFile.name,
                it
            )
        }
        // val requestBody = createBodyRequestDriverOrGuide(currentUser)
        viewModelScope.launch {
            safeApiCall {
                apiService.updateGuide(
                    name,
                    currentUser.countryId,
                    currentUser.stateId,
                    gender,
                    phone,
                    bio,
                    nationalityPart
                    // profilePicPart!!
                )
                // apiService.updateTourist(requestBody)
            }.collect {
                _updateGuideMutableData.value = it
            }
        }
    }
}
