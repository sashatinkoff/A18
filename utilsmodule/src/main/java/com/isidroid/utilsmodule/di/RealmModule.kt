package com.isidroid.utilsmodule.di

import android.app.Application
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import javax.inject.Singleton

@Module
class RealmModule(private val application: Application) {
    var migration: RealmMigration? = null
    var version = 1L

    @Singleton @Provides
    fun provideRealm(): Realm {
        Realm.init(application)
        val config = RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(version)


        if (migration != null) config.migration(migration!!)
        else config.deleteRealmIfMigrationNeeded()

        Realm.setDefaultConfiguration(config.build())
        return Realm.getDefaultInstance()
    }
}