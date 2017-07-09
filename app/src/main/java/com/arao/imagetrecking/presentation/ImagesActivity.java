package com.arao.imagetrecking.presentation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;

import com.arao.imagetrecking.R;
import com.arao.imagetrecking.di.AppComponent;
import com.arao.imagetrecking.di.DaggerAppComponent;
import com.arao.imagetrecking.domain.ImagesViewState;

import javax.inject.Inject;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ImagesActivity extends AppCompatActivity implements ImagesView {

    private static final int LOCATION_PERMISSIONS_REQUEST = 123;

    @Inject
    ImagesPresenter imagesPresenter;
    @Inject
    ImageUrlAdapter imageUrlAdapter;
    @Inject
    RecyclerView.ItemDecoration itemDecoration;

    private Button startButton;
    private ProgressBar loadingIndicator;
    private RecyclerView contentRecycler;
    private MenuItem stopMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resolveDependencies();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        initViews();
        imagesPresenter.initialise(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imagesPresenter.finalise();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_images_menu, menu);
        stopMenuItem = menu.findItem(R.id.action_stop);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_stop) {
            imagesPresenter.onStopButtonPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST) {
            imagesPresenter.onRequestPermissionResult(grantResults);
        }
    }

    @Override
    public void renderState(ImagesViewState state) {
        switch (state.getScreenState()) {
            case INITIAL:
                stopMenuItem.setVisible(false);
                contentRecycler.setVisibility(GONE);
                imageUrlAdapter.setData(state.getImageUrls());
                imageUrlAdapter.notifyDataSetChanged();
                startButton.setVisibility(VISIBLE);
                loadingIndicator.setVisibility(GONE);
                break;
            case IN_PROGRESS:
                startButton.setVisibility(GONE);
                loadingIndicator.setVisibility(VISIBLE);
                break;
            case SUCCESS:
                stopMenuItem.setVisible(true);
                startButton.setVisibility(GONE);
                loadingIndicator.setVisibility(GONE);
                contentRecycler.setVisibility(VISIBLE);
                imageUrlAdapter.setData(state.getImageUrls());
                imageUrlAdapter.notifyItemInserted(0);
                break;
            case ERROR:
                if (loadingIndicator.getVisibility() == VISIBLE) {
                    stopMenuItem.setVisible(false);
                    startButton.setVisibility(VISIBLE);
                    loadingIndicator.setVisibility(GONE);
                }
                Snackbar.make(findViewById(android.R.id.content),
                        state.getErrorMessage(), Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean isLocationPermissionGranted() {
        int fineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSIONS_REQUEST);
    }

    private void resolveDependencies() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .presentationModule(new PresentationModule(getApplicationContext()))
                .build();
        appComponent.inject(this);
    }

    @SuppressWarnings("ConstantConditions")
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> imagesPresenter.onStartButtonPressed());
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_progress_bar);
        contentRecycler = (RecyclerView) findViewById(R.id.content_recycler);
        contentRecycler.setLayoutManager(new LinearLayoutManager(this));
        contentRecycler.addItemDecoration(itemDecoration);
        contentRecycler.setAdapter(imageUrlAdapter);
    }
}
