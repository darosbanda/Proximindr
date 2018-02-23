package a.id1212.tabsexample;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;

import a.id1212.tabsexample.ReminderPackage.Reminder;
import a.id1212.tabsexample.ReminderPackage.ReminderListener;
import a.id1212.tabsexample.ReminderPackage.ReminderStorage;


/**
 * A fragment that launches other parts of the demo application.
 */
public class Tab1 extends Fragment implements OnMapReadyCallback {


    static boolean active = false;
    ReminderStorage reminderStorage = ReminderStorage.getInstance();
    CircleOptions outerOptions;
    CircleOptions innerOptions;
    Circle outerCircle;
    Circle innerCircle;
    MapView mMapView;
    private GoogleMap googleMap;
    LocationManager locationManager;
    LocationListener locationListener = createLocationListener();
    LocationListener enteringCircle = alertIfEnteringCircle();
    ReminderListener listener = new ReminderListener() {
        @Override
        public void onReminderAdded(Reminder r) {
            MarkerOptions options = new MarkerOptions();
            options.position(r.getLatLng()).title(r.getName());
            googleMap.addMarker(options);
        }

        @Override
        public void onReminderRemoved(Reminder r) {
            googleMap.clear();
            renderReminders();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.tab1, container,
                false);
        mMapView = v.findViewById(R.id.mapView);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        setOnFABClickListener(fab);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        reminderStorage.subscribe(listener);

        outerOptions = new CircleOptions()
                .strokeWidth(5.0f)
                .strokeColor(Color.BLACK)
                .fillColor(Color.TRANSPARENT)
                .radius(100);

        innerOptions = new CircleOptions()
                .radius(2)
                .fillColor(Color.BLACK)
                .strokeColor(Color.BLACK);

        locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);


        registerInitialLocationListener();

        registerDistanceListener();

        mMapView.getMapAsync(this);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    private void setOnFABClickListener(FloatingActionButton fab) {
        fab.setOnClickListener(view -> {
            DialogFragment newFragment = new Form();
            Bundle args = new Bundle();
            if (innerCircle == null) {
                return;
            }
            args.putDouble("lat", innerCircle.getCenter().latitude);
            args.putDouble("lng", innerCircle.getCenter().longitude);
            newFragment.setArguments(args);
            newFragment.show(getActivity().getSupportFragmentManager(), "addReminder");
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setUpMap();
    }

    @SuppressLint("MissingPermission")
    public void setUpMap(){
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setOnMapClickListener(latLng -> {
            if (outerCircle != null)
                outerCircle.remove();
            if (innerCircle != null)
                innerCircle.remove();
            innerCircle = googleMap.addCircle(innerOptions.center(latLng));
            outerCircle = googleMap.addCircle(outerOptions.center(latLng));
        });
        renderReminders();
    }

    private void renderReminders() {
        reminderStorage.getReminders().forEach(reminder -> {
            googleMap.addMarker(new MarkerOptions().position(reminder.getLatLng()).title(reminder.getName()));
        });
    }

    private LocationListener createLocationListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() < 50) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    locationManager.removeUpdates(locationListener);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    private boolean inRangeForNotification(Location location, Location target) {
        float distance = location.distanceTo(target);
        return distance < 100 && distance > 90;
    }

    private boolean isHeadingToTarget(Location location, Location target) {
        return (location.getBearing() > location.bearingTo(target) - 10) && (location.getBearing() < location.bearingTo(target) + 10);
    }


    private boolean timeForNotification(Location location, Reminder r) {
        Location target = new Location("");
        target.setLatitude(r.getLatLng().latitude);
        target.setLongitude(r.getLatLng().longitude);
        boolean headingToTarget = isHeadingToTarget(location, target);
        boolean inRange = inRangeForNotification(location, target);
        if (inRange) {
            return (r.isDeparting() && !headingToTarget) || (!r.isDeparting() && headingToTarget);
        }
        return false;
    }

    private LocationListener alertIfEnteringCircle() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() < 50 && !active) {
                    reminderStorage.getReminders().forEach(reminder -> {
                        if (timeForNotification(location, reminder)) {
                            sendNotification(reminder);
                        }
                    });
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    private void sendNotification(Reminder r) {
        String CHANNEL_ID = "my_channel_01";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.common_full_open_on_phone)
                        .setContentTitle(r.getName())
                        .setContentText(r.getMessage());

        Context test = getActivity();
        if (test == null) {
            System.out.println("FAIL");
            return;
        }
        Intent resultIntent = new Intent(getActivity(), MainActivity.class);
        resultIntent.setAction("OPEN_TAB_2");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());

    }

    @SuppressLint("MissingPermission")
    private void registerInitialLocationListener() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void registerDistanceListener() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, enteringCircle);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, enteringCircle);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}