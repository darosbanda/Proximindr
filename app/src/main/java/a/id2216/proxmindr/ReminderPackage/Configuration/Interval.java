package a.id2216.proxmindr.ReminderPackage.Configuration;


import com.google.firebase.database.Exclude;

import org.joda.time.LocalTime;

public class Interval {
    public String from;
    public String to;

    public Interval(LocalTime from, LocalTime to) {
        this.from = from.toString();
        this.to = to.toString();
    }

    public Interval() {
    }

    @Exclude
    public LocalTime getFrom() {
        return LocalTime.parse(from);
    }

    @Exclude
    public LocalTime getTo() {
        return LocalTime.parse(to);
    }

    @Override
    public String toString() {
        return "Between " + LocalTime.parse(from).toString("HH:mm") + " and " + LocalTime.parse(to).toString("HH:mm");
    }
}
