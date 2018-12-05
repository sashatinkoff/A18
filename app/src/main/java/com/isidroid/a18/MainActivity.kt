package com.isidroid.a18

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import com.isidroid.utils.BaseActivity
import timber.log.Timber
import android.net.NetworkRequest
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception


class MainActivity : BaseActivity() {
    var isConnected = false
    private lateinit var manager: ConnectivityManager
    private lateinit var nc: NC
    val networkRequest = NetworkRequest.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        nc = NC(manager)

        button.setOnClickListener { execute() }
    }

    private fun execute() {
        try {
            if (!isConnected) {
                manager.registerNetworkCallback(networkRequest, NC(manager))
            } else {
                manager.unregisterNetworkCallback(nc)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }

        isConnected = !isConnected
    }

    class NC(private val manager: ConnectivityManager) : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            debug("onLost")
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            debug("onLosing")
        }

        override fun onAvailable(network: Network) {
            debug("onAvailable")
        }

        override fun onUnavailable() {
            debug("onUnavailable")
        }

        private fun debug(type: String) {
            Timber.tag("NetworkCallback").i("$type " +
                    "${manager.activeNetworkInfo?.extraInfo}, " +
                    "${manager.activeNetworkInfo?.detailedState}")
        }
    }
}
