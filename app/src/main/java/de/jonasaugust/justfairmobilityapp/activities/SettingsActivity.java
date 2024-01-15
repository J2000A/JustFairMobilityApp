package de.jonasaugust.justfairmobilityapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.toasts.ToastBuilder;

// TODO Implement
public class SettingsActivity extends ActivityRoot {

    private TextView title;
    private View back;
    private View privacy;
    private View terms;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = findViewById(R.id.title);
        back = findViewById(R.id.back);
        privacy = findViewById(R.id.privacy);
        terms = findViewById(R.id.terms);
    }

    @Override
    protected void updateData() {
        title.setText(R.string.settings);
    }

    @Override
    protected void setListeners() {
        back.setOnClickListener(view -> onBackPressed());
        privacy.setOnClickListener(view -> openUrl(getString(R.string.privacy_url)));
        terms.setOnClickListener(view -> openUrl(getString(R.string.terms_url)));
    }

    private void openUrl(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            ToastBuilder.show(this, e);
        }
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
        return false;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }
}
