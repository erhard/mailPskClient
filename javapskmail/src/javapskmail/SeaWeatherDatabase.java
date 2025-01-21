/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sebastian_pohl
 */
public class SeaWeatherDatabase {

    // first key is Day:UTC
    // second key is location name
    private TreeMap<String, TreeMap<String, SeaWeather>> internalDatabase;

    private TreeMap<String, Integer> locationWeekdays;

    public SeaWeatherDatabase() {
        internalDatabase = new TreeMap<String, TreeMap<String, SeaWeather>>();

        locationWeekdays = new TreeMap<String, Integer>();
    }

    public boolean isEmpty() {
        return internalDatabase.isEmpty();
    }

    public void writeFile(File file) {
        //System.out.print(file.exists());

        BufferedWriter fileWriter = null;
        try {
            FileWriter fstream = new FileWriter(file);
            fileWriter = new BufferedWriter(fstream);
            fileWriter.write(this.getReportAsString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public String toString() {
        String output = "";

        Iterator<String> iter = internalDatabase.keySet().iterator();

        while (iter.hasNext()) {
            String firstKey = iter.next();
            output += firstKey + "\n------\n";
            TreeMap<String, SeaWeather> tempTree = internalDatabase.get(firstKey);
            output += tempTree.values() + "\n------\n";
        }

        return output;
    }

    public String getReportAsString() {
        String out = "";
        Set<String> locationKeys = new HashSet<String>();
        Set<String> weekdayKeys = internalDatabase.keySet();

        //System.out.println("weekday Set: " + weekdayKeys);

        Iterator<String> weekdayIter = weekdayKeys.iterator();
        while (weekdayIter.hasNext()) {
            String weekdayKey = weekdayIter.next();

            //System.out.println("Weekday to be processed: " + weekdayKey);

            locationKeys.addAll(internalDatabase.get(weekdayKey).keySet());
        }

        //System.out.println("location keys: " + locationKeys);

        Iterator<String> locationIter = locationKeys.iterator();
        while (locationIter.hasNext()) {
            String key = locationIter.next();
            //System.out.println("key processed (location name): " + key);
            out += getReportAsString(key);
        }
        return out;
    }

    public String getReportAsString(String locationName) {
        //System.out.println("location weekday: " + locationWeekdays.get(locationName));

        return getReportAsString(locationName, locationWeekdays.get(locationName));
    }

    public String getReportAsString(String locationName, int startValue) {

        String out = "";

        Set<String> currKeysUnsorted = internalDatabase.keySet();

        TreeSet<String> currKeys = new TreeSet(new WeekDayComparator(startValue));

        currKeys.addAll(currKeysUnsorted);

        Iterator<String> iter = currKeys.iterator();

        boolean reportNotEmpty = false;

        boolean headerWritten = false;

        while (iter.hasNext()) {

            TreeMap<String, SeaWeather> tempTree = get(iter.next());

            if (!headerWritten && tempTree != null && tempTree.containsKey(locationName)) {

                out += "                      " + locationName + " ";

                out += "(" + tempTree.get(locationName).getLocationLat().getDirectedDecimal() + " ";

                out += tempTree.get(locationName).getLocationLong().getDirectedDecimal() + ") \n";

                out += "   Tag Zeit Windrichtung in 10m Windgeschw. Boeen 10m Wellenhoehe   Wetter \n";

                out += "                   Hoehe            10m \n";

                out += "       UTC       Windrose        Beaufort   Beaufort       m      Textkuerzel \n";

                headerWritten = true;

            }

            if (tempTree.containsKey(locationName)) {

                reportNotEmpty = true;

                out += tempTree.get(locationName).getOriginalString() + "\n";

            }

        }

        if (reportNotEmpty)
            return out;
        else
            return "";

    }

    public Collection<TreeMap<String, SeaWeather>> values() {
        return internalDatabase.values();
    }

    public Set<String> keySet() {
        return internalDatabase.keySet();
    }

    public void deleteLocation(String locationName) {

        Set<String> weekdayKeys = internalDatabase.keySet();

        Iterator<String> weekdayIter = weekdayKeys.iterator();

        while (weekdayIter.hasNext()) {

            String weekdayKey = weekdayIter.next();

            if (internalDatabase.get(weekdayKey).containsKey(locationName)) {
                internalDatabase.get(weekdayKey).remove(locationName);               
            }
        }

        locationWeekdays.remove(locationName);
    }

    public void putWeekDay(String locationName, int start) {
        locationWeekdays.put(locationName, new Integer(start));
    }

    public void put(SeaWeather s) {
        String firstKey = s.getWeekDay() + ":" + s.getUTC();
        String secondKey = s.getLocationName();

        if (internalDatabase.containsKey(firstKey)) {
            TreeMap<String, SeaWeather> tempTree = internalDatabase.get(firstKey);
            if (tempTree != null) {
                tempTree.put(secondKey, s);
            } else {
                tempTree = new TreeMap<String, SeaWeather>();
                tempTree.put(secondKey, s);
                internalDatabase.put(firstKey, tempTree);
            }
        } else {
            TreeMap<String, SeaWeather> tempTree = new TreeMap<String, SeaWeather>();
            tempTree.put(secondKey, s);
            internalDatabase.put(firstKey, tempTree);
        }


    }

    public TreeMap<String, SeaWeather> get(String str) {
        return internalDatabase.get(str);
    }


    public static void main(String[] args) {
        try {
            SeaWeatherParser parser = new SeaWeatherParser(new File("wx"));

            parser.inject("                      GOLFE-LION (42.17N 4.47E) WT: 18 C\n" +
                "   Tag Zeit Windrichtung in 10m Windgeschw. Boeen 10m Wellenhoehe   Wetter\n" +
                "                  Hoehe            10m\n" +
                "      UTC       Windrose        Beaufort   Beaufort       m      Textkuerzel\n" +
                "   Di   12           S               5                    1.5");

            System.out.println(parser.getInternalDatabase().getReportAsString());

            parser.getInternalDatabase().writeFile(new File ("wx2"));

            parser = new SeaWeatherParser(new File("wx2"));

            System.out.println(parser.getInternalDatabase().getReportAsString());

        } catch (IOException ex) {
            Logger.getLogger(SeaWeatherDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
