package com.isidroid.utilsmodule

import androidx.annotation.CallSuper
import com.isidroid.utilsmodule.YRealm
import io.realm.Realm
import io.realm.RealmModel
import io.realm.kotlin.deleteFromRealm
import io.realm.kotlin.isManaged

interface DataModel : RealmModel {
    @CallSuper
    fun save() {
        if (onSave())
            YRealm.realmExe { it.insertOrUpdate(this) }
    }


    @CallSuper
    fun delete() {
        YRealm.realmExe {
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


