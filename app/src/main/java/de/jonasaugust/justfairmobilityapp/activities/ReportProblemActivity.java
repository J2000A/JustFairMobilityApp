package de.jonasaugust.justfairmobilityapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.text.DecimalFormat;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.data.ProblemReport;
import de.jonasaugust.justfairmobilityapp.helpers.Converter;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.dialogs.DialogBuilder;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.toasts.ToastBuilder;

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

    // Request Codes
    private final int REQUEST_IMAGE_CAPTURE = 7557;

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void updateData(Object object) {
        if (problemReport.getCategory() != null)
            category.setText(problemReport.getCategory());

        if (problemReport.getLocation() != null) {
            DecimalFormat df = new DecimalFormat("#.####");
            location.setText("Lat: " + df.format(problemReport.getLocation().latitude) + "  Lon: " + df.format(problemReport.getLocation().longitude));
        }

        photosContainer.removeAllViews();
        int index = 0;
        for (Bitmap bitmap : problemReport.getPhotos()) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, Converter.dpToPx(150)
            );
            layoutParams.setMargins(0, Converter.dpToPx(8), 0, 0);
            imageView.setLayoutParams(layoutParams);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                photosContainer.addView(imageView);
                int finalIndex = index;
                imageView.setOnClickListener(view -> deleteImageDialog(finalIndex));
            }
            index++;
        }

        description.setText(problemReport.getDescription());
    }

    @Override
    protected void setListeners() {
        back.setOnClickListener(view -> onBackPressed());
        category.setOnClickListener(view -> {/*TODO*/});
        location.setOnClickListener(view -> selectLocationWithMaps());
        photos.setOnClickListener(view -> dispatchTakePictureIntent());
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            ToastBuilder.show(this, R.string.report_problem_photos_no_camera);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            problemReport.addPhoto(imageBitmap);
            updateData(null);
        } else {
            ToastBuilder.show(this, R.string.report_problem_photos_error);
        }
    }

    private void deleteImageDialog(int index) {
        new DialogBuilder(R.string.delete, R.drawable.icon_baseline_delete_forever_24, this)
                .addText(R.string.report_problem_photos_delete)
                .setButtonShort(R.string.cancel, true, null)
                .setButtonRed(R.string.delete, true, view -> {
                    problemReport.removePhoto(index);
                    updateData(null);
                })
                .show();
    }
}
