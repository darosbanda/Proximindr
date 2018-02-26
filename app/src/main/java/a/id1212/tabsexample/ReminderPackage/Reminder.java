package a.id1212.tabsexample.ReminderPackage;

import com.google.android.gms.maps.model.LatLng;

import a.id1212.tabsexample.Configuration.ReminderConfiguration;

public class Reminder {
    private String name;
    private String message;
    private LatLng latLng;
    private ReminderConfiguration config;

    public Reminder(String name, String message, LatLng latLng, ReminderConfiguration config) {
        this.message = message;
        this.latLng = latLng;
        this.name = name;
        this.config = config;
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

    public boolean isValid() {
        return config.isValid();
    }

    public boolean isDeparting() {
        return config.isDeparting();
    }

    public ReminderConfiguration getConfig() {
        return config;
    }

    public void notificationSent() {
        config.setLatestNotification();
    }
}
