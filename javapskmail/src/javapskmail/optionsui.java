/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.awt.Component;

/**
 *
 * @author sebastian_pohl
 */
public interface optionsui {

    public void setNewsgroup(String str);

    public String getNewsgroup();

    public void setNewsgroupenabled(String str);

    public String getNewsgroupenabled();

    public void setAPRSIcon(String str);

    public String getAPRSIcon();

    public void setPosTime(String str);

    public String getPosTime();

    public void setOperatorName(String str);

    public String getOperatorName();

    public void setPicturePath(String str);

    public String getPicturePath();

    public void setPictureName(String str);

    public String getPictureName();

    public void setCallsign(String call);

    public String getCallsign();

    public void setBeaconqrg(String beacon);

    public Object getSpinBeaconMinute();

    public String getBeaconqrg();

    public void setServer(String call);

    public String getServer();

    public void setLatitude(String string);

    public String getLatitude();

    public void setLongitude(String string);

    public String getLongitude();

    public void setBlockLength(String string);

    public String getBlockLength();

    public Object getSpinOffsetSecond();

    public Object getSpinOffsetMinute();

    public Object getDCDSpinner();

    public void setDCDSpinner(Object o);

    public void removeAllGPSSerialPort();

    public void addGPSSerialPort(Object o);

    public Object getGPSSerialPort();

    public void setVisible(boolean b);

    public boolean GPSConnectionSelected();

    public void setGPSConnection(boolean b);

    public void GPSSpeedEnabled(boolean set);

    public void selectGPSSpeed(Object o);

    public Object getGPSSpeed();

    public void GPSSerialPortEnabled(boolean set);

    public void selectGPSSerialPort(Object o);

    public void setSpinRetries(Object o);

    public Object getSpinRetries();

    public void setSpinIdleTime(Object o);

    public Object getSpinIdleTime();

    public void setSpinTXdelay(Object o);

    public Object getSpinTXdelay();

    public void setSpinOffsetMinute(Object o);

    public void setSpinOffsetSecond(Object o);

    public void setTxtPophost(String str);

    public String getTxtPophost();

    public void setTxtPopUser(String str);

    public String getTxtPopUser();

    public void setTxtPopPassword(String str);

    public String getTxtPopPassword();

    public void setTxtReplyTo(String str);

    public String getTxtReplyTo();

    public void dispose();

    public void setLocationRelativeTo(Component c);
}
