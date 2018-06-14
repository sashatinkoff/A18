package com.isidroid.a18.di.game

import javax.inject.Inject

class GameSession {
    @Inject lateinit var data: GameData
    override fun toString(): String {
        return "GameSession(data=$data)"
    }

}