/*
 * config.java  
 *   
 * Copyright (C) 2008 Pär Crusefalk (SM0RWO)  
 *   
 * This program is distributed in the hope that it will be useful,  
 * but WITHOUT ANY WARRANTY; without even the implied warranty of  
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
 * GNU General Public License for more details.  
 *   
 * You should have received a copy of the GNU General Public License  
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 */
package javapskmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author per
 */
public class config {
// Private variables 
    // options dialogue

    private String callsign;
    private String linktoserver;
    private String blocklength;
    private String latitude;
    private String longitude;
    private String course;
    private String speed;
    private String beaconqrg;
    private String beacon;
    // picture settings
    private String operatorName;
    private String picturePath;
    private String pictureName;
    // email settings
    private String pophost;
    private String popuser;
    private String poppassword;
    private String replyto;
    private String findupassword;
    // Configuration
    private String logfile;
    private int maxretries;
    private int idletime;
    private int txdelay;
    private int offsetminute;
    private int offsetsecond;
    private String Webpage1 = "none";
    private String Webpage2 = "none";
    private String Webpage3 = "none";
    private String Webpage4 = "none";
    private String Webpage5 = "none";
    private String Webpage6 = "none";
    // various values
    private String statustxt;
    private String wxtxt;
    private String filepath;
    private String newsgroup;
    private String newsgroupenabled;
    // Common properties file object
    Properties configFile;

    // constructor
    /**
     * 
     */
    public config(String path) {

        filepath = path;
        configFile = new Properties();
        // Check the config file, create and fill if necessary
        initialcheckconfigfile();
        try {
            statustxt = getPreference("STATUS");
            //if (statustxt.equals("INTERMAR PSKmail V 1.0")) setPreference("STATUS", "INTERMAR PSKmail V 1.0");
            callsign = getPreference("CALL");
            linktoserver = getPreference("SERVER");
            latitude = getPreference("LATITUDE");
            longitude = getPreference("LONGITUDE");
            blocklength = getPreference("BLOCKLENGTH");
            beaconqrg = getPreference("BEACONQRG", "0");
            beacon = getPreference("BEACON", "1");
            operatorName = getPreference("OPERATORNAME", "operator");
            picturePath = getPreference("PICTUREPATH", "icons/default.gif");
            pictureName = getPreference("PICTURENAME", "picturename");
            Webpage1 = getPreference("URL1");
            Webpage2 = getPreference("URL2");
            Webpage3 = getPreference("URL3");
            Webpage4 = getPreference("URL4");
            Webpage5 = getPreference("URL5");
            Webpage6 = getPreference("URL6");
            wxtxt = getPreference("WX");
            newsgroup = getPreference("NEWSGROUP", "PSKAPRS");
            newsgroupenabled = getPreference("NEWSGROUP", "false");
        } catch (Exception e) {
            callsign = "N0CALL";
            linktoserver = "N0CALL";
            blocklength = "5";
            latitude = "0.0";
            longitude = "0.0";
            beaconqrg = "0";
            beacon = "1";
            operatorName = "operator";
            picturePath = "icons/default.gif";
            pictureName = "picturename";
            statustxt = "INTERMAR PSKmail V 1.5";
            //if (statustxt.equals("INTERMAR PSKmail V 1.0")) setPreference("STATUS", "INTERMAR PSKmail V 1.0");
            Webpage1 = "none";
            Webpage2 = "none";
            Webpage3 = "none";
            Webpage4 = "none";
            Webpage5 = "none";
            Webpage6 = "none";
            wxtxt = "";
            newsgroup = "PSKAPR";
            newsgroupenabled = "false";
        }
    }

