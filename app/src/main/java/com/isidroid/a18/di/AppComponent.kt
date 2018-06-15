package com.isidroid.a18.di

import com.isidroid.a18.App
import com.isidroid.utilsmodule.ScreenUtils
import dagger.Component
import javax.inject.Singleton

@Singleton @Component(modules = [AppModule::class])
interface AppComponent {
    var screenUtils: ScreenUtils

    fun inject(app: App)
}