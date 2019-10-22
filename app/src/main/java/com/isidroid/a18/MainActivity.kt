package com.isidroid.a18

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.perms.askPermission
import com.isidroid.utils.BindActivity
import timber.log.Timber

class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    private val viewmodel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val locationHelper = LocationHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        askPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
            locationHelper.start(
                onLocation = { Timber.tag("check_locations").i("location=$it") },
                onError = { Timber.tag("check_locations").e(it.message) }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            locationHelper.codeResolution -> locationHelper.onResult(resultCode)
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}