/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sebastian_pohl
 */
public class SeaWeatherParser {

    /**
     * @return the parsed
     */
    public boolean isParsed() {
        return parsed;
    }

    /**
     * @return the internalDatabase
     */
    public SeaWeatherDatabase getInternalDatabase() {
        return internalDatabase;
    }

    public enum LineType {
        DATE_LINE,
        HEAD_LINE,
        DATA_LINE,
        WASTE_LINE
    }

    private boolean parsed = false;

    private SeaWeatherDatabase internalDatabase;

    private int currentStart = 0;

    private String currentLocationName = null;

    private MapCoordinate currentLocationLat = null;
    private MapCoordinate currentLocationLong = null;

    private String originalString = null;

    public SeaWeatherParser() {

        originalString = "";
        internalDatabase = new SeaWeatherDatabase();

    }

    public SeaWeatherParser(String str) {

        this();

        originalString = str;
        parsed = false;

    }

    public SeaWeatherParser(File file) throws IOException {

        this();

        // System.out.println("Parsing file " + file.getName());

        BufferedReader fileScanner = new BufferedReader(new FileReader(file));

        String line;

        while ((line = fileScanner.readLine()) != null) {
            //System.out.println("injected line: " + line + "(end)");
            // originalString += line + "\n";
            this.inject(line + "\n");
        }

        parseThis();

        //System.out.println(internalDatabase.isEmpty());
    }

    public void inject(String str) {

        originalString += str;
        parsed = false;

        parseThis();

    }

    public void inject(File file) throws IOException {

        BufferedReader fileScanner = new BufferedReader(new FileReader(file));

        String line;

        while ((line = fileScanner.readLine()) != null) {
            originalString += line + "\n";
            this.inject(line + "\n");
        }

        parseThis();

    }

    public void resetDatabase() {
        internalDatabase = new SeaWeatherDatabase();
    }

    public void parseThis() {
        // SeaWeatherDatabase tempBase = new SeaWeatherDatabase();

        String[] dissected = originalString.split("\n");

        for (int i = 0; i < dissected.length; i++) {

           //System.out.println("dissected line: " + identifyLineType(dissected[i]));

           boolean screwed = (currentLocationName == null || currentLocationLong == null ||
                   currentLocationLat == null);

           switch(identifyLineType(dissected[i])) {
                case DATE_LINE: parseDateLine(dissected[i]);
                    break;
                case HEAD_LINE: parseHeadLine(dissected[i]);
                    break;
                case DATA_LINE: if (screwed) break;
                    SeaWeather tempSea = parseDataLine(dissected[i]);
                    internalDatabase.put(tempSea);
                    internalDatabase.putWeekDay(currentLocationName, currentStart);
                    break;
                case WASTE_LINE: if (screwed) break; // do nothing
                    break;
                default: break;
            }
        }

        parsed = true;

        // internalDatabase = tempBase;
    }

    private LineType identifyLineType(String str) {

        // check for head line
        Pattern p = Pattern.compile(".*\\(.*\\).*");
        Matcher m = p.matcher(str);

        if (m.matches()) {
            //System.out.println("Head line.");
            return LineType.HEAD_LINE;
        }

        // check for data line
        p = Pattern.compile("\\s*(?:(?:Mo)|(?:Di)|(?:Mi)|(?:Do)|(?:Fr)|(?:Sa)|(?:So))\\s*\\d{2}.*");
        m = p.matcher(str);

        if (m.matches())
            return LineType.DATA_LINE;

        return LineType.WASTE_LINE;
    }

    private int parseDateLine(String str) {

        return 0;

    }

    // decodes a head line
    private void parseHeadLine(String str) {

        Pattern p = Pattern.compile("\\s*([^\\s]*)\\s+\\(([\\d\\.]*)([N,S]{1})\\s([\\d\\.]*)([E,W]{1})\\).*");

        //System.out.println("Matched string: " + str);

        Matcher m = p.matcher(str);

        if (m.matches()) {
            //System.out.println("Good news, everyone.");
//            for (int i = 1; i <= m.groupCount(); i++) {
//                System.out.println(m.group(i));
//            }


            currentLocationName = m.group(1);

            currentLocationLat = new MapCoordinate(new Double(m.group(2)), m.group(3).charAt(0));

            currentLocationLong = new MapCoordinate(new Double(m.group(4)), m.group(5).charAt(0));

            internalDatabase.deleteLocation(currentLocationName);            
        }
    }

    // decodes a data line
    private SeaWeather parseDataLine(String str) {

        SeaWeather temp = null;

        int l = str.length();

        String filler = "";

        for (int i = 0; i < 80 - l; i++) {
            filler += " ";
        }

        str += filler;

        String day = str.substring(2,5).trim();
        String UTC = str.substring(6,10).trim();
        String direction = str.substring(11,30).trim();
        String speed = str.substring(31,42).trim();
        String gust = str.substring(43,52).trim();
        String waveHeight = str.substring(53,65).trim();
        String shortWeather = str.substring(65,77).trim();

        // System.out.println("current location " + currentLocationName);

        temp = new SeaWeather(currentLocationName, currentLocationLat, currentLocationLong,
                day, UTC, direction, speed, gust, waveHeight, shortWeather, str);

        return temp;

    }

    public static void main(String[] args) {

        // Pattern p = Pattern.compile("(?:(?:Mo)|(?:Di)){1,3}");
        // Matcher m = p.matcher("MoDiMo");

        // System.out.println(m.matches());

        String testString = "                      BALEARES-SW (38.96N 0.87E) WT: 22 C\n" +
                "   Tag Zeit Windrichtung in 10m Windgeschw. Boeen 10m Wellenhoehe   Wetter\n" +
                "                   Hoehe            10m\n" +
                "       UTC       Windrose        Beaufort   Beaufort       m      Textkuerzel\n" +
                "   Di   12         SE-S             3-4                    1\n" +
                "   Di   18           S               3                    0.5\n" +
                "   Mi   00           S               5                     1\n" +
                "   Mi   06          SW               6          7          1\n" +
                "   Mi   12         SW-W              4                    1.5\n" +
                "   Mi   18         W-NW              5                    1.5\n" +
                "   Do   00         W-NW              5                     1\n" +
                "   Do   06         SW-W              5                    1.5\n" +
                "   Do   12         SW-W             6-7        7-8        1.5\n" +
                "   Do   18           N              4-5        6-7         2\n" +
                "   Fr   00         W-NW             3-4                   1.5\n" +
                "                                      BALEARES-NE (40.58N 2.90E) WT: 21 C\n" +
                "   Tag Zeit Windrichtung in 10m Windgeschw. Boeen 10m Wellenhoehe   Wetter\n" +
                "                   Hoehe            10m\n" +
                "       UTC       Windrose        Beaufort   Beaufort       m      Textkuerzel\n" +
                "   Di   12           S              4-5                   1.5\n" +
                "   Di   18         SE-S              5                     1\n" +
                "   Mi   00         SE-S              5                     1\n" +
                "   Mi   06         S-SW              4                    1.5\n" +
                "   Mi   12         S-SW             5-6        6-7         2          TS\n" +
                "   Mi   18         SW-W             4-5                    2\n" +
                "   Do   00         SW-W             2-3                   1.5\n" +
                "   Do   06          NW              2-3                    1\n" +
                "   Do   12         SW-W              4                     1         RAIN\n" +
                "   Do   18           N              4-5                    1\n" +
                "   Fr   00           N               3                    1.5;\n";

        SeaWeatherParser test = new SeaWeatherParser(testString);
        test.parseThis();

        System.out.println(test.getInternalDatabase());

    }

}
