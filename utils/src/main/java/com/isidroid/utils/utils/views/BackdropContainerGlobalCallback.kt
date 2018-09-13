package com.isidroid.utils.utils.views

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

internal class BackdropContainerGlobalCallback(val listener: OnGlobalLayoutChanged) : ViewTreeObserver.OnGlobalLayoutListener {
    private var view: ViewGroup? = null
    private var isAnimating = false
    private var height = -1


    fun attach(view: View?) = apply { this.view = view as? ViewGroup }

    override fun onGlobalLayout() {
        view?.let {
            val height = YViewUtils.height(it, true)

            if (this.height != height) {
                if (isAnimating) listener.onChangeForAnimation(height)
                else listener.onChange(height)
            }

            this.height = height
            isAnimating = false
        }
    }


    fun show() = apply {
        view ?: throw Exception("View is not attached to backdrop")

        isAnimating = true
        height = -1

        view?.viewTreeObserver?.apply {
            removeOnGlobalLayoutListener(this@BackdropContainerGlobalCallback)
            addOnGlobalLayoutListener(this@BackdropContainerGlobalCallback)
        }
    }

    fun hide() {
        view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
        listener.onChangeForAnimation(0)
    }

    fun destroy() {
        height = -1
        listener.onChangeForAnimation(0)

        view?.removeAllViews()
        view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
    }

    interface OnGlobalLayoutChanged {
        fun onChangeForAnimation(height: Int)
        fun onChange(height: Int)
    }
}