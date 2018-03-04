package a.id2216.proxmindr.ReminderPackage;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import a.id2216.proxmindr.FirebaseHandler;
import a.id2216.proxmindr.Listeners.FirebaseListener;
import a.id2216.proxmindr.Listeners.ReminderListener;


public class ReminderStorage {
    private static ReminderStorage ourInstance;
    private FirebaseHandler fbHandler;

    public static ReminderStorage getInstance() {
        if (ourInstance == null) {
            ourInstance = new ReminderStorage(new FirebaseHandler());
        }
        return ourInstance;
    }

    private Map<String, Reminder> reminders = new HashMap<>();
    private HashSet<ReminderListener> listeners = new HashSet<>();

    public ReminderStorage (FirebaseHandler fbHandler) {
        this.fbHandler = fbHandler;

        this.fbHandler.fetchReminders(new FirebaseListener() {
            @Override
            public void reminderAdded(String key, Reminder reminder) {
                reminders.put(key, reminder);
                notifyAddition(key, reminder);
            }

            @Override
            public void reminderRemoved(String key) {
                reminders.remove(key);
                notifyDeletion(key);
            }
        });
    }

    private void notifyDeletion(String key) {
        listeners.forEach(listener -> {
            listener.onReminderRemoved(key);
        });
    }


    public void addReminder(Reminder r) {
        fbHandler.storeReminder(r);
    }

    private void notifyAddition(String key, Reminder r) {
        listeners.forEach(listener -> {
            listener.onReminderAdded(key, r);
        });
    }

    public void subscribe(ReminderListener listener) {
        listeners.add(listener);
    }

    public Map<String, Reminder> getReminders() {
        return reminders;
    }

    public void removeReminder(String key) {
        fbHandler.deleteReminder(key);
    }

    public List<Reminder> getValidReminders() {
        return reminders.values().stream().filter(Reminder::isValid).collect(Collectors.toList());
    }

    public static void destroy() {
        ourInstance = null;
    }
}
