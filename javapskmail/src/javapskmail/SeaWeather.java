/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

/**
 *
 * @author sebastian_pohl
 */
public class SeaWeather {

    private String locationName = "";

    private MapCoordinate locationLat = null;

    private MapCoordinate locationLong = null;

    private String weekDay = "";

    private String UTC = "";

    private String windDirection = "";

    private String windSpeed = "";

    // Böengeschwindigkeit
    private String gustSpeed = "";

    private String waveHeight = "";

    private String shortWeather = "";

    private String originalString = "";

    public SeaWeather(String locationName,
            MapCoordinate locationLat,
            MapCoordinate locationLong,
            String weekDay,
            String UTC,
            String windDirection,
            String windSpeed,
            String gustSpeed,
            String waveHeight,
            String shortWeather,
            String originalString) {
        this.locationName = locationName;
        this.locationLat = locationLat;
        this.locationLong = locationLong;
        this.weekDay = weekDay;
        this.UTC = UTC;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.gustSpeed = gustSpeed;
        this.waveHeight = waveHeight;
        this.shortWeather = shortWeather;
        this.originalString = originalString;
    }

    @Override
    public String toString() {
        return getLocationName() + "," + getLocationLat() + "," + getLocationLong() + "," + getWeekDay() + "," +
            getUTC() + "," + getWindDirection() + "," + getWindSpeed() + "," + getGustSpeed() + "," + getWaveHeight() + ","
            + getShortWeather();
    }

    /**
     * @return the locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * @return the locationLat
     */
    public MapCoordinate getLocationLat() {
        return locationLat;
    }

    /**
     * @return the locationLong
     */
    public MapCoordinate getLocationLong() {
        return locationLong;
    }

    /**
     * @return the weekDay
     */
    public String getWeekDay() {
        return weekDay;
    }

    /**
     * @return the UTC
     */
    public String getUTC() {
        return UTC;
    }

    /**
     * @return the windDirection
     */
    public String getWindDirection() {
        return windDirection;
    }

    /**
     * @return the windSpeed
     */
    public String getWindSpeed() {
        return windSpeed;
    }

    /**
     * @return the gustSpeed
     */
    public String getGustSpeed() {
        return gustSpeed;
    }

    /**
     * @return the waveHeight
     */
    public String getWaveHeight() {
        return waveHeight;
    }

    /**
     * @return the shortWeather
     */
    public String getShortWeather() {
        return shortWeather;
    }

    /**
     * @return the originalString
     */
    public String getOriginalString() {
        return originalString;
    }

}
