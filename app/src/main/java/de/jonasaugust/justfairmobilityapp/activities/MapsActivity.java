package de.jonasaugust.justfairmobilityapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.Manifest;
import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.toasts.ToastBuilder;

public class MapsActivity extends ActivityRoot implements OnMapReadyCallback {

    // TODO: Create API Key and add it to the project:
    // 1)
    // Create Google Cloud Console Project
    // https://developers.google.com/maps/documentation/android-sdk/cloud-setup?hl=de#create
    // 2)
    // Create API Key
    // https://developers.google.com/maps/documentation/android-sdk/get-api-key?hl=de
    // 3)
    // Create file 'local.properties' in the project root
    // Add in this file the API Key by adding the following line:
    // MAPS_API_KEY=########-#########################-#####
    // (see also https://developers.google.com/maps/documentation/android-sdk/config?hl=de#kotlin)

    // Variables for Extras Bundle
    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String ZOOM = "zoom";

    // Default Values
    public static final double DEFAULT_LAT = 48.136213;
    public static final double DEFAULT_LON = 11.577145;
    public static final int DEFAULT_ZOOM = 10;

    // Views
    private View back;
    private GoogleMap map;
    private TextView info;
    private Button save;

    // State
    private LatLng latLng = null;
    private int zoom = DEFAULT_ZOOM;
    private boolean mapReady = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 4564;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            latLng = new LatLng(getIntent().getDoubleExtra(LAT, DEFAULT_LAT), getIntent().getDoubleExtra(LON, DEFAULT_LON));
            zoom = getIntent().getExtras().getInt(ZOOM, DEFAULT_ZOOM);
        }

        // Initialising the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            ToastBuilder.show(this, new IllegalStateException("Map Fragment was null"));
        }

        back = findViewById(R.id.back);
        info = findViewById(R.id.textView);
        save = findViewById(R.id.select);
    }

    @Override
    protected void setListeners() {
        back.setOnClickListener(view -> onBackPressed());
        save.setOnClickListener(view -> onSave());
    }

    @Override
    protected void updateData(Object object) {
        if (mapReady) {
            latLng = map.getCameraPosition().target;
            zoom = (int) map.getCameraPosition().zoom;
        }
        if (latLng != null) {
            info.setText(getString(R.string.maps_info, latLng.latitude + "", latLng.longitude + "", zoom + ""));
        } else {
            info.setText(getString(R.string.maps_info, "?", "?", "?"));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setRotateGesturesEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (latLng == null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(DEFAULT_LAT, DEFAULT_LON), DEFAULT_ZOOM));
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        }

        map.setOnCameraMoveListener(() -> runOnUiThread(() -> updateData(null)));

        // Check and request location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        mapReady = true;
        map = googleMap;
        updateData(null);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.setMyLocationEnabled(true);
            } else {
                ToastBuilder.show(this, R.string.maps_no_location_permission);
            }
        }
    }

    private void onSave() {
        if (!mapReady) finish();

        Intent returnIntent = new Intent();
        returnIntent.putExtra(LAT, map.getCameraPosition().target.latitude);
        returnIntent.putExtra(LON, map.getCameraPosition().target.longitude);
        returnIntent.putExtra(ZOOM, (int) map.getCameraPosition().zoom);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected View[] getViewsToDisable() {
        return new View[] {save, findViewById(R.id.map)};
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
        return R.layout.activity_maps;
    }
}
