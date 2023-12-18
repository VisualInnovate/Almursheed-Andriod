package com.visualinnovate.almursheed.commonView.more.aboutUs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.visualinnovate.almursheed.common.toSingleEvent
import com.visualinnovate.almursheed.commonView.more.aboutUs.model.AboutUsResponse
import com.visualinnovate.almursheed.network.ApiService
import com.visualinnovate.almursheed.network.BaseApiResponse
import com.visualinnovate.almursheed.utils.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutUsViewModel @Inject constructor(
    private val apiService: ApiService,
    application: Application,
) : BaseApiResponse(application) {

    private val _getAboutUsResponse: MutableLiveData<ResponseHandler<AboutUsResponse?>> =
        MutableLiveData()
    val getAboutUsLiveData: LiveData<ResponseHandler<AboutUsResponse?>> =
        _getAboutUsResponse.toSingleEvent()

    fun getAboutUsPage() {
        viewModelScope.launch {
            safeApiCall {
                // Make your API call here using Retrofit service or similar
                apiService.getAboutUsPage()
            }.collect {
                _getAboutUsResponse.value = it
            }
        }
    }
}