package com.arao.imagetrecking.data.location;

import android.location.Location;

import com.arao.imagetrecking.domain.Coordinate;
import com.arao.imagetrecking.domain.LocationDataSource;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

@Singleton
class LocationRepository extends LocationCallback implements LocationDataSource {

    private final PublishSubject<Coordinate> coordinatePublishSubject;
    private final FusedLocationProviderClient locationProviderClient;
    private final LocationRequest locationRequest;

    @Inject
    LocationRepository(PublishSubject<Coordinate> coordinatePublishSubject,
                       FusedLocationProviderClient locationProviderClient,
                       LocationRequest locationRequest) {
        this.coordinatePublishSubject = coordinatePublishSubject;
        this.locationProviderClient = locationProviderClient;
        this.locationRequest = locationRequest;
    }

    @Override
    public Observable<Coordinate> getLocationUpdates(long expirationDuration, long minDistance) {

        locationRequest.setSmallestDisplacement(minDistance)
                .setInterval(1000 * 5)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setExpirationDuration(expirationDuration);

        try {
            locationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                    Location location = locationResult.getLastLocation();

                    if (location != null) {
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        coordinatePublishSubject.onNext(new Coordinate(longitude, latitude));
                    }
                }
            }, null);
        } catch (SecurityException e) {
            coordinatePublishSubject.onError(e);
        }
        return coordinatePublishSubject;
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);

        Location location = locationResult.getLastLocation();

        if (location != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            coordinatePublishSubject.onNext(new Coordinate(longitude, latitude));
        }
    }

}
