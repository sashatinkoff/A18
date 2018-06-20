package com.isidroid.a18

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.isidroid.loggermodule.Diagnostics
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        execute()
    }

    private fun execute() {
        Timber.i("execute")

        Diagnostics.get.start()
        Timber.i("1")
        Diagnostics.get.stop()


    }

}
