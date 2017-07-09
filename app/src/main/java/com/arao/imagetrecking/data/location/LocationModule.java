package com.arao.imagetrecking.data.location;

import android.content.Context;

import com.arao.imagetrecking.domain.Coordinate;
import com.arao.imagetrecking.domain.LocationDataSource;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.PublishSubject;

@Module
public class LocationModule {

    @Provides
    @Singleton
    LocationDataSource locationDataSource(LocationRepository locationRepository) {
        return locationRepository;
    }

    @Provides
    PublishSubject<Coordinate> coordinatePublishSubject() {
        return PublishSubject.create();
    }

    @Provides
    FusedLocationProviderClient fusedLocationProviderClient(Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

    @Provides
    LocationRequest locationRequest() {
        return LocationRequest.create();
    }

}
