package a.id2216.proxmindr.ReminderPackage.Configuration;


import com.google.firebase.database.Exclude;

import org.joda.time.LocalDate;


public class Period {
    public String from;
    public String to;

    public Period(LocalDate from, LocalDate to) {
        this.from = from.toString();
        this.to = to.toString();
    }

    public Period() {
    }


    public boolean withinPeriod() {
        LocalDate currentDate = new LocalDate();
        return currentDate.compareTo(LocalDate.parse(from)) > 0 && currentDate.compareTo(LocalDate.parse(to)) < 0;
    }


    @Exclude
    public LocalDate getFrom() {
        return LocalDate.parse(from);
    }

    @Exclude
    public LocalDate getTo() {
        return LocalDate.parse(to);
    }

    @Override
    public String toString() {

        return "Between " + from + " and " + to;
    }
}
