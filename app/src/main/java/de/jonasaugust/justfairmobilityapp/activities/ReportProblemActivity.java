package de.jonasaugust.justfairmobilityapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.data.ProblemReport;
import de.jonasaugust.justfairmobilityapp.helpers.Converter;
import de.jonasaugust.justfairmobilityapp.helpers.compatability.DateCompat;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.buttons.ButtonDesigner;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.dialogs.DialogBuilder;
import de.jonasaugust.justfairmobilityapp.helpers.view_builders.toasts.ToastBuilder;

public class ReportProblemActivity extends ActivityRoot {

    // Views
    private View back;
    private MaterialButton category;
    private MaterialButton location;
    private Button photos;
    private LinearLayout photosContainer;
    private TextInputLayout descriptionLayout;
    private TextInputEditText description;
    private Button send;

    // Request Codes
    private final int REQUEST_IMAGE_CAPTURE = 7557;

    // State
    private final ProblemReport problemReport = new ProblemReport();
    private static int uploadedPictures = 0;
    private UploadPicturesThread uploadPicturesThread;
    private UploadReportThread uploadReportThread;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uploadedPictures = 0;

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
    protected void updateData() {
        if (problemReport.getCategory() != null) {
            ButtonDesigner.designButtonSelectedOnSurface(category, false, false, this);
            category.setText(problemReport.getCategory());
        }

        if (problemReport.getLocation() != null) {
            ButtonDesigner.designButtonSelectedOnSurface(location, false, false, this);
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
        category.setOnClickListener(view -> chooseCategory());
        location.setOnClickListener(view -> selectLocationWithMaps());
        photos.setOnClickListener(view -> dispatchTakePictureIntent());
        send.setOnClickListener(view -> sendReport());
    }

    @Override
    protected View[] getViewsToDisable() {
        return new View[] {back, category, location, photos, description, descriptionLayout, send};
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
                updateData();
            }
    );

