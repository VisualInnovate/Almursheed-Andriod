package com.visualinnovate.almursheed.commonView.more

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.TotalEarningResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTicketsViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseViewModel(apiService, application) {

    private val _tickets: MutableLiveData<ResponseHandler<TotalEarningResponse?>> =
        MutableLiveData()
    val tickets: LiveData<ResponseHandler<TotalEarningResponse?>> =
        _tickets.toSingleEvent()

    fun getMyTickets() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getMyTickets(SharedPreference.getUser().id)
            }.collect {
                _tickets.value = it
            }
        }
    }

}
