package com.isidroid.a18.data

import io.realm.Realm
import io.realm.RealmModel
import io.realm.kotlin.deleteFromRealm

interface DataModel : RealmModel {
    fun save() {
        realmExe { it.insertOrUpdate(this) }
    }

    fun delete() {
        realmExe { deleteFromRealm() }
    }

    companion object {
        fun realmExe(execute: (realm: Realm) -> Unit) {
            Realm.getDefaultInstance().apply {
                beginTransaction()
                execute(this)
                commitTransaction()
            }
        }
    }
}


