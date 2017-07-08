package com.arao.imagetrecking.presentation;

import com.arao.imagetrecking.domain.TrackImagesUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentationModule {

    @Provides
    ImagesPresenter imagesPresenter(TrackImagesUseCase trackImagesUseCase) {
        return new ImagesPresenter(trackImagesUseCase);
    }

}
