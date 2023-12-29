package com.visualinnovate.almursheed.home.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.*
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val application: Application,
) : BaseViewModel(apiService, application) {

    var latestDriversList: List<DriverAndGuideItem?>? = ArrayList()
    var latestGuidesList: List<DriverAndGuideItem?>? = ArrayList()
    var driversList: List<DriverAndGuideItem?>? = ArrayList()
    var guidesList: List<DriverAndGuideItem?>? = ArrayList()

    private val _getAllBannerMutableData: MutableLiveData<ResponseHandler<BannerResponse?>> =
        MutableLiveData()
    val getAllBannerLiveData: LiveData<ResponseHandler<BannerResponse?>> =
        _getAllBannerMutableData.toSingleEvent()

    private val _driverLatestMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val driverLatestLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        _driverLatestMutableData.toSingleEvent()

    private val _guideLatestMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val guideLatestLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> = _guideLatestMutableData.toSingleEvent()

    private val _driverMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val driverLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> = _driverMutableData.toSingleEvent()

    private val _guideMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val guideLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> = _guideMutableData.toSingleEvent()

    private val _offerMutableData: MutableLiveData<ResponseHandler<OfferResponse?>> =
        MutableLiveData()
    val offerLiveData: LiveData<ResponseHandler<OfferResponse?>> = _offerMutableData.toSingleEvent()

    private val _offerDetailsMutableData: MutableLiveData<ResponseHandler<OfferDetailsResponse?>> =
        MutableLiveData()
    val offerDetailsLiveData: LiveData<ResponseHandler<OfferDetailsResponse?>> =
        _offerDetailsMutableData.toSingleEvent()

    private val _accommodationMutableData: MutableLiveData<ResponseHandler<AccommodationResponse?>> =
        MutableLiveData()
    val accommodationLiveData: LiveData<ResponseHandler<AccommodationResponse?>> =
        _accommodationMutableData.toSingleEvent()

    private val _accommodationDetailsMutable: MutableLiveData<ResponseHandler<AccommodationDetailsResponse?>> =
        MutableLiveData()
    val accommodationDetailsLiveData: LiveData<ResponseHandler<AccommodationDetailsResponse?>> =
        _accommodationDetailsMutable.toSingleEvent()

    private val _driverDetailsMutable: MutableLiveData<ResponseHandler<DriverDetailsResponse?>> =
        MutableLiveData()
    val driverDetailsLiveData: LiveData<ResponseHandler<DriverDetailsResponse?>> =
        _driverDetailsMutable.toSingleEvent()

    private val _guideDetailsMutable: MutableLiveData<ResponseHandler<GuideDetailsResponse?>> =
        MutableLiveData()
    val guideDetailsLiveData: LiveData<ResponseHandler<GuideDetailsResponse?>> =
        _guideDetailsMutable.toSingleEvent()

    fun getAllBanners() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllBanners()
            }.collect {
                _getAllBannerMutableData.value = it
            }
        }
    }

    fun getLatestDriver() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getLatestDriver()
            }.collect {
                _driverLatestMutableData.value = it
            }
        }
    }

    fun getLatestGuides() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getLatestGuide()
            }.collect {
                _guideLatestMutableData.value = it
            }
        }
    }

    fun fetchAllDrivers(
        country: String? = null,
        city: String? = null,
        carCategory: String? = null,
        carModel: String? = null,
        searchData: String? = null,
        price: String? = null,
        rate: String? = null,
    ) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllDrivers(country, city, carCategory, carModel, searchData, price, rate)
            }.collect {
                _driverMutableData.value = it
            }
        }
    }

    fun fetchAllGuides(
        country: String? = null,
        city: String? = null,
        language: ArrayList<Int?>? = null,
        searchData: String? = null,
        price: String? = null,
        rate: String? = null,
    ) {
        val lang = language?.toString()
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllGuides(country, city, lang, searchData, price, rate)
            }.collect {
                _guideMutableData.value = it
            }
        }
    }

    private fun formatLanguageToArray(language: ArrayList<Int?>?): Map<String, Int?> {
        // this method to send array at queryParameter like this status[0]=pending&status[1]=approved
        val requestData = mutableMapOf<String, Int?>()
        language?.forEachIndexed { index, lang ->
            requestData["language_id[$index]"] = lang
        }
        return requestData
    }

    fun fetchOfferResponse() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllOffers()
            }.collect {
                _offerMutableData.value = it
            }
        }
    }

    fun getOfferDetailsById(offerId: Int) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getOfferDetailsById(offerId)
            }.collect {
                _offerDetailsMutableData.value = it
            }
        }
    }

    fun fetchAccommodationsList(
        country: String? = null,
        city: String? = null,
        category: String? = null,
        roomCount: String? = null,
        searchData: String? = null,
        price: String? = null,
    ) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllAccommodation(country,city,category,roomCount,searchData,price)
            }.collect {
                _accommodationMutableData.value = it
            }
        }
    }

    fun fetchAccommodationDetails(id: Int) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAccommodationDetailsById(id)
            }.collect {
                _accommodationDetailsMutable.value = it
            }
        }
    }

    fun getDriverDetailsById(driverId: Int?) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getDriverDetailsById(driverId!!)
            }.collect {
                _driverDetailsMutable.value = it
            }
        }
    }

    fun getGuideDetailsById(driverId: Int?) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getGuideDetailsById(driverId!!)
            }.collect {
                _guideDetailsMutable.value = it
            }
        }
    }

    fun handleIsFavouriteDrivers(favourite: Boolean?) {
        when (_driverMutableData.value) {
            is ResponseHandler.Success -> {
                val list =
                    (_driverMutableData.value as ResponseHandler.Success<DriversAndGuidesListResponse?>).data
                list?.drivers?.forEach {
                    if (it?.id == selectedUserPosition) {
                        it.isFavourite = favourite
                    }
                }
                (_driverMutableData.value as ResponseHandler.Success<DriversAndGuidesListResponse?>).data!!.drivers =
                    list!!.drivers
                _driverMutableData.value = _driverMutableData.value
            }

            else -> {}
        }
    }

    fun handleIsFavouriteGuides(favourite: Boolean?) {
        when (_guideMutableData.value) {
            is ResponseHandler.Success -> {
                val list =
                    (_guideMutableData.value as ResponseHandler.Success<DriversAndGuidesListResponse?>).data
                list?.drivers?.forEach {
                    if (it?.id == selectedUserPosition) {
                        it.isFavourite = favourite
                    }
                }
                (_guideMutableData.value as ResponseHandler.Success<DriversAndGuidesListResponse?>).data!!.drivers =
                    list!!.drivers
                _guideMutableData.value = _guideMutableData.value
            }

            else -> {}
        }
    }

    fun handleIsFavouriteLatestGuides(favourite: Boolean?) {
        when (_guideLatestMutableData.value) {
            is ResponseHandler.Success -> {
                val list =
                    (_guideLatestMutableData.value as ResponseHandler.Success<DriversAndGuidesListResponse?>).data
                list?.drivers?.forEach {
                    if (it?.id == selectedUserPosition) {
                        it.isFavourite = favourite
                    }
                }
                (_guideLatestMutableData.value as ResponseHandler.Success<DriversAndGuidesListResponse?>).data!!.drivers =
                    list!!.drivers
                _guideLatestMutableData.value = _guideLatestMutableData.value
            }

            else -> {}
        }
    }

    fun handleIsFavouriteLatestDrivers(favourite: Boolean?) {
        when (_driverLatestMutableData.value) {
            is ResponseHandler.Success -> {
                val list =
                    (_driverLatestMutableData.value as ResponseHandler.Success<DriversAndGuidesListResponse?>).data
                list?.drivers?.forEach {
                    if (it?.id == selectedUserPosition) {
                        it.isFavourite = favourite
                    }
                }
                (_driverLatestMutableData.value as ResponseHandler.Success<DriversAndGuidesListResponse?>).data!!.drivers =
                    list!!.drivers
                _driverLatestMutableData.value = _driverLatestMutableData.value
            }

            else -> {}
        }
    }

    var selectedUserPosition = -1
}
