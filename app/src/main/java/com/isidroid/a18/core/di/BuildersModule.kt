package com.isidroid.a18.core.di

import com.isidroid.a18.MainActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    // Add bindings for other sub-components here
}