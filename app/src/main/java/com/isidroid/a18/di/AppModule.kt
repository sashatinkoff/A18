package com.isidroid.a18.di

import android.app.Application
import com.isidroid.utilsmodule.ScreenUtils
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {
    @Singleton @Provides
    fun provideScreenUtils(): ScreenUtils {
        return ScreenUtils(application)
    }
}