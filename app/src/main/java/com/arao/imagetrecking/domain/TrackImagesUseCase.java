package com.arao.imagetrecking.domain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class TrackImagesUseCase {

    private static final double ACCURACY_FACTOR = 0.0005;
    private static final int MIN_DISTANCE_LOCATION_UPDATE = 100;
    private static final int EXPIRATION_TIME_LOCATION_UPDATE = 2 * 60 * 60 * 1000;

    private final BehaviorSubject<ImagesViewState> stateBehaviorSubject;
    private final ImageDataSource imageDataSource;
    private final LocationDataSource locationDataSource;

    private List<String> imageUrls = new ArrayList<>();

    @Inject
    TrackImagesUseCase(BehaviorSubject<ImagesViewState> stateBehaviorSubject,
                       ImageDataSource imageDataSource,
                       LocationDataSource locationDataSource) {
        this.stateBehaviorSubject = stateBehaviorSubject;
        this.imageDataSource = imageDataSource;
        this.locationDataSource = locationDataSource;
    }

    public Observable<ImagesViewState> startTrackingImages() {
        stateBehaviorSubject.onNext(ImagesViewState.LOADING);
        locationDataSource.getLocationUpdates(EXPIRATION_TIME_LOCATION_UPDATE, MIN_DISTANCE_LOCATION_UPDATE)
                .subscribe(this::requestImageForLocation);
        return stateBehaviorSubject;
    }

    private void requestImageForLocation(Coordinate coordinate) {
        double longitude = coordinate.getLongitude();
        double latitude = coordinate.getLatitude();

        double minLon = longitude - ACCURACY_FACTOR;
        double minLat = latitude - ACCURACY_FACTOR;
        double maxLon = longitude + ACCURACY_FACTOR;
        double maxLat = latitude + ACCURACY_FACTOR;

        imageDataSource.getImageUrlForLocation(minLon, minLat, maxLon, maxLat)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::processResponse);
    }

    private void processResponse(String url) {
        imageUrls.add(0, url);
        ImagesViewState successResponse = new ImagesViewState(ScreenState.SUCESS, imageUrls, "");
        stateBehaviorSubject.onNext(successResponse);
    }

}
