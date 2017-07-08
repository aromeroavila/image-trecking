package com.arao.imagetrecking.presentation;

import com.arao.imagetrecking.domain.ImagesViewState;
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
        stateDisposable = trackImagesUseCase.startTrackingImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onStateChange);
    }

    void finalise() {
        imagesView = null;
        stateDisposable.dispose();
    }

    private void onStateChange(ImagesViewState imagesViewState) {
        imagesView.renderState(imagesViewState);
    }

}
