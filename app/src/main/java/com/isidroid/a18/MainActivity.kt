package com.isidroid.a18

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.isidroid.loggermodule.Diagnostics
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Diagnostics.instance.clearLogs()

        Flowable.interval(1, TimeUnit.SECONDS)
                .take(10)
                .subscribe { Timber.e("Take it easy $it") }

        button.setOnClickListener { execute2() }
    }

    private var position = 0
    private fun execute2() {
        position++
        Timber.i("execute2 w/position=$position")

        when (position) {
            1 -> Diagnostics.instance.start()
            2 -> shareLogs()
            3 -> Diagnostics.instance.cancel()
            4 -> shareLogs()
            5 -> {}
            6 -> Diagnostics.instance.startInfo("Custom logs")
            7 -> Diagnostics.instance.stopInfo("Custom logs")
            8 -> shareLogs()
        }

    }

    private fun shareLogs() {
        Diagnostics.instance.getShareLogsIntent(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    startActivity(it)
                }
    }

}
