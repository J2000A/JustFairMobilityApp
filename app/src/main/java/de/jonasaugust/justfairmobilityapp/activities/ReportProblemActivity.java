package de.jonasaugust.justfairmobilityapp.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.net.URL;

import de.jonasaugust.justfairmobilityapp.R;
import de.jonasaugust.justfairmobilityapp.data.ProblemReport;
import de.jonasaugust.justfairmobilityapp.helpers.Umrechner;

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
                    LinearLayout.LayoutParams.MATCH_PARENT, Umrechner.dpToPx(80)
            );
            layoutParams.setMargins(0, Umrechner.dpToPx(8), 0, 0);
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
