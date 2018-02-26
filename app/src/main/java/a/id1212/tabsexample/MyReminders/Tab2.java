package a.id1212.tabsexample.MyReminders;



import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import a.id1212.tabsexample.Configuration.ReminderConfiguration;
import a.id1212.tabsexample.R;
import a.id1212.tabsexample.ReminderPackage.Reminder;
import a.id1212.tabsexample.ReminderPackage.ReminderListener;
import a.id1212.tabsexample.ReminderPackage.ReminderStorage;


public class Tab2 extends Fragment {



    ReminderStorage reminderStorage = ReminderStorage.getInstance();
    View rootView;
    ExpandableListView lv;
    private List<String> groups = new ArrayList<>();
    private HashMap<String, List<String>> children = new HashMap<>();
    private ReminderListener listener = new ReminderListener() {
        @Override
        public void onReminderAdded(Reminder r) {
            storeReminder(r);
            lv.setAdapter(new ExpandableListAdapter(groups, children, LayoutInflater.from(getActivity())));
        }
        @Override
        public void onReminderRemoved(int position) {
            removeReminder(position);
        }
    };

    private void removeReminder(int position) {
        children.remove(groups.get(position));
        groups.remove(position);
    }


    private void storeReminder(Reminder r) {
        Geocoder geo = new Geocoder(getActivity());
        ReminderConfiguration config = r.getConfig();
        groups.add(r.getName());
        List<String> temp = new ArrayList<>();
        temp.add(r.getMessage());
        String address = "Could not find address";
        try {
            address = geo.getFromLocation(r.getLatLng().latitude, r.getLatLng().longitude, 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        temp.add(address);
        temp.add("Remind me on: " + (r.isDeparting() ? "Departure" : "Arrival"));


        if (!config.isOnce()) {
            temp.add(config.getWeek().toString());
            if (config.getInterval() != null) {
                temp.add(config.getInterval().toString());
            }
            if (config.getPeriod() != null) {
                temp.add(config.getPeriod().toString());
            }
        }
        children.put(r.getName(), temp);
    }

    private void getReminders() {

        List<Reminder> reminders = reminderStorage.getReminders();
        reminders.forEach(this::storeReminder);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reminderStorage.subscribe(listener);
        getReminders();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2, container, false);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv = view.findViewById(R.id.expListView);
        lv.setAdapter(new ExpandableListAdapter(groups, children, LayoutInflater.from(getActivity())));
        lv.setGroupIndicator(null);
    }


}
