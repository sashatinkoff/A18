package com.isidroid.views.backdrop

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.button.MaterialButton

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
        updateIcon(true)
    }

    private fun updateIcon(isCollapsed: Boolean) {
        when (view) {
            is ImageView -> updateImage(view, isCollapsed)
            is MaterialButton -> updateButtonIcon(view, isCollapsed)
        }
    }

    private fun updateImage(view: ImageView, isCollapsed: Boolean) {
        val icon = icon(isCollapsed)
        view.setImageDrawable(icon)
        animate(icon)
    }

    private fun updateButtonIcon(button: MaterialButton, isCollapsed: Boolean) {
        button.icon = icon(isCollapsed)
        animate(button.icon)
    }

    private fun animate(icon: Drawable) {
        if (isAnimateSupport)
            (icon as? AnimatedVectorDrawableCompat)?.start()
    }

    private fun icon(isCollapsed: Boolean): Drawable {
        return if (isCollapsed && isAnimateSupport) collapseIcon
        else if (!isCollapsed && isAnimateSupport) expandIcon
        else if (isCollapsed) expandIcon
        else collapseIcon
    }

    override fun onCollapse() {
        updateIcon(true)
    }

    override fun onExpand() {
        updateIcon(false)
    }

}