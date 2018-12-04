package com.isidroid.a18.data

import io.realm.DynamicRealm
import io.realm.RealmList
import io.realm.RealmMigration
import timber.log.Timber

class DbMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        Timber.e("migrate")

        try {
            realm.schema.get("PhotoSetting")?.apply {
                addRealmListField("tmp_left", Float::class.java)
                addRealmListField("tmp_right", Float::class.java)

                transform { obj ->
                    val left = RealmList<Float>()
                    left.add(obj.getObject("leftEye")?.getFloat("x"))
                    left.add(obj.getObject("leftEye")?.getFloat("y"))

                    val right = RealmList<Float>()
                    right.add(obj.getObject("rightEye")?.getFloat("x"))
                    right.add(obj.getObject("rightEye")?.getFloat("y"))

                    obj.set("tmp_left", left)
                    obj.set("tmp_right", right)
                }

                removeField("leftEye")
                removeField("rightEye")

                renameField("tmp_left", "leftEye")
                renameField("tmp_right", "rightEye")
            }

            realm.schema.rename("Event", "Project")
            realm.schema.get("Project")?.apply {
                setRequired("guid", true)
            }


            arrayOf("Alarm", "Day", "Freemium", "LocalChange", "ScheduleLog").forEach {
                realm.schema.remove(it)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}