package com.isidroid.a18.sample

import java.util.*

class Employee(var email: String, var age: Int = 20) {
    val guid: String = UUID.randomUUID().toString().substring(0, 5)
    var image = "https://sun9-3.userapi.com/c635101/v635101817/29976/QZaVMiOVSoA.jpg"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Employee

        if (guid != other.guid) return false

        return true
    }

    override fun hashCode(): Int {
        return guid.hashCode()
    }

    override fun toString(): String {
        return "Employee(email='$email', guid=$guid)"
    }
}