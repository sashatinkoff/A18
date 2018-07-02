package com.isidroid.a18

import com.isidroid.a18.data.DataModel
import io.realm.annotations.RealmClass

@RealmClass
open class Item: DataModel {
    override fun save() {
        super.save()
    }

    override fun delete() {
        super.delete()
    }

    override fun onSave(): Boolean {
        return super.onSave()
    }

    override fun onDelete(): Boolean {
        return super.onDelete()
    }
}