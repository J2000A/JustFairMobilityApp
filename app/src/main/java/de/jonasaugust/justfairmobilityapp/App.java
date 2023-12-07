package de.jonasaugust.justfairmobilityapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;

import java.util.Locale;

import de.jonasaugust.justfairmobilityapp.data.SettingsAdapter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Set Orientation per Default on horizontal
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        base = SettingsAdapter.setSprache(SettingsAdapter.getSprache(base), false, base);
        SettingsAdapter.updateNightMode(SettingsAdapter.getNightMode(base));
        super.attachBaseContext(base);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        Locale locale = new Locale(SettingsAdapter.getSprache(this).getLocaleString());

        SplitInstallRequest.Builder builder = SplitInstallRequest.newBuilder();
        builder.addLanguage(locale);
        SplitInstallManager manager = SplitInstallManagerFactory.create(getApplicationContext());
        manager.startInstall(builder.build());

        newConfig.setLocale(locale);
        super.onConfigurationChanged(newConfig);
    }

    @SuppressLint("MissingPermission")
    public static boolean noInternetConnection(Context context) {
        ConnectivityManager con_manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return !(con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected());
    }
}
