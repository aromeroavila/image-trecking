package com.arao.imagetrecking.domain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class TrackImagesUseCase {

    private final BehaviorSubject<ImagesViewState> stateBehaviorSubject;
    private final ImageDataSource imageDataSource;

    @Inject
    TrackImagesUseCase(BehaviorSubject<ImagesViewState> stateBehaviorSubject,
                       ImageDataSource imageDataSource) {
        this.stateBehaviorSubject = stateBehaviorSubject;
        this.imageDataSource = imageDataSource;
    }

    public Observable<ImagesViewState> startTrackingImages() {
        stateBehaviorSubject.onNext(ImagesViewState.LOADING);
        imageDataSource.getImageUrlForLocation(-10, -10, 10, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::processResponse);
        return stateBehaviorSubject;
    }

    private void processResponse(String url) {
        List<String> urls = new ArrayList<>();
        urls.add(url);
        ImagesViewState resul = new ImagesViewState(ScreenState.SUCESS, urls);
        stateBehaviorSubject.onNext(resul);
    }

}
