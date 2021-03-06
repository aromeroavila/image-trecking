package com.arao.imagetrecking.data.flickr;

import com.arao.imagetrecking.BuildConfig;
import com.arao.imagetrecking.domain.ImageDataSource;

import javax.inject.Inject;

import io.reactivex.Single;

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
    public Single<String> getImageUrlForLocation(double minLon, double minLat, double maxLon, double maxLat) {
        String bBoxString = minLon + "," + minLat + "," + maxLon + "," + maxLat;

        return flickrService.getImageForLocation(BuildConfig.FLICKR_API_KEY, bBoxString, ITEMS_PER_PAGE,
                PHOTOS_ONLY_CONTENT_TYPE, STREET_ACCURACY)
                .flatMap(this::processResponse);
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    private Single<String> processResponse(FlickrResponse flickrResponse) {
        if (flickrResponse.getPhotos().size() > 0) {
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
        } else {
            throw new RuntimeException("There are no images available for the current location");
        }
    }

}
