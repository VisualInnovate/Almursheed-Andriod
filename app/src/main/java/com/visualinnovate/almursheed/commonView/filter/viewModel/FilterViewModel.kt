package com.visualinnovate.almursheed.commonView.filter.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.DriversAndGuidesListResponse
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val appContext: Application,
) : BaseApiResponse(appContext) {

    private val _resetData: MutableLiveData<Boolean> =
        MutableLiveData()
    val resetData: LiveData<Boolean> =
        _resetData.toSingleEvent()

    var from: String = ""
    var searchData: String? = null
    var price: String? = null
    var countryId: String? = null
    var cityId: String? = null
    var cityName: String? = null
    var countryName: String? = null
    var rate: String? = null

    // driver
    var carCategory: String? = null
    var carModel: String? = null

    // guide
    var languageName: String? = null
    var languageId: ArrayList<Int?>? = null

    // accommodation
    var accommodationCategoryId: String? = null
    var roomsCountId: String? = null

    var accommodationCategoryName: String? = null
    var roomsName: String? = null
    var type = "Driver"

    private var isFromFilter = false

    fun clearDataOnInit() {
        carCategory = null
        carModel = null
        rate = null
        countryId = null
        cityId = null
        cityName = null
        countryName = null
        languageId = null
        languageName = null
        price = null
        accommodationCategoryId = null
        roomsCountId = null
        accommodationCategoryName = null
        roomsName = null
        type = "Driver"
        _resetData.value = true
    }

    fun checkDestinationFromFilter(): Boolean {
        return isFromFilter
    }

    fun setFromFilter(fromFilter: Boolean) {
        isFromFilter = fromFilter
    }
}
