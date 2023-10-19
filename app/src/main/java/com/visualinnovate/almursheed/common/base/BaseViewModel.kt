package com.visualinnovate.almursheed.common.base

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.home.model.FavoriteResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application
) : BaseApiResponse(application) {

    private val _isFavoriteResponse: MutableLiveData<ResponseHandler<FavoriteResponse?>> =
        MutableLiveData()
    val isFavoriteResponse: LiveData<ResponseHandler<FavoriteResponse?>> =
        _isFavoriteResponse.toSingleEvent()

    fun addAndRemoveFavorite(driverAndGuideId: String, type: String) {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.addAndRemoveFavorite(driverAndGuideId, type)
            }.collect {
                _isFavoriteResponse.value = it
            }
        }
    }
}