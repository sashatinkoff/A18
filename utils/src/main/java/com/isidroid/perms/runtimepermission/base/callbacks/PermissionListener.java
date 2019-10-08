package com.isidroid.perms.runtimepermission.base.callbacks;


import com.isidroid.perms.runtimepermission.base.PermissionResult;

import java.util.List;

public interface PermissionListener {
    void onAccepted(PermissionResult permissionResult, List<String> accepted);
    void onDenied(PermissionResult permissionResult, List<String> denied, List<String> foreverDenied);
}
