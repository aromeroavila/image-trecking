package com.arao.imagetrecking.presentation;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import javax.inject.Inject;

class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    @Inject
    public VerticalSpaceItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = 10;
        }
    }

}
