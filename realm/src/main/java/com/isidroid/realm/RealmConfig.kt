package com.isidroid.realm

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration

class RealmConfig(
    application: Application,
    migration: RealmMigration? = null,
    version: Long = -1L,
    dbname: String = "default.realm"
) {
    init {
        if (version == -1L) throw Exception("Version is not set")

        Realm.init(application)
        val config = RealmConfiguration.Builder()
            .name(dbname)
            .schemaVersion(version)


        if (migration != null) config.migration(migration)
        else config.deleteRealmIfMigrationNeeded()

        Realm.setDefaultConfiguration(config.build())
    }
}