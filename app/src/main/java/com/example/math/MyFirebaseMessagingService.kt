package com.example.math

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        //MyToast.showMessage(applicationContext,"push")
        // Check if message contains a data payload.
//        Log.i("MyPush", "push")
        if (remoteMessage.getData().size > 0) {

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //Log.i("MyPush", "if: ")
            } else {
                // Handle message within 10 seconds
                //Log.i("MyPush", "else: ")
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        var memory = SharedPreferencesHandler(null)
//        memory.saveFireBaseToken(token)

    }

}