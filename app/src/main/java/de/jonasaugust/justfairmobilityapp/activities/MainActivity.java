package de.jonasaugust.justfairmobilityapp.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import de.jonasaugust.justfairmobilityapp.R;

public class MainActivity extends de.jonasaugust.flugplatz_app.activities.ActivityRoot {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void updateData(Object object) {
        // nothing
    }

    @Override
    protected void setListeners() {
        // TODO
    }

    @Override
    protected View[] getViewsToDisable() {
        return new View[0];
    }

    @Override
    protected ProgressBar getProgressBar() {
        return findViewById(R.id.progressBar);
    }

    @Override
    protected boolean confirmBack() {
        return false;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }
}