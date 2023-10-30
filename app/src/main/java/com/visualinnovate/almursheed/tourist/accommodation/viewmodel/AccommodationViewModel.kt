package com.visualinnovate.almursheed.tourist.accommodation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.AccommodationDetailsResponse
import com.visualinnovate.almursheed.home.model.AccommodationResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccommodationViewModel @Inject constructor(
    private val apiService: ApiService,
    private val application: Application,
) : BaseViewModel(apiService, application) {

    private val _accommodationMutableData: MutableLiveData<ResponseHandler<AccommodationResponse?>> =
        MutableLiveData()
    val accommodationLiveData: LiveData<ResponseHandler<AccommodationResponse?>> =
        _accommodationMutableData.toSingleEvent()

    private val _accommodationDetailsMutable: MutableLiveData<ResponseHandler<AccommodationDetailsResponse?>> =
        MutableLiveData()
    val accommodationDetailsLiveData: LiveData<ResponseHandler<AccommodationDetailsResponse?>> =
        _accommodationDetailsMutable.toSingleEvent()

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
                apiService.getAllAccommodation(
                    country,
                    city,
                    category,
                    roomCount,
                    searchData,
                    price
                )
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
}
