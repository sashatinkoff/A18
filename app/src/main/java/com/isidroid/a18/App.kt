package com.isidroid.a18

import android.app.Application
import com.isidroid.a18.di.AppComponent
import com.isidroid.a18.di.AppModule
import com.isidroid.a18.di.DaggerAppComponent

class App : Application() {
    companion object {
        lateinit var instance: App
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        component = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
                .apply { inject(this@App) }
    }

}