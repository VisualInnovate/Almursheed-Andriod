package com.visualinnovate.almursheed.commonView.notification.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.commonView.notification.model.NotificationResponse
import com.visualinnovate.almursheed.home.model.FlightResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application
) : BaseApiResponse(application) {

    private val _getUnreadNotificationsMutableData: MutableLiveData<ResponseHandler<NotificationResponse?>> =
        MutableLiveData()
    val getUnreadNotificationLiveData: LiveData<ResponseHandler<NotificationResponse?>> =
        _getUnreadNotificationsMutableData.toSingleEvent()

    private val _getMarkAsReadNotificationMutableData: MutableLiveData<ResponseHandler<NotificationResponse?>> =
        MutableLiveData()
    val getMarkAsReadNotificationLiveData: LiveData<ResponseHandler<NotificationResponse?>> =
        _getMarkAsReadNotificationMutableData.toSingleEvent()


    fun getUnreadNotificationsResponse() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getUnreadNotifications()
            }.asLiveData().observeForever {
                _getUnreadNotificationsMutableData.value = it
            }
        }
    }

    fun getMarkAsReadNotificationResponse() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getMarkAsReadNotifications()
            }.asLiveData().observeForever {
                _getMarkAsReadNotificationMutableData.value = it
            }
        }
    }
}
