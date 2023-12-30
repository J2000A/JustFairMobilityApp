package de.jonasaugust.justfairmobilityapp.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.ProgressBar;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.toasts.ToastBuilder;

public class MainActivity extends ActivityRoot {

    View settingsButton;
    View commentNotificationButton;
    View surveyNotificationButton;
    View reportProblemButton;
    View answerSurveyButton;
    View atlasButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsButton = findViewById(R.id.settings);
        commentNotificationButton = findViewById(R.id.commentNotificationButton);
        surveyNotificationButton = findViewById(R.id.surveyNotificationButton);
        reportProblemButton = findViewById(R.id.reportProblemCardView);
        answerSurveyButton = findViewById(R.id.answerSurveyCardView);
        atlasButton = findViewById(R.id.atlasCardView);
    }

    @Override
    protected void updateData(Object object) {
        // nothing
    }

    @Override
    protected void setListeners() {
        settingsButton.setOnClickListener(view -> startActivity(new Intent(this, SettingsActivity.class)));
        commentNotificationButton.setOnClickListener(view -> startActivity(new Intent(this, ViewReportActivity.class)));
        surveyNotificationButton.setOnClickListener(view -> startActivity(new Intent(this, AnswerSurveyActivity.class)));
        reportProblemButton.setOnClickListener(view -> startActivity(new Intent(this, ReportProblemActivity.class)));
        answerSurveyButton.setOnClickListener(view -> startActivity(new Intent(this, AnswerSurveyActivity.class)));
        atlasButton.setOnClickListener(view -> openAtlas());
    }

    private void openAtlas() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.atlas_url))));
        } catch (Exception e) {
            ToastBuilder.show(this, e);
        }
    }

    @Override
    protected View[] getViewsToDisable() {
        return new View[] {settingsButton, commentNotificationButton, surveyNotificationButton, reportProblemButton, answerSurveyButton, atlasButton};
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