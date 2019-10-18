package com.isidroid.utils.extensions

import android.view.View
import android.widget.ViewFlipper

fun ViewFlipper.addOnChangeListener(onChange: (Int) -> Unit) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        private var currentDisplayed = -1

        override fun onLayoutChange(
            v: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            if (currentDisplayed != displayedChild) {
                onChange(displayedChild)
                currentDisplayed = displayedChild
            }
        }
    })
}