package com.isidroid.a18

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import com.isidroid.a18.Db.versionUp
import com.isidroid.realm.YRealm
import com.isidroid.utils.BaseActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.io.File
import android.app.AlarmManager
import android.content.Context.ALARM_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val targetFile = File(Realm.getDefaultInstance().configuration.realmDirectory, Realm.getDefaultInstance().configuration.realmFileName)
        Timber.e("onCreate ${targetFile.length()}, version=${Realm.getDefaultInstance().configuration.schemaVersion}")

        setContentView(R.layout.activity_main)

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(CompositePermissionListener())
                .check()

        btnRestore.setOnClickListener { restore() }
        btnBackup.setOnClickListener { YRealm.backup(this) }
        btnRestart.setOnClickListener {
            versionUp()

            finish()
        }
    }

    fun restore() {
        val dbname = Realm.getDefaultInstance().configuration.realmFileName
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val restoredFile = File(directory, dbname)
        val result = restore(restoredFile)

        Timber.i("filesize.after=${result?.length()}, version=${Realm.getDefaultInstance().configuration.schemaVersion}")
    }

    private fun restore(restoredFile: File): File? {
        val dbname = Realm.getDefaultInstance().configuration.realmFileName
        val targetFile = File(Realm.getDefaultInstance().configuration.realmDirectory, dbname)

        Timber.i("filesize.before=${targetFile.length()}")

        return restoredFile.copyTo(targetFile, true)
    }
}

object Db {
    fun prefs() = PreferenceManager.getDefaultSharedPreferences(App.instance)
    fun version() = prefs().getLong("version", 200L)
    fun versionUp() = prefs().edit().putLong("version", version() + 1).apply()
}
