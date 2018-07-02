package com.isidroid.a18.core.di

import android.app.Application
import com.isidroid.a18.BuildConfig
import com.isidroid.utilsmodule.ScreenUtils
import com.isidroid.utilsmodule.upgrade.UpgradeHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application){
    @Singleton @Provides
    fun provideScreenUtils(): ScreenUtils {
        return ScreenUtils(application)
    }

    @Singleton @Provides
    fun upgradeHelper(): UpgradeHelper {
        return UpgradeHelper(application, BuildConfig.VERSION_CODE)
    }

}