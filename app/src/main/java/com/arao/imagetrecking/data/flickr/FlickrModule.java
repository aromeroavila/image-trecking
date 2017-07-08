package com.arao.imagetrecking.data.flickr;

import com.arao.imagetrecking.domain.ImageDataSource;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class FlickrModule {

    @Provides
    ImageDataSource imageDataSource(FlickrRepository flickrRepository) {
        return flickrRepository;
    }

    @Provides
    FlickrService flickrService(Retrofit retrofit) {
        return retrofit.create(FlickrService.class);
    }

}
