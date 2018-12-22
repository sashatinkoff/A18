package com.isidroid.a18

import android.Manifest
import android.os.Bundle
import com.isidroid.utils.BaseActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(CompositePermissionListener()).check()

        btnRead.setOnClickListener { GpsService.start(this) }
        btnCar.setOnClickListener { GpsService.stop(this) }

        GpsClient.connect(this)
                .onLocationChanged { Timber.i("onLocationChanged ${javaClass.simpleName} $it") }
                .disconnect()
    }
}