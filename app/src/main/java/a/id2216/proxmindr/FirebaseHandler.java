package a.id2216.proxmindr;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import a.id2216.proxmindr.Listeners.FirebaseListener;
import a.id2216.proxmindr.ReminderPackage.Reminder;


/**
 * Created by daros on 2018-02-27.
 */

public class FirebaseHandler {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();



    public void storeReminder(Reminder r) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = database.getReference("users");
        ref.child(currentUser.getUid()).child("reminders").push().setValue(r);
    }

    public void deleteReminder(String key) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = database.getReference("users");
        ref.child(currentUser.getUid()).child("reminders").child(key).removeValue();
    }

    public void fetchReminders(FirebaseListener listener) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = database.getReference("users");
        ref.child(currentUser.getUid()).child("reminders").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Reminder reminder = dataSnapshot.getValue(Reminder.class);
                listener.reminderAdded(key, reminder);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                listener.reminderRemoved(key);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
