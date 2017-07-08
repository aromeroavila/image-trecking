package com.arao.imagetrecking.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.arao.imagetrecking.R;
import com.arao.imagetrecking.di.AppComponent;
import com.arao.imagetrecking.di.DaggerAppComponent;

import javax.inject.Inject;

public class ImagesActivity extends AppCompatActivity implements ImagesView {

    @Inject
    ImagesPresenter imagesPresenter;

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

    private void resolveDependencies() {
        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);
    }

    private void initViews() {
        startButton = (Button) findViewById(R.id.start_button);
        // TODO Replace by lambda
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesPresenter.onStartButtonPressed();
            }
        });
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_progress_bar);
        contentRecycler = (RecyclerView) findViewById(R.id.content_recycler);
    }

}
