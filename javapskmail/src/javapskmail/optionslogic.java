/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javapskmail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author sebastian_pohl
 */
public class optionslogic {

    private optionsui parentui;
    private String callsign = "N0CAL";
    private String linktoserver = "N0CAL";
    private String beaconqrg = "0";
    private String latitude = "0.0";
    private String longitude = "0.0";
    private String blocklength = "5";
    private String gpsport = "/dev/ttyS0";
    private String gpsspeed = "4800";
    private boolean gpsenabled = false;
    private String operatorName = "operator";
    private String picturePath = "picPath";
    private String pictureName = "picName";
    private String aprsIcon = "y";
    private Session mysession;
    private arq myarq;
    private String newsgroup = "PSKAPRS";
    private String newsgroupenabled = "false";

    public optionslogic(optionsui currentui) {
        parentui = currentui;
    }

    public String getAPRSIcon() {
        return this.aprsIcon;
    }

    public void setAPRSIcon(String str) {
        this.aprsIcon = str;
        parentui.setAPRSIcon(str);
    }

    public String getOperatorName() {
        return this.operatorName;
    }

    public void setOperatorName(String str) {
        this.operatorName = str;
        parentui.setOperatorName(str);
    }

    public String getPicturePath() {
        return this.picturePath;
    }

    public void setPicturePath(String str) {
        this.picturePath = str;
        parentui.setPicturePath(str);
    }

    public String getNewsgroup() {
        return this.newsgroup;
    }

    public void setNewsgroup(String str) {
        this.newsgroup = str;
        parentui.setNewsgroup(str);
    }

    public String getNewsgroupenabled() {
        return this.newsgroupenabled;
    }

    public void setNewsgroupenabled(String str) {
        this.newsgroupenabled = str;
        parentui.setNewsgroupenabled(str);
    }

    public String getPictureName() {
        return this.pictureName;
    }

    public void setPictureName(String str) {
        this.pictureName = str;
        parentui.setPictureName(str);
    }

    public String getCallsign() {
        return this.callsign;
    }

    public void setCallsign(String str) {
        this.callsign = str;
        parentui.setCallsign(str);
    }

    public void setBeaconqrg(String str) {
        this.beaconqrg = str;
        parentui.setBeaconqrg(str);
    }

    public String getBeaconqrg() {
        return this.beaconqrg;
    }

    public String getServer() {
        return this.linktoserver;
    }

    public void setServer(String str) {
        this.linktoserver = str;
        parentui.setServer(str);
    }

