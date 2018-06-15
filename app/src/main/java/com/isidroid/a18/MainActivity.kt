package com.isidroid.a18

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.isidroid.utilsmodule.Diagnostics
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {
    //    @Inject lateinit var prefs: SharedPreferences
    @Inject lateinit var gson: Gson
    @field:[Inject Named("version")] lateinit var version: String
    @field:[Inject Named("name")] lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        App.netComponent.inject(this)


        var message = mutableListOf(version, name)
        var json = gson.toJson(message)
        Diagnostics.i("$json")
    }
}
