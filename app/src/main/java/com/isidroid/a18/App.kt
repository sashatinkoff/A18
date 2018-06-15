package com.isidroid.a18

import android.app.Application
import com.isidroid.a18.di.DaggerNetComponent
import com.isidroid.a18.di.NetComponent
import com.isidroid.a18.di.NetModule
import com.isidroid.utilsmodule.VersionsModule

class App : Application() {
    companion object {
        lateinit var instance: App
        lateinit var netComponent: NetComponent
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        netComponent = DaggerNetComponent.builder()
                .netModule(NetModule("http://ya.ru"))
                .build()
    }

}