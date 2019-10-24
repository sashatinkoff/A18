package com.isidroid.utils.utils

import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.isidroid.utils.extensions.getRootView
import com.isidroid.utils.extensions.isKeyboardOpen

internal class KeyboardEventListener(
    private val lifecycleObserver: LifecycleObserver,
    private val callback: (isOpen: Boolean) -> Unit
) : LifecycleObserver {

    private val activity: AppCompatActivity =
        when (lifecycleObserver) {
            is AppCompatActivity -> lifecycleObserver
            is Fragment -> lifecycleObserver.activity as AppCompatActivity
            else -> throw Exception("A lifecycleObserver is neither Fragment or AppCompatActivity")
        }

    fun getRootView(): View = when (lifecycleObserver) {
        is AppCompatActivity -> lifecycleObserver.getRootView()
        is Fragment -> lifecycleObserver.view!!
        else -> throw Exception("A lifecycleObserver is neither Fragment or AppCompatActivity")
    }

    fun lifecycle() = when (lifecycleObserver) {
        is AppCompatActivity -> lifecycleObserver.lifecycle
        is Fragment -> lifecycleObserver.lifecycle
        else -> throw Exception("A lifecycleObserver is neither Fragment or AppCompatActivity")
    }

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
        lifecycle().addObserver(this)
        registerKeyboardListener()
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    fun onLifecyclePause() = unregisterKeyboardListener()

    @OnLifecycleEvent(value = Lifecycle.Event.ON_RESUME)
    fun onLifecycleResume() = registerKeyboardListener()

    private fun dispatchKeyboardEvent(isOpen: Boolean) = callback(isOpen)
    private fun registerKeyboardListener() =
        getRootView().viewTreeObserver.addOnGlobalLayoutListener(listener)

    private fun unregisterKeyboardListener() =
        getRootView().viewTreeObserver.removeOnGlobalLayoutListener(listener)


}