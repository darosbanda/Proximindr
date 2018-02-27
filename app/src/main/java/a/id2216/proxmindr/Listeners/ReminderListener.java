package a.id2216.proxmindr.Listeners;

import a.id2216.proxmindr.ReminderPackage.Reminder;

public interface ReminderListener {

    void onReminderAdded(String key, Reminder r);
    void onReminderRemoved(String key);
}
