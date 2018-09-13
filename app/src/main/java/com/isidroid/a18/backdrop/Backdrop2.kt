package com.isidroid.a18.backdrop

import android.animation.ObjectAnimator
import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import com.isidroid.utils.R
import com.isidroid.utils.utils.views.YViewUtils

const val STATE_EXPANDED = "STATE_EXPANDED"
const val STATE_COLLAPSED = "STATE_COLLAPSED"
const val STATE_TO_COLLAPSE = "STATE_TO_COLLAPSE"
const val STATE_TO_EXPAND = "STATE_TO_EXPAND"

class Backdrop2(
        private val frontContainer: View,
        private val backContainer: View,
        view: View? = null) : ViewTreeObserver.OnGlobalLayoutListener {

    private var interpolator: Interpolator = BounceInterpolator()
    private val activity: Activity = frontContainer.context as Activity
    private val height: Int
    private var duration = 500L
    private var state = STATE_COLLAPSED
    private var view: View? = null

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        this.view = view ?: FrameLayout(activity)
        (backContainer as? ViewGroup)?.addView(this.view)
    }

    // builder
    fun withInterpolator(interpolator: Interpolator) = apply { this.interpolator = interpolator }

    fun withView(view: View) = apply { this.view = view }
    fun withDuration(duration: Long) = apply { this.duration = duration }


    fun view(): View = view!!

    fun show() {
        if (state != STATE_COLLAPSED) return
        state = STATE_TO_EXPAND

        toggle()
    }

    fun hide() {
        if (state != STATE_EXPANDED) return
        state = STATE_TO_COLLAPSE
        toggle()
    }

    fun toggle() {
        state = when (state) {
            STATE_EXPANDED -> STATE_TO_COLLAPSE
            STATE_COLLAPSED -> STATE_TO_EXPAND
            else -> state
        }

        if (YViewUtils.height(backContainer, true) > 0) onGlobalLayout()

        backContainer.viewTreeObserver.apply {
            removeOnGlobalLayoutListener(this@Backdrop2)
            addOnGlobalLayoutListener(this@Backdrop2)
        }
    }

    fun destroy() {
        if (state != STATE_COLLAPSED) {
            state = STATE_TO_COLLAPSE
            animate(0) { (view as? ViewGroup)?.removeAllViews() }
        }
    }

    private fun animate(containerHeight: Int, callback: (() -> Unit)? = null) {
        ObjectAnimator.ofFloat(frontContainer, "translationY", translateY(containerHeight)).apply {
            duration = this@Backdrop2.duration
            interpolator = this@Backdrop2.interpolator
            doOnEnd {
                state = when (state) {
                    STATE_TO_COLLAPSE -> STATE_COLLAPSED
                    else -> STATE_EXPANDED
                }

                callback?.invoke()
            }
            start()
        }
    }

    private fun translateY(containerHeight: Int): Float {
        val translateFullScreen = (height - activity.resources.getDimensionPixelSize(R.dimen.navigation_reveal_height))
        var translateY = containerHeight
        if (translateY == 0 || translateY > translateFullScreen) translateY = translateFullScreen
        return (if (state == STATE_TO_EXPAND) translateY else 0).toFloat()
    }

    // ViewTreeObserver.OnGlobalLayoutListener
    override fun onGlobalLayout() {
        val backHeight = YViewUtils.height(backContainer, true)
        if (state == STATE_TO_COLLAPSE || state == STATE_TO_EXPAND) animate(backHeight)
    }
}