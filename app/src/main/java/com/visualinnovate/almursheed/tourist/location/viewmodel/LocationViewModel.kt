package com.visualinnovate.almursheed.tourist.location.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.AttractiveDetailsResponse
import com.visualinnovate.almursheed.home.model.AttractivesListResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseViewModel(apiService, application) {

    private val _attractiveMutableData: MutableLiveData<ResponseHandler<AttractivesListResponse?>> =
        MutableLiveData()
    val attractiveLiveData: LiveData<ResponseHandler<AttractivesListResponse?>> =
        _attractiveMutableData.toSingleEvent()

    private val _attractiveDetailsMutableData: MutableLiveData<ResponseHandler<AttractiveDetailsResponse?>> =
        MutableLiveData()
    val attractiveDetailsLiveData: LiveData<ResponseHandler<AttractiveDetailsResponse?>> =
        _attractiveDetailsMutableData.toSingleEvent()

    fun getAllAttractiveLocation() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAttractives()
            }.collect {
                _attractiveMutableData.value = it
            }
        }
    }

    fun getAttractiveDetailsById(locationId: Int?) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAttractiveDetailsById(locationId!!)
            }.collect {
                _attractiveDetailsMutableData.value = it
            }
        }
    }
}
