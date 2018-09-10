package com.isidroid.utilsmodule

import android.app.Activity
import android.os.Environment
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.realm.Realm
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

    fun backup(activity: Activity?) {
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