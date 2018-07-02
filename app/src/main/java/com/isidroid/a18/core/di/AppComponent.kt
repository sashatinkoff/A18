package com.isidroid.a18.core.di

import com.isidroid.a18.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, BuildersModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder
        fun appModule(module: AppModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}