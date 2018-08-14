package com.isidroid.a18

import android.app.Activity
import android.app.Application
import com.isidroid.a18.core.di.AppComponent
import com.isidroid.a18.core.di.DaggerAppComponent
import com.isidroid.loggermodule.Diagnostics
import com.isidroid.utilsmodule.di.AppModule
import com.isidroid.utilsmodule.di.RealmModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class App : Application(), HasActivityInjector {
    companion object {
        lateinit var instance: App
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        component = DaggerAppComponent.builder()
                .appModule(AppModule(this, BuildConfig.VERSION_CODE))
                .realmModule(RealmModule(this).apply {
                    version = 1L
                    migration = null
                })
                .application(this)
                .build()
                .apply { inject(this@App) }



        Diagnostics.create(this).apply {
            authority = "${BuildConfig.APPLICATION_ID}.fileprovider"
        }

        NotificationsChannels()
    }


    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}