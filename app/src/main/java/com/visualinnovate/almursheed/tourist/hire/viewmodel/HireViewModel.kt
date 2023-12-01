package com.visualinnovate.almursheed.tourist.hire.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.auth.model.MessageResponse
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.commonView.myOrders.models.DayModel
import com.visualinnovate.almursheed.commonView.price.models.PriceItem
import com.visualinnovate.almursheed.home.model.CreateOrderResponse
import com.visualinnovate.almursheed.home.model.DriverAndGuideItem
import com.visualinnovate.almursheed.home.model.DriversAndGuidesListResponse
import com.visualinnovate.almursheed.home.model.Order
import com.visualinnovate.almursheed.home.model.OrderDetail
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

    private var selectedDriverAndGuide: DriverAndGuideItem? = null
    private var selectedCityId: Int? = null
    private var selectedCityName: String? = null

    private val _cites: MutableLiveData<ArrayList<PriceItem>?> =
        MutableLiveData()
    val cites: LiveData<ArrayList<PriceItem>?> = _cites.toSingleEvent()

    fun setSelectedDriverOrGuide(user: DriverAndGuideItem?) {
        selectedDriverAndGuide = user
        setDefaultCities(user)
    }

    private fun setDefaultCities(user: DriverAndGuideItem?) {
        // set default selected city is first city
        val cities = ArrayList<PriceItem>()
        if (!user?.priceServices.isNullOrEmpty()) {
            selectedCityName = user!!.priceServices?.get(0)?.cityName
            selectedCityId = user?.priceServices?.get(0)?.cityId
        }

        user?.priceServices?.forEach {
            cities.add(it!!)
        }
        _cites.value = cities
    }

    fun setSelectedCity(name: String, id: String) {
        selectedCityId = id.toInt()
        selectedCityName = name
    }

    fun getSelectedDriverAndGuideCities(): ArrayList<PriceItem>? = _cites.value

    fun getSelectedCityName(): String? = selectedCityName
    fun getSelectedCityId(): Int? = selectedCityId

    fun getSelectedDriverAndGuideName(): String? = selectedDriverAndGuide?.name

    /*init {
        getAllDriversByDistCityId()
        getAllGuidesByDistCityId()
    }*/

    var allDrivers = ArrayList<DriverAndGuideItem>()
    var allGuides = ArrayList<DriverAndGuideItem>()

    private val _allDriversAndGuidesMutableData: MutableLiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        MutableLiveData()
    val allDriversAndGuidesLiveData: LiveData<ResponseHandler<DriversAndGuidesListResponse?>> =
        _allDriversAndGuidesMutableData

    private val _createOrderMutableData: MutableLiveData<ResponseHandler<CreateOrderResponse?>> =
        MutableLiveData()
    val createOrderLiveData: LiveData<ResponseHandler<CreateOrderResponse?>> =
        _createOrderMutableData.toSingleEvent()

    private val _submitOrderMutableData: MutableLiveData<ResponseHandler<MessageResponse?>> =
        MutableLiveData()
    val submitOrderLiveData: LiveData<ResponseHandler<MessageResponse?>> =
        _submitOrderMutableData.toSingleEvent()

    fun getAllDriversByDistCityId() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllDriversByDistCityId(SharedPreference.getCityId()!!)
            }.collect {
                when (it) {
                    is ResponseHandler.Success -> {
                        allDrivers = (it.data?.drivers as ArrayList<DriverAndGuideItem>?)!!
                    }

                    else -> {}
                }
            }
        }
    }

    fun getAllGuidesByDistCityId() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAllGuidesByDistCityId(SharedPreference.getCityId()!!.toInt())
            }.collect {
                // _allGuideMutableData.value = it
                when (it) {
                    is ResponseHandler.Success -> {
                        allGuides = (it.data?.drivers as ArrayList<DriverAndGuideItem>?)!!
                    }

                    else -> {}
                }
            }
        }
    }

    fun submitOrder(orderId: Int) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.submitOrder(orderId)
            }.collect {
                _submitOrderMutableData.value = it
            }
        }
    }

    fun createOrder(tripType: Int, currentLocation: Location?, startDate: String, endDate: String, userType: Int, selectedDays: ArrayList<DayModel>) {
        val order = prepareOrderForApiCall(tripType, currentLocation, startDate, endDate, userType, selectedDays)

        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.createOrder(order)
            }.collect {
                _createOrderMutableData.value = it
            }
        }
    }

    private fun prepareOrderForApiCall(
        tripType: Int,
        currentLocation: Location?,
        startDate: String,
        endDate: String,
        userType: Int,
        selectedDays: ArrayList<DayModel>,
    ): RequestCreateOrder {
        val order = Order(
            trip_type = tripType,
            start_date = startDate,
            end_date = endDate,
            country_id = selectedDriverAndGuide?.countryId?:0,
            lat = currentLocation?.latitude.toString(),
            longitude = currentLocation?.longitude.toString(),
        )

        val orderDetailsList = ArrayList<OrderDetail>()
        selectedDays.forEach {
            orderDetailsList.add(OrderDetail(date = it.date ?: "", it.cityId!!))
        }

        return RequestCreateOrder(
            user_id = selectedDriverAndGuide!!.id!!,
            user_type = userType, // 1 driver, 2 guide
            order = order,
            order_details = orderDetailsList,
        )
    }

    fun clearData() {
        selectedDriverAndGuide = null
        selectedCityName = null
        selectedCityId = null
        _cites.value = ArrayList()
    }
}
