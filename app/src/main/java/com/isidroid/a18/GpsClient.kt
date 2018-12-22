package com.isidroid.a18

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*

@SuppressLint("MissingPermission")
class GpsClient private constructor(private val gpsClient: FusedLocationProviderClient) {
    private var lastLocation: Location? = null
    private var onLocationChanged: ((GpsLocation) -> Unit)? = null

    fun onLocationChanged(action: (GpsLocation) -> Unit) = apply { onLocationChanged = action }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations)
                location?.let { onLocationChanged(it) }
        }
    }

    init {
        gpsClient.lastLocation?.addOnSuccessListener { location ->
            location?.let { onLocationChanged(it) }
        }

        val locationRequest = LocationRequest.create().apply {
            this.interval = GPS_INTERVAL
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        gpsClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    fun onLocationChanged(location: Location) {
        val distance = lastLocation?.distanceTo(location)
        val isChanged = distance == null || distance > 0.0f

        if (isChanged) {
            lastLocation = location
            onLocationChanged?.invoke(GpsLocation(location.latitude, location.longitude, location.speed, distance
                    ?: 0f))
        }
    }

    fun disconnect() = gpsClient.removeLocationUpdates(locationCallback)

    companion object {
        private const val GPS_INTERVAL = 10_000L
        fun connect(context: Context) = GpsClient(LocationServices.getFusedLocationProviderClient(context))
    }

    data class GpsLocation(val latitude: Double, val longitude: Double, val speed: Float, val distance: Float)
}