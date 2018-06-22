package com.isidroid.a18

import android.content.Intent
import android.os.Bundle
import com.isidroid.loggermodule.Diagnostics
import com.isidroid.utilsmodule.BaseActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Flowable.interval(1, TimeUnit.SECONDS)
                .take(10)
//                .subscribe { Timber.e("Take it easy $it") }

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
            5 -> {
            }
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
                    it?.apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("sashatinkoff@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "Debug info ${BuildConfig.APPLICATION_ID}")
                        type = "message/rfc822"

                        startActivity(this)
                    }
                }
    }

}
