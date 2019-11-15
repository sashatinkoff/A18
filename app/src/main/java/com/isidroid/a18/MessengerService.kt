package com.isidroid.a18

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.os.Messenger
import timber.log.Timber


class MessengerService : Service() {
    private var position = -1
    private lateinit var messenger: Messenger
    override fun onBind(intent: Intent?) = messenger.binder

    override fun onCreate() {
        super.onCreate()
        mPosition++
        position = mPosition

        messenger = Messenger(IncomingHandler(position))


        Timber.i("sdfsdfsdf onCreate.$position")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("sdfsdfsdf onDestroy.$position")
    }

    class IncomingHandler(private val position: Int) : Handler() {
        override fun handleMessage(msg: Message) {
            val data = msg.data.keySet().map { "$it=${msg.data[it]}" }

            Timber.i("sdfsdfsdf on serv.$position msg.what=${msg.what} data=$data")

            when (msg.what) {
                else -> super.handleMessage(msg)
            }
        }
    }

    companion object {
        var mPosition = 0
    }
}