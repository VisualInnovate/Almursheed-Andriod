package com.visualinnovate.almursheed.commonView.myOrders.viewModel

import android.app.Application
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    fun getAllOrdersDriverGuide() {
//        viewModelScope.launch {
//            safeApiCall {
//                // Make your API call here using Retrofit service or similar
//              //  apiService.createOrder(requestCreateOrder)
//            }.collect {
//                //_createOrderMutableData.value = it
//            }
//        }
    }

    fun getAllOrdersTourist() {
//        viewModelScope.launch {
//            safeApiCall {
//                // Make your API call here using Retrofit service or similar
//               // apiService.submitOrder(orderId)
//            }.collect {
//              //  _submitOrderMutableData.value = it
//            }
//        }
    }
}
