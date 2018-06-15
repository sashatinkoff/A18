package com.isidroid.utilsmodule

import dagger.Component

@Component(modules = [VersionsModule::class])
interface VersionsComponent {
    fun inject(billing: Billing)
}