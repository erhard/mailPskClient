/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * class to store map coordinate in either decimal and geodesic coordinate
 * based on the entry of either of the coordinates form while transforming
 * internally to the others.
 * Provides methods to retrieve either transform.
 *
 * @author sebastian_pohl
 */
public class MapCoordinate {

    // geodesic coordinates
    // negative degree means either W (west) or S (south)
    // positive degree means either E (east) or N (north)
    private int degree;
    private int minute;
    private int second;

    private char direction;

    // decimal coordinates
    private double decimal;

    public MapCoordinate(int degree, int minute, int second, char direction) {
        this.degree = degree;
        this.minute = minute;
        this.second = second;
        this.direction = direction;

        // it is save to transform after construction
        this.transformGeoDeci();
    }

    public MapCoordinate(double decimal, char direction) {
       this.decimal = decimal;
       this.direction = direction;

       // it is save to transform after construction
       try {
           this.transformDeciGeo();
       } catch (Exception e) {
           e.printStackTrace();
       }

    }

    // only save to call , if degree , minute and second are not null
    private void transformGeoDeci() {
        this.decimal = this.degree + this.minute / 60.0 +
               this.second / 3600.0;
    }

    private void transformDeciGeo() throws NumberFormatException {
        String string = (new Double(decimal)).toString();

        Pattern p = Pattern.compile("(\\d+)(?:\\.(\\d+))");
        Matcher m = p.matcher(string);
        if (m.lookingAt()) {
            degree = (new Integer(m.group(1))).intValue();
            try {
                string = m.group(2);
                
            } catch(Exception e) {
                minute = 0;
                second = 0;
                return;
            }
        }

        string = (new Double((new Double("0." + string)) * 60)).toString();

        p = Pattern.compile("(\\d+)(?:\\.(\\d+))");
        m = p.matcher(string);
        if (m.lookingAt()) {
            minute = (new Integer(m.group(1))).intValue();
            try {
                string = m.group(2);
            } catch(Exception e) {
                second = 0;
                return;
            }
        }

        string = (new Double((new Double("0." + string)) * 60)).toString();

        p = Pattern.compile("(\\d+).*");
        m = p.matcher(string);
        if (m.lookingAt()) {
            second = (new Integer(m.group(1))).intValue();
        }
    }

    /**
     * @return the degree
     */
    public int getDegree() {
        return degree;
    }

    /**
     * @return the minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * @return the second
     */
    public int getSecond() {
        return second;
    }

    /**
     * @return the decimal
     */
    public double getDecimal() {
        return decimal;
    }

    @Override
    public String toString() {
        return this.decimal + (new Character(getDirection())).toString() + ":" + degree + "." + minute + "." + second + getDirection();
    }

    public String getGeodesic() {
        return degree + "." + minute + "." + second + getDirection();
    }

    public static void main(String args[]) {
        MapCoordinate test1 = new MapCoordinate(12.5,'E');
        MapCoordinate test2 = new MapCoordinate(30,30,30,'S');

        System.out.println(test1 + "," + test2);

    }

    /**
     * @return the direction
     */
    public char getDirection() {
        return direction;
    }

    public int getSign() {
        if ((direction == 'E') || (direction == 'N')) {
            return 1;
        } else {
            return -1;
        }
    }

    public String getDirectedDecimal() {
        return this.decimal + "" + direction;
    }

    public double getSignedDecimal() {
        return getSign() * decimal;
    }
}
