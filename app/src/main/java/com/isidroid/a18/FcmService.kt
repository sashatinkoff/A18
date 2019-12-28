package com.isidroid.a18

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.isidroid.a18.core.NotificationsChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import java.net.URL

class FcmService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Timber.i("onNewToken token=$token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let { notification ->
            sendNotification(
                title = notification.title.orEmpty(),
                body = notification.body.orEmpty(),
                imageUrl = notification.imageUrl
            )
        }
    }
}

fun Context.sendNotification(
    title: String = "",
    body: String = "",
    url: String? = null,
    imageUrl: Uri? = null
) {
    val intent = if (url != null) android.content.Intent(
        android.content.Intent.ACTION_VIEW,
        Uri.parse(url)
    )
    else android.content.Intent(this, MainActivity::class.java)
        .addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP)

    val pendingIntent = android.app.PendingIntent.getActivity(
        this,
        101,
        intent,
        android.app.PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(this, NotificationsChannels.DEFAULT_CHANNEL)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(title)
        .setStyle(NotificationCompat.BigTextStyle().bigText(body))
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)


    imageUrl?.let {
        try {


            runBlocking {
                withContext(Dispatchers.IO) {
                    val input = URL(it.toString()).openStream()
                    BitmapFactory.decodeStream(input)
                        ?.let { bitmap ->
                            builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(body))
                        }
                    input.close()
                }
            }
        } catch (e: Exception) {
        }
    }

    NotificationManagerCompat.from(this).notify(101, builder.build())
}
