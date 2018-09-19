package com.isidroid.realm

import android.app.Activity
import android.os.Environment
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.realm.Realm
import io.realm.RealmModel
import java.io.File
import java.lang.reflect.Type


object YRealm {
    val gson: Gson by lazy {
        GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
    }

    fun realmExe(execute: (realm: Realm) -> Unit) {
        Realm.getDefaultInstance().apply {
            val transaction = isInTransaction
            if (!transaction) beginTransaction()
            execute(this)
            if (!transaction) commitTransaction()
        }
    }

    fun backup(activity: Activity? = null) {
        try {
            val destination = File(Environment.getExternalStorageDirectory(), "default.realm")
            if (destination.exists()) destination.delete()
            Realm.getDefaultInstance().writeCopyTo(destination)

            activity?.let { a ->
                a.runOnUiThread { Toast.makeText(a, "Realm copied", Toast.LENGTH_SHORT).show() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun update(list: List<RealmModel>, gson: Gson? = null) {
        if (list.isEmpty()) return
        val cls = list.first().javaClass
        val gsonHandler = gson ?: GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create()

        val json = gsonHandler.toJson(list)
        realmExe { it.createOrUpdateAllFromJson(cls, json) }
    }


    fun toJson(item: Any): String {
        return gson.toJson(item)
    }

    fun <T> fromJson(json: String, type: Type): T {
        return gson.fromJson<T>(json, type)
    }

    fun <T> fromJson(json: String, cl: Class<T>): T {
        return gson.fromJson<T>(json, cl)
    }
}