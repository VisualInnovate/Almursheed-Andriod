package com.visualinnovate.almursheed

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.orhanobut.hawk.Hawk
import com.pusher.pushnotifications.PushNotifications
import com.visualinnovate.almursheed.common.realTime.PusherProvider
import com.visualinnovate.almursheed.common.realTime.RealTimeManager
import com.visualinnovate.almursheed.utils.Constant
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    // Initialize RealTimeManager here
    val realTimeManager: RealTimeManager by lazy {
        RealTimeManager(PusherProvider("BuildConfig.PUSHER_APP_KEY", "Constant.PUSHER_APP_CLUSTER"))
    }
    override fun onCreate() {
        super.onCreate()

        // Initialize Hawk as like shared preference
        Hawk.init(applicationContext).build()

        // Initialize Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
}
