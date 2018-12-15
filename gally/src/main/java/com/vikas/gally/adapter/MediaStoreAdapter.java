package com.vikas.gally.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.vikas.gally.R;
import com.vikas.gally.listener.OnRecyclerClickListener;
import com.vikas.gally.model.MediaModel;
import com.vikas.gally.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

public class MediaStoreAdapter extends RecyclerView.Adapter <MediaStoreAdapter.MediaHolder> {

    private Context mContext;
    private ArrayList<MediaModel> mMedia;
    private OnRecyclerClickListener mClickListener;
    public static final String CHANGE_ITEM_STATE = "change_item_state";
    private int tickColor;
    int size;


    public MediaStoreAdapter(Context mContext,
                             ArrayList<MediaModel> media,
                             int tickColor,
                             OnRecyclerClickListener clickListener) {
        this.mContext = mContext;
        this.mMedia = media;
        this.mClickListener = clickListener;
        this.tickColor = tickColor;
        size = mContext.getResources().getDisplayMetrics().widthPixels / 3;
    }

    @NonNull
    @Override
    public MediaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.media_item, viewGroup, false);
        return new MediaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaHolder mediaHolder, int position) {
        MediaModel mediaModel = mMedia.get(position);
        ImageLoader.getInstance().displayImage("file://" + mediaModel.getImageUrl(),
                mediaHolder.mediaView,
                ImageUtil.getLocalImageOptions());

        setItemState(mediaHolder, mediaModel.isSelected());
    }

    @Override
    public void onBindViewHolder(@NonNull MediaHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else if (payloads.get(0) instanceof Bundle) {
            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle.containsKey(CHANGE_ITEM_STATE)) {
                setItemState(holder, mMedia.get(position).isSelected());
            }
        }
    }

    private void setItemState(MediaHolder mediaHolder, boolean isSelected) {
        mediaHolder.tickView.setImageResource(isSelected ? R.drawable.ic_done : 0);
        mediaHolder.foregroundFrame.setVisibility(isSelected ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mMedia.size();
    }

    /**
     * method to update media list
     * @param imagePaths
     */
    public void updateMediaList(List<MediaModel> imagePaths) {
        mMedia.clear();
        mMedia.addAll(imagePaths);
        notifyDataSetChanged();
    }

     class MediaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mediaView,tickView;
        View foregroundFrame;
        MediaHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mediaView = itemView.findViewById(R.id.media_imageView);
            foregroundFrame = itemView.findViewById(R.id.foregroundFrame);
            tickView = itemView.findViewById(R.id.tickView);
            tickView.setColorFilter(ContextCompat.getColor(mContext,tickColor),
                    PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onRecyclerClick(v,getAdapterPosition());
            }
        }
    }
}
