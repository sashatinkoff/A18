package com.isidroid.a18.sample

import com.isidroid.a18.data.DataModel
import io.realm.RealmList
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class Person : DataModel {
    @PrimaryKey var guid = UUID.randomUUID().toString().substring(0, 5)
    var names = RealmList<String>()
    var age = 19
    var jobs: RealmList<Job>? = null
    var birthday = Date()
    var weight = 67.2


    override fun toString(): String {
        return "Person(guid='$guid', names=${names.joinToString(", ")}, " +
                "jobs=${jobs?.joinToString(", ")}, " +
                "birthday=$birthday, weight=$weight)"
    }
}