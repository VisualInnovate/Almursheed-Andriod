package com.visualinnovate.almursheed.common.permission

import android.Manifest.permission.*
import android.os.Build
import androidx.annotation.RequiresApi

sealed class Permission(vararg val permissions: String) {

    object Camera : Permission(CAMERA)
    object Location : Permission(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    object Storage : Permission(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object Storage13 : Permission(READ_MEDIA_IMAGES)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    object Notification : Permission(POST_NOTIFICATIONS)
    object CallPhone : Permission(CALL_PHONE)

    companion object {
        fun from(permission: String) = when (permission) {
            ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> Location
            WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE -> Storage
            CAMERA -> Camera
            POST_NOTIFICATIONS -> Notification
            READ_MEDIA_IMAGES -> Storage13
            else -> throw IllegalArgumentException("Unknown permission: $permission")
        }
    }
}
