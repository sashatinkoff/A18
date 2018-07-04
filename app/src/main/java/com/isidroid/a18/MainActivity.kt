package com.isidroid.a18

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list = mutableListOf<Date>()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        arrayListOf(
                "10:56",
                "10:54",
                "10:50",
                "10:48",
                "10:46",
                "10:45",
                "10:40",
                "10:20",
                "10:00"
        ).forEach {
            val time = "2018-07-04 $it"
            list.add(formatter.parse(time))
        }


        val timeout = 10L // in minutes
        val deadline = Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(timeout))
        list.filter { it.after(deadline) }
                .forEach { Timber.i("after is $it") }


    }


}
