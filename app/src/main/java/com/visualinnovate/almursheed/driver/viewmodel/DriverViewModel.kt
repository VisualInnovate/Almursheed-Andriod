package com.visualinnovate.almursheed.driver.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.*
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val apiService: ApiService,
    private val application: Application,
) : BaseViewModel(apiService, application) {

    private var selectedUserPosition = -1

    var latestDriversList: List<DriverAndGuideItem?>? = ArrayList()
    var driversList: List<DriverAndGuideItem?>? = ArrayList()

    private val _driverLatestMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val driverLatestLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        _driverLatestMutableData.toSingleEvent()

    private val _driverMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val driverLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> = _driverMutableData.toSingleEvent()

    private val _driverDetailsMutable: MutableLiveData<ResponseHandler<DriverDetailsResponse?>> =
        MutableLiveData()
    val driverDetailsLiveData: LiveData<ResponseHandler<DriverDetailsResponse?>> =
        _driverDetailsMutable.toSingleEvent()

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
}
