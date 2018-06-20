package com.isidroid.a18

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.isidroid.loggermodule.Diagnostics
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    private fun execute() {
        Diagnostics.instance.clearLogs()
        Diagnostics.instance.start()
        Timber.i("Please add something here")
        Diagnostics.instance.stop()

        Diagnostics.instance.start("Some errors")
        Timber.i("started")
    }
}
