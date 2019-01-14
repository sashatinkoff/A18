package com.isidroid.utils.utils.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*

open class BottomsheetHelper(private val view: View) {
    lateinit var behavior: BottomSheetBehavior<View>
    private var onSlide: ((BottomsheetHelper, View, Float) -> Unit)? = null
    private var onStateChanged: ((BottomsheetHelper, View, Int) -> Unit)? = null
    private var onCollapsed: ((BottomsheetHelper, View) -> Unit)? = null
    private var onExpanded: ((BottomsheetHelper, View) -> Unit)? = null
    private var onHidden: ((BottomsheetHelper, View) -> Unit)? = null
    private var onHalfExpanded: ((BottomsheetHelper, View) -> Unit)? = null
    private var dim: Dim? = null

    fun onSlide(callback: (helper: BottomsheetHelper, view: View, offset: Float) -> Unit) = apply { this.onSlide = callback }
    fun onStateChanged(callback: (helper: BottomsheetHelper, view: View, state: Int) -> Unit) = apply { this.onStateChanged = callback }
    fun onCollapsed(callback: (helper: BottomsheetHelper, view: View) -> Unit) = apply { this.onCollapsed = callback }
    fun onExpanded(callback: (helper: BottomsheetHelper, view: View) -> Unit) = apply { this.onExpanded = callback }
    fun onHidden(callback: (helper: BottomsheetHelper, view: View) -> Unit) = apply { this.onHidden = callback }
    fun onHalfExpanded(callback: (helper: BottomsheetHelper, view: View) -> Unit) = apply { this.onHalfExpanded = callback }
    fun withDim(dim: Dim) = apply { this.dim = dim }
    fun withDim(view: View?) = apply { this.dim = Dim(view).create() }

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
                STATE_EXPANDED -> onExpanded?.invoke(this@BottomsheetHelper, view)
                STATE_HIDDEN -> onHidden?.invoke(this@BottomsheetHelper, view)
                STATE_HALF_EXPANDED -> onHalfExpanded?.invoke(this@BottomsheetHelper, view)
                STATE_SETTLING -> dim?.show()
                STATE_DRAGGING -> {
                }
            }
        }
    }

    fun create() = apply {
        behavior = BottomSheetBehavior.from(view)
        behavior.setBottomSheetCallback(bottomSheetCallback)
    }

    open fun onStateChanged(view: View, state: Int) {}

    fun expand() = apply { behavior.state = STATE_EXPANDED }
    fun collapse() = apply { behavior.state = STATE_COLLAPSED }
    fun hide() = apply { behavior.state = STATE_HIDDEN }

    fun isExpanded() = behavior.state == STATE_EXPANDED
    fun isCollapsed() = behavior.state == STATE_COLLAPSED
    fun isHidden() = behavior.state == STATE_HIDDEN

    fun state(state: Int): String {
        return when (state) {
            STATE_COLLAPSED -> "STATE_COLLAPSED"
            STATE_EXPANDED -> "STATE_EXPANDED"
            STATE_HIDDEN -> "STATE_HIDDEN"
            STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
            else -> ""
        }
    }

    class Dim(private var parent: View?) {
        private lateinit var dim: ColorDrawable
        internal var color = Color.BLACK
        private var dimAmount = .7f
        private var isActive = false

        fun color(color: Int) = apply { this.color = color }
        fun dimAmount(dimAmount: Float) = apply { this.dimAmount = dimAmount }

        fun create() = apply {
            parent ?: return@apply
            dim = ColorDrawable(color)
            dim.setBounds(0, 0, parent!!.width, parent!!.height)
            dim.alpha = (255 * dimAmount).toInt()
        }

        fun show() = apply {
            parent ?: return@apply
            if (!isActive) {
                isActive = true
                val overlay = parent!!.overlay
                overlay.add(dim)
            }
        }

        fun hide() = apply {
            parent ?: return@apply
            val overlay = parent!!.overlay
            overlay.clear()
            isActive = false
        }

    }
}