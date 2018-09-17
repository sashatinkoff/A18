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
import timber.log.Timber

const val STATE_EXPANDED = "STATE_EXPANDED"
const val STATE_COLLAPSED = "STATE_COLLAPSED"
const val STATE_TO_COLLAPSE = "STATE_TO_COLLAPSE"
const val STATE_TO_EXPAND = "STATE_TO_EXPAND"
const val STATE_EXPAND_STARTED = "STATE_EXPAND_STARTED"
const val STATE_COLLAPSE_STARTED = "STATE_COLLAPSE_STARTED"
const val STATE_SKIP = "STATE_SKIP"
const val STATE_TO_DESTROY = "STATE_TO_DESTROY"


const val VIEW_TAG = "BackdropView"

class Backdrop2(
        private val frontContainer: View,
        private val backContainer: View) : ViewTreeObserver.OnGlobalLayoutListener {

    private val activity: Activity = frontContainer.context as Activity
    private val height: Int
    private var duration = 500L
    private var interpolator: Interpolator = BounceInterpolator()
    private var isDetroying = false

    private val listeners = mutableListOf<BackdropListener>()
    private var state = STATE_COLLAPSED
        set(value) {
            when (value) {
                STATE_TO_COLLAPSE -> listeners.forEach { it.onCollapse() }
                STATE_TO_EXPAND -> listeners.forEach { it.onExpand() }
                STATE_EXPAND_STARTED -> if (!isExecuting()) listeners.forEach { it.onExpandStarted() }
                STATE_COLLAPSE_STARTED -> if (!isExecuting()) listeners.forEach { it.onCollapseStarted() }
                STATE_EXPANDED -> listeners.forEach { it.onExpandDone() }
                STATE_COLLAPSED -> listeners.forEach { it.onCollapseDone() }
                STATE_TO_DESTROY -> listeners.forEach { it.onDestroyStarted() }
            }
            field = value
        }

    init {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        view(FrameLayout(activity))
    }

    // builders
    fun view(view: View) = apply {
        view.tag = VIEW_TAG

        (backContainer as? ViewGroup)?.apply {
            // let's remove all views then attach only one
            view()?.let { v -> removeView(v) }
            addView(view)
        }
    }

    fun duration(duration: Long) = apply { this.duration = duration }
    fun interpolator(interpolator: Interpolator) = apply { this.interpolator = interpolator }

    fun addListener(listener: BackdropListener) = apply { this.listeners.add(listener) }
    fun clear() = apply {
        state = STATE_SKIP
        val view = FrameLayout(activity)
        view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                state = STATE_COLLAPSED
            }

        })
        view(view)
    }

    fun view(): View? = (backContainer as? ViewGroup)?.findViewWithTag(VIEW_TAG)
    fun isCollapsed() = state == STATE_COLLAPSED
    fun isExpanded() = state == STATE_EXPANDED
    fun isExecuting() = state == STATE_COLLAPSE_STARTED || state == STATE_EXPAND_STARTED || state == STATE_SKIP

    private var decorators = mutableListOf<BackdropDecorator>()
    fun addDecorator(decorator: BackdropDecorator) {
        this.decorators.add(decorator)
        addListener(decorator)
    }

    /**
     * set clear to true if you want to recreate backdrop content
     */
    fun expand(clear: Boolean = false) {
        if (clear) clear()
        backContainer.post {
            if (isCollapsed()) toggle()
        }
    }

    fun collapse() {
        if (isExpanded()) toggle()
    }

    fun toggle() {
        state = when (state) {
            STATE_EXPANDED -> STATE_TO_COLLAPSE
            STATE_COLLAPSED -> STATE_TO_EXPAND
            else -> state
        }

        if (backContainer.height > 0) onGlobalLayout()

        backContainer.viewTreeObserver.apply {
            removeOnGlobalLayoutListener(this@Backdrop2)
            addOnGlobalLayoutListener(this@Backdrop2)
        }
    }

    private fun animate(containerHeight: Int, isAnimate: Boolean, action: (() -> Unit)? = null) {
        state = when (state) {
            STATE_TO_EXPAND -> STATE_EXPAND_STARTED
            STATE_TO_COLLAPSE -> STATE_COLLAPSE_STARTED
            STATE_COLLAPSED -> return
            else -> state
        }

        val animationDuration = if (isAnimate) duration else 0L

        ObjectAnimator.ofFloat(frontContainer, "translationY", translateY(containerHeight)).apply {
            duration = animationDuration
            interpolator = this@Backdrop2.interpolator

            doOnEnd {
                if (frontContainer.translationY != translateY(backContainer.height)) animate(backContainer.height, true)
                else {
                    state = when (state) {
                        STATE_COLLAPSE_STARTED -> STATE_COLLAPSED
                        else -> STATE_EXPANDED
                    }

                    action?.invoke()
                }
            }

            start()
        }
    }

    private fun translateY(containerHeight: Int): Float {
        val translateFullScreen = (height - activity.resources.getDimensionPixelSize(R.dimen.navigation_reveal_height))
        var translateY = containerHeight
        if (translateY == 0 || translateY > translateFullScreen) translateY = translateFullScreen
        return (if (state == STATE_EXPAND_STARTED || state == STATE_EXPANDED) translateY else 0).toFloat()
    }

    fun destroy() {
        isDetroying = true
        state = STATE_TO_DESTROY

        val destroyAction: () -> Unit = {
            (view() as? ViewGroup)?.removeAllViews()
            listeners.forEach { it.onDestroy() }
            listeners.clear()

            decorators.forEach { listeners.add(it) }
            isDetroying = false
        }

        if (!isCollapsed()) {
            state = STATE_TO_COLLAPSE
            animate(0, false, destroyAction)
        } else {
            destroyAction()
        }

    }

    // ViewTreeObserver.OnGlobalLayoutListener
    override fun onGlobalLayout() {
        if (isExecuting() || isDetroying) return
        val backHeight = backContainer.height
        val isAnimate = state == STATE_TO_COLLAPSE || state == STATE_TO_EXPAND

        animate(backHeight, isAnimate)
    }
}