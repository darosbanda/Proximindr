package a.id1212.tabsexample.ReminderPackage;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class ReminderStorage {
    private static final ReminderStorage ourInstance = new ReminderStorage();

    public static ReminderStorage getInstance() {
        return ourInstance;
    }

    private List<Reminder> reminders = new ArrayList<>();
    private HashSet<ReminderListener> listeners = new HashSet<>();

    private ReminderStorage() {
        reminders.add(new Reminder("Electrum", "Reminder 1", new LatLng(59.404150, 17.949280), true));
        reminders.add(new Reminder("Gallerian", "Reminder 2", new LatLng(59.403283, 17.946557), false));
        reminders.add(new Reminder("Helenelund", "Reminder 3", new LatLng(59.409813, 17.962679), true));

    }

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
        removeNotification(r);
    }

    private void removeNotification(Reminder r) {
        listeners.forEach(listener -> {
            listener.onReminderRemoved(r);
        });
    }

}
