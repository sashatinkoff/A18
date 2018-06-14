package com.isidroid.a18.di.game

import dagger.Component

@Component(modules = [GameModule::class])
interface GameComponent {
    fun inject(session: GameSession)
}