package com.saeedsoft.security.stuff;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.util.List;

import hugo.weaving.DebugLog;

@SuppressWarnings("SimplifiableIfStatement")
public class PermUtil {
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    @SuppressLint("InlinedApi")
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    @DebugLog
    public static boolean checkReadContacts(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @DebugLog
    public static boolean checkCallPhone(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @DebugLog
    public static boolean checkWriteExternalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED;
        }
        return true;
    }

    @DebugLog
    public static boolean checkReadExternalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED;
        }
        return true;
    }

    @DebugLog
    private static boolean checkNotificationPolicyAccess(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ManagerUtil.getNotificationManager(context).isNotificationPolicyAccessGranted();
        }
        return true;
    }


    @DebugLog
    public static boolean[] checkInitialPermissions(AppContext context) {
        return new boolean[]{checkCallPhone(context), checkNotificationPolicyAccess(context),
                checkProtectedApps(context)};
    }

    /**
     * This is for Huawei phones. It has built in a "feature" called Protected Apps, that can be
     * accessed from the phone settings (Battery Manager > Protected Apps). This allows elected
     * apps to keep running after the screen is turned off.
     */
    private static boolean checkProtectedApps(AppContext context) {
        if (context.getAppPreferences().isShowProtectedAppsMessage()) {
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize" +
                    ".process.ProtectActivity");
            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() == 0;
        }
        return true;
    }


}
