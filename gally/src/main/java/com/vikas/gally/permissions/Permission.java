package com.vikas.gally.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Permission {

    public static boolean hasWritePermission(Context context) {
        return !isMarshMallow() ||
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(Activity activity,String[] permissions,int requestCode){
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    private static boolean isMarshMallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
