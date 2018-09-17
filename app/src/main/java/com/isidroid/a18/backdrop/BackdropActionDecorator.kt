package com.isidroid.a18.backdrop

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class BackdropActionDecorator(private val view: View?) : BackdropDecorator() {
    private val activity: Activity = view?.context as Activity

    private lateinit var expandIcon: Drawable
    private lateinit var collapseIcon: Drawable
    private var isAnimateSupport: Boolean = false

    fun withAnimation() = apply { this.isAnimateSupport = true }

    fun icons(expandIcon: Int, collapseIcon: Int) = apply {
        if (isAnimateSupport) {
            this.expandIcon = AnimatedVectorDrawableCompat.create(activity, expandIcon) as Drawable
            this.collapseIcon = AnimatedVectorDrawableCompat.create(activity, collapseIcon) as Drawable
        } else {
            this.expandIcon = ContextCompat.getDrawable(activity, expandIcon)!!
            this.collapseIcon = ContextCompat.getDrawable(activity, collapseIcon)!!
        }
    }

    override fun onCreate() {
        updateIcon(true, false)
    }

    private fun updateIcon(isCollapsed: Boolean, animate: Boolean) {
        when (view) {
            is ImageView -> updateImage(view, isCollapsed, animate)
            is MaterialButton -> updateButtonIcon(view, isCollapsed, animate)
        }
    }

    private fun updateImage(view: ImageView, isCollapsed: Boolean, animate: Boolean) {
        val icon = icon(isCollapsed, animate)
        view.setImageDrawable(icon)
        animate(icon, animate)
    }

    private fun updateButtonIcon(button: MaterialButton, isCollapsed: Boolean, animate: Boolean) {
        button.icon = icon(isCollapsed, animate)
        animate(button.icon, animate)
    }

    private fun animate(icon: Drawable, animate: Boolean) {
        if (animate && isAnimateSupport)
            (icon as? AnimatedVectorDrawableCompat)?.start()
    }

    private fun icon(isCollapsed: Boolean, animate: Boolean): Drawable {
        return if (isCollapsed && animate && isAnimateSupport) collapseIcon
        else if (!isCollapsed && animate && isAnimateSupport) expandIcon
        else if (isCollapsed) expandIcon
        else collapseIcon
    }

    override fun onCollapse() {
        if (!isDestroying)
            updateIcon(true, true)
    }

    override fun onExpand() {
        if (!isDestroying)
            updateIcon(false, true)
    }

    override fun onDestroyStarted() {
        super.onDestroyStarted()
        updateIcon(true, false)
    }
}