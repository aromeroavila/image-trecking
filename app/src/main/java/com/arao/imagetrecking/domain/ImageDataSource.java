package com.arao.imagetrecking.domain;

import io.reactivex.Single;

public interface ImageDataSource {

    Single<String> getImageUrlForLocation(double minLon, double minLat, double maxLon, double maxLat);

}
