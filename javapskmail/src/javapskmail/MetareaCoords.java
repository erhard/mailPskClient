/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

/**
 *
 * @author sebastian_pohl
 */
public class MetareaCoords {

    private int number = -1;

    private MapCoordinate longitude = null;

    private MapCoordinate latitude = null;

    public MetareaCoords(int n, MapCoordinate longitude, MapCoordinate latitude) {
        this.number = n;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return the longitude
     */
    public MapCoordinate getLongitude() {
        return longitude;
    }

    /**
     * @return the latitude
     */
    public MapCoordinate getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return number + "," + longitude + "," + latitude;
    }

}
