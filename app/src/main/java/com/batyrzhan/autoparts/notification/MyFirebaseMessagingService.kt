package com.batyrzhan.autoparts.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.batyrzhan.autoparts.MainActivity
import com.batyrzhan.autoparts.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "E-Commerce Android App"

    private val NOTIFICATION_CHANNEL_ID = "ecommerce_channel_01"



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //It is optional
        Log.e(TAG, "From: " + remoteMessage.from)
        Log.e(
            TAG,
            "Notification Message Body: " + remoteMessage.notification?.body
        )
        //Calling method to generate notification

        remoteMessage.notification?.title?.let {
            remoteMessage.notification?.body?.let { it1 ->
                sendNotification(
                    it,
                    it1
                )
            }
        }

    }

    //This method is only generating push notification
    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

}