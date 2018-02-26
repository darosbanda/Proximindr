package a.id1212.tabsexample.Configuration;


import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;


public class Period {
    private LocalDate from;
    private LocalDate to;

    public Period(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    public boolean withinPeriod() {
        LocalDate currentDate = new LocalDate();
        return currentDate.compareTo(from) > 0 && currentDate.compareTo(to) < 0;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("M d y");
        return "Between " + sdf.format(from) + " and " + sdf.format(to);
    }
}
