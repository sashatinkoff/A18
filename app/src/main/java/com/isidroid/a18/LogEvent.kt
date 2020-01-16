package com.isidroid.a18

import com.exponea.sdk.Exponea
import com.exponea.sdk.models.CustomerIds
import com.exponea.sdk.models.PropertiesList

enum class Event(val api: String) {
    LOGIN("auth_login"),
    PUSH_CLICK("push_click");
}

fun Event.track(properties: HashMap<String, Any>? = null, exponea: Boolean = true) {
    val trackProps = properties ?: hashMapOf()
    if (exponea) {
        when (this) {
            Event.LOGIN -> {
                val customerIds = CustomerIds().withId("phone", trackProps["phone"])

                Exponea.identifyCustomer(
                        customerIds = customerIds,
                        properties = PropertiesList(trackProps)
                )
            }
            else -> Exponea.trackEvent(
                    eventType = api,
                    properties = PropertiesList(trackProps)
            )
        }
    }

}