package com.isidroid.a18.sample

import com.isidroid.a18.data.DataModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

@RealmClass
open class Job : DataModel {
    @PrimaryKey var guid = UUID.randomUUID().toString().substring(0, 4)
    var name: String? = null

    override fun toString(): String {
        return "Job(guid='$guid', name=$name)"
    }
}