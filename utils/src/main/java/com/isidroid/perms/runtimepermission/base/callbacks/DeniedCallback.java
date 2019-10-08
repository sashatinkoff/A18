package com.isidroid.perms.runtimepermission.base.callbacks;


import com.isidroid.perms.runtimepermission.base.PermissionResult;

public interface DeniedCallback {
    void onDenied(PermissionResult result);
}
