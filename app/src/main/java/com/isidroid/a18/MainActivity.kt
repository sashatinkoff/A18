package com.isidroid.a18

import android.Manifest
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import com.isidroid.utils.BaseActivity
import timber.log.Timber
import android.net.NetworkRequest
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.CompositePermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnStart.setOnClickListener { startService(Intent(this, YService::class.java)) }
        btnStop.setOnClickListener {
            startService(Intent(this, YService::class.java)
                    .setAction("STOP"))
        }
    }


}

class YService : Service() {
    lateinit var gpsClient: FusedLocationProviderClient

    val locationRequest = LocationRequest.create().apply {
        interval = (1000 * 3 * 1)
        fastestInterval = interval / 2
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            Timber.e("onLocationResult $locationResult")
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()

        gpsClient = LocationServices.getFusedLocationProviderClient(this)
        gpsClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP") stopSelf()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        gpsClient.removeLocationUpdates(locationCallback)
        Timber.e("onDestroy")
    }
}