package a.id2216.proxmindr.Tabs;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import a.id2216.proxmindr.Form;
import a.id2216.proxmindr.MainActivity;
import a.id2216.proxmindr.R;
import a.id2216.proxmindr.ReminderPackage.Reminder;
import a.id2216.proxmindr.Listeners.ReminderListener;
import a.id2216.proxmindr.ReminderPackage.ReminderStorage;


/**
 * A fragment that launches other parts of the demo application.
 */
public class MapTab extends Fragment implements OnMapReadyCallback {


    private static final String TAG = "TAB1";
    static boolean active = false;
    ReminderStorage reminderStorage = ReminderStorage.getInstance();
    CircleOptions outerOptions;
    CircleOptions innerOptions;
    Circle outerCircle;
    Circle innerCircle;
    MapView mMapView;
    private GoogleMap googleMap;
    LocationManager locationManager;
    LocationListener notificationDispatcher = notificationDispatcher();
    ReminderListener listener = new ReminderListener() {
        @Override
        public void onReminderAdded(String key, Reminder r) {
            MarkerOptions options = new MarkerOptions();
            options.position(r.getLatLng()).title(r.getName());
            googleMap.addMarker(options);
        }

        @Override
        public void onReminderRemoved(String key) {
            googleMap.clear();
            renderReminders();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.map_tab, container,
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
                Toast.makeText(getActivity(), "Please select a location for a reminder", Toast.LENGTH_LONG).show();
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
        reminderStorage.getReminders().values().forEach(reminder -> {
            googleMap.addMarker(new MarkerOptions().position(reminder.getLatLng()).title(reminder.getName()));
        });
    }

    private LocationListener initialLocationListener() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location.getAccuracy() < 50) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                    locationManager.removeUpdates(this);
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
        return distance < 120 && distance > 80;
    }

    private boolean isHeadingToTarget(Location location, Location target) {
        float currentBearing = location.getBearing();
        float targetBearing = location.bearingTo(target);
        return (currentBearing > (targetBearing - 45)) && (currentBearing < (targetBearing + 45));
    }


    private boolean timeForNotification(Location location, Reminder r) {
        Location target = new Location("");
        target.setLatitude(r.getLatLng().latitude);
        target.setLongitude(r.getLatLng().longitude);
        boolean inRange = inRangeForNotification(location, target);
        if (inRange) {
            boolean headingToTarget = isHeadingToTarget(location, target);
            return (r.isDeparting() && !headingToTarget) || (!r.isDeparting() && headingToTarget);
        }
        return false;
    }

    private LocationListener notificationDispatcher() {
        return new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged: Acc: " + location.getAccuracy());
                if (location.getAccuracy() < 50 && !active) {
                    reminderStorage.getValidReminders().forEach(reminder -> {
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

        r.notificationSent();

    }

    @SuppressLint("MissingPermission")
    private void registerInitialLocationListener() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, initialLocationListener());
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, initialLocationListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void registerDistanceListener() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, notificationDispatcher);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, notificationDispatcher);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}