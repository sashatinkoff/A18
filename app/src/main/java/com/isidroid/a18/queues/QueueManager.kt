package com.isidroid.a18.queues

import com.isidroid.utils.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class QueueManager<T>(private val compositeDisposable: CompositeDisposable = CompositeDisposable()) {
    private var rxExecution = PublishSubject.create<QueueItem<T>>().toSerialized()
    private val enqueue = mutableListOf<QueueItem<T>>()
    private var maxAttempts = 3
    private var progress = arrayListOf(0, 0)

    private lateinit var onExecute: (T) -> String
    private lateinit var onError: (Throwable) -> Unit
    private lateinit var onProgress: (T, Int, Int) -> Unit

    fun onProgress(value: (item: T, progress: Int, total: Int) -> Unit) = apply { this.onProgress = value }
    fun onExecute(value: (T) -> String) = apply { this.onExecute = value }
    fun onError(value: (Throwable) -> Unit) = apply { this.onError = value }
    fun maxAttempts(value: Int) = apply { this.maxAttempts = value }

    fun create() = apply { subscribeSubject() }
    fun destroy() = apply {
        compositeDisposable.clear()
        enqueue.clear()
        progress[0] = 0
        progress[1] = 0
    }

    private fun subscribeSubject() {
        rxExecution.observeOn(Schedulers.io())
                .doOnNext {
                    var result: String? = null
                    val state = try {
                        result = onExecute(it.data)

                        it.attempts--

                        when {
                            result == RESULT_SUCCESS -> STATE_COMPLETED
                            result == RESULT_FAIL && it.attempts > 0 -> STATE_RETRY
                            else -> STATE_FAILED
                        }
                    } catch (e: Exception) {
                        rxExecution.onNext(it)
                        STATE_FAILED
                    }

                    if (result !in arrayListOf(RESULT_SUCCESS, RESULT_FAIL, RESULT_ABORT))
                        throw Exception("Invalid result, must be one of QueueManager.success(), fail() or abort()")

                    it.state(state)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it.isCompleted()) {
                                enqueue.remove(it)
                                progress[0] = progress[0] + 1
                                onProgress(it.data, progress[0], progress[1])
                            }
                        },
                        { onError.invoke(it) }
                )

                .addTo(compositeDisposable)
    }

    fun add(item: T) = apply {
        val queueItem = QueueItem(item, maxAttempts)
        if (enqueue.none { it.data == item }) {
            enqueue.add(queueItem)
            progress[1]++
        }

        rxExecution.onNext(queueItem)
    }


    fun cancel(item: T) = apply {
        val queueId = enqueue.indexOfFirst { it.data == item }
        if (queueId < 0) return@apply

        rxExecution.onComplete()
        rxExecution = PublishSubject.create<QueueItem<T>>().toSerialized()

        compositeDisposable.clear()
        enqueue.removeAt(queueId)

        val newQueue = mutableListOf<QueueItem<T>>()
        newQueue.addAll(enqueue.filter { it.data != item && !it.isCompleted() })
        enqueue.clear()

        progress[1] = progress[0]
        subscribeSubject()
        newQueue.forEach { add(it.data) }
    }

    companion object {
        const val RESULT_SUCCESS = "RESULT_SUCCESS"
        const val RESULT_FAIL = "RESULT_FAIL"
        const val RESULT_ABORT = "RESULT_ABORT"

        fun success() = RESULT_SUCCESS
        fun fail() = RESULT_FAIL
        fun abort() = RESULT_ABORT
    }
}