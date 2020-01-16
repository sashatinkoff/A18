package com.isidroid.a18

import com.exponea.sdk.Exponea
import com.google.firebase.messaging.FirebaseMessagingService
import com.isidroid.a18.dto.EventRegPushToken
import org.greenrobot.eventbus.EventBus

class MessageService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Exponea.trackPushToken(fcmToken = token)
        EventBus.getDefault().post(EventRegPushToken())
    }
}