package com.visualinnovate.almursheed.home.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.home.model.*
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _driverLatestMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val driverLatestLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        _driverLatestMutableData

    private val _guideLatestMutableData: MutableLiveData<ResponseHandler<GuideListResponse?>> =
        MutableLiveData()
    val guideLatestLiveData: LiveData<ResponseHandler<GuideListResponse?>> = _guideLatestMutableData

    private val _driverMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val driverLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> = _driverMutableData

    private val _guideMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val guideLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> = _guideMutableData

    private val _offerMutableData: MutableLiveData<ResponseHandler<OfferResponse?>> =
        MutableLiveData()
    val offerLiveData: LiveData<ResponseHandler<OfferResponse?>> = _offerMutableData

    private val _offerDetailsMutableData: MutableLiveData<ResponseHandler<OfferDetailsResponse?>> =
        MutableLiveData()
    val offerDetailsLiveData: LiveData<ResponseHandler<OfferDetailsResponse?>> =
        _offerDetailsMutableData

    private val _accommodationMutableData: MutableLiveData<ResponseHandler<AccommodationResponse?>> =
        MutableLiveData()
    val accommodationLiveData: LiveData<ResponseHandler<AccommodationResponse?>> =
        _accommodationMutableData

    private val _accommodationDetailsMutable: MutableLiveData<ResponseHandler<AccommodationDetailsResponse?>> =
        MutableLiveData()
    val accommodationDetailsLiveData: LiveData<ResponseHandler<AccommodationDetailsResponse?>> =
        _accommodationDetailsMutable

    private val _attractivesMutableData: MutableLiveData<ResponseHandler<AttractivesListResponse?>> =
        MutableLiveData()
    val attractivesLiveData: LiveData<ResponseHandler<AttractivesListResponse?>> =
        _attractivesMutableData

    private val _attractivesDetailsMutableData: MutableLiveData<ResponseHandler<AttraciveDetailsResponse?>> =
        MutableLiveData()
    val attractivesDetailsLiveData: LiveData<ResponseHandler<AttraciveDetailsResponse?>> =
        _attractivesDetailsMutableData

    private val _driverDetailsMutable: MutableLiveData<ResponseHandler<DriverDetailsResponse?>> =
        MutableLiveData()
    val driverDetailsLiveData: LiveData<ResponseHandler<DriverDetailsResponse?>> =
        _driverDetailsMutable

    private val _guideDetailsMutable: MutableLiveData<ResponseHandler<GuideDetailsResponse?>> =
        MutableLiveData()
    val guideDetailsLiveData: LiveData<ResponseHandler<GuideDetailsResponse?>> =
        _guideDetailsMutable

    fun getLatestDriver(cityId: Int?) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getLatestDriver(cityId!!)
            }.collect {
                _driverLatestMutableData.value = it
            }
        }
    }

    fun getLatestGuides(cityId: Int?) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getLatestGuide(cityId!!)
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
        language: String? = null,
        searchData: String? = null,
        price: String? = null,
        rate: String? = null,

    ) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllGuides(country, city, language, searchData, price, rate)
            }.collect {
                _guideMutableData.value = it
            }
        }
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

    fun fetchAccommodationsList() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllAccommodation()
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

    fun fetchAttractivesList() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAttractives()
            }.collect {
                _attractivesMutableData.value = it
            }
        }
    }

    fun getAttractivesDetailsById(locationId: Int?) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAttractiveDetailsById(locationId!!)
            }.collect {
                _attractivesDetailsMutableData.value = it
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
}
