package com.visualinnovate.almursheed.home.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.home.model.FlightResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application
) : BaseApiResponse(application) {

    private val _flightMutableData: MutableLiveData<ResponseHandler<FlightResponse?>> =
        MutableLiveData()
    val flightLiveData: LiveData<ResponseHandler<FlightResponse?>> = _flightMutableData

    fun fetchFlightResponse() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllFlights()
            }.asLiveData().observeForever {
                _flightMutableData.value = it
            }
        }
    }
}
