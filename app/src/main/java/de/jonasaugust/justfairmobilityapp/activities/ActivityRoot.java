package de.jonasaugust.flugplatz_app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.data.SettingsAdapter;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.dialogs.DialogBuilder;

public abstract class ActivityRoot extends AppCompatActivity {

    protected SettingsAdapter settingsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        // Initialise Adapters
        settingsAdapter = new SettingsAdapter(this);

        hideKeyboard();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setListeners();
        updateData(null);
    }

    protected void hideKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(SettingsAdapter.setSprache(SettingsAdapter.getSprache(base),false, base));
    }

    @Override
    public void onBackPressed() {
        if (confirmBack()) {
            DialogBuilder.showBackConfirmationDialog(this, view -> super.onBackPressed());
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void startLoading() {
        hideKeyboard();
        ProgressBar p = getProgressBar();
        if (p != null) p.setVisibility(View.VISIBLE);
        View[] l = getViewsToDisable();
        if (l == null) return;
        for (View v : l) {
            if (v.getId() == R.id.back) continue;
            v.setEnabled(false);
        }
    }

    protected void stopLoading() {
        runOnUiThread(() -> {
            ProgressBar p = getProgressBar();
            if (p != null) p.setVisibility(View.GONE);
            View[] l = getViewsToDisable();
            if (l == null) return;
            for (View v : l) {
                v.setEnabled(true);
            }
        });
    }

    protected abstract void updateData(Object object);

    protected abstract void setListeners();

    protected abstract View[] getViewsToDisable();

    protected abstract ProgressBar getProgressBar();

    protected abstract boolean confirmBack();

    protected abstract int getContentView();
}
