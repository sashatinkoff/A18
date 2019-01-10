package com.isidroid.a18.queues

import java.util.*

internal const val STATE_CREATED = "STATE_CREATED"
internal const val STATE_COMPLETED = "STATE_COMPLETED"
internal const val STATE_RETRY = "STATE_RETRY"
internal const val STATE_FAILED = "STATE_FAILED"

open class QueueItem<T>(var data: T, var attempts: Int) {
    val uid = UUID.randomUUID().toString().substring(0, 5)
    var createdAt = Date()
    var updatedAt: Date? = null
    var state = STATE_CREATED

    fun state(state: String) = apply {
        updatedAt = Date()
        this.state = state
    }

    fun isCreated() = state == STATE_CREATED
    fun isCompleted() = state == STATE_COMPLETED

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as QueueItem<*>
        return uid == other.uid
    }

    override fun hashCode() = uid.hashCode()

}