package com.arao.imagetrecking.presentation;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentationModule {

    @Provides
    ImagesPresenter imagesPresenter() {
        return new ImagesPresenter();
    }

}
