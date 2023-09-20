package com.visualinnovate.almursheed.home.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.auth.model.MessageResponse
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.CreateOrderResponse
import com.visualinnovate.almursheed.home.model.DriverItem
import com.visualinnovate.almursheed.home.model.DriverListResponse
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

    var order: CreateOrderResponse? = null

    init {
        getAllDriversByDistCityId()
        getAllGuidesByDistCityId()
    }

    var allDrivers = ArrayList<DriverItem>()
    var allGuides = ArrayList<DriverItem>()

    private val _allDriversAndGuidesMutableData: MutableLiveData<ResponseHandler<DriverListResponse?>> =
        MutableLiveData()
    val allDriversAndGuidesLiveData: LiveData<ResponseHandler<DriverListResponse?>> =
        _allDriversAndGuidesMutableData

    private val _createOrderMutableData: MutableLiveData<ResponseHandler<CreateOrderResponse?>> =
        MutableLiveData()
    val createOrderLiveData: LiveData<ResponseHandler<CreateOrderResponse?>> =
        _createOrderMutableData.toSingleEvent()

    private val _submitOrderMutableData: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val submitOrderLiveData: LiveData<ResponseHandler<MessageResponse?>> =
        _submitOrderMutableData.toSingleEvent()

    private fun getAllDriversByDistCityId() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllDriversByDistCityId(SharedPreference.getUser()?.desCityId!!)
            }.collect {
                when (it) {
                    is ResponseHandler.Success -> {
                        allDrivers = (it.data?.drivers as ArrayList<DriverItem>?)!!
                    }

                    else -> {}
                }
            }
        }
    }

    private fun getAllGuidesByDistCityId() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllGuidesByDistCityId(SharedPreference.getUser()?.desCityId!!)
            }.collect {
                // _allGuideMutableData.value = it
                when (it) {
                    is ResponseHandler.Success -> {
                        allGuides = (it.data?.drivers as ArrayList<DriverItem>?)!!
                    }

                    else -> {}
                }
            }
        }
    }

    fun createOrder(requestCreateOrder: RequestCreateOrder) {
        Log.d("createOrder", "requestCreateOrder $requestCreateOrder")
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.createOrder(requestCreateOrder)
            }.collect {
                _createOrderMutableData.value = it
            }
        }
    }

    fun submitOrder(orderId: Int) {
        Log.d("submitOrder", "orderId $orderId")
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.submitOrder(orderId)
            }.collect {
                _submitOrderMutableData.value = it
            }
        }
    }
}
