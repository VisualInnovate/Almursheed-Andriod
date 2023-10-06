package com.visualinnovate.almursheed.common.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.RemoteMessage
import com.pusher.pushnotifications.fcm.MessagingService
import com.visualinnovate.almursheed.R
import com.visualinnovate.almursheed.auth.model.User
import com.visualinnovate.almursheed.common.SharedPreference
import com.visualinnovate.almursheed.home.MainActivity
import com.visualinnovate.almursheed.utils.Constant.CHANNEL_ID
import java.util.Random

class MessagingNotificationService : MessagingService() {

    override fun onNewToken(token: String) {
        // Incoming device token from FCM 🔒
        updateToken(token)
    }

    private fun updateToken(token: String) {
        val user: User? = SharedPreference.getUser()
        if (user != null) {
            /*val databaseReference =
                FirebaseDatabase.getInstance().getReference("Users").child(user.userId)
            val map: MutableMap<String, Any> = HashMap()
            map["token"] = token
            databaseReference.updateChildren(map)*/
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // We just got a notification 🔥
        if (remoteMessage.data.isNotEmpty()) {
            val data: Map<String, String> = remoteMessage.data
            val notification: RemoteMessage.Notification? = remoteMessage.notification

            notification?.let {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    createOreoNotification(notification)
                } else {
                    createNormalNotification(notification)
                }
            }
        }
    }

    // create normal notification in case if android less than android(Oreo) [android 8]
    private fun createNormalNotification(notificationData: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(notificationData.title)
            .setContentText(notificationData.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.ic_mursheed_logo)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            //.setColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
            .setColor(ContextCompat.getColor(this, R.color.primary))
            .setColor(resources.getColor(R.color.primary))

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(Random().nextInt(85 - 65), builder.build())
    }

    // create oreo notification in case if android bigger than android(Oreo) [android 8]
    private fun createOreoNotification(notificationData: RemoteMessage.Notification) {

        val channel = NotificationChannel(
            CHANNEL_ID, "Message", NotificationManager.IMPORTANCE_HIGH
        )

        channel.setShowBadge(true)
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(notificationData.title)
            .setContentText(notificationData.body)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_mursheed_logo)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            //.setColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(Random().nextInt(85 - 65), notification)
    }

}