package com.arao.imagetrecking.presentation;

import com.arao.imagetrecking.domain.ImagesViewState;
import com.arao.imagetrecking.domain.ScreenState;
import com.arao.imagetrecking.domain.TrackImagesUseCase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class ImagesPresenter {

    private final TrackImagesUseCase trackImagesUseCase;

    private ImagesView imagesView;
    private Disposable stateDisposable;

    ImagesPresenter(TrackImagesUseCase trackImagesUseCase) {
        this.trackImagesUseCase = trackImagesUseCase;
    }

    void initialise(ImagesView imagesView) {
        this.imagesView = imagesView;
    }

    void onStartButtonPressed() {
        if (imagesView.isLocationPermissionGranted()) {
            stateDisposable = trackImagesUseCase.startTrackingImages()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onStateChange);
        } else {
            imagesView.requestLocationPermissions();
        }
    }

    void onStopButtonPressed() {
        trackImagesUseCase.stopTrackingImages();
        stateDisposable.dispose();
        imagesView.renderState(ImagesViewState.INITIAL);
    }

    void finalise() {
        imagesView = null;
        stateDisposable.dispose();
    }

    void onRequestPermissionResult(int[] grantResults) {
        if (grantResults.length > 0) {
            boolean allPermissionsGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != 0) {
                    allPermissionsGranted = false;
                }
            }
            if (allPermissionsGranted) {
                onStartButtonPressed();
            } else {
                onPermissionRequestDenied();
            }
        } else {
            onPermissionRequestDenied();
        }
    }

    private void onStateChange(ImagesViewState imagesViewState) {
        imagesView.renderState(imagesViewState);
    }

    private void onPermissionRequestDenied() {
        ImagesViewState permissionError = new ImagesViewState(ScreenState.ERROR, null,
                "The location permission is required to use this app");
        imagesView.renderState(permissionError);
    }
}
