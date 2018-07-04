package com.isidroid.utilsmodule.utils.views
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
import com.isidroid.utilsmodule.R
import com.isidroid.utilsmodule.utils.YViewUtils

open class BackdropHandler(private val sheet: View,
                           private var clickView: View? = null) {


    var interpolator: Interpolator? = null
    private val activity: Activity = sheet.context as Activity
    private val animatorSet = AnimatorSet()
    private val height: Int
    private var backdropShown = false

    private var openIcon: Drawable? = null
    private var closeIcon: Drawable? = null
    private var backgroundContainerHeight: Int? = null

    var duration = 500L

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    fun backgroundContainerHeight(height: Int? = null) {
        backgroundContainerHeight = height
    }

    fun backgroundContainer(view: View? = null) {
        view?.post { backgroundContainerHeight = YViewUtils.height(view) }
    }

    fun animateIcons(openIcon: Int?, closeIcon: Int? = null) {
        closeIcon?.let { this.openIcon = AnimatedVectorDrawableCompat.create(activity, it) }
        openIcon?.let { this.closeIcon = AnimatedVectorDrawableCompat.create(activity, it) }
    }

    fun icons(openIcon: Int?, closeIcon: Int?) {
        closeIcon?.let { this.openIcon = AnimatedVectorDrawableCompat.create(activity, it) }
        openIcon?.let { this.closeIcon = AnimatedVectorDrawableCompat.create(activity, it) }
    }

    fun isBackdropShown(): Boolean {
        return backdropShown
    }

    fun show() {
        backdropShown = true
        execute()
    }

    fun hide() {
        backdropShown = false
        execute()
    }

    fun toggle() {
        backdropShown = !backdropShown
        execute()
    }

    private fun execute() {
        // Cancel the existing animations
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()

        clickView?.let { updateIcon(it) }


        val translateFullScreen = (height - activity.resources.getDimensionPixelSize(R.dimen.navigation_reveal_height))
        var translateY = backgroundContainerHeight ?: translateFullScreen
        if (translateY == 0 || translateY > translateFullScreen) translateY = translateFullScreen

        val animator = ObjectAnimator.ofFloat(sheet, "translationY", (if (backdropShown) translateY else 0).toFloat())
        animator.duration = duration
        if (interpolator != null) animator.interpolator = interpolator
        animatorSet.play(animator)
        animator.start()
    }


    fun onClick() {
        toggle()
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