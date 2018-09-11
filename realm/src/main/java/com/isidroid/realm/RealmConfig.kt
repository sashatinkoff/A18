package com.isidroid.realm

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration

class RealmConfig(private val application: Application) {
    private var migration: RealmMigration? = null
    private var version = -1L
    private var dbname = "default.realm"

    fun migration(migration: RealmMigration?) = apply { this.migration = migration }
    fun version(version: Long) = apply { this.version = version }
    fun name(dbname: String) = apply { this.dbname = dbname }

    fun create() {
        if (version == -1L) throw Exception("Version is not set")

        Realm.init(application)
        val config = RealmConfiguration.Builder()
                .name(dbname)
                .schemaVersion(version)


        if (migration != null) config.migration(migration!!)
        else config.deleteRealmIfMigrationNeeded()

        Realm.setDefaultConfiguration(config.build())
    }
}