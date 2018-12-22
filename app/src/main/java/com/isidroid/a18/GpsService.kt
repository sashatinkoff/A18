package com.isidroid.a18

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

class GpsService : Service() {
    private var gpsClient: GpsClient? = null
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("onStartCommand startId=$startId")
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate")
        gpsClient = GpsClient.connect(this)
                .onLocationChanged { Timber.i("onLocationChanged ${javaClass.simpleName} $it") }
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
        gpsClient?.disconnect()
    }

    companion object {
        fun intent(context: Context) = Intent(context, GpsService::class.java)

        fun start(context: Context) = context.startService(intent(context))
        fun stop(context: Context) = context.stopService(intent(context))
    }
}