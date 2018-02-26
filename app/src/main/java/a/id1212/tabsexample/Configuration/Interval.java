package a.id1212.tabsexample.Configuration;


import org.joda.time.LocalTime;

public class Interval {
    LocalTime from;
    LocalTime to;

    public Interval(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalTime getFrom() {
        return from;
    }

    public LocalTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "Between " + from.toString("HH:mm") + " and " + to.toString("HH:mm");
    }
}
