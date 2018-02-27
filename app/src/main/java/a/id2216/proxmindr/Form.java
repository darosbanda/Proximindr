package a.id2216.proxmindr;

import android.app.AlertDialog;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.dpro.widgets.WeekdaysPicker;


import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;

import a.id2216.proxmindr.ReminderPackage.Configuration.Interval;
import a.id2216.proxmindr.ReminderPackage.Configuration.Period;
import a.id2216.proxmindr.ReminderPackage.Configuration.ReminderConfiguration;
import a.id2216.proxmindr.ReminderPackage.Configuration.Week;
import a.id2216.proxmindr.ReminderPackage.Reminder;
import a.id2216.proxmindr.ReminderPackage.ReminderStorage;

import com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener;
import com.borax12.materialdaterangepicker.time.TimePickerDialog.OnTimeSetListener;

public class Form extends DialogFragment implements OnDateSetListener, OnTimeSetListener {
    ReminderStorage rstorage = ReminderStorage.getInstance();
    FirebaseHandler fbHandler = new FirebaseHandler();
    Period period;
    Interval interval;
    Week week;
    EditText dates;
    EditText times;
    ToggleButton datePicker;
    ToggleButton timePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Calendar now = Calendar.getInstance();

        final View view = inflater.inflate(R.layout.form_view, null);

        final EditText name = view.findViewById(R.id.editText);
        final EditText message = view.findViewById(R.id.editText2);
        dates = view.findViewById(R.id.period_dates);
        times = view.findViewById(R.id.interval_times);
        week = new Week();

        WeekdaysPicker widget = view.findViewById(R.id.weekdays);
        widget.setOnWeekdaysChangeListener((view1, clickedDayOfWeek, selectedDays) -> week.setDays(selectedDays));

        final ToggleButton recurring = view.findViewById(R.id.recurring);
        recurring.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                week.setDays(widget.getSelectedDays());
            }
            View layout = view.findViewById(R.id.extras);
            layout.setVisibility(b ? View.VISIBLE : View.GONE);
        });



        TimePickerDialog tpd = TimePickerDialog.newInstance(this, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE),
                true);

        timePicker = view.findViewById(R.id.my_time_picker);
        timePicker.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                times.setVisibility(View.GONE);
                interval = null;
            } else if (interval == null) {
                timePicker.setChecked(false);
                if (!tpd.isAdded()) {
                    tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
                }
            }
        });


        DatePickerDialog dpd = DatePickerDialog.newInstance(this, now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

       datePicker = view.findViewById(R.id.my_date_picker);
        datePicker.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!b) {
                dates.setVisibility(View.GONE);
                period = null;
            } else if (period == null) {
                datePicker.setChecked(false);
                if (!dpd.isAdded()) {
                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                }
            }
        });

        final ToggleButton departing = view.findViewById(R.id.departing);


        final double lat = getArguments().getDouble("lat");
        final double lng = getArguments().getDouble("lng");


        builder.setView(view)
                .setPositiveButton("Add reminder", (dialogInterface, i) -> {
                    if (recurring.isChecked() && week.getDays().size() == 0) {
                        Toast.makeText(getActivity(), "Please specify the days you want to be reminded.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (name.getText().equals("") || message.getText().equals("")) {
                        Toast.makeText(getActivity(), "Please add a title and a message for the reminder.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ReminderConfiguration config = getReminderConfiguration(recurring, departing);
                    String reminderName = name.getText().toString();
                    String reminderMessage = message.getText().toString();
                    Reminder r = new Reminder(reminderName, reminderMessage, lat, lng, config);
                    rstorage.addReminder(r);
                }).setNegativeButton("Cancel", (dialogInterface, i) -> Form.this.getDialog().cancel());

        return builder.create();
    }

    @NonNull
    private ReminderConfiguration getReminderConfiguration(ToggleButton recurring, ToggleButton departing) {
        ReminderConfiguration config = new ReminderConfiguration();
        if (recurring.isChecked()) {
            config.setWeek(week);
            config.setInterval(interval);
            config.setPeriod(period);
        }
        config.setRecurring(recurring.isChecked());
        config.setDeparting(departing.isChecked());
        return config;
    }


    @Override
    public void onDateSet(com.borax12.materialdaterangepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        LocalDate from = new LocalDate(year, monthOfYear + 1, dayOfMonth);
        LocalDate to = new LocalDate(yearEnd, monthOfYearEnd + 1, dayOfMonthEnd);
        period = new Period(from, to);
        dates.setText(String.format("%s and %s", period.getFrom(), period.getTo()));
        dates.setVisibility(View.VISIBLE);
        datePicker.setChecked(true);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {
        LocalTime from = new LocalTime(hourOfDay, minute);
        LocalTime to = new LocalTime(hourOfDayEnd, minuteEnd);
        interval = new Interval(from, to);
        String fromString = interval.getFrom().toString("HH:mm");
        String toString = interval.getTo().toString("HH:mm");
        times.setText(String.format("%s and %s", fromString, toString));
        times.setVisibility(View.VISIBLE);
        timePicker.setChecked(true);
    }
}
