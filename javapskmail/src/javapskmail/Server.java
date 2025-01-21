/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.util.Vector;

/**
 *
 * @author sebastian_pohl
 */
public class Server {

    private String callSign = "";

    private String country = "";

    private String frequency = "";

    private String position = "";

    public Server(String callSign,
        String country, String frequency, String position) {

        this.callSign = callSign;
        this.country = country;
        this.frequency = frequency;
        this.position = position;
    }

    public Vector buildVector() {
        Vector rowVector = new Vector();

        rowVector.add(callSign);
        rowVector.add(country);
        rowVector.add(frequency);
        rowVector.add(position);

        return rowVector;
    }

    @Override
    public String toString() {
        // return the server data in cvs format
        return getCallSign() + ";" + getCountry() + ";" + getFrequency() + ";" + getPosition();
    }

    /**
     * @return the callSign
     */
    public String getCallSign() {
        return callSign;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the frequency
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

}
