package com.arao.imagetrecking.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    @Inject
    ImagesPresenter imagesPresenter;
    @Inject
    ImageUrlAdapter imageUrlAdapter;
    @Inject
    RecyclerView.ItemDecoration itemDecoration;

    private Button startButton;
    private ProgressBar loadingIndicator;
    private RecyclerView contentRecycler;

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
    public void renderState(ImagesViewState state) {
        switch (state.getScreenState()) {
            case INITIAL:
                startButton.setVisibility(VISIBLE);
                loadingIndicator.setVisibility(GONE);
                break;
            case IN_PROGRESS:
                startButton.setVisibility(GONE);
                loadingIndicator.setVisibility(VISIBLE);
                break;
            case SUCESS:
                startButton.setVisibility(GONE);
                loadingIndicator.setVisibility(GONE);
                contentRecycler.setVisibility(VISIBLE);
                imageUrlAdapter.setData(state.getImageUrls());
                break;
            case ERROR:
                break;
        }
    }

    private void resolveDependencies() {
        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);
    }

    private void initViews() {
        startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> imagesPresenter.onStartButtonPressed());
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_progress_bar);
        contentRecycler = (RecyclerView) findViewById(R.id.content_recycler);
        contentRecycler.setLayoutManager(new LinearLayoutManager(this));
        contentRecycler.addItemDecoration(itemDecoration);
        contentRecycler.setAdapter(imageUrlAdapter);
    }

}
