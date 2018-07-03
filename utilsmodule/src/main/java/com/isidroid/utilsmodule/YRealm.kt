package com.isidroid.utilsmodule

import android.app.Activity
import android.os.Environment
import android.widget.Toast
import io.realm.Realm
import java.io.File

object YRealm {
    fun realmExe(execute: (realm: Realm) -> Unit) {
        Realm.getDefaultInstance().apply {
            val transaction = isInTransaction
            if (!transaction) beginTransaction()
            execute(this)
            if (!transaction) commitTransaction()
        }
    }

    fun backup(activity: Activity?) {
        try {
            val destination = File(Environment.getExternalStorageDirectory(), "default.realm")
            if (destination.exists()) destination.delete()
            Realm.getDefaultInstance().writeCopyTo(destination)

            activity?.let {
                it.runOnUiThread { Toast.makeText(it, "Realm copied", Toast.LENGTH_SHORT).show() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}