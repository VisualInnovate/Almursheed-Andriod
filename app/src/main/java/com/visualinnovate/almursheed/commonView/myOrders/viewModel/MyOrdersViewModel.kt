package com.visualinnovate.almursheed.commonView.myOrders.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersItem
import com.visualinnovate.almursheed.commonView.myOrders.models.MyOrdersModel
import com.visualinnovate.almursheed.commonView.myOrders.models.RateResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _orders: MutableLiveData<ResponseHandler<MyOrdersModel?>?> =
        MutableLiveData()
    val orders: LiveData<ResponseHandler<MyOrdersModel?>?> = _orders

    private val _addRate: MutableLiveData<ResponseHandler<RateResponse?>?> =
        MutableLiveData()
    val addRate: LiveData<ResponseHandler<RateResponse?>?> = _addRate

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

    fun addRate(
        rate: Float, // rate -> tourist_rating
        comment: String,
        reviewableId: Int, // driver_id or guide_id
        type: Int, // 0 is driver, 1  is guide
    ) {
        viewModelScope.launch {
            safeApiCall {
                apiService.addRate(rate.toString(), comment, reviewableId, type)
            }.collect {
                _addRate.value = it
            }
        }
    }

}
