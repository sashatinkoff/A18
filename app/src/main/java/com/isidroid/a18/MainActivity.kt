package com.isidroid.a18

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.isidroid.utilsmodule.ScreenUtils
import com.isidroid.utilsmodule.upgrade.UpgradeHelper
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject lateinit var upgradeHelper: UpgradeHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.i("version=${upgradeHelper.version}, isUpdated=${upgradeHelper.isUpgraded}," +
                " isInstalled=${upgradeHelper.isInstalled}")

    }

}
