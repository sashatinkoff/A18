package com.isidroid.utilsmodule

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier


@Module
class VersionsModule {
    var name: String? = null
    var version: String? = null

    @Provides @Version
    fun provideVersion(): String {
        return version ?: "undefined version"
    }

    @Provides @Name
    fun provideName(): String {
        return name ?: "unknown person"
    }

    @Qualifier @Retention(AnnotationRetention.RUNTIME) annotation class Version
    @Qualifier @Retention(AnnotationRetention.RUNTIME) annotation class Name
}
