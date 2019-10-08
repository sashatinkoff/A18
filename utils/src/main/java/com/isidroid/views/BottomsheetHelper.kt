package com.isidroid.views

import android.animation.Animator
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*

open class BottomsheetHelper(
    private val view: View,
    var onSlide: ((BottomsheetHelper, View, Float) -> Unit)? = null,
    var onStateChanged: ((BottomsheetHelper, View, Int) -> Unit)? = null,
    var onCollapsed: ((BottomsheetHelper, View) -> Unit)? = null,
    var onExpanded: ((BottomsheetHelper, View) -> Unit)? = null,
    var onHidden: ((BottomsheetHelper, View) -> Unit)? = null,
    var onHalfExpanded: ((BottomsheetHelper, View) -> Unit)? = null
) {
    lateinit var behavior: BottomSheetBehavior<View>
    internal var dim: Dim? = null

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(view: View, offset: Float) {
            onSlide?.invoke(this@BottomsheetHelper, view, offset)
        }

        override fun onStateChanged(view: View, state: Int) {
            onStateChanged?.invoke(this@BottomsheetHelper, view, state)
            when (state) {
                STATE_COLLAPSED -> {
                    dim?.hide()
                    onCollapsed?.invoke(this@BottomsheetHelper, view)
                }
                STATE_EXPANDED -> {
                    dim?.show()
                    onExpanded?.invoke(this@BottomsheetHelper, view)
                }
                STATE_HIDDEN -> {
                    dim?.hide()
                    onHidden?.invoke(this@BottomsheetHelper, view)
                }
                STATE_HALF_EXPANDED -> {
                    dim?.show()
                    onHalfExpanded?.invoke(this@BottomsheetHelper, view)
                }
                STATE_SETTLING -> {
                }
                STATE_DRAGGING -> {
                }
            }
        }
    }

    fun create() = apply {
        behavior = from(view)
        behavior.bottomSheetCallback = bottomSheetCallback
        ViewCompat.setElevation(view, 255f)
    }

    fun withDim(view: CoordinatorLayout?) = apply { this.dim = Dim(view).attach(this) }

    fun callbacks(
        onSlide: ((BottomsheetHelper, View, Float) -> Unit)? = null,
        onStateChanged: ((BottomsheetHelper, View, Int) -> Unit)? = null,
        onCollapsed: ((BottomsheetHelper, View) -> Unit)? = null,
        onExpanded: ((BottomsheetHelper, View) -> Unit)? = null,
        onHidden: ((BottomsheetHelper, View) -> Unit)? = null,
        onHalfExpanded: ((BottomsheetHelper, View) -> Unit)? = null
    ) = apply {
        this.onSlide = onSlide
        this.onStateChanged = onStateChanged
        this.onCollapsed = onCollapsed
        this.onExpanded = onExpanded
        this.onHidden = onHidden
        this.onHalfExpanded = onHalfExpanded
    }

    fun expand() = apply {
        dim?.show()
        behavior.state = STATE_EXPANDED
    }

    fun hide() = apply {
        dim?.hide()
        behavior.state = STATE_HIDDEN
    }

    fun collapse() = apply {
        dim?.hide()
        behavior.state = STATE_COLLAPSED
    }

    open fun onStateChanged(view: View, state: Int) {}
    fun isExpanded() = behavior.state == STATE_EXPANDED
    fun isCollapsed() = behavior.state == STATE_COLLAPSED
    fun isHidden() = behavior.state == STATE_HIDDEN

    class Dim(private var parent: CoordinatorLayout?) {
        internal var overlay: View? = null
        private var color = Color.BLACK
        private var alpha = .7f
        private var bottom: BottomsheetHelper? = null
        private var duration = 500
        private var interpolator: Interpolator = DecelerateInterpolator()
        private var isActive = false
            set(value) {
                field = value
                onChange()
            }

        fun color(color: Int) = apply { this.color = color }
        fun duration(duration: Int) = apply { this.duration = duration }
        fun alpha(alpha: Float) = apply { this.alpha = alpha }
        fun interpolator(interpolator: Interpolator) = apply { this.interpolator = interpolator }

        internal fun attach(bottomsheetHelper: BottomsheetHelper) =
            apply { this.bottom = bottomsheetHelper }

        fun create() = apply { createOverlay() }

        private fun createOverlay() {
            (parent as? ViewGroup)?.let {
                overlay = LinearLayout(it.context).apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    alpha = 0f
                    visibility = View.GONE
                    setBackgroundColor(color)
                    setOnClickListener {
                        this@Dim.bottom?.collapse()
                    }
                    it.addView(this)
                }
            }
        }

        fun show() = apply { if (!isActive) isActive = true }
        fun hide() = apply { if (isActive) isActive = false }

        fun onChange() {
            val alpha = if (isActive) alpha else 0f
            overlay?.animate()
                ?.setDuration(duration.toLong())
                ?.setInterpolator(interpolator)
                ?.alpha(alpha)
                ?.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        overlay?.visibility = if (isActive) View.VISIBLE else View.GONE
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        overlay?.visibility = View.VISIBLE
                    }

                })
        }
    }
}