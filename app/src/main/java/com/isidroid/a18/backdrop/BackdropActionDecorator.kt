package com.isidroid.a18.backdrop

import android.app.Activity
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.button.MaterialButton

class BackdropActionDecorator(private val view: View, private val backdrop: Backdrop2) {
    private val activity: Activity = view.context as Activity

    private var expandIcon: Drawable? = null
    private var collapseIcon: Drawable? = null


    fun icons(expandIcon: Int?, collapseIcon: Int?) = apply {
        collapseIcon?.let { this.expandIcon = AnimatedVectorDrawableCompat.create(activity, it) }
        expandIcon?.let { this.collapseIcon = AnimatedVectorDrawableCompat.create(activity, it) }
    }

    fun create() = apply {
        updateIcon(true, false)

        backdrop.onCollapse { updateIcon(false, true) }
        backdrop.onExpand { updateIcon(true, true) }
    }


    private fun updateIcon(isCollapsed: Boolean, animate: Boolean) {
        when (view) {
            is ImageView -> updateImage(view, isCollapsed, animate)
            is MaterialButton -> updateButtonIcon(view, isCollapsed, animate)
        }
    }

    private fun drawable(isCollapsed: Boolean): Drawable? {
        return if (isCollapsed && collapseIcon != null) collapseIcon
        else if (expandIcon != null) expandIcon
        else null
    }

    private fun updateImage(view: ImageView, isCollapsed: Boolean, animate: Boolean) {
        drawable(isCollapsed)?.let {
            view.setImageDrawable(it)
            if (animate) (it as? AnimatedVectorDrawableCompat)?.start()
        }
    }

    private fun updateButtonIcon(button: MaterialButton, isCollapsed: Boolean, animate: Boolean) {
        drawable(isCollapsed)?.let {
            button.icon = it
            if (animate) (it as? AnimatedVectorDrawableCompat)?.start()
        }
    }

}