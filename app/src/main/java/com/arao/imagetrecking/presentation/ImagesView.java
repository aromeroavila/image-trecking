package com.arao.imagetrecking.presentation;

import com.arao.imagetrecking.domain.ImagesViewState;

interface ImagesView {

    void renderState(ImagesViewState imagesViewState);

    boolean isLocationPermissionGranted();

    void requestLocationPermissions();

}
