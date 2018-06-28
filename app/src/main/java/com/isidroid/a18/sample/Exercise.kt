package com.isidroid.a18.sample

import com.isidroid.a18.data.DataModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.kotlin.isManaged
import io.realm.kotlin.isValid

@RealmClass
open class Exercise : DataModel {
    @PrimaryKey var guid = "sdf"
    override fun toString(): String {
        return "Exercise(guid='$guid', isvalid=${isValid()}, isManaged=${isManaged()}})"
    }


}