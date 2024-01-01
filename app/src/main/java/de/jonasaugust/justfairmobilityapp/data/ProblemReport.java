package de.jonasaugust.justfairmobilityapp.data;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ProblemReport {

    private String category = null;
    private LatLng location = null;
    private List<String> photos = new ArrayList<>();
    private String description = "";

    public String getCategory() {
        return category;
    }

    public ProblemReport setCategory(String category) {
        this.category = category;
        return this;
    }

    public LatLng getLocation() {
        return location;
    }

    public ProblemReport setLocation(LatLng location) {
        this.location = location;
        return this;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public ProblemReport addPhoto(String photo) {
        photos.add(photo);
        return this;
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
}
