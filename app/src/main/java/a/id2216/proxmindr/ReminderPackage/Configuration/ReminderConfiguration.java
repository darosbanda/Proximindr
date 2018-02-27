package a.id2216.proxmindr.ReminderPackage.Configuration;


import com.google.firebase.database.Exclude;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class ReminderConfiguration {
    public Week week;
    public Interval interval;
    public Period period;
    public boolean recurring;
    public boolean departing;
    public String latestNotification;

    public ReminderConfiguration() {
    }

    public boolean isValid() {

        if (!recurring) {
            return latestNotification == null;
        }

        if (period != null && !period.withinPeriod()) {
            return false;
        }

        if (week != null && !withinInterval()) {
            return false;
        }

        return true;
    }

    @Exclude
    public Week getWeek() {
        return week;
    }

    @Exclude
    public void setWeek(Week week) {
        this.week = week;
    }

    @Exclude
    public Period getPeriod() {
        return period;
    }

    @Exclude
    public void setPeriod(Period period) {
        this.period = period;
    }

    @Exclude
    public boolean isRecurring() {
        return recurring;
    }

    @Exclude
    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    @Exclude
    public boolean isDeparting() {
        return departing;
    }

    @Exclude
    public void setDeparting(boolean departing) {
        this.departing = departing;
    }

    @Exclude
    public void setLatestNotification() {
        latestNotification = new LocalDate().toString();
    }

    @Exclude
    public Interval getInterval() {
        return interval;
    }

    @Exclude
    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public boolean withinInterval() {
        LocalTime time = new LocalTime();
        return interval == null || (time.isBefore(interval.getTo()) && interval.getFrom().isBefore(time));
    }

    @Override
    public String toString() {
        return "ReminderConfiguration{" +
                "week=" + week +
                ", interval=" + interval +
                ", period=" + period +
                ", recurring=" + recurring +
                ", departing=" + departing +
                ", latestNotification=" + latestNotification +
                '}';
    }
}
