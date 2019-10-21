package com.isidroid.a18

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

@SuppressLint("MissingPermission")
class LocationHelper(private val activity: Activity) {
    private val client by lazy { LocationServices.getFusedLocationProviderClient(activity) }
    private val settingsClient by lazy { LocationServices.getSettingsClient(activity) }
    val codeResolution = 200

    private var onLocation: ((Location) -> Unit)? = null
    private var onError: ((Throwable) -> Unit)? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {
            result?.lastLocation?.let { onLocation?.invoke(it) }
        }

        override fun onLocationAvailability(result: LocationAvailability?) {
            if (result?.isLocationAvailable != true) {
                onError?.invoke(LocationNotAvailableException())
                legacyLocationManager()
            }
        }
    }

    var locationRequest = LocationRequest().apply {
        interval = 1_000L
        fastestInterval = 1_000L
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        numUpdates = 1
    }

    private val settingsRequest: LocationSettingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)
        .build()

    private fun legacyLocationManager() {
        val locationManager =
            activity.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_FINE
            powerRequirement = Criteria.NO_REQUIREMENT
        }

        val bestProvider = locationManager.getBestProvider(criteria, true)
        val location = locationManager.getLastKnownLocation(bestProvider)

        if (location != null) {
            onLocation?.invoke(location)
            return
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                location?.let { onLocation?.invoke(location) }
                locationManager.removeUpdates(this)
            }

            override fun onProviderDisabled(provider: String?) {
                onError?.invoke(ProviderDisableException(provider))
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }
        }

        locationManager.allProviders
            .filter { locationManager.isProviderEnabled(it) }
            .forEach {
                locationManager.requestLocationUpdates(
                    it,
                    1000L,
                    10f,
                    listener,
                    Looper.getMainLooper()
                )
            }

    }

    fun start(
        onLocation: ((Location) -> Unit),
        onError: ((Throwable) -> Unit)
    ) {
        this.onLocation = onLocation
        this.onError = onError

        settingsClient.checkLocationSettings(settingsRequest)
            .addOnSuccessListener { location() }
            .addOnFailureListener { e ->
                when ((e as? ApiException)?.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> onResolutionRequired(e)
                }
            }
    }

    private fun location() {
        client.lastLocation.addOnSuccessListener {
            if (it != null) onLocation?.invoke(it)
            else client.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
            .addOnFailureListener { onError?.invoke(it) }
    }

    private fun onResolutionRequired(e: ApiException) {
        try {
            val rae = e as ResolvableApiException
            rae.startResolutionForResult(activity, codeResolution)

            onError?.invoke(rae)
        } catch (sie: IntentSender.SendIntentException) {
            onError?.invoke(sie)
        }
    }

    fun onResult(resultCode: Int) {
        if (resultCode == AppCompatActivity.RESULT_OK) location()
        else onError?.invoke(LocationNotAvailableException())
    }

    class LocationNotAvailableException : Throwable()
    class ProviderDisableException(val provider: String?) : Throwable()
}