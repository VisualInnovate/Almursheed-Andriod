package com.visualinnovate.almursheed.driver.filter.viewModel

import android.app.Application
import com.visualinnovate.almursheed.network.BaseApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    application: Application,
) : BaseApiResponse(application) {

    var searchData: String? = ""
    var carCategory: String? = null
    var carModel: String? = null
    var rate: String? = null
    var price: Int = 0
    var type = "Driver"


    fun clearDataOnInit(){
        searchData = ""
        carCategory = ""
        carModel = ""
        rate = ""
        price = 0
        type = "Driver"
    }

}
