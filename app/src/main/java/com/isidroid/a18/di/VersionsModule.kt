package com.isidroid.a18.di

import javax.inject.Named

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier


@Module
class VersionsModule {
    @Provides @Version
    fun provideVersion(): String {
        return "1.0.0"
    }

    @Provides @Name
    fun provideName(): String {
        return "Sasha"
    }

    @Qualifier @Retention(AnnotationRetention.RUNTIME) annotation class Version
    @Qualifier @Retention(AnnotationRetention.RUNTIME) annotation class Name
}