    /** @noinspection deprecation*/
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
            updateData();
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            ToastBuilder.show(this, R.string.report_problem_photos_error);
        }
    }

    private void deleteImageDialog(int index) {
        if (!photos.isEnabled()) return;
        new DialogBuilder(R.string.delete, R.drawable.icon_baseline_delete_forever_24, this)
                .addText(R.string.report_problem_photos_delete)
                .setButtonShort(R.string.cancel, true, null)
                .setButtonRed(R.string.delete, true, view -> {
                    problemReport.removePhoto(index);
                    updateData();
                })
                .show();
    }

    private void chooseCategory() {
        DialogBuilder builder = new DialogBuilder(R.string.report_problem_category_action, -1, true, this);
        View[] views = Arrays.stream(getResources().getStringArray(R.array.report_problem_category_items))
                .sequential()
                .map(s -> {
                    @SuppressLint("DiscouragedApi")
                    int iconId = getResources().getIdentifier("category_" + s.replace(" ", "_").toLowerCase(), "drawable", getPackageName());
                    return DialogBuilder.generateButton(s, iconId, this, view -> chooseSubCategory(s, iconId, builder));
                }).toArray(View[]::new);
        builder
                .setConfirmBack(false)
                .addViewList(views)
                .show();
    }

    private void chooseSubCategory(String parentCategory, int iconId, DialogBuilder parentBuilder) {
        String resourceName = "report_problem_category_" + parentCategory.replace(" ", "_").toLowerCase() + "_items";
        @SuppressLint("DiscouragedApi")
        int resourceId = getResources().getIdentifier(resourceName, "array", getPackageName());
        DialogBuilder builder = new DialogBuilder(parentCategory, -1, true, this);
        View[] views = Arrays.stream(getResources().getStringArray(resourceId))
                .sequential()
                .map(s -> DialogBuilder.generateButton(s, iconId, this, view -> {
                    problemReport.setCategory(parentCategory + " ► " + s);
                    builder.dismiss();
                    parentBuilder.dismiss();
                    updateData();
                })).toArray(View[]::new);
        builder
                .setConfirmBack(false)
                .addViewList(views)
                .show();
    }

    private void sendReport() {
        boolean missing = false;

        if (problemReport.getCategory() == null) {
            ButtonDesigner.designErrorButton(category, this);
            missing = true;
        }

        if (problemReport.getLocation() == null) {
            ButtonDesigner.designErrorButton(location, this);
            missing = true;
        }

        if (missing) {
            ToastBuilder.show(this, R.string.report_problem_missing);
            return;
        }

        startLoading();

        problemReport.setDescription(description.getText() != null ? description.getText().toString() : "");

        String[] photoIds = new String[problemReport.getPhotos().size()];
        for (int i = 0; i < photoIds.length; i++) {
            photoIds[i] = UUID.randomUUID().toString() + ".jpg";
        }
        uploadPicturesThread = new UploadPicturesThread(problemReport.getPhotos().toArray(new Bitmap[0]), photoIds);
        uploadReportThread = new UploadReportThread(problemReport, photoIds, () -> {
            ToastBuilder.show(this, R.string.report_problem_success);
            finish();
        });
        uploadPicturesThread.start();
        uploadReportThread.start();
    }

    private synchronized void incUploadCounter() {
        uploadedPictures++;
    }

    private class UploadPicturesThread extends Thread {

        private final  FirebaseStorage storage = FirebaseStorage.getInstance();
        private final StorageReference storageRef = storage.getReference();
        private final Bitmap[] bitmaps;
        private final String[] ids;

        public UploadPicturesThread(Bitmap[] bitmaps, String[] ids) {
            this.bitmaps = bitmaps;
            this.ids = ids;
            if (bitmaps.length != ids.length) throw new IllegalArgumentException();
        }

        @Override
        public void run() {
            super.run();
            for (int i = 0; i < bitmaps.length; i++) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmaps[i].compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] data = stream.toByteArray();
                StorageReference storageReference = storage.getReference().child(ids[i]);
                storageReference.putBytes(data)
                        .addOnSuccessListener(taskSnapshot -> incUploadCounter())
                        .addOnFailureListener(ReportProblemActivity.this::onException);
            }
            System.out.println("Upload Pictures Finished");
        }
    }

    private interface FinishedListener {void onFinished();}
    private class UploadReportThread extends Thread {

        private final  FirebaseFirestore db = FirebaseFirestore.getInstance();
        private final  ProblemReport problemReport;
        private final String[] photoIds;
        private final FinishedListener finishedListener;

        public UploadReportThread(ProblemReport problemReport, String[] photoIds, FinishedListener finishedListener) {
            this.problemReport = problemReport;
            this.photoIds = photoIds;
            this.finishedListener = finishedListener;
        }

        @Override
        public void run() {
            super.run();

            Map<String, Object> data = new HashMap<>();

            data.put("photos", (List<String>) Arrays.asList(photoIds));
            data.put("description", problemReport.getDescription());
            DateCompat dateCompat = new DateCompat();
            data.put("date", (dateCompat.getDate()) + "." + (dateCompat.getMonth() + 1) + "." + (dateCompat.getYear() + 1900));
            data.put("location", new GeoPoint(problemReport.getLocation().latitude, problemReport.getLocation().longitude));
            data.put("category", problemReport.getCategory());

            System.out.println("Waiting for Pictures");

            while (uploadedPictures < problemReport.getPhotos().size()) {
                try {
                    //noinspection BusyWait
                    sleep(50);
                } catch (InterruptedException e) {
                    onException(e);
                }
            }

            System.out.println("Waiting for Pictures Done");

            DocumentReference docRef = db.collection("feedback").document(UUID.randomUUID().toString());

            docRef.set(data)
                    .addOnSuccessListener(aVoid -> finishedListener.onFinished())
                    .addOnFailureListener(ReportProblemActivity.this::onException);

        }
    }

    private void onException(Exception exception) {
        uploadPicturesThread.stop();
        uploadReportThread.stop();
        uploadPicturesThread = null;
        uploadReportThread = null;
        ToastBuilder.show(this, exception);
        stopLoading();
    }
}
