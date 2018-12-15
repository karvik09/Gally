package com.vikas.gally.util;

import android.os.Environment;

public class GallyFileUtils {

    public static boolean isFileRedable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)
                || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}
