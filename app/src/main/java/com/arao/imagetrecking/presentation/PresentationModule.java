package com.arao.imagetrecking.presentation;

import android.support.v7.widget.RecyclerView;

import com.arao.imagetrecking.domain.TrackImagesUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentationModule {

    @Provides
    ImagesPresenter imagesPresenter(TrackImagesUseCase trackImagesUseCase) {
        return new ImagesPresenter(trackImagesUseCase);
    }

    @Provides
    RecyclerView.ItemDecoration itemDecoration(VerticalSpaceItemDecoration verticalSpaceItemDecoration) {
        return verticalSpaceItemDecoration;
    }

}
