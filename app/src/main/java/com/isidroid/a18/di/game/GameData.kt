package com.isidroid.a18.di.game

import java.util.*

class GameData {
    var guid = UUID.randomUUID().toString().substring(0, 5)
    var message = "generated"

    override fun toString(): String {
        return "GameData(guid='$guid', message='$message')"
    }
}