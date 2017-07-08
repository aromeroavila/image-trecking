package com.arao.imagetrecking.di;

import com.arao.imagetrecking.domain.DomainModule;
import com.arao.imagetrecking.presentation.ImagesActivity;
import com.arao.imagetrecking.presentation.PresentationModule;

import dagger.Component;

@Component(modules = {PresentationModule.class, DomainModule.class})
public interface AppComponent {

    void inject(ImagesActivity imagesActivity);

}
