package com.isidroid.utils.utils

import android.app.Application
import android.preference.PreferenceManager

const val KEY_VERSION = "version"
const val INSTALL_VERSION = -1

class UpgradeHelper private constructor(application: Application, val version: Int) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(application)
    val isInstalled = prefs.getInt(KEY_VERSION, INSTALL_VERSION) == INSTALL_VERSION
    val isUpgraded = version != prefs.getInt(KEY_VERSION, INSTALL_VERSION) && !isInstalled

    init {
        prefs.edit().putInt(KEY_VERSION, version).apply()
    }

    companion object {
        private var instance: UpgradeHelper? = null

        fun create(app: Application, version: Int): UpgradeHelper {
            return synchronized(this) {
                if (instance == null) instance = UpgradeHelper(app, version)
                instance!!
            }
        }

        fun get(): UpgradeHelper {
            return instance!!
        }
    }
}