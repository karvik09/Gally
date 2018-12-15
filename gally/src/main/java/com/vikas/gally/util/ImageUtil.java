package com.vikas.gally.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.vikas.gally.R;

public class ImageUtil {
    /**
     * method for local-image-options
     * @return
     */
    public static DisplayImageOptions getLocalImageOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
//                .displayer(new FadeInBitmapDisplayer(100,true,true,true))
                .showImageOnFail(R.drawable.image_fail_drawable)
                .showImageOnLoading(R.drawable.image_fail_drawable)
                .showImageForEmptyUri(R.drawable.image_fail_drawable)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .build();
    }
}
