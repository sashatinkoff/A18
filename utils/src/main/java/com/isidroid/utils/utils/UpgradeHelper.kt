package com.isidroid.utils.utils

import android.app.Application
import android.preference.PreferenceManager

const val KEY_CODE = "current_code"
const val KEY_VERSION = "current_version"

const val KEY_LAST_CODE = "last_code"
const val KEY_LAST_VERSION = "last_version"

const val INSTALL_VERSION = -1

class UpgradeHelper private constructor(application: Application, val code: Int, val name: String) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(application)
    val isInstalled = prefs.getInt(KEY_CODE, INSTALL_VERSION) == INSTALL_VERSION
    val isUpgraded = code != prefs.getInt(KEY_CODE, INSTALL_VERSION) && !isInstalled

    val lastCode: Int by lazy { prefs.getInt(KEY_LAST_CODE, -1) }
    val lastVersion: String by lazy { prefs.getString(KEY_LAST_VERSION, null) }

    init {
        val editor = prefs.edit()

        if (isInstalled) {
            editor.putInt(KEY_CODE, code)
            editor.putString(KEY_VERSION, name)
        } else if (isUpgraded) {
            val oldCode = prefs.getInt(KEY_CODE, -1)
            val oldName = prefs.getString(KEY_VERSION, null)

            editor.putInt(KEY_CODE, code)
            editor.putString(KEY_VERSION, name)

            editor.putInt(KEY_LAST_CODE, oldCode)
            editor.putString(KEY_LAST_VERSION, oldName)
        }

        editor.apply()
    }

    override fun toString(): String {
        return "UpgradeHelper(code=$code, name='$name', " +
                "isInstalled=$isInstalled, isUpgraded=$isUpgraded, " +
                "lastCode=$lastCode, lastVersion=$lastVersion)"
    }

    companion object {
        private var instance: UpgradeHelper? = null

        fun create(app: Application, code: Int, version: String): UpgradeHelper {
            return synchronized(this) {
                if (instance == null) instance = UpgradeHelper(app, code, version)
                instance!!
            }
        }

        fun get(): UpgradeHelper {
            return instance!!
        }
    }


}