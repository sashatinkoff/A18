package com.isidroid.a18.di

import javax.inject.Named

import dagger.Module
import dagger.Provides

@Module
class VersionsModule {
    @Provides @Named("version")
    fun provideVersion(): String {
        return "1.0.0"
    }

    @Provides @Named("name")
    fun provideName(): String {
        return "Sasha"
    }
}
