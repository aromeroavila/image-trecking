package com.arao.imagetrecking.domain;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class TrackImagesUseCase {

    static final double ACCURACY_FACTOR = 0.0005;
    static final int MIN_DISTANCE_LOCATION_UPDATE = 100;
    static final int EXPIRATION_TIME_LOCATION_UPDATE = 2 * 60 * 60 * 1000;

    private final ImageDataSource imageDataSource;
    private final LocationDataSource locationDataSource;

    private BehaviorSubject<ImagesViewState> stateBehaviorSubject = BehaviorSubject.create();
    private List<String> imageUrls = new ArrayList<>();
    private Disposable locationDisposable;

    @Inject
    TrackImagesUseCase(ImageDataSource imageDataSource,
                       LocationDataSource locationDataSource) {
        this.imageDataSource = imageDataSource;
        this.locationDataSource = locationDataSource;
    }

    public Observable<ImagesViewState> startTrackingImages() {
        stateBehaviorSubject.onNext(ImagesViewState.LOADING);
        locationDisposable = locationDataSource.getLocationUpdates(EXPIRATION_TIME_LOCATION_UPDATE, MIN_DISTANCE_LOCATION_UPDATE)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(this::requestImageForLocation, this::processError);
        return stateBehaviorSubject;
    }

    public void stopTrackingImages() {
        locationDataSource.stopLocationUpdates();
        locationDisposable.dispose();
        imageUrls.clear();
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
                .subscribe(this::processResponse, this::processError);
    }

    private void processResponse(String url) {
        imageUrls.add(0, url);
        ImagesViewState successResponse = new ImagesViewState(ScreenState.SUCCESS, imageUrls, "");
        stateBehaviorSubject.onNext(successResponse);
    }

    private void processError(Throwable throwable) {
        ImagesViewState errorResponse = new ImagesViewState(ScreenState.ERROR, null, throwable.getMessage());
        stateBehaviorSubject.onNext(errorResponse);
    }

}
