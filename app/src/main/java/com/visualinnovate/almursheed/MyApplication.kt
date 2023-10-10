package com.visualinnovate.almursheed

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.pusher.pushnotifications.PushNotifications
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Hawk as like shared preference
        Hawk.init(applicationContext).build()

        // Initialize PushNotification
        // PushNotifications.start(applicationContext, "140343aa-f173-4a2d-940a-7724c7c12be1");
    }
}
