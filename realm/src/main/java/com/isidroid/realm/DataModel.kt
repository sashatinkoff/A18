package com.isidroid.realm

import androidx.annotation.CallSuper
import io.realm.Realm
import io.realm.RealmModel
import io.realm.kotlin.deleteFromRealm
import io.realm.kotlin.isManaged

interface DataModel : RealmModel {
    @CallSuper
    fun save() {
        if (onSave())
            YRealm.realmExeMain { it.insertOrUpdate(this) }
    }


    @CallSuper
    fun delete() {
        YRealm.realmExeMain {
            if (onDelete()) {
                var item2 = this
                if (!isManaged()) item2 = Realm.getDefaultInstance().copyToRealmOrUpdate(item2)
                item2.deleteFromRealm()
            }
        }
    }

    /**
     * Executes before saving the object
     * @return if true then the object will be saved
     */
    fun onSave(): Boolean {
        return true
    }

    /**
     * Executes before deleting the object
     * @return if true then the object will be deleted
     */
    fun onDelete(): Boolean {
        return true
    }
}