    public void setLatitude(String str) {
        this.latitude = str;
        parentui.setLatitude(str);
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLongitude(String str) {
        this.longitude = str;
        parentui.setLongitude(str);
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setBlockLength(String str) {
        this.blocklength = str;
        parentui.setBlockLength(str);
        if (Integer.parseInt(blocklength) > 5) {
            Main.defaultmode = modemmodeenum.PSK500R;
        } else {
            Main.defaultmode = modemmodeenum.PSK250;
        }
    }

    public String getBlockLength() {
        return this.blocklength;
    }

    public void setSeconds() {
        try {
            Object BeaconSecond = parentui.getSpinOffsetSecond();
            Main.Second = Integer.parseInt(BeaconSecond.toString());
        } catch (NumberFormatException numberFormatException) {
            Main.log.writelog("Problem when setting offset seconds value!", numberFormatException, true);
        }
    }

    public void setDCD() {
        try {
            Object DCDValue = parentui.getDCDSpinner();
            Main.DCD = Integer.parseInt(DCDValue.toString());
            Main.MAXDCD = Integer.parseInt(DCDValue.toString());
        } catch (Exception ex) {
            Main.log.writelog("Problem when setting DCD value!", ex, true);
        }
    }

    public void getComPorts() {
        serialport mySerial;
        ArrayList portList;

        try {
            // Only do this if the port is closed
            if (!Main.gpsport.getconnectstate()) {
                mySerial = Main.gpsport;
                portList = mySerial.getCommports();
                boolean portFound = false;

                // Do not do this if the port is open

                // remove the items first
                parentui.removeAllGPSSerialPort();

                ListIterator li = portList.listIterator();
                while (li.hasNext()) {
                    parentui.addGPSSerialPort(li.next());
                    portFound = true;
                }
                if (!portFound) {
                    System.out.println("port not found.");
                }
            }
        } catch (Exception e) {
            Main.log.writelog("Problem fetching serial ports from the system!", e, true);
        }
    }

    /**
     * Get an integer from the config file, setup using default value if missing
     * @param key
     * @param defvalue
     * @return
     */
    public Integer GetSpinValue(String key, Integer defvalue) {
        Integer retval = defvalue;
        // Get the possibly saved value
        String mystr = Main.configuration.getPreference(key);
        if (!mystr.equals("")) {
            retval = Integer.parseInt(mystr);
        }
        return retval;
    }

    public void optionslogic_start() {
        mysession = new Session();
        myarq = new arq();
        try {

            // Setup user data tab
            setCallsign(Main.configuration.getPreference("CALL"));
            setServer(Main.configuration.getPreference("SERVER"));
            setBeaconqrg(Main.configuration.getPreference("BEACONQRG"));

            setLatitude(Main.configuration.getPreference("LATITUDE"));
            setLongitude(Main.configuration.getPreference("LONGITUDE"));
            setBlockLength(Main.configuration.getPreference("BLOCKLENGTH"));

            setOperatorName(Main.configuration.getPreference("OPERATORNAME"));
            setPicturePath(Main.configuration.getPreference("PICTUREPATH"));
            setPictureName(Main.configuration.getPreference("PICTURENAME"));

            setAPRSIcon(Main.configuration.getPreference("ICON"));
            setNewsgroup(Main.configuration.getPreference("NEWSGROUP"));
            setNewsgroupenabled(Main.configuration.getPreference("NEWSGROUPENABLED"));

            // setup serial port list
            getComPorts();
            // Enable the controls if the gps is not running
            this.enablegpscontrols(!Main.gpsport.curstate);

            // IF the gps is already running then just display the settings
            if (Main.gpsport.curstate) {
                parentui.removeAllGPSSerialPort();
                parentui.addGPSSerialPort(Main.configuration.getPreference("GPSPORT"));
                parentui.selectGPSSerialPort(Main.configuration.getPreference("GPSPORT"));
            }
            parentui.selectGPSSpeed(Main.configuration.getPreference("GPSSPEED"));
            String enablegps = Main.configuration.getPreference("GPSENABLED");
            if (enablegps.equals("yes")) {
                parentui.setGPSConnection(true);
            } else {
                parentui.setGPSConnection(false);
            }

            // Setup email tab
            parentui.setTxtPophost(Main.configuration.getPreference("POPHOST"));
            parentui.setTxtPopUser(Main.configuration.getPreference("POPUSER"));
            parentui.setTxtPopPassword(Main.configuration.getPreference("POPPASS"));
            parentui.setTxtReplyTo(Main.configuration.getPreference("RETURNADDRESS"));

            // Setup configuration tab
            parentui.setDCDSpinner(GetSpinValue("DCD", 1));
            parentui.setSpinRetries(GetSpinValue("RETRIES", 16));
            parentui.setSpinIdleTime(GetSpinValue("IDLETIME", 15));
            parentui.setSpinTXdelay(GetSpinValue("TXDELAY", 0));
            parentui.setSpinOffsetMinute(GetSpinValue("OFFSETMINUTE", 0));
            parentui.setSpinOffsetSecond(GetSpinValue("OFFSETSECONDS", 20));
        } catch (Exception ex) {
            Main.log.writelog("Error when fetching settings!", ex, true);
        }
    }

    public void txtLinktoActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
        linktoserver = parentui.getServer();
    }

    public void bOKActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
        // Update the config object and have it save itself

        try {
            String path = Main.HomePath + Main.Dirprefix;
            config cf = new config(path);

            if (parentui.getCallsign().length() > 0) {
                callsign = parentui.getCallsign();
                cf.setPreference("CALL", callsign.toUpperCase());
            }
            if (parentui.getServer().length() > 0) {
                linktoserver = parentui.getServer();
                cf.setPreference("SERVER", linktoserver.toUpperCase());
            }

            Object myobject = parentui.getSpinBeaconMinute();
            beaconqrg = myobject.toString();
            cf.setBeaconqrg(beaconqrg);

            if (parentui.getLatitude().length() > 0) {
                latitude = parentui.getLatitude();
                cf.setPreference("LATITUDE", latitude);
            }
            if (parentui.getLongitude().length() > 0) {
                longitude = parentui.getLongitude();
                cf.setPreference("LONGITUDE", longitude);
            }
            if (parentui.getBlockLength().length() > 0) {
                blocklength = parentui.getBlockLength();
                cf.setPreference("BLOCKLENGTH", blocklength);
//       System.out.println(blocklength + "\n");
                if (Integer.parseInt(blocklength) > 5) {
                    Main.defaultmode = modemmodeenum.PSK500R;
                } else {
                    Main.defaultmode = modemmodeenum.PSK250;
                }
            }

            if (parentui.getOperatorName().length() > 0) {
                operatorName = parentui.getOperatorName();
                cf.setPreference("OPERATORNAME", operatorName);
            }

            if (parentui.getPicturePath().length() > 0) {
                picturePath = parentui.getPicturePath();
                cf.setPreference("PICTUREPATH", picturePath);
            }

            if (parentui.getPictureName().length() > 0) {
                pictureName = parentui.getPictureName();
                cf.setPreference("PICTURENAME", pictureName);
            }

            if (parentui.getAPRSIcon().length() > 0) {
                aprsIcon = parentui.getAPRSIcon();
                cf.setPreference("ICON", aprsIcon);
            }

            //GPS Section, only save when set in a disconnected state. This is due to rxtx that only
            // gives the available ports, not all the ports.
            Boolean mygpstest = Main.gpsport.curstate;
            if (!mygpstest) {
                gpsport = (String) parentui.getGPSSerialPort();
                if (gpsport != null) {
                    cf.setPreference("GPSPORT", gpsport);
                }
                gpsspeed = (String) parentui.getGPSSpeed();
                if (gpsspeed != null) {
                    cf.setPreference("GPSSPEED", gpsspeed);
                }
            }

            gpsenabled = parentui.GPSConnectionSelected();
            if (gpsenabled) {
                cf.setPreference("GPSENABLED", "yes");
            } else {
                cf.setPreference("GPSENABLED", "no");
            }

            // Configuration tab
            myobject = parentui.getDCDSpinner();
            cf.setPreference("DCD", myobject.toString());
            myobject = parentui.getSpinRetries();
            cf.setPreference("RETRIES", myobject.toString());
            myobject = parentui.getSpinIdleTime();
            cf.setPreference("IDLETIME", myobject.toString());
            myobject = parentui.getSpinTXdelay();
            cf.setPreference("TXDELAY", myobject.toString());
            myobject = parentui.getSpinOffsetMinute();
            cf.setPreference("OFFSETMINUTE", myobject.toString());
            myobject = parentui.getSpinOffsetSecond();
            cf.setPreference("OFFSETSECONDS", myobject.toString());

            // mail options
            if (parentui.getTxtPophost().length() > 0) {
                cf.setPreference("POPHOST", parentui.getTxtPophost());
            }
            if (parentui.getTxtPopUser().length() > 0) {
                cf.setPreference("POPUSER", parentui.getTxtPopUser());
            }
            if (parentui.getTxtPopPassword().length() > 0) {
                cf.setPreference("POPPASS", parentui.getTxtPopPassword());
            }
            if (parentui.getTxtReplyTo().length() > 0) {
                cf.setPreference("RETURNADDRESS", parentui.getTxtReplyTo());
            }

            if (parentui.getNewsgroup().length() > 0) {
                cf.setPreference("NEWSGROUP", parentui.getNewsgroup());
            }

            if (parentui.getNewsgroupenabled().equals("true") || parentui.getNewsgroupenabled().equals("false")) {
                cf.setPreference("NEWSGROUPENABLED", parentui.getNewsgroupenabled());
            }

            // Handle gps settings
            handlegpsupdown();

        } catch (Exception ex) {
            Main.log.writelog("Error encountered when storing preferences!", ex, true);
        }

        //#################################
        try {
            // store the config file if present

            File f1 = new File(Main.tempConfPath);
            File f2 = new File(Main.HomePath + Main.Dirprefix + "configuration.xml");

            if (f1.isFile()) {

                InputStream in = new FileInputStream(f1);

                //Overwrite the file.
                OutputStream out = new FileOutputStream(f2);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                myarq.Message("Config File stored.", 10);
            }
        } catch (Exception e) {
            myarq.Message("problem writing the config file", 10);
        }
        //##################################
        parentui.setVisible(false);
    }

