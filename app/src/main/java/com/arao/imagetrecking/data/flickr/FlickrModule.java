package com.arao.imagetrecking.data.flickr;

import com.arao.imagetrecking.domain.ImageDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class FlickrModule {

    private static final String FLICKR_BASE_URL = "https://api.flickr.com/services/";

    @Provides
    @Singleton
    Retrofit retrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(FLICKR_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Provides
    ImageDataSource imageDataSource(FlickrRepository flickrRepository) {
        return flickrRepository;
    }

    @Provides
    FlickrService flickrService(Retrofit retrofit) {
        return retrofit.create(FlickrService.class);
    }

}
