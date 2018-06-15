package com.isidroid.a18.di

import com.isidroid.a18.MainActivity
import com.isidroid.utilsmodule.VersionsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class])
interface NetComponent {
    fun inject(activity: MainActivity)
}