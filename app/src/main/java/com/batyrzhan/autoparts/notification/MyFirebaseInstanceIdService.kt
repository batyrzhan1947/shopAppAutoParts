package com.batyrzhan.autoparts.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseInstanceIdService : FirebaseMessagingService() {

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.e("NEW_TOKEN", s)
    }
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }

}