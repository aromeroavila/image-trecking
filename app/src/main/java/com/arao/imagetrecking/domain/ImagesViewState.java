package com.arao.imagetrecking.domain;

import static com.arao.imagetrecking.domain.ScreenState.IN_PROGRESS;

public class ImagesViewState {

    public static final ImagesViewState LOADING = new ImagesViewState(IN_PROGRESS);

    private ScreenState screenState;

    public ImagesViewState(ScreenState screenState) {
        this.screenState = screenState;
    }

    public ScreenState getScreenState() {
        return screenState;
    }

    public void setScreenState(ScreenState screenState) {
        this.screenState = screenState;
    }
}
