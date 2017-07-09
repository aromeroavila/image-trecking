package com.arao.imagetrecking.domain;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static com.arao.imagetrecking.domain.TrackImagesUseCase.ACCURACY_FACTOR;
import static com.arao.imagetrecking.domain.TrackImagesUseCase.EXPIRATION_TIME_LOCATION_UPDATE;
import static com.arao.imagetrecking.domain.TrackImagesUseCase.MIN_DISTANCE_LOCATION_UPDATE;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrackImagesUseCaseTest {

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock private ImageDataSource imageDataSource;
    @Mock private LocationDataSource locationDataSource;

    @Test
    public void shouldRequestCurrentLocation_whenStartTrackingImages() {
        TrackImagesUseCase trackImagesUseCase = givenATrackImageUseCase_and_aSuccessfulLocationResponse();

        trackImagesUseCase.startTrackingImages();

        verify(locationDataSource).getLocationUpdates(EXPIRATION_TIME_LOCATION_UPDATE, MIN_DISTANCE_LOCATION_UPDATE);
    }

    @Test
    public void shouldEmitErrorState_whenFailedToRequestALocation() {
        TrackImagesUseCase trackImagesUseCase = givenATrackImageUseCase_and_anErrorLocationResponse();

        TestObserver<ImagesViewState> testObserver = trackImagesUseCase.startTrackingImages().test();

        assertStateChange(testObserver, 0, ScreenState.ERROR);
    }

    @Test
    public void shouldRequestAnImage_whenReceivingALocationUpdate() {
        TrackImagesUseCase trackImagesUseCase = givenATrackImageUseCase_and_aSuccessfulLocationResponse();

        trackImagesUseCase.startTrackingImages();

        verify(imageDataSource).getImageUrlForLocation(1 - ACCURACY_FACTOR, 2 - ACCURACY_FACTOR, 1 + ACCURACY_FACTOR, 2 + ACCURACY_FACTOR);
    }

    @Test
    public void shouldEmitErrorState_whenFailedToRequestAnImage() {
        TrackImagesUseCase trackImagesUseCase = givenATrackImageUseCase_and_aSuccessfulLocationResponse_and_anErrorImageResponse();

        TestObserver<ImagesViewState> testObserver = trackImagesUseCase.startTrackingImages().test();

        assertStateChange(testObserver, 0, ScreenState.ERROR);
    }

    private void assertStateChange(TestObserver<ImagesViewState> testObserver, int event, ScreenState state) {
        List<ImagesViewState> values = testObserver.values();
        Assertions.assertThat(values.get(event).getScreenState()).isEqualTo(state);
    }

    private TrackImagesUseCase givenATrackImageUseCase_and_anErrorLocationResponse() {
        when(locationDataSource.getLocationUpdates(EXPIRATION_TIME_LOCATION_UPDATE, MIN_DISTANCE_LOCATION_UPDATE))
                .thenReturn(Observable.error(new RuntimeException("Test")));
        return givenATrackImageUseCase();
    }

    private TrackImagesUseCase givenATrackImageUseCase_and_aSuccessfulLocationResponse_and_anErrorImageResponse() {
        when(imageDataSource.getImageUrlForLocation(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(Single.error(new RuntimeException("Test")));
        return givenATrackImageUseCase_and_aSuccessfulLocationResponse();
    }

    private TrackImagesUseCase givenATrackImageUseCase_and_aSuccessfulLocationResponse() {
        when(locationDataSource.getLocationUpdates(EXPIRATION_TIME_LOCATION_UPDATE, MIN_DISTANCE_LOCATION_UPDATE))
                .thenReturn(Observable.just(new Coordinate(1, 2)));
        return givenATrackImageUseCase();
    }

    private TrackImagesUseCase givenATrackImageUseCase() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        return new TrackImagesUseCase(imageDataSource, locationDataSource);
    }


}