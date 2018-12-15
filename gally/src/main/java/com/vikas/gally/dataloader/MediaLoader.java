package com.vikas.gally.dataloader;

import android.content.Context;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.vikas.gally.model.MediaModel;

import java.util.ArrayList;
import java.util.List;

public class MediaLoader {

    public static List<MediaModel> loadImages(Context context) {


        Uri externalContentUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri internalContentUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA};
        //DATA is the path to the corresponding image. We only need this for loading //image into a recyclerview

        String sortOrder = MediaStore.Images.ImageColumns.DATE_ADDED + " DESC";
        //This sorts all images such that recent ones appear first

        Cursor internalCursor = context.getContentResolver().query(internalContentUri, projection, null, null, sortOrder);
        Cursor externalCursor = context.getContentResolver().query(externalContentUri, projection, null, null, sortOrder);

        MergeCursor mergeCursor = new MergeCursor(new Cursor[]{internalCursor, externalCursor});

        List<MediaModel> imagePaths = new ArrayList<>();

        if (mergeCursor.moveToFirst()) {
            int imagePathIndex = mergeCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            do {
                String path = mergeCursor.getString(imagePathIndex);

                MediaModel mediaModel = new MediaModel();
                mediaModel.setImageUrl(path);

                imagePaths.add(mediaModel);
            } while (mergeCursor.moveToNext());
        }
        mergeCursor.close();
        return imagePaths;
    }

}
