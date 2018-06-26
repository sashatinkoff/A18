package com.isidroid.a18

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.isidroid.a18.sample.Exercise
import io.realm.Realm
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("before=${count()}")
        Exercise().apply {
            save()
        }
        Timber.i("affter=${count()}")
    }

    fun count(): Long {
        return Realm.getDefaultInstance().where(Exercise::class.java).count()
    }
}
