package com.vikas.gally.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vikas.gally.R;
import com.vikas.gally.adapter.MediaStoreAdapter;
import com.vikas.gally.dataloader.MediaLoader;
import com.vikas.gally.listener.OnRecyclerClickListener;
import com.vikas.gally.model.MediaModel;
import com.vikas.gally.permissions.Permission;
import com.vikas.gally.util.Decorator;
import com.vikas.gally.util.GallyFileUtils;
import com.vikas.gally.util.ItemDecorationAlbumColumn;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MediaStoreActivity extends BaseActivity implements OnRecyclerClickListener {

    String TAG = "MediaStoreActivity";
    private static final int MENU_ITEM_DONE = 788;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 1;
    private MediaStoreAdapter mMediaAdapter;
    private final ArrayList<MediaModel> mMedia = new ArrayList<>();
    private int mMaxSelection;
    private Toolbar mToolbar;
    private final ArrayList<String> mSelectedPath = new ArrayList<>();
    private String mTitle;


    public static final String EXTRA_IMAGE_PATHS = "image_paths";
    public static final int REQUEST_CODE_LAUNCH = 536;

    /**
     * maunch activity
     *
     * @param activity
     * @param decorator
     */
    public static void launch(Activity activity, Decorator decorator) {

        Intent intent = new Intent(activity, MediaStoreActivity.class);
        if (decorator != null) {
            intent.putExtra(MAX_SELECTION, decorator.getMaxSelection() == 0 ? 1 : decorator.getMaxSelection());
            intent.putExtra(TOOLBAR_TITLE, TextUtils.isEmpty(decorator.getTitle()) ? "Gally" : decorator.getTitle());
            intent.putExtra(TICK_COLOR, decorator.getTickColor());
        }

        ActivityCompat.startActivityForResult(activity, intent, REQUEST_CODE_LAUNCH, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mMaxSelection = getIntent().getIntExtra(MAX_SELECTION, 1);
        mTitle = getIntent().getStringExtra(TOOLBAR_TITLE);
        int theme = getIntent().getIntExtra(THEME, -1);
        int tickColor = getIntent().getIntExtra(TICK_COLOR, R.color.colorPrimary);
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_store);
        mToolbar = findViewById(R.id.gally_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setTitle(mTitle);

        RecyclerView recyclerView = findViewById(R.id.media_images);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        mMediaAdapter = new MediaStoreAdapter(this, mMedia,tickColor, this);
        recyclerView.setAdapter(mMediaAdapter);
        ItemDecorationAlbumColumn dividerItemDecoration = new ItemDecorationAlbumColumn(5,
                layoutManager.getSpanCount());
        recyclerView.addItemDecoration(dividerItemDecoration);
        checkReadPermission();
    }



    private void checkReadPermission() {
        if (Permission.hasWritePermission(this)) {
            checkFileReadState();
        } else {
            Permission.requestPermission(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_REQUEST);
        }
    }

    private void checkFileReadState() {
        if (GallyFileUtils.isFileRedable()) {
            new ImageLoadTask(this).execute();

        } else {
            Log.d(TAG, "file read not allowed");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(0, MENU_ITEM_DONE, 0, "Done");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case MENU_ITEM_DONE:
                setResult();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRecyclerClick(View view, int position) {
        MediaModel mediaModel = mMedia.get(position);
        if (mMaxSelection == 1) {
            mSelectedPath.add(mediaModel.getImageUrl());
            setResult();
            return;
        }
        if (mediaModel.isSelected()) {
            mediaModel.setSelected(false);
            mSelectedPath.remove(mediaModel.getImageUrl());
            Bundle bundle = new Bundle();
            bundle.putBoolean(MediaStoreAdapter.CHANGE_ITEM_STATE,true);
            mMediaAdapter.notifyItemChanged(position,bundle);
        }else {

            if (mSelectedPath.size() < mMaxSelection) {
                mediaModel.setSelected(true);
                mSelectedPath.add(mediaModel.getImageUrl());
                Bundle bundle = new Bundle();
                bundle.putBoolean(MediaStoreAdapter.CHANGE_ITEM_STATE,true);
                mMediaAdapter.notifyItemChanged(position,bundle);
            }
        }
        refreshToolbar();

    }

    /**
     * method to set result
     */
    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IMAGE_PATHS, mSelectedPath);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void refreshToolbar() {
        if (mSelectedPath.isEmpty()) {
            mToolbar.setTitle(mTitle);
            mToolbar.getMenu().findItem(MENU_ITEM_DONE).setVisible(false);
        }else {
            mToolbar.setTitle(mSelectedPath.size() + "\\" + mMaxSelection + " Selected");
            mToolbar.getMenu().findItem(MENU_ITEM_DONE).setVisible(true);
        }
    }

    private static class ImageLoadTask extends AsyncTask<Void, Void, List<MediaModel>> {
        private WeakReference<MediaStoreActivity> activityRef;

        ImageLoadTask(MediaStoreActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        protected List<MediaModel> doInBackground(Void... voids) {
            return MediaLoader.loadImages(activityRef.get());
        }

        @Override
        protected void onPostExecute(List<MediaModel> imagePaths) {
            super.onPostExecute(imagePaths);
            if (activityRef.get() != null) {
                activityRef.get().mMediaAdapter.updateMediaList(imagePaths);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!mSelectedPath.isEmpty()) {
            unselectAll();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * method to unselect all
     */
    private void unselectAll() {
        mSelectedPath.clear();
        refreshToolbar();
        for (MediaModel mediaModel : mMedia) {
            mediaModel.setSelected(false);
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean(MediaStoreAdapter.CHANGE_ITEM_STATE,true);
        mMediaAdapter.notifyItemRangeChanged(0, mMedia.size(), bundle);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkFileReadState();
        } else {
            if (shouldShowRequestPermissionRationale(permissions[0])) {
                showToast("Rationale");
            } else {
                showToast("Permission denied!");
            }
        }
    }
}
