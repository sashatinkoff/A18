package com.isidroid.utils.extensions

import android.app.Activity
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.isidroid.utils.utils.KeyboardEventListener
import kotlin.math.roundToInt

internal fun Activity.getRootView(): View = findViewById<View>(android.R.id.content)

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
fun AppCompatActivity.onKeyboardVisibility(callback: (isOpen: Boolean) -> Unit) {
    KeyboardEventListener(this, callback)
}

fun Activity.hideSoftKeyboard() {
    if (isFinishing) return
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}


