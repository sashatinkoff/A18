package com.isidroid.a18

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.exponea.sdk.Exponea
import com.exponea.sdk.models.ExponeaConfiguration
import com.isidroid.a18.core.AppInit


class App : Application() {
    companion object {
        lateinit var instance: App
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        instance = this

        AppInit.create(this)
        exponea()
    }

    fun exponea() = apply {
        if (!Exponea.isInitialized) {
            Exponea.init(this, ExponeaConfiguration().apply {
                authorization = getString(R.string.exponea_auth)
                projectToken = getString(R.string.exponea_project_token)
                baseURL = getString(R.string.exponea_base_url)
                httpLoggingLevel = ExponeaConfiguration.HttpLoggingLevel.BODY
            })
        }
    }
}