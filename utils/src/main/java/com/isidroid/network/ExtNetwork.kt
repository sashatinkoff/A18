package com.isidroid.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun Context?.isInternetAvailable(): Boolean = with(this) {
    this ?: return@with false
    var result = false

    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        .apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = activeNetwork ?: return false
                val actNw = getNetworkCapabilities(networkCapabilities) ?: return false

                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } else {
                run {
                    activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            else -> false
                        }
                    }
                }
            }
        }
    result
}