    /**
     * If there is no config file then one should be created,
     * it should have good defaults.
     */
    private void initialcheckconfigfile() {

        try {
            // Check if there is a configuration file
            boolean exists = (new File(filepath + "configuration.xml")).exists();
            // There is no file, we must create one
            if (!exists) {
                OutputStream fo = new FileOutputStream(filepath + "configuration.xml");
                configFile.setProperty("CALL", "N0CALL");
                configFile.setProperty("SERVER", "N0CALL");
                configFile.setProperty("BLOCKLENGTH", "5");
                configFile.setProperty("LATITUDE", "0.0");
                configFile.setProperty("LONGITUDE", "0.0");
                configFile.setProperty("BEACONQRG", "0");
                configFile.setProperty("BEACON", "1");
                configFile.setProperty("STATUS", Main.application);
                // Gps defaults
                configFile.setProperty("GPSPORT", "/dev/ttyS0");
                configFile.setProperty("GPSSPEED", "4800");
                configFile.setProperty("GPSENABLED", "no");
                // ICON and DCD
                configFile.setProperty("DCD", "3");
                configFile.setProperty("ICON", "/Y");

                configFile.setProperty("OPERATORNAME", "operator");
                configFile.setProperty("PICTUREPATH", "icons/default.gif");
                configFile.setProperty("PICTURENAME", "picturename");

                // Mail options
                configFile.setProperty("POPHOST", "none");
                configFile.setProperty("POPUSER", "none");
                configFile.setProperty("POPPASS", "none");
                configFile.setProperty("RETURNADDRESS", "myself@myemail.com");

                configFile.setProperty("URL1", "none");
                configFile.setProperty("URL2", "none");
                configFile.setProperty("URL3", "none");
                configFile.setProperty("URL4", "none");
                configFile.setProperty("URL5", "none");
                configFile.setProperty("URL6", "none");

                configFile.setProperty("WX", "");

                configFile.setProperty("NEWSGROUP", "PSKAPRS");

                configFile.setProperty("NEWSGROUPENABLED", "false");

                configFile.storeToXML(fo, "Configuration file for INTERMAR PSKmail WinV1.5 client");
                fo.close();
            }
        } catch (Exception e) {
            Main.log.writelog("Could not create settings file, directory permission trouble?", true);
        }
    }

    /**
     * 
     * @return
     */
    public String getCallsign() {
        return callsign;
    }

    /**
     * 
     * @param newcall
     */
    public void setCallsign(String newcall) {
        callsign = newcall;
    }

    /**
     * 
     * @return
     */
    public String getServer() {
        return linktoserver;
    }

    /**
     * 
     * @param newcall
     */
    public void setServer(String newcall) {
        linktoserver = newcall;
        setPreference("SERVER", newcall);
    }

    /**
     * 
     * @param newlat
     */
    public void setLatitude(String newlat) {
        latitude = newlat;
    }

    /**
     * 
     * @return
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     *
     * @param newlon
     */
    public void setLongitude(String newlon) {
        longitude = newlon;
    }

    /**
     *
     * @return
     */
    public String getLongitude() {
        return longitude;
    }

    public void setSpeed(String newspeed) {
        speed = newspeed;
    }

    public String getSpeed() {
        return speed;
    }

    public void setCourse(String newcourse) {
        course = newcourse;
    }

    public String getCourse() {
        return course;
    }

    /**
     *
     * @param newlength
     */
    public void setBlocklength(String newlength) {
        blocklength = newlength;
    }

    /**
     *
     * @return
     */
    public String getBlocklength() {
        return blocklength;
    }

    /**
     *
     * @param newqrg
     */
    public void setBeaconqrg(String newqrg) {
        beaconqrg = newqrg;
        setPreference("BEACONQRG", newqrg);
    }

    /**
     *
     * @return
     */
    public String getBeaconqrg() {
        return beaconqrg;
    }

    public void SetBeacon(String newbeacon) {
        beacon = newbeacon;
        setPreference("BEACON", beacon);
    }

    public String getBeacon() {
        return beacon;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return statustxt;
    }

