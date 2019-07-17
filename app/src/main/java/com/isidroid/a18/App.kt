package com.isidroid.a18

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.isidroid.a18.core.AppInit


class App : Application() {
    companion object {
        lateinit var instance: App
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        instance = this

        AppInit.create(this)
    }
}