package com.isidroid.a18.di.game

import dagger.Module
import dagger.Provides

@Module
class GameModule {

    @Provides
    fun provideGameData(): GameData {
        return GameData()
    }
}