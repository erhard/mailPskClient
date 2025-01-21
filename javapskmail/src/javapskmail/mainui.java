/*
 * Interface every UI has to implement to work with the logic
 */

package javapskmail;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import javax.swing.JButton;

/**
 *
 * @author sebastian_pohl
 */
public interface mainui {
    public void setOperatingMode(String str);

    public void setGPSIndicator(boolean b);

    public String getPosReport();

    public void setPosReport(String str);

    public void appendPosReport(String str);

    public void setPosReport(TreeMap<String, TreeMap<String, PosData>> posReport);

    public void appendPosReport(TreeMap<String, TreeMap<String, PosData>> posReport);

    public void addPosReport(TreeMap<String, PosData> posReport);

    public void addPosReport(Collection<PosData> posReport);

    public void addPosReport(PosData p);

    public TreeMap<String, TreeMap<String, PosData>> getMainPosReport();

    public PosReportMapPanel getMainPosReportMapPanel();

    public void setOperatorName(String str);

    public String getOperatorName();

    public void setPicturePath(String str);

    public String getPicturePath();

    public void setPictureName(String str);

    public String getPictureName();

    public void setStatus(String msg);

    public String getStatus();

    public void setLatitude(String msg);

    public String getLatitude();

    public void setLongitude(String msg);

    public String getLongitude();

    public void setChkBeacon(boolean state);

    public boolean getChkBeacon();

    public String getAPRSIcon();

    public void setCallsign(String str);

    public void setAPRSIcon(String str);

    public void setSpnMinute(int i);

    public String getSpnMinute();

    public void removeAllCboServer();

    public void addCboServer(String server);

    public void setSelectedCboServer(String server);

    public String getSelectedCboServer();

    public void setSelectedCboAPRSIcon(String instring);

    public void setDCDColor(Color color);

    public void appendTextArea3(String instring);

    public void appendMainWindow(String instring);

    public void appendMSGWindow(String instring);

    public void appendHeadersWindow(String instring);

    public void appendFilesTxtArea(String instring);

    public void setLabelStatus(String instring);

    public void setStatusLabel(String instring);

    public void setStatusLabelForeground(Color color);

    public void setFixAt(String instring);

    public void setCourse(String instring);

    public String getCourse();

    public void setSpeed(String instring);

    public String getSpeed();

    public void setProgressBarValue(int n);

    public void setProgressBarStringPainted(boolean b);

    public void setClock(String instring);

    public void setConnectButtonText(String text);

    public void setFileConnectButtonText(String text);

    public String getBeaconPeriod();

    public void dispose();

    public java.awt.Frame getParentFrame();

    public String getTxtMainEntry();

    public void setTxtMainEntry(String instring);

    public JButton getFileReadButton();

    public JButton getPosReadButton();

    public JButton getTextReadButton();

    public JButton getSYNOPReadButton();

    public JButton getGRIBReadButton();

    public String getContents(File aFile);

    public void deleteContent(File file)
        throws FileNotFoundException, IOException;

    public void setFilesTextArea(String instring);

    public boolean getMnuMailScanning();

    public void setMailHeadersWindow(String instring);

    public JButton getUpdateFileButton();

    public void updatemodeset(modemmodeenum mymode);

    public void updatemailscanset(Integer what);

    public void setBoardWX(String str);

    public String getBoardWX();

    public WXSetupDialog getWXDialog();

    public void setLocationRelativeTo(Component c);

    public void setVisible(boolean b);

    public void addServer(String str);

    public void setServer(String str);

    // Initial map methods
    /**
     * Add a waypoint to the map
     * @param lat Decimal latitude
     * @param lon Decimal longitude
     */
    public void addWaypoint(String lat, String lon);
    /**
     * Paint the waypoints in the hash table
     */
    public void paintWaypoints();
    /**
     * Clear map of waypoints
     */
    public void clearWaypoints();

    public void changeTabTo(int index);

    public void setLinkPeriod(int l);

    public int getLinkPeriod();

    public void setLinkIndicator(boolean b);

    public void setLinkTime(String str);

    public String getClock();

    public fourbuttons getPanelFour();
}
