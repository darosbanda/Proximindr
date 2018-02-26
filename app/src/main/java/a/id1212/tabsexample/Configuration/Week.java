package a.id1212.tabsexample.Configuration;


import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by daros on 2018-02-26.
 */

public class Week {


    private List<Integer> days;
    static private Map<Integer, String> map = new HashMap<>();
    static {
        map.put(2, "Monday");
        map.put(3, "Tuesday");
        map.put(4, "Wednesday");
        map.put(5, "Thursday");
        map.put(6, "Friday");
        map.put(7, "Saturday");
        map.put(1, "Sunday");

    }

    public Week() {

    }


    public void setDays(List<Integer> days) {
        this.days = days;
    }



    @Override
    public String toString() {

        String result = "Days: ";

        for(Integer i : days) {
            result += map.get(i) + ", ";
        }

        return result.substring(0, result.length()-2);

    }


    public List<Integer> getDays() {
        return days;
    }
}
