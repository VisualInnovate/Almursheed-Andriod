package com.visualinnovate.almursheed.home.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.home.model.AccommodationDetailsResponse
import com.visualinnovate.almursheed.home.model.AccommodationResponse
import com.visualinnovate.almursheed.home.model.AttraciveDetailsResponse
import com.visualinnovate.almursheed.home.model.AttractivesListResponse
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.home.model.DriverDetailsResponse
import com.visualinnovate.almursheed.home.model.DriversAndGuidesListResponse
import com.visualinnovate.almursheed.home.model.GuideDetailsResponse
import com.visualinnovate.almursheed.home.model.OfferDetailsResponse
import com.visualinnovate.almursheed.home.model.OfferResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application
) : BaseViewModel(apiService, application) {

    var driversList: List<DriverAndGuideItem?>? = ArrayList()
    private val _driverLatestMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val driverLatestLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        _driverLatestMutableData

    private val _guideLatestMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val guideLatestLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        _guideLatestMutableData


    private val _driverMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val driverLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        _driverMutableData

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

    fun fetchAllDrivers() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllDrivers()
            }.collect {
                _driverMutableData.value = it
            }
        }
    }

    fun fetchAllGuides() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllGuides()
            }.collect {
                _guideMutableData.value =
                    it ///////////////////////////////////////////////////////////////////////////////////////////////////
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

    fun handleIsFavourite(favourite: Boolean?) {
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

    var selectedUserPosition = -1
}
