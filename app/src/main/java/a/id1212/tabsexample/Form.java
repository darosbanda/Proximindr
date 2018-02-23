package a.id1212.tabsexample;

import android.app.AlertDialog;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;

import a.id1212.tabsexample.ReminderPackage.Reminder;
import a.id1212.tabsexample.ReminderPackage.ReminderStorage;

public class Form extends DialogFragment {
    ReminderStorage rstorage = ReminderStorage.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View test = inflater.inflate(R.layout.form_view, null);

        final EditText name = test.findViewById(R.id.editText);
        final EditText message = test.findViewById(R.id.editText2);
        final ToggleButton departing = test.findViewById(R.id.toggleButton);
        final double lat = getArguments().getDouble("lat");
        final double lng = getArguments().getDouble("lng");

        builder.setView(test)
                .setPositiveButton("Add reminder", (dialogInterface, i) -> {
                    Reminder r = new Reminder(name.getText().toString(),message.getText().toString(), new LatLng(lat,lng), departing.isChecked());
                    rstorage.addReminder(r);
                    System.out.println(name.getText().toString());
                    System.out.println(message.getText().toString());
                }).setNegativeButton("Cancel", (dialogInterface, i) -> Form.this.getDialog().cancel());

        return builder.create();
    }
}
