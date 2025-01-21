/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.util.Comparator;

/**
 *
 * @author sebastian_pohl
 */
public class WeekDayComparator implements Comparator<String> {

    int startValue = 1; // Monday

    public WeekDayComparator() {
        super();
    }

    public WeekDayComparator(int start) {
        this();

        startValue = start;
    }

    private int translateWeekday(String str) {

        if (str.equals("Mo")) return 1;
        if (str.equals("Di")) return 2;
        if (str.equals("Mi")) return 3;
        if (str.equals("Do")) return 4;
        if (str.equals("Fr")) return 5;
        if (str.equals("Sa")) return 6;
        if (str.equals("So")) return 7;

        return 0;

    }

    private int translateWeekdayWithStart(String str) {

        int translate = translateWeekday(str);

        if (translate < this.startValue)
            translate += 7;

        return translate;

    }

    private int translateUTC(String str) {

        Integer temp = new Integer(str);

        return temp;

    }

    public int compare(String o1, String o2) {

        String w1 = o1.substring(0,2);
        String w2 = o2.substring(0,2);

        String UTC1 = o1.substring(3,5);
        String UTC2 = o2.substring(3,5);

        if (translateWeekdayWithStart(w1) < translateWeekdayWithStart(w2))
            return -1;

        if (translateWeekdayWithStart(w1) > translateWeekdayWithStart(w2))
            return 1;

        if (translateUTC(UTC1) < (translateUTC(UTC2))) {
            return -1;
        } else
            if (translateUTC(UTC1) > (translateUTC(UTC2))) {
                return 1;
            } else
                return 0;

    }

}
