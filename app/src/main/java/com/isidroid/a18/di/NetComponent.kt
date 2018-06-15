package com.isidroid.a18.di

import com.isidroid.a18.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetModule::class, VersionsModule::class])
interface NetComponent {
    fun inject(activity: MainActivity)
}