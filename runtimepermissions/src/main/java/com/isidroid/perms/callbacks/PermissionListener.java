package com.isidroid.perms.callbacks;


import com.isidroid.perms.PermissionResult;

import java.util.List;

public interface PermissionListener {
    void onAccepted(PermissionResult permissionResult, List<String> accepted);
    void onDenied(PermissionResult permissionResult, List<String> denied, List<String> foreverDenied);
}
