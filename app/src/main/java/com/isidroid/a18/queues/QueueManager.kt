package com.isidroid.a18.queues

import com.isidroid.utils.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.Exception

class QueueManager(private val compositeDisposable: CompositeDisposable = CompositeDisposable()) {
    private var rxExecution = PublishSubject.create<QueueItem>().toSerialized()
    private val enqueue = mutableListOf<QueueItem>()
    private lateinit var executor: QueueuExecutor<QueueItem>

    fun executor(value: QueueuExecutor<QueueItem>) = apply { this.executor = executor }
    fun create() = apply { subscribeSubject() }
    fun destroy() = apply { compositeDisposable.clear() }
    fun add(item: T) = apply {
        if (!enqueue.contains(item)) enqueue.add(item)
        rxExecution.onNext(item)
    }

    private fun subscribeSubject() {
        rxExecution.observeOn(Schedulers.io())
                .doOnNext {
                    val state =
                            try {
                                if (executor.execute(it)) STATE_COMPLETED else STATE_FAILED
                            } catch (e: Exception) {
                                rxExecution.onNext(it)
                                STATE_RETRY
                            }

                    it.state(state)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isCompleted()) enqueue.remove(it)
                }.addTo(compositeDisposable)
    }


    fun cancel(item: T) = apply {
        enqueue.firstOrNull { it == item }?.let { enqueue.remove(it) }
        compositeDisposable.clear()
        subscribeSubject()
        enqueue.filter { it != item && !it.isCompleted() }.forEach { add(it) }
    }


    interface QueueuExecutor<T : QueueItem> {
        fun execute(item: T): Boolean
    }
}