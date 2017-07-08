package com.arao.imagetrecking.data.flickr;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrService {

    @GET("rest?method=flickr.photos.search")
    Single<FlickrResponse> getImageForLocation(@Query("api_key") String apiKey,
                                               @Query("bbox") String bbox,
                                               @Query("per_page") int perPage,
                                               @Query("content_type") int contentType,
                                               @Query("accuracy") int accuracy);

}
