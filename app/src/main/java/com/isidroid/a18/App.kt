package com.isidroid.a18

import android.app.Activity
import android.app.Application
import com.isidroid.a18.di.AppComponent
import com.isidroid.a18.di.AppModule
import com.isidroid.a18.di.DaggerAppComponent
import com.isidroid.loggermodule.Diagnostics
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import io.realm.Realm
import io.realm.RealmConfiguration
import dagger.android.DispatchingAndroidInjector
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
                .appModule(AppModule(this))
                .application(this)
                .build()
                .apply { inject(this@App) }


        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .name("myrealm.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
//                .modules(MySchemaModule())
//                .migration(MyMigration())
                .build()
        Realm.setDefaultConfiguration(config)

        Diagnostics.create(this).apply {
            authority = "${BuildConfig.APPLICATION_ID}.fileprovider"
        }
    }


    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}