package com.isidroid.a18.data

import com.isidroid.utilsmodule.YRealm
import io.realm.Realm
import io.realm.RealmModel
import io.realm.kotlin.deleteFromRealm
import io.realm.kotlin.isManaged

interface DataModel : RealmModel {
    fun save() {
        YRealm.realmExe { it.insertOrUpdate(this) }
    }

    fun delete() {
        YRealm.realmExe {
            var item2 = this
            if (!isManaged()) item2 = Realm.getDefaultInstance().copyToRealmOrUpdate(item2)
            item2.deleteFromRealm()
        }
    }

}


