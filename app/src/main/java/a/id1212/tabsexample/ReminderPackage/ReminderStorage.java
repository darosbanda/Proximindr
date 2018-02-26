package a.id1212.tabsexample.ReminderPackage;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import a.id1212.tabsexample.Configuration.ReminderConfiguration;


public class ReminderStorage {
    private static final ReminderStorage ourInstance = new ReminderStorage();

    public static ReminderStorage getInstance() {
        return ourInstance;
    }

    private List<Reminder> reminders = new ArrayList<>();
    private HashSet<ReminderListener> listeners = new HashSet<>();


    public void addReminder(Reminder r) {
        reminders.add(r);
        addNotification(r);
    }

    private void addNotification(Reminder r) {
        listeners.forEach(listener -> {
            listener.onReminderAdded(r);
        });
    }

    public void subscribe(ReminderListener listener) {
        listeners.add(listener);
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void removeReminder(int reminderPosition) {
        Reminder r = reminders.remove(reminderPosition);
        removeNotification(reminderPosition);
    }

    private void removeNotification(int position) {
        listeners.forEach(listener -> {
            listener.onReminderRemoved(position);
        });
    }

    public List<Reminder> getValidReminders() {
        List<Reminder> validReminders = reminders.stream().filter(Reminder::isValid).collect(Collectors.toList());
        return validReminders;
    }
}
