package com.isidroid.utilsmodule

import io.realm.Realm

object YRealm {

    fun realmExe(execute: (realm: Realm) -> Unit) {
        Realm.getDefaultInstance().apply {
            val transaction = isInTransaction
            if (!transaction) beginTransaction()
            execute(this)
            if (!transaction) commitTransaction()
        }
    }
}