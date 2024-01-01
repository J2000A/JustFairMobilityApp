package de.jonasaugust.justfairmobilityapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.data.ProblemReport;
import de.jonasaugust.justfairmobilityapp.helpers.Converter;

public class ReportProblemActivity extends ActivityRoot {

    // Views
    View back;
    Button category;
    Button location;
    Button photos;
    LinearLayout photosContainer;
    TextInputLayout descriptionLayout;
    TextInputEditText description;
    Button send;

    // State
    ProblemReport problemReport = new ProblemReport();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        back = findViewById(R.id.back);
        category = findViewById(R.id.category);
        location = findViewById(R.id.location);
        photos = findViewById(R.id.photos);
        photosContainer = findViewById(R.id.photosContainer);
        descriptionLayout = findViewById(R.id.descriptionLayout);
        description = findViewById(R.id.description);
        send = findViewById(R.id.send);
    }

    @Override
    protected void updateData(Object object) {
        if (problemReport.getCategory() != null)
            category.setText(problemReport.getCategory());

        if (problemReport.getLocation() != null)
            location.setText(problemReport.getLocation().toString());

        for (String photoPath : problemReport.getPhotos()) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, Converter.dpToPx(80)
            );
            layoutParams.setMargins(0, Converter.dpToPx(8), 0, 0);
            imageView.setLayoutParams(layoutParams);
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                photosContainer.addView(imageView);
            }
        }

        description.setText(problemReport.getDescription());
    }

    @Override
    protected void setListeners() {
        back.setOnClickListener(view -> onBackPressed());
        category.setOnClickListener(view -> {/*TODO*/});
        location.setOnClickListener(view -> selectLocationWithMaps());
        photos.setOnClickListener(view -> {/*TODO*/});
        send.setOnClickListener(view -> {/*TODO*/});
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

    private void selectLocationWithMaps() {
        Intent intent = new Intent(this, MapsActivity.class);
        if (problemReport.getLocation() != null) {
            intent.putExtra(MapsActivity.LAT, Double.valueOf(problemReport.getLocation().latitude));
            intent.putExtra(MapsActivity.LON, Double.valueOf(problemReport.getLocation().longitude));
            intent.putExtra(MapsActivity.ZOOM, 14);
        }
        resultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (result.getResultCode() == Activity.RESULT_OK && data != null) {
                    problemReport.setLocation(
                            new LatLng(data.getDoubleExtra(MapsActivity.LAT, -1),
                                    data.getDoubleExtra(MapsActivity.LON, -1))
                    );
                }
                updateData(null);
            }
    );
}
