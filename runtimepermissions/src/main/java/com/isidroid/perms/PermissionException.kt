package com.isidroid.perms

class PermissionException(val permissionResult: PermissionResult) : Exception() {

    val accepted: List<String> = permissionResult.accepted
    private val foreverDenied: List<String> = permissionResult.foreverDenied
    private val denied: List<String> = permissionResult.denied
    private val runtimePermission: RuntimePermission = permissionResult.runtimePermission

    fun goToSettings() = permissionResult.goToSettings()
    fun askAgain() = permissionResult.askAgain()
    fun isAccepted() = permissionResult.isAccepted
    fun hasDenied() = permissionResult.hasDenied()
    fun hasForeverDenied() = permissionResult.hasForeverDenied()
}