    /**
     * Enable or disable the gps controls
     * @param set
     */
    public void enablegpscontrols(Boolean set) {
        parentui.GPSSerialPortEnabled(set);
        parentui.GPSSpeedEnabled(set);
    }

    /**
     * Open the port if its selected and not open before
     */
    public void handlegpsupdown() {
        // Take care of GPS settings and enable/disable it
        String portforgps = Main.configuration.getPreference("GPSPORT");
        String speedforgps = Main.configuration.getPreference("GPSSPEED");
        int speedygps = Integer.parseInt(speedforgps);

        try {
            if (!Main.gpsport.getconnectstate()) {
                // Not connected
                if (parentui.GPSConnectionSelected()) { // But would like to be
                    Main.gpsport.connect(portforgps, speedygps);
                    // Check if the port is open
                    if (!Main.gpsport.curstate) {
                        // Disconnect and set it off
                        Main.gpsport.disconnect();
                        Main.configuration.setPreference("GPSENABLED", "no");
                    }
                }
            } else {   // Connected
                if (!parentui.GPSConnectionSelected()) { // But would like it not to be
                    // Check if the port is open
                    if (Main.gpsport.curstate) {
                        // Disconnect and set it off
                        Main.gpsport.disconnect();
                        Main.configuration.setPreference("GPSENABLED", "no");
                    }
                }
            }
        } catch (Exception ex) {
            Main.log.writelog("Could not open/close GPS port!", ex, true);
        }
    }

    public void txtCallsignActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
        callsign = parentui.getCallsign();

    }

    public void txtCallsignFocusLost(java.awt.event.FocusEvent evt) {
// TODO add your handling code here:
    }

    public void txtBlocklengthActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        blocklength = parentui.getBlockLength();
    }

    public void chkGPSConnectionActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
    }

    public void cboGPSSpeedActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
    }

    public void chkGPSConnectionStateChanged(javax.swing.event.ChangeEvent evt) {
        // Get the checkbox state, too tired to remember how now (need sleep)
    }

    public void spinOffsetSecondsStateChanged(javax.swing.event.ChangeEvent evt) {
        // TODO add your handling code here:
        setSeconds();
    }

    public void DCDSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {
        // TODO add your handling code here:
        setDCD();
    }

    public void ServerUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            mysession.sendUpdate();
        } else {
            parentui.setVisible(false);
            myarq.Message("You need to connect first...", 5);
        }
    }
}
