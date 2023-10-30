package com.visualinnovate.almursheed.tourist.location.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.AttraciveDetailsResponse
import com.visualinnovate.almursheed.home.model.AttractivesListResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val apiService: ApiService,
    private val application: Application,
) : BaseViewModel(apiService, application) {


    private val _attractivesMutableData: MutableLiveData<ResponseHandler<AttractivesListResponse?>> =
        MutableLiveData()
    val attractivesLiveData: LiveData<ResponseHandler<AttractivesListResponse?>> =
        _attractivesMutableData.toSingleEvent()

    private val _attractivesDetailsMutableData: MutableLiveData<ResponseHandler<AttraciveDetailsResponse?>> =
        MutableLiveData()
    val attractivesDetailsLiveData: LiveData<ResponseHandler<AttraciveDetailsResponse?>> =
        _attractivesDetailsMutableData.toSingleEvent()

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
}
