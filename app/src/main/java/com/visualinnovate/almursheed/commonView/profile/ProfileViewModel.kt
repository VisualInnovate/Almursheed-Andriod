package com.visualinnovate.almursheed.commonView.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.UpdateResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.Constant
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
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

    private val _personalInformation: MutableLiveData<ResponseHandler<UpdateResponse?>> =
        MutableLiveData()
    val personalInformation: LiveData<ResponseHandler<UpdateResponse?>> =
        _personalInformation.toSingleEvent()

    private val _editLocation: MutableLiveData<ResponseHandler<UpdateResponse?>> =
        MutableLiveData()
    val editLocationLiveData: LiveData<ResponseHandler<UpdateResponse?>> =
        _editLocation.toSingleEvent()

    private val _updateGuideMutableData: MutableLiveData<ResponseHandler<UpdateResponse?>> =
        MutableLiveData()
    val updateGuideLiveData: LiveData<ResponseHandler<UpdateResponse?>> =
        _updateGuideMutableData.toSingleEvent()

    fun updateDriverCarInformation(currentUser: User, carImages: ArrayList<String>) {
        val carNumber =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.carNumber.toString())

        val carType =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.carType.toString())

        val carBrand =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentUser.carBrandName.toString(),
            )
        val carManufacture =
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                currentUser.carManufacturingDate.toString(),
            )
        val licenceNumber = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            currentUser.licenceNumber.toString(),
        )
        val govId =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.govId.toString())

        val language =
            RequestBody.create("text/plain".toMediaTypeOrNull(), "English" /*currentUser.language.toString()*/)

        val carPhotos = if (checkCarImages(carImages).isNotEmpty()) {
            val list = ArrayList<MultipartBody.Part?>()
            carImages.forEach {
                list.add(checkImagePath(it,"car_photos"))
            }
            list
        } else {
            null
        }

        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.updateDriverCarInformations(
                    govId,
                    licenceNumber,
                    carNumber,
                    carType,
                    carBrand,
                    carManufacture,
                   // language,
                    carPhotos,
                )
            }.collect {
                _updateDriverMutableData.value = it
            }
        }
    }

    private fun checkImagePath(imagePath: String? , name:String): MultipartBody.Part? {
        return if (imagePath != null) {
            val picFile = File(imagePath)
            val picRequestBody =
                picFile.asRequestBody("image/*".toMediaTypeOrNull())

            picRequestBody.let {
                MultipartBody.Part.createFormData(
                    name,
                    picFile.name,
                    it,
                )
            }
        } else {
            null
        }
    }

    private fun checkCarImages(carImages: ArrayList<String>): ArrayList<String> {
        val carPhotos = ArrayList<String>()
        carImages.forEach {
            carPhotos.add(it)
        }
        return carPhotos
    }

//    fun updateTourist(currentUser: User, imagePath: String?) {
//        val name =
//            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.name.toString())
//
//        val destCityIdPart =
//            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.destCityId.toString())
//
//        val gender =
//            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.gender.toString())
//
//        val nationalityPart =
//            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.nationality.toString())
//
//        viewModelScope.launch {
//            safeApiCall {
//                apiService.updateTourist(
//                    name,
//                    destCityIdPart,
//                    gender,
//                    nationalityPart,
//                    checkImagePath(imagePath),
//                )
//            }.collect {
//                _updateTouristMutableData.value = it
//            }
//        }
//    }

    fun updatePersonalInformation(currentUser: User, imagePath: String?) {
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.name.toString())

        val destCityIdPart =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.destCityId.toString())

        val gender =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.gender.toString())

        val nationalityPart =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.nationality.toString())

        val phonePart =
            RequestBody.create("text/plain".toMediaTypeOrNull(), currentUser.phone.toString())

        when (currentUser.type) {
            Constant.ROLE_DRIVER -> {
                viewModelScope.launch {
                    safeApiCall {
                        apiService.updateDriverPersonalInformation(
                            name,
                            destCityIdPart,
                            gender,
                            nationalityPart,
                            phonePart,
                            checkImagePath(imagePath,"personal_pictures"),
                        )
                    }.collect {
                        _personalInformation.value = it
                    }
                }
            }

            Constant.ROLE_GUIDES -> {
//                viewModelScope.launch {
//                    safeApiCall {
//                        apiService.updateTourist(
//                            name,
//                            destCityIdPart,
//                            gender,
//                            nationalityPart,
//                            phonePart,
//                            checkImagePath(imagePath),
//                        )
//                    }.collect {
//                        _personalInformation.value = it
//                    }
//                }
            }
            Constant.ROLE_TOURIST -> {
                viewModelScope.launch {
                    safeApiCall {
                        apiService.updateTourist(
                            name,
                            destCityIdPart,
                            gender,
                            nationalityPart,
                            checkImagePath(imagePath,"personal_pictures"),
                        )
                    }.collect {
                        _personalInformation.value = it
                    }
                }
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
