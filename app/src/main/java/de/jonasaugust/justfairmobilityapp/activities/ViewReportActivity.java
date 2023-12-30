package de.jonasaugust.justfairmobilityapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import de.jonasaugust.justfairmobilityapp.R;

public class ViewReportActivity extends ActivityRoot {

    private TextView title;
    private View back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
    }

    @Override
    protected void updateData(Object object) {
        title.setText(R.string.view_report);
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
        return R.layout.activity_todo;
    }
}
