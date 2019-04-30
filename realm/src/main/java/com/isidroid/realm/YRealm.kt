package com.isidroid.realm

import android.app.Activity
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.realm.Realm
import io.realm.RealmModel
import java.io.File
import java.lang.reflect.Type


object YRealm {
    private const val GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ"
    val gson = GsonBuilder().setDateFormat(GSON_DATE_FORMAT).create()

    fun get() = Realm.getDefaultInstance()
    fun refresh() = get().apply { refresh() }
    fun toJson(item: Any) = gson.toJson(item)
    fun <T> fromJson(json: String, type: Type): T = gson.fromJson<T>(json, type)
    fun <T> fromJson(json: String, cl: Class<T>): T = gson.fromJson<T>(json, cl)

    fun realmExe(execute: (realm: Realm) -> Unit) {
        get().apply {
            val transaction = isInTransaction
            if (!transaction) beginTransaction()
            execute(this)
            if (!transaction) commitTransaction()
        }
    }

    fun realmExeMain(execute: (realm: Realm) -> Unit) {
        Handler(Looper.getMainLooper()).run { realmExe(execute) }
    }

    fun update(list: List<RealmModel>, gson: Gson? = null) {
        if (list.isEmpty()) return
        val cls = list.first().javaClass
        val gsonHandler = gson ?: GsonBuilder()
            .setDateFormat(GSON_DATE_FORMAT)
            .create()

        val json = gsonHandler.toJson(list)
        realmExe { it.createOrUpdateAllFromJson(cls, json) }
    }

    fun backup(activity: Activity? = null, directory: File = Environment.getExternalStorageDirectory()) = try {
        val destination = File(directory, get().configuration.realmFileName)
        if (destination.exists()) destination.delete()
        get().writeCopyTo(destination)

        activity?.let { a ->
            a.runOnUiThread { Toast.makeText(a, "Realm copied", Toast.LENGTH_SHORT).show() }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    fun restore(directory: File = Environment.getExternalStorageDirectory()) = try {
        val dbname = get().configuration.realmFileName
        val restoredFile = File(directory, dbname)
        val targetFile = File(get().configuration.realmDirectory, dbname)
        restoredFile.copyTo(targetFile, true)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

}