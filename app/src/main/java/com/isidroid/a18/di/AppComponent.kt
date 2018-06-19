package com.isidroid.a18.di

import com.isidroid.a18.App
import com.isidroid.utilsmodule.Diagnostics
import com.isidroid.utilsmodule.ScreenUtils
import dagger.Component
import java.io.File
import javax.inject.Singleton

@Singleton @Component(modules = [AppModule::class])
interface AppComponent {
    var screenUtils: ScreenUtils
    var logFile: File
    var diagnostics: Diagnostics

    fun inject(app: App)
}