package com.isidroid.a18

import android.os.Bundle
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import com.isidroid.utils.addTo
import com.isidroid.utils.subscribeIoMain
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import io.reactivex.subjects.UnicastSubject
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class MainActivity : BindActivity<ActivityMainBinding>() {
    override val resource = R.layout.activity_main
    private val manager = Manager(CompositeDisposable())
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnSubmit.setOnClickListener {
            counter++
            manager.add(Status(counter))
        }

        btnCancel.setOnClickListener {
            manager.cancel(counter - 1)
        }
    }

    private class Manager(private val compositeDisposable: CompositeDisposable) {
        private var rxExecution = PublishSubject.create<Status>().toSerialized()
        private val enqueue = mutableListOf<Status>()

        init {
            subscribeSubject()
        }

        private fun subscribeSubject() {
            rxExecution.observeOn(Schedulers.io())
                    .doOnNext {
                        try {
                            execute(it)
                        } catch (e: Exception) {
//                            loge("skip ${e.message}")
                            it.retry = true
                            rxExecution.onNext(it)
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it.isComplete) enqueue.remove(it)
                        log("complete ${it.uid}, enqueue=${enqueue.size}")
                    }
                    .addTo(compositeDisposable)
        }

        fun execute(status: Status) {
//            log("execute ${status.uid}")
//            if (status.uid == 2 && !status.retry) throw Exception("Failed to execute it")

            if (!status.isComplete)
                (0..10_000_000).forEach {
                    Random.nextLong(10_000, 100_000)
                    status.result++
                }

            status.isComplete = true
        }

        fun add(status: Status) {
            if (!enqueue.contains(status)) enqueue.add(status)
            rxExecution.onNext(status)
        }

        fun cancel(id: Int) {
            var message = "cancel $id, enqueue.before=${enqueue.size}, disposable=${compositeDisposable.size()}"

            enqueue.firstOrNull { it.uid == id }?.let { enqueue.remove(it) }
            compositeDisposable.clear()

            message += ", enqueue.afterDelete=${enqueue.size}, disposable=${compositeDisposable.size()}"

            subscribeSubject()
            enqueue.filter { it.uid != id && !it.isComplete }.forEach { add(it) }

            message += ", enqueue.afterReAdd=${enqueue.size}, disposable=${compositeDisposable.size()}"
            loge("$message")
        }

        private fun log(msg: String?) = Timber.tag("decoupletest").i("$msg on ${Thread.currentThread().name}")
        private fun loge(msg: String?) = Timber.tag("decoupletest").e("$msg on  ${Thread.currentThread().name}")

    }

    private class Status(val uid: Int, var isComplete: Boolean = false) {
        var result = 0L
        var retry = false

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Status
            return (uid == other.uid)
        }

        override fun hashCode() = uid.hashCode()
        override fun toString() = "Status('$uid', $isComplete)"
    }
}