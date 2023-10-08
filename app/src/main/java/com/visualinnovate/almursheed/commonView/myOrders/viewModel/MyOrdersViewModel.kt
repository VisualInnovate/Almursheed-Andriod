package com.visualinnovate.almursheed.commonView.myOrders.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersModel
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _orders: MutableLiveData<ResponseHandler<MyOrdersModel?>?> =
        MutableLiveData()
    val orders: LiveData<ResponseHandler<MyOrdersModel?>?> = _orders

    var orderDetails: MyOrdersItem? = null

    private var job: Job? = null

    fun getOrders(status: String) {
        job?.cancel()
        job = viewModelScope.launch {
            safeApiCall {
                apiService.getMyOrders(status)
            }.collect {
                _orders.value = it
            }
        }
    }

    fun addRate(rate: Float, rateComment: String?) {

    }

}
