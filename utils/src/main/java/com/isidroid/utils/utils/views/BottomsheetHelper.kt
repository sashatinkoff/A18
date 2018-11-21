package com.isidroid.utils.utils.views

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*

open class BottomsheetHelper(private val view: View) {
    lateinit var behavior: BottomSheetBehavior<View>
    private var onSlide: ((BottomsheetHelper, View, Float) -> Unit)? = null
    private var onStateChanged: ((BottomsheetHelper, View, Int) -> Unit)? = null
    private var onCollapsed: ((View, Int) -> Unit)? = null
    private var onExpanded: ((View, Int) -> Unit)? = null
    private var onHidden: ((View, Int) -> Unit)? = null
    private var onHalfExpanded: ((View, Int) -> Unit)? = null

    fun onSlide(callback: (helper: BottomsheetHelper, view: View, offset: Float) -> Unit) = apply { this.onSlide = callback }
    fun onStateChanged(callback: (helper: BottomsheetHelper, view: View, state: Int) -> Unit) = apply { this.onStateChanged = callback }
    fun onCollapsed(callback: (view: View, state: Int) -> Unit) = apply { this.onCollapsed = callback }
    fun onExpanded(callback: (view: View, state: Int) -> Unit) = apply { this.onExpanded = callback }
    fun onHidden(callback: (view: View, state: Int) -> Unit) = apply { this.onHidden = callback }
    fun onHalfExpanded(callback: (view: View, state: Int) -> Unit) = apply { this.onHalfExpanded = callback }

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(view: View, offset: Float) {
            onSlide?.invoke(this@BottomsheetHelper, view, offset)
        }

        override fun onStateChanged(view: View, state: Int) {
            onStateChanged?.invoke(this@BottomsheetHelper, view, state)
            when (state) {
                STATE_COLLAPSED -> onCollapsed?.invoke(view, state)
                STATE_EXPANDED -> onExpanded?.invoke(view, state)
                STATE_HIDDEN -> onHidden?.invoke(view, state)
                STATE_HALF_EXPANDED -> onHalfExpanded?.invoke(view, state)
                else -> {
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
}