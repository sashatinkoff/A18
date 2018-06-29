package com.isidroid.a18.di

import android.app.Application
import com.isidroid.utilsmodule.ScreenUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
//    @Singleton @Provides
//    fun provideScreenUtils(): ScreenUtils {
//        return ScreenUtils(application)
//    }

    @Singleton @Provides
    fun provideCommonHelloService(): CommonHelloServise {
        return CommonHelloServise()
    }
}