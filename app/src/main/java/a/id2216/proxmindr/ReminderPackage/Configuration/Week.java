package a.id2216.proxmindr.ReminderPackage.Configuration;


import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by daros on 2018-02-26.
 */

public class Week {


    public List<Integer> days = new ArrayList<>();
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

    @Exclude
    public void setDays(List<Integer> days) {
        this.days = days;
    }



    @Override
    public String toString() {

        StringBuilder result = new StringBuilder("Days: ");

        for(Integer i : days) {
            result.append(map.get(i)).append(", ");
        }

        return result.substring(0, result.length()-2);

    }

    @Exclude
    public List<Integer> getDays() {
        return days;
    }
}
