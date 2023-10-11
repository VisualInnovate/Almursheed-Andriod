package com.visualinnovate.almursheed.driver.filter.viewModel

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
    var searchData: String? = ""
    var carCategory: String? = appContext.getString(R.string.all)
    var country: String? = appContext.getString(R.string.all)
    var city: String? = appContext.getString(R.string.all)
    var carModel: String? = appContext.getString(R.string.all)
    var rate: String? = appContext.getString(R.string.all)
    var language: String? = appContext.getString(R.string.all)
    var price: Int = 0
    var type = "Driver"

    fun clearDataOnInit() {
        carCategory = appContext.getString(R.string.all)
        carModel = appContext.getString(R.string.all)
        rate = appContext.getString(R.string.all)
        country = appContext.getString(R.string.all)
        city = appContext.getString(R.string.all)
        language = appContext.getString(R.string.all)
        price = 0
        type = "Driver"
    }
}
