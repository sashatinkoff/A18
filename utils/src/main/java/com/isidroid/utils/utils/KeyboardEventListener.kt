package com.isidroid.utils.utils

import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.isidroid.utils.extensions.getRootView
import com.isidroid.utils.extensions.isKeyboardOpen

internal class KeyboardEventListener(
    private val activity: AppCompatActivity,
    private val callback: (isOpen: Boolean) -> Unit
) : LifecycleObserver {

    private val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private var lastState: Boolean = activity.isKeyboardOpen()

        override fun onGlobalLayout() {
            val isOpen = activity.isKeyboardOpen()
            if (isOpen == lastState) {
                return
            } else {
                dispatchKeyboardEvent(isOpen)
                lastState = isOpen
            }
        }
    }

    init {
        activity.lifecycle.addObserver(this)
        registerKeyboardListener()
    }


    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    fun onLifecyclePause() = unregisterKeyboardListener()

    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    fun onLifecycleResume() = registerKeyboardListener()

    private fun dispatchKeyboardEvent(isOpen: Boolean) = callback(isOpen)
    private fun registerKeyboardListener() =
        activity.getRootView().viewTreeObserver.addOnGlobalLayoutListener(listener)

    private fun unregisterKeyboardListener() =
        activity.getRootView().viewTreeObserver.removeOnGlobalLayoutListener(listener)
}