package com.isidroid.a18

import com.isidroid.a18.base.PermissionResult
import com.isidroid.a18.base.RuntimePermission


fun androidx.fragment.app.Fragment.askPermission(vararg permissions: String, acceptedblock: (PermissionResult) -> Unit): KotlinRuntimePermission {
    return KotlinRuntimePermission(
        RuntimePermission.askPermission(activity)
            .request(permissions.toList())
            .onAccepted(acceptedblock)
    )
}

fun androidx.fragment.app.FragmentActivity.askPermission(vararg permissions: String, acceptedblock: (PermissionResult) -> Unit): KotlinRuntimePermission {
    return KotlinRuntimePermission(
        RuntimePermission.askPermission(this)
            .request(permissions.toList())
            .onAccepted(acceptedblock)
    )
}

class KotlinRuntimePermission(private var runtimePermission: RuntimePermission) {

    init {
        runtimePermission.ask()
    }

    fun onDeclined(block: ((PermissionResult) -> Unit)) : KotlinRuntimePermission {
        runtimePermission.onResponse{
            if(it.hasDenied() || it.hasForeverDenied()){
                block(it)
            }
        }
        return this
    }
}