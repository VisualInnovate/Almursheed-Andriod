package com.visualinnovate.almursheed.commonView.filter.viewModel

import android.app.Application
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.network.BaseApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val appContext: Application,
) : BaseApiResponse(appContext) {

    var from: String = ""
    var searchData: String? = null
    var carCategory: String? = null
    var countryId: String? = null
    var cityId: String? = null
    var cityName: String? = null
    var countryName: String? = null
    var carModel: String? = null
    var rate: String? = null
    var language: String? = appContext.getString(R.string.all)
    var price: String? = null
    var type = "Driver"

    var isFromFilter = false

    fun clearDataOnInit() {
        carCategory = null
        carModel = null
        rate = null
        countryId = null
        cityId = null
        cityName= null
        countryName = null
        language = null
        price = null
        type = "Driver"
    }

    fun isFromFilter(){

    }
}
