package a.id2216.proxmindr.ReminderPackage;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import a.id2216.proxmindr.ReminderPackage.Configuration.ReminderConfiguration;

public class Reminder {
    public String name;
    public String message;
    public double lat;
    public double lng;
    public ReminderConfiguration config;

    public Reminder(String name, String message, double lat, double lng, ReminderConfiguration config) {
        this.message = message;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.config = config;
    }

    public Reminder() {
    }

    @Exclude
    public String getMessage() {
        return message;
    }

    @Exclude
    public void setMessage(String message) {
        this.message = message;
    }

    @Exclude
    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    @Exclude
    public void setLatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }


    @Exclude
    public String getName() {
        return name;
    }

    @Exclude
    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public boolean isValid() {
        return config.isValid();
    }


    @Exclude
    public boolean isDeparting() {
        return config.isDeparting();
    }

    @Exclude
    public ReminderConfiguration getConfig() {
        return config;
    }

    public void notificationSent() {
        config.setLatestNotification();
    }


}
