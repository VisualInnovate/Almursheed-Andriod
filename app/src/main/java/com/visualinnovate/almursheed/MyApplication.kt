package com.visualinnovate.almursheed

import android.app.Application
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Hawk as like shared preference
        Hawk.init(applicationContext).build()
    }
}
