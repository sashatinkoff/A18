package com.isidroid.a18.data

import io.realm.Realm
import io.realm.RealmModel

interface DataModel : RealmModel {
    fun save() {
        Realm.getDefaultInstance().apply {
            beginTransaction()
            insertOrUpdate(this@DataModel)
            commitTransaction()
        }
    }
}

