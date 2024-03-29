package de.jonasaugust.justfairmobilityapp.data;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ProblemReport {

    private String category = null;
    private LatLng location = null;
    private final List<Bitmap> photos = new ArrayList<>();
    private String description = "";

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public List<Bitmap> getPhotos() {
        return photos;
    }

    public void addPhoto(Bitmap photo) {
        photos.add(photo);
    }

    public String getDescription() {
        return description;
    }

    public ProblemReport setDescription(String description) {
        if (description == null)
            description = "";
        this.description = description;
        return this;
    }

    public void removePhoto(int index) {
        photos.remove(index);
    }
}
