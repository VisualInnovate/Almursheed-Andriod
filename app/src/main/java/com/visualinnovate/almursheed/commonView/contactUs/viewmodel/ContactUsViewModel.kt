package com.visualinnovate.almursheed.commonView.contactUs.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.base.BaseViewModel
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.commonView.contactUs.model.MyTicketResponse
import com.visualinnovate.almursheed.commonView.contactUs.model.TicketDetailsResponse
import com.visualinnovate.almursheed.home.model.ContactUsResponse
import com.visualinnovate.almursheed.home.model.UpdateResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseViewModel(apiService, application) {

    private val _contactUsMutableData: MutableLiveData<ResponseHandler<ContactUsResponse?>> =
        MutableLiveData()
    val contactUsLiveData: LiveData<ResponseHandler<ContactUsResponse?>> =
        _contactUsMutableData.toSingleEvent()

    private val _tickets: MutableLiveData<ResponseHandler<MyTicketResponse?>> =
        MutableLiveData()
    val tickets: LiveData<ResponseHandler<MyTicketResponse?>> =
        _tickets.toSingleEvent()

    private val _getTicketDetails: MutableLiveData<ResponseHandler<TicketDetailsResponse?>> =
        MutableLiveData()
    val getTicketDetails: LiveData<ResponseHandler<TicketDetailsResponse?>> =
        _getTicketDetails.toSingleEvent()


    fun sendToContactUs(subject: String?, type: String?, priority: String?, message: String?) {
        viewModelScope.launch {
            safeApiCall {
                apiService.sendToContactUs(subject, type, priority, message)
            }.collect {
                _contactUsMutableData.value = it
            }
        }
    }

    fun getMyTickets() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getMyTickets() // SharedPreference.getUser().id
            }.collect {
                _tickets.value = it
            }
        }
    }

    fun getTicketDetailsById(ticketId: Int?) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getTicketDetailsById(ticketId) // SharedPreference.getUser().id
            }.collect {
                _getTicketDetails.value = it
            }
        }
    }
}