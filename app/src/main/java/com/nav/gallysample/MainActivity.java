package com.nav.gallysample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.vikas.gally.Gally;
import com.vikas.gally.activity.MediaStoreActivity;
import com.vikas.gally.util.Decorator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.BitSet;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MediaStoreActivity.REQUEST_CODE_LAUNCH && resultCode == RESULT_OK) {

            ArrayList<String> selectedPath = data.getStringArrayListExtra(MediaStoreActivity.EXTRA_IMAGE_PATHS);
            System.out.println(selectedPath);
            if (!selectedPath.isEmpty()) {
                new DecodeBitmapTask(imageView).execute(selectedPath.get(0));
            }
        }
    }

    private static class DecodeBitmapTask extends AsyncTask<Object,Void,Bitmap>{

        private WeakReference<ImageView> imageRef;

        private DecodeBitmapTask(ImageView imageView) {
            imageRef = new WeakReference<>(imageView);
        }
        @Override
        protected Bitmap doInBackground(Object... objects) {
            String imagePath = (String) objects[0];
            return BitmapFactory.decodeFile(imagePath);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null && imageRef.get() != null) {
                imageRef.get().setImageBitmap(bitmap);
            }
        }
    }

    public void onMediaStoreClick(View view) {

        Decorator decorator = new Decorator.Builder()
                .setMaxSelection(7)
                .setTitle("GallySample")
                .setTickColor(R.color.colorAccent)
                .build();

        Gally.getInstance()
                .decorateWith(decorator)
                .launch(this);

    }
}
