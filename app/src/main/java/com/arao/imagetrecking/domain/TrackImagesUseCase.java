package com.arao.imagetrecking.domain;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class TrackImagesUseCase {

    private final BehaviorSubject<ImagesViewState> stateBehaviorSubject;

    @Inject
    TrackImagesUseCase(BehaviorSubject<ImagesViewState> stateBehaviorSubject) {
        this.stateBehaviorSubject = stateBehaviorSubject;
    }

    public Observable<ImagesViewState> startTrackingImages() {
        stateBehaviorSubject.onNext(ImagesViewState.LOADING);
        return stateBehaviorSubject;
    }

}
