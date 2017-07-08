package com.arao.imagetrecking.presentation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arao.imagetrecking.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

public class ImageUrlAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<String> imageUrls;

    @Inject
    ImageUrlAdapter() {
    }

    void setData(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_list_item, parent, false);

        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);

        Picasso.with(holder.imageView.getContext())
                .load(imageUrl)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (imageUrls != null) {
            return imageUrls.size();
        }
        return 0;
    }
}
