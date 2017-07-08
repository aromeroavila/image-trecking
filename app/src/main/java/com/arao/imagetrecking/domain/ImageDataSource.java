package com.arao.imagetrecking.domain;

import io.reactivex.Single;

public interface ImageDataSource {

    Single<String> getImageUrlForLocation(int minLon, int maxLon, int minLat, int maxLat);

}
