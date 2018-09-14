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
import androidx.core.animation.doOnEnd
import com.isidroid.utils.R
import com.isidroid.utils.utils.views.YViewUtils

const val STATE_EXPANDED = "STATE_EXPANDED"
const val STATE_COLLAPSED = "STATE_COLLAPSED"
const val STATE_TO_COLLAPSE = "STATE_TO_COLLAPSE"
const val STATE_TO_EXPAND = "STATE_TO_EXPAND"
const val STATE_EXPANDING = "STATE_EXPANDING"
const val STATE_COLLAPSING = "STATE_COLLAPSING"

const val VIEW_TAG = "BackdropView"

class Backdrop2(
        private val frontContainer: View,
        private val backContainer: View) : ViewTreeObserver.OnGlobalLayoutListener {

    private var interpolator: Interpolator = BounceInterpolator()
    private val activity: Activity = frontContainer.context as Activity
    private val height: Int
    private var duration = 500L
    private var state = STATE_COLLAPSED

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        withView(FrameLayout(activity))
    }

    // builder
    fun withView(view: View) = apply {
        view.tag = VIEW_TAG

        (backContainer as? ViewGroup)?.apply {
            // let's remove all views then attach only one
            view()?.let { v -> removeView(v) }
            addView(view)
        }
    }

    fun withDuration(duration: Long) = apply { this.duration = duration }
    fun withInterpolator(interpolator: Interpolator) = apply { this.interpolator = interpolator }

    fun view(): View? = (backContainer as? ViewGroup)?.findViewWithTag(VIEW_TAG)

    fun show() {
        if (!isCollapsed()) return
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
            animate(0) {
                (view() as? ViewGroup)?.removeAllViews()
            }
        }
    }

    private fun animate(containerHeight: Int, callback: (() -> Unit)? = null) {
        state = when (state) {
            STATE_TO_EXPAND -> STATE_EXPANDING
            STATE_TO_COLLAPSE -> STATE_COLLAPSING
            else -> state
        }

        ObjectAnimator.ofFloat(frontContainer, "translationY", translateY(containerHeight)).apply {
            duration = this@Backdrop2.duration
            interpolator = this@Backdrop2.interpolator
            doOnEnd {
                state = when (state) {
                    STATE_COLLAPSING -> STATE_COLLAPSED
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
        return (if (state == STATE_EXPANDING || state == STATE_EXPANDED) translateY else 0).toFloat()
    }

    fun isCollapsed() = state == STATE_COLLAPSED
    fun isExpanded() = state == STATE_EXPANDED

    // ViewTreeObserver.OnGlobalLayoutListener
    override fun onGlobalLayout() {
        val backHeight = YViewUtils.height(backContainer, true)

        if (state == STATE_TO_COLLAPSE || state == STATE_TO_EXPAND) {
            animate(backHeight)
        } else if (isExpanded()) {
            frontContainer.translationY = translateY(backHeight)
        }

    }
}