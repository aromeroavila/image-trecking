package com.arao.imagetrecking.presentation;

class ImagesPresenter {

    private ImagesView imagesView;

    void initialise(ImagesView imagesView) {
        this.imagesView = imagesView;
    }

    void onStartButtonPressed() {
        // Subscribe to UseCase
    }

    void finalise() {
        imagesView = null;
        // Dispose
    }

}
