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

        var exercise = Exercise()
        Timber.i("exercise1=$exercise")

        Timber.i("1=${count()}")
        exercise.save()
        Timber.i("exercise2=$exercise")

        Timber.i("2=${count()}")

        val ex2 = Realm.getDefaultInstance().where(Exercise::class.java).equalTo("guid", exercise.guid).findFirst()
        Timber.i("ex2=$ex2")


        exercise.delete()
        Timber.i("3=${count()}")
    }

    fun count(): Long {
        return Realm.getDefaultInstance().where(Exercise::class.java).count()
    }
}
