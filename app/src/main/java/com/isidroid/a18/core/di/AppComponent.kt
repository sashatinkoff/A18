package com.isidroid.a18.core.di

import com.isidroid.a18.App
import com.isidroid.utilsmodule.di.AppModule
import com.isidroid.utilsmodule.di.RealmModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, BuildersModule::class, RealmModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun appModule(module: AppModule): Builder
        fun realmModule(module: RealmModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}