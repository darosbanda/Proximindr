package a.id2216.proxmindr.Listeners;

import a.id2216.proxmindr.ReminderPackage.Reminder;

/**
 * Created by daros on 2018-02-27.
 */

public interface FirebaseListener {

    void reminderAdded(String key, Reminder reminder);

    void reminderRemoved(String key);
}