    public void SetWebPages(String url1, String url2, String url3, String url4, String url5, String url6) {
        Webpage1 = url1;
        Webpage2 = url2;
        Webpage3 = url3;
        Webpage4 = url4;
        Webpage5 = url5;
        Webpage6 = url6;
    }

    /**
     *
     * @param newstatus
     */
    public void setStatus(String newstatus) {
        statustxt = newstatus;
    }

    public String getWX() {
        return wxtxt;
    }

    public void setWX(String str) {
        wxtxt = str;
    }

    public void saveURLs() {
        if (Webpage1.length() > 0) {
            setPreference("URL1", Webpage1);
        }
        if (Webpage2.length() > 0) {
            setPreference("URL2", Webpage2);
        }
        if (Webpage3.length() > 0) {
            setPreference("URL3", Webpage3);
        }
        if (Webpage4.length() > 0) {
            setPreference("URL4", Webpage4);
        }
        if (Webpage5.length() > 0) {
            setPreference("URL5", Webpage5);
        }
        if (Webpage6.length() > 0) {
            setPreference("URL6", Webpage6);
        }

    }

    /** /
     * Load properties and set config object
     * @param Key 
     * @param Value
     * @return
     */
    public void setPreference(String Key, String Value) {
        try {
            InputStream f = new FileInputStream(Main.tempConfPath);
            configFile.loadFromXML(f);
            f.close();
            configFile.setProperty(Key, Value);
            OutputStream fo = new FileOutputStream(Main.tempConfPath);
            configFile.storeToXML(fo, "Configuration file for IMA PSKmail client");
        } catch (Exception e) {
            String logerror = "Could not store setting: " + Key + "\n" + e.getMessage() + "\n";
            StackTraceElement[] test;
            test = e.getStackTrace();
            for (int i = 0; i < test.length; i++) {
                logerror += test[i].toString() + "\n";
            }
            Main.log.writelog(logerror, true);
        }
    }

    /**
     *
     * @param Key
     * @return
     */
    public String getPreference(String Key) {
        String myReturn = "";
        try {
            InputStream f = new FileInputStream(Main.tempConfPath);
            configFile.loadFromXML(f);
            f.close();
            myReturn = configFile.getProperty(Key);
            if (myReturn.equals(null)) {
                myReturn = "";
            }
        } catch (Exception ex) {
            return "";
        }
        //   System.out.println("value=" + configFile.getProperty(Key) + "\n");  // debug
        if (!myReturn.equals(null)) {
            return (myReturn);
        } else {
            return "";
        }
    }

    /**
     * Get the saved value, if its not there then use the default value
     * @param Key
     * @param Default
     * @return
     */
    public String getPreference(String Key, String Default) {
        Properties configFile = new Properties();
        String myReturn = "";
        try {
            InputStream f = new FileInputStream(Main.tempConfPath);
            configFile.loadFromXML(f);
            f.close();
            myReturn = configFile.getProperty(Key);
            if (myReturn.equals(null)) {
                myReturn = "";
            }
            return myReturn;
        } catch (Exception ex) {
            return Default;
        }
    }

    /**
     * @return the operatorName
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName the operatorName to set
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * @return the picturePath
     */
    public String getPicturePath() {
        return picturePath;
    }

    /**
     * @param picturePath the picturePath to set
     */
    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    /**
     * @return the pictureName
     */
    public String getPictureName() {
        return pictureName;
    }

    /**
     * @param pictureName the pictureName to set
     */
    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    /**
     * @return the newsgroup
     */
    public String getNewsgroup() {
        return newsgroup;
    }

    /**
     * @param newsgroup the newsgroup to set
     */
    public void setNewsgroup(String newsgroup) {
        this.newsgroup = newsgroup;
    }

    public String getNewsgroupenabled() {
        return newsgroupenabled;
    }

    public void setNewsgroupenabled(String newsgroupenabled) {
        this.newsgroupenabled = newsgroupenabled;
    }
}
