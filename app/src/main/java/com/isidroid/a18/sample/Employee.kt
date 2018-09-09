package com.isidroid.a18.sample

class Employee(val id: Int, var name: String, var age: Int = 20) {
     var image = "https://sun9-3.userapi.com/c635101/v635101817/29976/QZaVMiOVSoA.jpg"

     override fun equals(other: Any?): Boolean {
          if (this === other) return true
          if (javaClass != other?.javaClass) return false

          other as Employee

          if (id != other.id) return false

          return true
     }

     override fun hashCode(): Int {
          return id
     }

}