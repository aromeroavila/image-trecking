package com.arao.imagetrecking.di;

import com.arao.imagetrecking.data.flickr.FlickrModule;
import com.arao.imagetrecking.data.location.LocationModule;
import com.arao.imagetrecking.presentation.ImagesActivity;
import com.arao.imagetrecking.presentation.PresentationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {PresentationModule.class, FlickrModule.class, LocationModule.class})
public interface AppComponent {

    void inject(ImagesActivity imagesActivity);

}
