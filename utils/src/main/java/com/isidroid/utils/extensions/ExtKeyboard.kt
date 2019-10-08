package com.isidroid.utils.extensions

import android.app.Activity
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.math.roundToInt

fun Activity.getRootView(): View = findViewById<View>(android.R.id.content)

fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    val errorValue =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics)

    getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = errorValue.roundToInt()
    return heightDiff > marginOfError
}

fun Activity.isKeyboardClosed() = !this.isKeyboardOpen()

class KeyboardEventListener(
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
    @CallSuper
    fun onLifecyclePause() = unregisterKeyboardListener()

    private fun dispatchKeyboardEvent(isOpen: Boolean) = callback(isOpen)
    private fun registerKeyboardListener() =
        activity.getRootView().viewTreeObserver.addOnGlobalLayoutListener(listener)

    private fun unregisterKeyboardListener() =
        activity.getRootView().viewTreeObserver.removeOnGlobalLayoutListener(listener)
}