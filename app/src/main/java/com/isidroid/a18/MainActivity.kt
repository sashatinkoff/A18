package com.isidroid.a18

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.isidroid.utilsmodule.Billing
import com.isidroid.utilsmodule.DaggerVersionsComponent
import com.isidroid.utilsmodule.Diagnostics
import com.isidroid.utilsmodule.VersionsModule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val versionsComponent = DaggerVersionsComponent.builder()
                .versionsModule(VersionsModule().apply {
                    name = "Superhero"
                    version = "2.2"
                })
                .build()
        val billing = Billing().apply { versionsComponent.inject(this) }
        Diagnostics.i(this, "onCreate w/billing=$billing")
    }
}
