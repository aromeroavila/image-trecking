package com.arao.imagetrecking.data.flickr;

import com.arao.imagetrecking.BuildConfig;
import com.arao.imagetrecking.domain.ImageDataSource;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleSource;

class FlickrRepository implements ImageDataSource {

    private static final int STREET_ACCURACY = 16;
    private static final int PHOTOS_ONLY_CONTENT_TYPE = 1;
    private static final int ITEMS_PER_PAGE = 1;

    private final FlickrService flickrService;

    @Inject
    FlickrRepository(FlickrService flickrService) {
        this.flickrService = flickrService;
    }

    @Override
    public Single<String> getImageUrlForLocation(int minLon, int minLat, int maxLon, int maxLat) {
        String bBoxString = minLon + "," + minLat + "," + maxLon + "," + maxLat;

        return flickrService.getImageForLocation(BuildConfig.FLICKR_API_KEY, bBoxString, ITEMS_PER_PAGE,
                PHOTOS_ONLY_CONTENT_TYPE, STREET_ACCURACY)
                .flatMap(this::processResponse);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private SingleSource<String> processResponse(FlickrResponse flickrResponse) {
        Photo photo = flickrResponse.getPhotos().get(0);

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("https://farm");
        urlBuilder.append(photo.getFarm());
        urlBuilder.append(".staticflickr.com/");
        urlBuilder.append(photo.getServer());
        urlBuilder.append("/");
        urlBuilder.append(photo.getId());
        urlBuilder.append("_");
        urlBuilder.append(photo.getSecret());
        urlBuilder.append(".jpg");

        return Single.just(urlBuilder.toString());
    }
}
