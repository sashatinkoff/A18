package com.isidroid.realm

import android.util.Log
import androidx.annotation.CallSuper
import io.realm.Realm
import io.realm.RealmModel
import io.realm.kotlin.deleteFromRealm
import io.realm.kotlin.isManaged

interface DataModel : RealmModel {
    @CallSuper
    fun save() = apply {
        if (onSave())
            YRealm.realmExeMain {
                Log.e("workflow", "save ${Thread.currentThread().name}")
                it.insertOrUpdate(this)
            }
    }


    @CallSuper
    fun delete() = apply {
        YRealm.realmExeMain {
            if (onDelete()) {
                var item2 = this
                if (!isManaged()) item2 = it.copyToRealmOrUpdate(item2)
                item2.deleteFromRealm()
            }
        }
    }

    @CallSuper
    fun update() = apply {
        val json = YRealm.gson.toJson(arrayListOf((this)))
        YRealm.realmExeMain {
            it.createOrUpdateAllFromJson(javaClass, json)
        }
    }

    fun refresh() = apply { YRealm.refresh() }

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

    /**
     * Executes before updating the object
     * if some fields are null (or empty) in the UPDATING object they won't be updated in the object
     * that is already is database
     * @return if true then the object will be updated
     */
    fun onUpdate(): Boolean {
        return true
    }
}


