package com.isidroid.a18

import android.os.Bundle
import com.exponea.sdk.Exponea
import com.exponea.sdk.models.CustomerIds
import com.exponea.sdk.models.NotificationData
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.a18.dto.EventRegPushToken
import com.isidroid.a18.extensions.alert
import com.isidroid.utils.BindActivity
import com.isidroid.utils.Tasks
import com.isidroid.utils.extensions.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)

        btnCredentials.setOnClickListener {
            hideSoftKeyboard()

            val phone = inputPhone.text.toString()
            if (phone.isEmpty()) {
                alert(message = "Телефон не может быть пустым", positive = "Okay")
                return@setOnClickListener
            }
            info("Отправка данных пользователя Exponea.identifyCustomer")
            Event.LOGIN.track(properties = hashMapOf("phone" to phone))
        }

        btnClickPush.setOnClickListener {
            hideSoftKeyboard()

            info("Отправка обычного ивент нажатия на уведомление Exponea.trackEvent")
            Event.PUSH_CLICK.track()

            info("Отправка специвента нажатия на уведомление Exponea.trackClickedPush(data = NotificationData())")
            Exponea.trackClickedPush(data = NotificationData())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)

        Tasks.io(doWork = { FirebaseInstanceId.getInstance().deleteInstanceId() })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun registerPushToken(event: EventRegPushToken) {
        info("Push token is registered")
    }

    private fun info(message: String) {
        val content = textview.text.toString()
        var text = message
        if (content.isNotEmpty()) text += "\n============="
        text += "\n$content"

        textview.text = text
    }
}