package com.arao.imagetrecking.domain;

import io.reactivex.Observable;

public interface LocationDataSource {

    Observable<Coordinate> getLocationUpdates(long expirationDuration, long minDistance);

}
