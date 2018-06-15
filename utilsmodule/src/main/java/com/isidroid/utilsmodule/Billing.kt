package com.isidroid.utilsmodule

import javax.inject.Inject

class Billing {
    @field: [Inject VersionsModule.Version] lateinit var version: String
    override fun toString(): String {
        return "Billing(version='$version')"
    }
}