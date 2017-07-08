package com.arao.imagetrecking.domain;

import java.util.List;

import static com.arao.imagetrecking.domain.ScreenState.IN_PROGRESS;

public class ImagesViewState {

    public static final ImagesViewState INITIAL = new ImagesViewState(ScreenState.INITIAL, null);
    public static final ImagesViewState LOADING = new ImagesViewState(IN_PROGRESS, null);

    private ScreenState screenState;
    private List<String> imageUrls;

    public ImagesViewState(ScreenState screenState, List<String> imageUrls) {
        this.screenState = screenState;
        this.imageUrls = imageUrls;
    }

    public ScreenState getScreenState() {
        return screenState;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

}
