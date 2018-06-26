package com.isidroid.a18

import android.app.Application
import com.isidroid.a18.di.AppComponent
import com.isidroid.a18.di.AppModule
import com.isidroid.a18.di.DaggerAppComponent
import com.isidroid.loggermodule.Diagnostics
import io.realm.Realm
import io.realm.RealmConfiguration

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
}