package com.isidroid.a18.ui

sealed class State {
    data class Error(val message: String) : State()
    object Loading : State()
    data class Data(val pet: Pet) : State()
}

data class Pet(val name: String, val age: Int)