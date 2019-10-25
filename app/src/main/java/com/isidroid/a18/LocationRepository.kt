package com.isidroid.a18

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.isidroid.a18.R
import timber.log.Timber

@SuppressLint("MissingPermission")
class LocationRepository(private val activity: Activity) {
    private val client by lazy { LocationServices.getFusedLocationProviderClient(activity) }
    private val settingsClient by lazy { LocationServices.getSettingsClient(activity) }
    private val locationInterval = 2_000L
    private val locationIntervalLimit = 10_000L
    private val handler = Handler()
    private val runOnNoLocation =
        Runnable { onError?.invoke(NoLocationFoundException("")) }

    lateinit var lastLocation: Location

    private var onLocation: ((Location) -> Unit)? = null
    private var onError: ((Throwable) -> Unit)? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result?.lastLocation?.let { location(it) }
        }

        override fun onLocationAvailability(result: LocationAvailability?) {
            if (result?.isLocationAvailable != true) {
                onError?.invoke(LocationNotAvailableException())
                legacyLocationManager()
            }
        }
    }

    var locationRequest = LocationRequest().apply {
        interval = locationInterval
        fastestInterval = locationInterval
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        numUpdates = 1
    }

    private val settingsRequest: LocationSettingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)
        .build()

    private fun legacyLocationManager() {
        val locationManager =
            activity.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let { location(location) }
                locationManager.removeUpdates(this)
            }

            override fun onProviderDisabled(provider: String?) {
                onError?.invoke(ProviderDisableException(provider))
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String?) {}
        }

        locationManager.allProviders
            .filter { locationManager.isProviderEnabled(it) }
            .forEach {
                locationManager.requestLocationUpdates(
                    it,
                    locationInterval,
                    0f,
                    listener,
                    Looper.myLooper()
                )
            }

        handler.postDelayed(runOnNoLocation, locationIntervalLimit)
    }

    fun start(
        onLocation: ((Location) -> Unit),
        onError: ((Throwable) -> Unit),
        useLast: Boolean = true
    ) {
        this.onLocation = onLocation
        this.onError = onError

        settingsClient.checkLocationSettings(settingsRequest)
            .addOnSuccessListener { location(useLast) }
            .addOnFailureListener { e ->
                when ((e as? ApiException)?.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> onResolutionRequired(e)
                }
            }
    }

    private fun requestUpdates() = client.requestLocationUpdates(
        locationRequest,
        locationCallback,
        Looper.getMainLooper()
    ).apply { handler.postDelayed(runOnNoLocation, locationIntervalLimit) }

    fun stop() {
        client.removeLocationUpdates(locationCallback)
    }

    private fun location(useLast: Boolean) {
        if (!useLast) requestUpdates()
        else client.lastLocation.addOnSuccessListener {
            if (it != null) location(it)
            else requestUpdates()
        }.addOnFailureListener { onError?.invoke(it) }
    }

    private fun onResolutionRequired(e: ApiException) {
        try {
            val rae = e as ResolvableApiException
            rae.startResolutionForResult(activity, CODE_GPS_RESOLUTION)
        } catch (sie: IntentSender.SendIntentException) {
            onError?.invoke(sie)
        }
    }

    private fun location(location: Location) {
        handler.removeMessages(0)
        lastLocation = location
        onLocation?.invoke(location)
    }

    fun onResult(resultCode: Int) {
        if (resultCode == AppCompatActivity.RESULT_OK) location(true)
        else onError?.invoke(LocationNotAvailableException())
    }

    fun isLocationReady() = ::lastLocation.isInitialized


    class LocationNotAvailableException : Throwable()
    class ProviderDisableException(val provider: String?) : Throwable()
    class NoLocationFoundException(m: String) : Throwable(m)

    companion object {
        const val CODE_GPS_RESOLUTION = 2000
    }
}