package com.arao.imagetrecking.domain;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.BehaviorSubject;

@Module
public class DomainModule {

    @Provides
    BehaviorSubject<ImagesViewState> behaviorSubject() {
        return BehaviorSubject.create();
    }

}
