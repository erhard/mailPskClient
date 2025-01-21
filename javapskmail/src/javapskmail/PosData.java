/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.awt.Rectangle;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sebastian_pohl
 */
public class PosData implements Comparable<PosData> {

    public final static int POSDATA_INDEFINIT = -1;
    public final static int POSDATA_STANDARD = 0;
    public final static int POSDATA_APRS = 1;
    public final static int POSDATA_INTERMAR_CLIENT = 2;

    // where does this pos data item come from ?
    // look deeper into PosData constants
    private int origin = -1;

    private String originalString = "";

    private String callsign;
    private String date;
    private String time;

    private String boardDate;
    private String boardTime;

    private MapCoordinate latitude;
    private MapCoordinate longitude;

    // APRS symbol, default is none
    private String aprs = "none";

    // rectangle for GUI containment
    private Rectangle guiRect = null;

    private boolean dataIsGood = false;

    private String optional = "";
    private boolean optionalFlag = false;

    // c = 0 -> Latitude
    // c = 1 -> Longitude
    private MapCoordinate parseCoordinate(String str, int c, int mode) throws IOException, NumberFormatException {
        //System.out.println(str + " " + c + " " + mode);
        switch (mode) {
            case 0:
                Pattern p = Pattern.compile("((?>-){0,1}\\d+)''(\\d+)'(\\d+)'");
                Matcher m = p.matcher(str);

                Integer degree = new Integer("0");
                Integer minute = new Integer("0");
                Integer second = new Integer("0");
                char temp = 'u';

                if (m.matches()) {
                    degree = new Integer(m.group(1));
                    minute = new Integer(m.group(2));
                    second = new Integer(m.group(3));

                    if (degree.intValue() < 0) {
                        if (c == 0) {
                            temp = 'S';
                        }
                        if (c == 1) {
                            temp = 'W';
                        }
                    } else {
                        if (c == 0) {
                            temp = 'N';
                        }
                        if (c == 1) {
                            temp = 'E';
                        }
                    }
                } else {
                    throw new IOException("wrong coordinate format");
                }

                return new MapCoordinate(Math.abs(degree.intValue()), minute.intValue(),
                        second.intValue(), temp);
            // Koordinate steht als dezimal bereit im str fpr PSKmail Report
            // Standard
            case 1:
                Double temp2 = new Double(str);
                char temp3 = 'u';
                if (temp2 < 0) {
                    if (c == 0) {
                        temp3 = 'S';
                    }
                    if (c == 1) {
                        temp3 = 'W';
                    }
                } else {
                    if (c == 0) {
                        temp3 = 'N';
                    }
                    if (c == 1) {
                        temp3 = 'E';
                    }
                }
                return new MapCoordinate(Math.abs(temp2), temp3);
            default:
                return new MapCoordinate(0, 0, 0, 'N');
        }
    }

    public PosData(String str, int mode) throws IOException, NumberFormatException {

        Pattern p;
        Matcher m;

        originalString = str;

        //System.out.println(str);

        switch (mode) {
            case POSDATA_STANDARD:
                p = Pattern.compile("([^;]+);([^;]+);([^;]+);([^;]+);([^;]+)(?>;(.*)){0,1}");
                m = p.matcher(str);

                if (m.matches()) {
                    origin = mode;
                    // the data found is good
                    dataIsGood = true;
                    // first group is Callsign
                    callsign = m.group(1);
                    // second group is Date
                    date = m.group(2);
                    // third group is Time
                    time = m.group(3);
                    // fourth group is Latitude
                    //System.out.println(m.group(4));
                    latitude = parseCoordinate(m.group(4), 0, 0);
                    // fifth group is Longitude
                    longitude = parseCoordinate(m.group(5), 1, 0);
                    // sixth group is either null or optional string
                    if (m.group(6) != null) {
                        optional = m.group(6);
                        optionalFlag = true;
                    }
                } else {
                    throw new IOException("wrong format in PosData Parser, mode " + mode);
                }
                break;
            case POSDATA_APRS:
                p = Pattern.compile("(\\d+);([^;]+);([^;]+);([^;]+);([^;]+);([^;]+);(?>(.*)){0,1}");
                m = p.matcher(str);
                if (m.matches()) {
                    origin = mode;
                    // the data found is good
                    dataIsGood = true;
                    // second group is Callsign
                    callsign = m.group(2);
                    // third group is Longitude
                    longitude = parseCoordinate(m.group(3),1,1);
                    // fourth group is Latitude
                    latitude = parseCoordinate(m.group(4),0,1);
                    // fifth group is APRS Symbol
                    aprs = m.group(5);
                    // sixth group contains date and time
                    //date = m.group(6);
                    //time = m.group(6);
                    date = parseDateTime(m.group(6),0,1);
                    time = parseDateTime(m.group(6),1,1);
                    // seventh group contains optional comment
                    if (m.group(7) != null) {
                        optional = m.group(7);
                        optionalFlag = true;
                    }
                } else {
                    throw new IOException("wrong format in PosData Parser, mode " + mode);
                }
                break;
            case POSDATA_INTERMAR_CLIENT:
                    //p = Pattern.compile("00\\S(\\S+)26 !(\\S+)([N,S]{1})/(\\S+)([E,W]{1})(\\S{1})/([^/]+)/(.*)");
                    p = Pattern.compile("([^;]+);00\\S(\\S+):26 !(\\S+)([N,S]{1})(\\S)(\\S+)([E,W]{1})(\\S{1})(.*)\\d{4}");
                    m = p.matcher(str);
                    if (m.matches()) {
                        origin = mode;
                        // the data found is good
                        dataIsGood = true;
                        // first group is the date/time
                        date = parseDateTime(m.group(1),0,1);
                        time = parseDateTime(m.group(1),1,1);
                        // first group is callsign
                        callsign = m.group(2);
                        // second and third group is latitude and latitude sign
                        latitude = new MapCoordinate((new Double(m.group(3))) / 100, m.group(4).charAt(0));
                        // fourth group is symbol for first, second or overlay APRS symbol table
                        // fifth and sixth group is longitude and longitude sign
                        longitude = new MapCoordinate((new Double(m.group(6)))/ 100, m.group(7).charAt(0));
                        // seventh group is the APRS symbol
                        aprs = m.group(5) + m.group(8);
                        // seventh group is the optional text (status + course/speed + wx)
                        if (m.group(9) != null) {
                            optional = m.group(9);
                            optionalFlag = true;
                        }
                        // System.out.println("Success!");
                    } else {
                        throw new IOException("wrong format in PosData Parser, mode " + mode);
                    }
                break;
            default:
                break;
        }

        calculate_Board();
    }

