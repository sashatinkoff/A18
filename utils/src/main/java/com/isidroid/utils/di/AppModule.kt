package com.isidroid.utils.di

import android.app.Application
import com.isidroid.utils.ScreenUtils
import com.isidroid.utils.utils.UpgradeHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application, private val versionCode: Int) {
    @Singleton @Provides
    fun provideScreenUtils(): ScreenUtils {
        return ScreenUtils(application)
    }

    @Singleton @Provides
    fun upgradeHelper(): UpgradeHelper {
        return UpgradeHelper(application, versionCode)
    }
}