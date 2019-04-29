package com.isidroid.views

import android.view.View
import android.widget.ImageView
import androidx.annotation.CallSuper
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.button.MaterialButton

open class IconAnimator(private val view: View, openIcon: Int, closeIcon: Int) {
    private var isOpen = true

    private var openIcon = AnimatedVectorDrawableCompat.create(view.context, openIcon)
    private var closeIcon= AnimatedVectorDrawableCompat.create(view.context, closeIcon)

    fun toggle() {
        isOpen = !isOpen
        execute()
    }

    fun open() {
        isOpen = true
        execute()
    }

    fun close() {
        isOpen = false
        execute()
    }

    @CallSuper protected open fun execute() {
        val drawable = if (isOpen) openIcon else closeIcon

        drawable?.let {
            val isAvailable = when (view) {
                is ImageView -> {
                    view.setImageDrawable(drawable)
                    true
                }

                is MaterialButton -> {
                    view.icon = drawable
                    true
                }

                else -> false
            }

            if (isAvailable) (it as? AnimatedVectorDrawableCompat)?.start()
        }
    }
}