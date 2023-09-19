package com.visualinnovate.almursheed.utils

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task

class LocationHelper private constructor(private val context: Context) {

    companion object : SingletonHolder<LocationHelper, Context>(::LocationHelper)

    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private lateinit var locationRequest: LocationRequest
    private var locationCallback: LocationCallback? = null

    init {
        buildLocationRequest()
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 3000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    fun listenLocationUpdate(location: (Location?) -> Unit = {}) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                location.invoke(locationResult.lastLocation)
            }
        }
        subscribeToLocationUpdates()
    }

    fun singleListenLocationUpdate(location: (Location?) -> Unit = {}) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                location.invoke(locationResult.lastLocation)
                stopLocation()
            }
        }
        subscribeToLocationUpdates()
    }

    private fun subscribeToLocationUpdates() {
        try {
            locationCallback?.let {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    it,
                    Looper.getMainLooper(),
                )
            }
        } catch (unlikely: SecurityException) {
            print(unlikely.message)
        }
    }

    fun stopLocation() {
        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
        }
    }

    fun checkLocationState(): Task<LocationSettingsResponse> {
        val builder =
            LocationSettingsRequest.Builder()
                .addLocationRequest(
                    LocationRequest.create().apply {
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    },
                )
        val client = LocationServices.getSettingsClient(context)
        return client.checkLocationSettings(builder.build())
    }
}