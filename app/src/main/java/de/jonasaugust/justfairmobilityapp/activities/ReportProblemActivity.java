package de.jonasaugust.justfairmobilityapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import de.jonasaugust.justfairmobilityapp.R;

public class ReportProblemActivity extends ActivityRoot {

    View back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        back = findViewById(R.id.back);
    }

    @Override
    protected void updateData(Object object) {
        // nothing
    }

    @Override
    protected void setListeners() {
        back.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected View[] getViewsToDisable() {
        return new View[] {back};
    }

    @Override
    protected ProgressBar getProgressBar() {
        return findViewById(R.id.progressBar);
    }

    @Override
    protected boolean confirmBack() {
        return true;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_report_problem;
    }
}
