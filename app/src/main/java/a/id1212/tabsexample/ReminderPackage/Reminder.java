package a.id1212.tabsexample.ReminderPackage;

import com.google.android.gms.maps.model.LatLng;

public class Reminder {
    private String name;
    private String message;
    private LatLng latLng;
    private boolean departing;

    public Reminder(String name, String message, LatLng latLng, boolean departing) {
        this.message = message;
        this.latLng = latLng;
        this.name = name;
        this.departing = departing;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeparting() {
        return departing;
    }

    public void setDeparting(boolean departing) {
        this.departing = departing;
    }
}
