package com.isidroid.utilsmodule.di

import android.app.Application
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration
import javax.inject.Singleton

@Module
class RealmModule(application: Application) {
    var migration: RealmMigration? = null
    var version = 1L

    init {
        Realm.init(application)
        val config = RealmConfiguration.Builder()
                .name("default.realm")
                .schemaVersion(version)


        if (migration != null) config.migration(migration!!)
        else config.deleteRealmIfMigrationNeeded()

        Realm.setDefaultConfiguration(config.build())
    }

    @Singleton @Provides
    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }
}