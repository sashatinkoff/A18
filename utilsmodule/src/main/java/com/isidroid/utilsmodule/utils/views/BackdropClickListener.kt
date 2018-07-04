package com.isidroid.utilsmodule.utils.views

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Interpolator
import android.widget.ImageView
import androidx.annotation.CallSuper
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.button.MaterialButton
import com.isidroid.utilsmodule.R

open class BackdropClickListener @JvmOverloads constructor(
        private val activity: Activity, private val sheet: View, var interpolator: Interpolator? = null) : View.OnClickListener {

    protected val animatorSet = AnimatorSet()
    protected val height: Int
    protected var backdropShown = false

    private var openIcon: Drawable? = null
    private var closeIcon: Drawable? = null

    var duration = 500L

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    fun animateIcons(openIcon: Int?, closeIcon: Int?) {
        closeIcon?.let { this.openIcon = AnimatedVectorDrawableCompat.create(activity, it) }
        openIcon?.let { this.closeIcon = AnimatedVectorDrawableCompat.create(activity, it) }
    }

    fun icons(openIcon: Int?, closeIcon: Int?) {
        closeIcon?.let { this.openIcon = AnimatedVectorDrawableCompat.create(activity, it) }
        openIcon?.let { this.closeIcon = AnimatedVectorDrawableCompat.create(activity, it) }
    }


    @CallSuper
    override fun onClick(view: View) {
        backdropShown = !backdropShown

        // Cancel the existing animations
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()

        updateIcon(view)

        val translateY = height - activity.resources.getDimensionPixelSize(R.dimen.navigation_reveal_height)

        val animator = ObjectAnimator.ofFloat(sheet, "translationY", (if (backdropShown) translateY else 0).toFloat())
        animator.duration = duration
        if (interpolator != null) animator.interpolator = interpolator
        animatorSet.play(animator)
        animator.start()
    }

    private fun updateIcon(view: View) {
        when (view) {
            is ImageView -> updateImage(view)
            is MaterialButton -> updateButtonIcon(view)
        }
    }

    private fun drawable(): Drawable? {
        return if (backdropShown && closeIcon != null) closeIcon
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
}