package com.isidroid.utils.utils.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Interpolator
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.button.MaterialButton
import com.isidroid.utils.R

open class Backdrop(
        private val mainContentView: View,
        private var clickView: View? = null) : BackdropContainerGlobalCallback.OnGlobalLayoutChanged {

    private var interpolator: Interpolator? = null
    private val activity: Activity = mainContentView.context as Activity
    private val globalLayoutListener = BackdropContainerGlobalCallback(this)
    private val height: Int
    private var duration = 500L

    private var openIcon: Drawable? = null
    private var closeIcon: Drawable? = null

    var isShown = false

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    fun duration(duration: Long) = apply { this.duration = duration }
    fun interpolator(interpolator: Interpolator) = apply { this.interpolator = interpolator }
    fun backdrop(view: View? = null) = apply { globalLayoutListener.attach(view) }

    fun icons(openIcon: Int?, closeIcon: Int?) = apply {
        closeIcon?.let { this.openIcon = AnimatedVectorDrawableCompat.create(activity, it) }
        openIcon?.let { this.closeIcon = AnimatedVectorDrawableCompat.create(activity, it) }
    }

    fun show() {
        isShown = true
        globalLayoutListener.show()
    }

    fun hide() {
        isShown = false
        globalLayoutListener.hide()
    }

    fun toggle() {
        isShown = !isShown
//        globalLayoutListener.toggle()
    }

    fun destroy(){
        globalLayoutListener.destroy()
    }

    private fun execute(containerHeight: Int) {
        // Cancel the existing animations
        val animatorSet = AnimatorSet()
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()

        clickView?.let { updateIcon(it) }

        val animator = ObjectAnimator.ofFloat(mainContentView, "translationY", translateY(containerHeight))
        animator.duration = duration
        interpolator?.let { animator.interpolator = it }
        animatorSet.play(animator)
        animator.start()
    }

    private fun translateY(containerHeight: Int): Float {
        val translateFullScreen = (height - activity.resources.getDimensionPixelSize(R.dimen.navigation_reveal_height))
        var translateY = containerHeight
        if (translateY == 0 || translateY > translateFullScreen) translateY = translateFullScreen
        return (if (isShown) translateY else 0).toFloat()
    }

    private fun updateIcon(view: View) {
        when (view) {
            is ImageView -> updateImage(view)
            is MaterialButton -> updateButtonIcon(view)
        }
    }

    private fun drawable(): Drawable? {
        return if (isShown && closeIcon != null) closeIcon
        else if (openIcon != null) openIcon
        else null
    }

    private fun updateImage(view: ImageView) {
        drawable()?.let {
            view.setImageDrawable(it)
            (it as? AnimatedVectorDrawableCompat)?.start()
        }
    }

    private fun updateButtonIcon(button: MaterialButton) {
        drawable()?.let {
            button.icon = it
            (it as? AnimatedVectorDrawableCompat)?.start()
        }
    }

    override fun onChangeForAnimation(height: Int) {
        execute(height)
    }

    override fun onChange(height: Int) {
        mainContentView.translationY = translateY(height)
    }
}