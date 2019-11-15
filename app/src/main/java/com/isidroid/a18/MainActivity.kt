package com.isidroid.a18

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.lifecycle.ViewModelProvider
import com.isidroid.a18.databinding.ActivityMainBinding
import com.isidroid.utils.BindActivity
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber


class MainActivity : BindActivity<ActivityMainBinding>(layoutRes = R.layout.activity_main) {
    private val viewmodel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }
    private val repository by lazy { LocationRepository(this) }

    private var messenger: Messenger? = null
    private var position = -1

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        globalPosition++
        position = globalPosition

        btnHello.setOnClickListener { sendHello() }
        btnOpen.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
    }

    override fun onStart() {
        super.onStart()
        var bindResult: Boolean? = null
        var messengerExists = messenger != null

        if (messenger == null)
            bindResult = bindService(
                Intent(this, MessengerService::class.java),
                connection, Context.BIND_IMPORTANT
            )

        Timber.i("sdfsdfsdf  onStart.$position, messenger=$messengerExists, bindResult=$bindResult")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("sdfsdfsdf onStop.$position messenger=${messenger != null}")

        messenger?.let { unbindService(connection) }
    }

    private fun sendHello() {
        try {
            val what = position
            val msg = Message.obtain(null, what, 0, 0)
            msg.data = Bundle().apply { putString("key", "sdfsdfsdfsdfsdf") }

            messenger?.send(msg)
        } catch (e: RemoteException) {
            Timber.e(e)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Timber.i("sdfsdfsdf onServiceConnected on activity.$position $className")
            messenger = Messenger(service)
        }

        override fun onServiceDisconnected(className: ComponentName) {
            Timber.e("sdfsdfsdf onServiceDisconnected on activity.$position $className")
            messenger = null
        }
    }

    companion object {
        var globalPosition = 0
    }
}