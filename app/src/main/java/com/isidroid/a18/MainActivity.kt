package com.isidroid.a18

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isidroid.utilsmodule.ScreenUtils
import dagger.android.AndroidInjection
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject lateinit var screenUtils: ScreenUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("onCreate ${screenUtils.width}")
    }

}
