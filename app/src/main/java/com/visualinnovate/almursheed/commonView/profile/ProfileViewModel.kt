package com.visualinnovate.almursheed.commonView.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.UpdateResponse
import com.visualinnovate.almursheed.network.ApiService
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
) : BaseViewModel(apiService, application) {

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

    private val _editLocation: MutableLiveData<ResponseHandler<UpdateResponse?>> =
        MutableLiveData()
    val editLocationLiveData: LiveData<ResponseHandler<UpdateResponse?>> =
        _editLocation.toSingleEvent()

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
                it,
            )
        }
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.name.toString())
        val countryId = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            currentUser.countryId.toString(),
        )
        val stateId = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            currentUser.stateId.toString(),
        )
        val gender = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            currentUser.gender.toString(),
        )
        val phone =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.phone.toString())
        val carNumber =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.carNumber.toString())
        val carType =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.carType.toString())
        val carBrandName =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentUser.carBrandName.toString(),
            )
        val carManufacturingDate =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentUser.carManufacturingDate.toString(),
            )
        val driverLicenceNumber = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            currentUser.driverLicenceNumber.toString(),
        )
        val govId =
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
                    countryId,
                    stateId,
                    gender,
                    phone,
                    // bio,
                    govId,
                    // languagesList,
                    // profilePicPart!!,
                    driverLicenceNumber,
                    // imagesList,
                    carNumber,
                    carType,
                    carBrandName,
                    carManufacturingDate,
                )
            }.collect {
                _updateDriverMutableData.value = it
            }
        }
    }

    private fun checkImagePath(imagePath: String?): MultipartBody.Part? {
        if (imagePath != null) {
            val profilePicFile = File(imagePath)
            val profilePicRequestBody =
                profilePicFile.asRequestBody("image/*".toMediaTypeOrNull())

            return profilePicRequestBody.let {
                MultipartBody.Part.createFormData(
                    "personal_pictures",
                    profilePicFile.name,
                    it,
                )
            }

        } else {
            return null
        }
    }

    fun updateTourist(currentUser: User, imagePath: String?) {
        Log.d("updateTourist", "currentUser: $currentUser")
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.name.toString())

        val destCityIdPart =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.destCityId.toString())

        val gender =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.gender.toString())

        val nationalityPart =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.nationality.toString())

        // val requestBody = createBodyRequestDriverOrGuide(currentUser)
        viewModelScope.launch {
            safeApiCall {
                apiService.updateTourist(
                    name,
                    destCityIdPart,
                    gender,
                    nationalityPart,
                    checkImagePath(imagePath),
                )
                // apiService.updateTourist(requestBody)
            }.collect {
                _updateTouristMutableData.value = it
            }
        }
    }

    fun updateLocationDriver(currentUser: User) {
        val stateId =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.stateId.toString())

        viewModelScope.launch {
            safeApiCall {
                apiService.updateLocationDriver(stateId)
            }.collect {
                _editLocation.value = it
            }
        }
    }

    fun updateLocationGuide(currentUser: User) {
        val stateId =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.stateId.toString())

        viewModelScope.launch {
            safeApiCall {
                apiService.updateLocationGuide(stateId)
                // apiService.updateTourist(requestBody)
            }.collect {
                _editLocation.value = it
            }
        }
    }

    fun updateLocationTourist(currentUser: User) {
        val stateId =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.stateId.toString())

        viewModelScope.launch {
            safeApiCall {
                apiService.updateLocationTourist(stateId)
                // apiService.updateTourist(requestBody)
            }.collect {
                _editLocation.value = it
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
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.name.toString())
        val phone =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentUser.phone.toString(),
            )
        val bio =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentUser.bio.toString(),
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
                it,
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
                    nationalityPart,
                    // profilePicPart!!
                )
                // apiService.updateTourist(requestBody)
            }.collect {
                _updateGuideMutableData.value = it
            }
        }
    }
}
