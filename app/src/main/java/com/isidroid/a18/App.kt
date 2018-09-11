package com.isidroid.a18

import android.app.Application
import com.isidroid.a18.core.AppInit


class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        AppInit.create(this)
    }
}