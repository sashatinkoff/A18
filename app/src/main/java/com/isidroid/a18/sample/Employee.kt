package com.isidroid.a18.sample

import java.util.*

class Employee(var email: String, var age: Int = 20) {
    val guid: String = UUID.randomUUID().toString().substring(0, 5)
    var image = "https://sun9-3.userapi.com/c635101/v635101817/29976/QZaVMiOVSoA.jpg"
}