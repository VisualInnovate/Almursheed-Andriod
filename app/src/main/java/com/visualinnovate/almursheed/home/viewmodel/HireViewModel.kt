package com.visualinnovate.almursheed.home.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.auth.model.MessageResponse
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.RequestCreateOrder
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HireViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _createOrderMutableData: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val createOrderLiveData: LiveData<ResponseHandler<MessageResponse?>> =
        _createOrderMutableData.toSingleEvent()

    fun createOrder(requestCreateOrder: RequestCreateOrder) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.createOrder(requestCreateOrder)
            }.collect {
                _createOrderMutableData.value = it
            }
        }
    }
}