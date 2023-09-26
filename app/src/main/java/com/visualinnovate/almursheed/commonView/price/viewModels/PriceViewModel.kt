package com.visualinnovate.almursheed.commonView.price.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.visualinnovate.almursheed.commonView.price.models.PricesResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _addNewPrice: MutableLiveData<ResponseHandler<Void?>> =
        MutableLiveData()
    val addNewPrice: LiveData<ResponseHandler<Void?>> = _addNewPrice

    private val _prices: MutableLiveData<ResponseHandler<PricesResponse?>> =
        MutableLiveData()
    val myPrices: LiveData<ResponseHandler<PricesResponse?>> = _prices

    fun getAllPrices() {
        viewModelScope.launch {
            safeApiCall {
                apiService.getUserPrices()
            }.collect {
                _prices.value = it
            }
        }
    }

    fun addNewPrice(cityId: String, price: String) {
        viewModelScope.launch {
            safeApiCall {
                val requestBody = createBodyRequest(cityId, price)
                apiService.addNewPrice(requestBody)
            }.collect {
                _addNewPrice.value = it
            }
        }
    }

    private fun createBodyRequest(cityId: String, price: String): RequestBody {
        val requestData = mapOf(
            "city_id" to cityId,
            "price" to price,
        )
        val gson = Gson()
        val jsonData = gson.toJson(requestData)
        return jsonData.toRequestBody("application/json".toMediaTypeOrNull())
    }
}
