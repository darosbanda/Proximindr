package a.id1212.tabsexample.Configuration;


import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

public class ReminderConfiguration {
    Week week;
    Interval interval;
    Period period;
    boolean once;
    boolean departing;
    LocalDate latestNotification;


    public boolean isValid() {

        if (once) {
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


    public Week getWeek() {
        return week;
    }

    public void setWeek(Week week) {
        this.week = week;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public boolean isOnce() {
        return once;
    }

    public void setOnce(boolean once) {
        this.once = once;
    }

    public boolean isDeparting() {
        return departing;
    }

    public void setDeparting(boolean departing) {
        this.departing = departing;
    }

    public void setLatestNotification() {
        latestNotification = new LocalDate();
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public boolean withinInterval() {
        LocalTime time = new LocalTime();
        return time.isBefore(interval.to) && interval.from.isBefore(time);
    }

    @Override
    public String toString() {
        return "ReminderConfiguration{" +
                "week=" + week +
                ", interval=" + interval +
                ", period=" + period +
                ", once=" + once +
                ", departing=" + departing +
                ", latestNotification=" + latestNotification +
                '}';
    }
}
