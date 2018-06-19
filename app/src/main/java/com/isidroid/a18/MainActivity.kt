package com.isidroid.a18

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.View
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = View(this)
        view.setOnClickListener { }
    }

    private fun mk() {
        try {
            var file = App.component.diagnostics.logcat()
            var authority = "${BuildConfig.APPLICATION_ID}.fileprovider"
            Timber.i("authority=$authority")
            var uri = FileProvider.getUriForFile(this, authority, file!!)

            val intent = Intent(ACTION_SEND)
            intent.putExtra(EXTRA_STREAM, uri)
            intent.type = "text/*";

            Timber.i("file=${file!!.absolutePath}, uri=${Uri.fromFile(file)}")

            startActivity(intent)

        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
