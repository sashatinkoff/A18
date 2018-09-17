package com.isidroid.a18.backdrop

import androidx.annotation.CallSuper
import java.util.*

abstract class BackdropDecorator : BackdropListener {
    private var guid = UUID.randomUUID().toString()
    protected var isDestroying = false

    fun add(backdrop: Backdrop2) = apply {
        backdrop.addDecorator(this)
        onCreate()
    }

    abstract fun onCreate()

    override fun onCollapse() {}
    override fun onExpand() {}
    override fun onExpandStarted() {}
    override fun onCollapseStarted() {}
    override fun onExpandDone() {}
    override fun onCollapseDone() {}
    @CallSuper
    override fun onDestroy() {
        isDestroying = false
    }

    @CallSuper
    override fun onDestroyStarted() {
        isDestroying = true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BackdropDecorator

        if (guid != other.guid) return false

        return true
    }

    override fun hashCode(): Int {
        return guid.hashCode()
    }


}