package com.arao.imagetrecking.presentation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.arao.imagetrecking.R;

class ImageViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;

    ImageViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_container);
    }
}
