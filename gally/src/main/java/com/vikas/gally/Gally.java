package com.vikas.gally;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.vikas.gally.activity.MediaStoreActivity;
import com.vikas.gally.util.Decorator;

public class Gally {


    private static Gally sInstance;
    private static final int DEFAULT_MAX_SELECTION = 1;
    private Decorator mDecorator;


    /**
     * Returns singleton class instance
     */

    private Gally() {

    }
    public static Gally getInstance() {
        if (sInstance == null) {
            synchronized (Gally.class) {
                if (sInstance == null) {
                    sInstance = new Gally();
                }
            }
        }
        return sInstance;
    }

    public Gally decorateWith(Decorator decorator) {
        this.mDecorator = decorator;
        return this;
    }

    public void launch(Activity activity) {
        if (activity == null) {
            throw new IllegalStateException("activity cannot be null");
        }
        initImageLoader(activity.getApplicationContext());
        MediaStoreActivity.launch(activity,mDecorator);
    }


    // Initialize ImageLoader with default configuration.
    private static void initImageLoader(Context context) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                .build();

        final int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass / 8;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(cacheSize)
                .diskCacheSize(10 * 1024 * 1024)
                .diskCacheFileCount(50)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(options)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

    }

}