    private void calculate_Board() {

        double signedLongitude = longitude.getSignedDecimal();
        int summand = 0;

        if (signedLongitude < 0)
            summand = ((int) signedLongitude) / 15;
        else
            summand = ((int) signedLongitude + 15) / 15;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date posDataDate = new Date();
        try {
            posDataDate = dateFormat.parse(date + " " + time);
        } catch(Exception e) {
            e.printStackTrace();
        }

        long newTime = posDataDate.getTime() + summand * 3600000;

        Date newDate = new Date(newTime);

        String newString = dateFormat.format(newDate);

        boardDate = newString.substring(0,10);
        boardTime = newString.substring(11,19);
    }

    private String parseDateTime(String str, int part, int mode) throws IOException {
        Pattern p;
        Matcher m;
        p = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2}:\\d{2})");
        m = p.matcher(str);
        if (m.matches()) {
            switch (mode) {
                case 1:
                    switch (part) {
                        case 0:
                            return m.group(1);
                        case 1:
                            return m.group(2);
                        default:
                            break;
                    }
                    break;
                default:
                    return "";
            }
        } else {
            throw new IOException("time date format wrong");
        }
        return "";
    }

    // constructor parses the incoming string according to PSKmail format
    public PosData(String str) throws IOException, NumberFormatException {
        Pattern p;
        Matcher m;

        // ----------- first kind of Pos Reports -------------
        p = Pattern.compile("([^;]+);([^;]+);([^;]+);([^;]+);([^;]+)(?>;(.*)){0,1}");
        m = p.matcher(str);

        if (m.matches()) {
            //Decode(str, POSDATA_STANDARD);
        }
        // ------------ second kind of Pos Report--------------
        p = Pattern.compile("(\\d+);([^;]+);([^;]+);([^;]+);([^;]+);([^;]+);(?>(.*)){0,1}");
        m = p.matcher(str);
        if (m.matches()) {
            //Decode(str, POSDATA_APRS);
        }

        p = Pattern.compile("([^;]+);00\\S(\\S+):26 !(\\S+)([N,S]{1})(\\S)(\\S+)([E,W]{1})(\\S{1})(.*)\\d{4}");
        m = p.matcher(str);
        if (m.matches()) {
            //Decode(str, POSDATA_INTERMAR_CLIENT);
        }
    }

    @Override
    public String toString() {
        return originalString;
    }

    public static void main(String args[]) {
        
        System.out.println("Initializing Pos Data");
        try {
            PosData test = new PosData("DK4XI;2009-06-21;16:21:00;-059''47'58';089''23'34';test",0);
            if (test.isDataIsGood()) {
                System.out.println(test);
            }

            PosData test2 = new PosData("1;VA3IRX;-80.145;25.79;/Y;2009-11-23 22:00:00; DAYS, HEADING FOR KEY WEST",1);
            if (test2.isDataIsGood()) {
                System.out.println(test2);
            }

            PosData test3 = new PosData("2009-11-23 21:00:00;00uDK4XI-15:26 !4918.07N/00716.10Eythis is no status/Wind:1,N/Sea:0,N/Hp:1000/T:20/Clear/A:V/P:53587",2);
            if (test3.isDataIsGood()) {
                System.out.println(test3);
            }

            System.out.println(test2.compareTo(test3) + " " + test3.compareTo(test2));
        } catch(Exception e){
            System.out.println("Something went completely nuts.");
            e.printStackTrace();
        }

        System.out.println("Done.");        
    }

    /**
     * @return the callsign
     */
    public String getCallsign() {
        return callsign;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @return the latitude
     */
    public MapCoordinate getLatitude() {
        return latitude;
    }

    /**
     * @return the longitude
     */
    public MapCoordinate getLongitude() {
        return longitude;
    }

    /**
     * @return the aprs
     */
    public String getAprs() {
        return aprs;
    }

    /**
     * @return the dataIsGood
     */
    public boolean isDataIsGood() {
        return dataIsGood;
    }

    /**
     * @return the optional
     */
    public String getOptional() {
        return optional;
    }

    /**
     * @return the optionalFlag
     */
    public boolean isOptionalFlag() {
        return optionalFlag;
    }

    /**
     * @return the guiRect
     */
    public Rectangle getGuiRect() {
        return guiRect;
    }

    /**
     * @param guiRect the guiRect to set
     */
    public void setGuiRect(Rectangle guiRect) {
        this.guiRect = guiRect;
    }

    public int compareTo(PosData o) {
        return (date + " " + time).compareTo(o.date + " " + o.time);
    }

    /**
     * @return the origin
     */
    public int getOrigin() {
        return origin;
    }

    public String getBoardTime() {
        return boardTime;
    }

    public String getBoardDate() {
        return boardDate;
    }
}
