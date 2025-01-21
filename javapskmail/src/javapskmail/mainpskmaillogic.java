/*
 * mainpskmaillogic
 *
 * created @ May 6th 2009
 */
package javapskmail;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author sebastian_pohl
 *
 *
 */
public class mainpskmaillogic {

    private mainui parentui;
    static int timercnt = 0;
    public arq myarq;
    /**
     *
     */
    public config myconfig;
    private Session mysession;
    private optionsui optionsDialog;
    private static int oldminute = 0;
    static final ResourceBundle mainpskmailui = java.util.ResourceBundle.getBundle("javapskmail/mainpskmailui",
            java.util.Locale.ENGLISH);
    private static String oldstatus = mainpskmailui.getString("Connected");
    private String Icon;
    private javax.swing.Timer t;
    private JFileChooser fc;
    private int BeaconQrg = 0;
    private Thread thread;

    public ResourceBundle getResources() {
        return mainpskmailui;
    }

    public mainui getParentUI() {
        return parentui;
    }

    public config getConfig() {
        return myconfig;
    }

    public optionsui getOptionDialog() {
        return optionsDialog;
    }

    public Thread threadFactory(final String msg) {
        return new Thread(new Runnable() {

            public void run() {
                while (!Main.Connected) {
                    try {
                        Thread.sleep(1000);
                        // do nothing
                    } catch (InterruptedException ex) {
                        Logger.getLogger(mainpskmaillogic.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    // do nothing
                }
                Main.TX_Text += msg;

                myarq.Message(mainpskmailui.getString("Getting_list_of_messages_from_the_web..."), 5);
            }
        });
    }

    public mainpskmaillogic(mainui currentui) {
        parentui = currentui;
        myarq = new arq();
        String path = Main.HomePath + Main.Dirprefix;
        myconfig = new config(path);
        mysession = new Session();
        fc = new JFileChooser();
    }

    public void mainpskmaillogic_start() {
        parentui.setStatus(myconfig.getStatus());
        parentui.setLatitude(myconfig.getPreference("LATITUDE"));
        parentui.setLongitude(myconfig.getPreference("LONGITUDE"));
        parentui.setOperatorName(myconfig.getPreference("OPERATORNAME"));
        parentui.setPicturePath(myconfig.getPreference("PICTUREPATH"));
        parentui.setPictureName(myconfig.getPreference("PICTURENAME"));
        parentui.setCallsign(myconfig.getPreference("CALL"));
        parentui.setAPRSIcon(myconfig.getPreference("ICON", "/Y"));
        parentui.setStatus(myconfig.getPreference("STATUS"));
        parentui.setBoardWX(myconfig.getPreference("WX"));
        if (myconfig.getBeacon().equals("1")) {
            parentui.setChkBeacon(true);
        } else {
            parentui.setChkBeacon(false);
        }

        parentui.setSpnMinute(Integer.parseInt(myconfig.getBeaconqrg()));
        // ICON
        String myIcon = myconfig.getPreference("ICON", "/Y");
        //parentui.setSelectedCboAPRSIcon(myIcon);
        Main.Icon = myIcon;

        // Fetch server to link to
        String myServer = myconfig.getPreference("SERVER");
        parentui.removeAllCboServer();

        // Add servers from main
        Iterator<String> iter = Main.Servers.iterator();
        while (iter.hasNext()) {
            parentui.addCboServer(iter.next());
        }
//        for (int i = 0; i < Main.Servers.length; i++) {
//            if (!Main.Servers[i].equals("")) {
//                parentui.addCboServer(Main.Servers[i]);
//            }
//        }
        parentui.setSelectedCboServer(myServer);
        //this.txtServer.setText(myServer);

        parentui.addWaypoint("59", "18");

        modemmodeenum mymode;
        if (Integer.parseInt(myconfig.getBlocklength()) > 5) {
            mymode = modemmodeenum.PSK500R;
            parentui.updatemodeset(mymode);
        } else {
            mymode = modemmodeenum.PSK250;
            parentui.updatemodeset(mymode);
        }

//        parentui.paintWaypoints();
// timer, 50 msec tick
        t = new javax.swing.Timer(50, new ActionListener() {

            public void actionPerformed(ActionEvent e) {


                // 50 ms second timer

                // update monitor window
                if (Main.monitor.length() > 0) {
                    parentui.appendTextArea3(Main.monitor);
                    Main.monitor = "";
                }
                // DCD indicator
                if (Main.DCD > 0) {
                    parentui.setDCDColor(Color.YELLOW);
                } else {
                    parentui.setDCDColor(Color.lightGray);
                }
                if (Main.TXActive) {
                    parentui.setDCDColor(Color.RED);
                }
                if (Main.BlockActive) {
                    parentui.setDCDColor(Color.cyan);
                }
                // update main window
                if (Main.mainwindow.length() > 0) {
                    parentui.appendMainWindow(Main.mainwindow);
                    Main.mainwindow = "";
                }

                // 20 x  50 = 1000 msec
                if (timercnt < 20) {
                    timercnt++;
                } else {
                    timercnt = 0;
                    Calendar cal = Calendar.getInstance();
                    int Hour = cal.get(Calendar.HOUR_OF_DAY);
                    int Minute = cal.get(Calendar.MINUTE);
                    int Second = cal.get(Calendar.SECOND);

                    // 1 second timer
                    // decrement DCD
                    if (Main.DCD > 0) {
                        Main.DCD--;
                    }

                    Main.theScheduler.execute();         // execute Scheduler once per loop

                    // increment GPScounter
                    Main.GPScounter++;

                    //System.out.println(Main.GPScounter);

                    parentui.setGPSIndicator(Main.GPScounter <= 5);

                    parentui.setOperatingMode(Main.currentOperatingMode);

//                    if (!Main.TXActive) { // only account delay, if TX is free
//                        if (Main.beacon_ok > 0) {
//                            Main.beacon_ok++;
//
//                            if (Main.beacon_ok >= Main.callDelay) {
//                                Main.beacon_ok = 0;
//                            }
//                        }
//
//                        if (Main.link_ok > 0) {
//                            Main.link_ok++;
//
//                            if (Main.link_ok >= Main.callDelay) {
//                                Main.link_ok = 0;
//                            }
//                        }
//
//                        if (Main.APRS_ok > 0) {
//                            Main.APRS_ok++;
//
//                            if (Main.APRS_ok >= Main.callDelay) {
//                                Main.APRS_ok = 0;
//                            }
//                        }
//
//                        if (Main.emergency_ok > 0) {
//                            Main.emergency_ok++;
//
//                            if (Main.emergency_ok >= Main.callDelay) {
//                                Main.emergency_ok = 0;
//                            }
//                        }
//                    }

//                    if (Main.resetOperationModeToggle) {
//                        if (Main.resetOperationModeCounter == 30) {
//                            // reset to default mode
//                            myarq.send_mode_command(Main.defaultmode);
////                            Main.sendLocked = false;
//                            //myarq.send_txrsid_command("OFF");
//                            // System.out.println("Counter forced reset.");
//                            //Main.resetOperationModeToggle = false;
//                            //Main.resetOperationModeCounter = 0;
//                        } else
//                            Main.resetOperationModeCounter++;
//                        // System.out.println(Main.resetOperationModeCounter);
//                    }

                    if (Main.metareaTimerToggle) {
                        // increment metareaTimer by 1 each second
                        Main.metareaTimer++;

                        if (Main.metareaTimer == 20) {
                            Main.metareaTimerToggle = false;
                            Main.metareaTimer = 0;
                            // perform kill switch at metarea recording
                            // and save recorded metarea data
                            Main.metareaRecorder.writeFile(new File(
                                    Main.metareaPath + "/" + Main.metareaRecorder.getFilename()));

                            Main.metareaRecorder.reset();
                        }
                    }

                    if (Main.BulletinResetTimerToggle) {
                        Main.BulletinResetTimer++;
                        if (Main.BulletinResetTimer == 20) {
                            Main.Bulletinmode = false;      // reset Bulletin Mode
                            Main.BulletinResetTimerToggle = false;
                            Main.BulletinResetTimer = 0;
                        }
                    }

                    if (Main.gribTimerToggle) {
                        Main.gribTimer++;

                        if (Main.gribTimer == 20) {
                            Main.gribTimerToggle = false;
                            Main.gribTimer = 0;
                            // perform kill switch at GRIB recording
                            // and DONT save recorded GRIB data (it is corrupted for sure)
                            //Main.gribRecorder.writeFile(new File(
                            //        Main.weatherPath + "/" + Main.gribRecorder.getFileName()));

                            Main.gribRecorder.reset();
                        }
                    }

                    if (Main.emergencyToggle) {
                        Main.emergencyTimer++;

                        if (Main.emergencyTimer >= 60) { // & !Main.sendLocked) {
                            Main.theScheduler.offer(new SendCommand(
                                    myarq, SendCommand.CommandMode.EMERGENCY, Main.modeprofile, Main.emergencyMessage));
                            Main.emergencyTimer = 0;

//                                Main.sendLocked = true;
                            // perform emergency broadcast anew
//                            try {
//                                //myarq.send_emergency(Main.emergencyMessage);
//                            } catch (InterruptedException ex) {
//                                ex.printStackTrace();
//                            }

                        }
                    }

                    // update message window
                    if (Main.MSGwindow.length() > 0) {
                        String hourformat = "0" + Integer.toString(Hour);
                        hourformat = hourformat.substring(hourformat.length() - 2);
                        String minuteformat = "0" + Integer.toString(Minute);
                        minuteformat = minuteformat.substring(minuteformat.length() - 2);
                        String newmessage = hourformat + ":" + minuteformat + " " + Main.MSGwindow;

                        parentui.appendMSGWindow(newmessage);
                        Main.MSGwindow = "";
                    }
                    // mail headers
                    if (Main.Mailheaderswindow.length() > 0) {
                        parentui.appendHeadersWindow(Main.Mailheaderswindow);
                        Main.Mailheaderswindow = "";
                    }
                    // files window
                    if (Main.FilesTextArea.length() > 0) {
                        parentui.appendFilesTxtArea(Main.FilesTextArea);
                        Main.FilesTextArea = "";
                    }

                    // Status Line messages
                    if (Main.StatusLineTimer > 0 & Main.Statusline.length() > 0) {
                        parentui.setLabelStatus(Main.Statusline);
                    }
                    if (Main.StatusLineTimer > 0) {
                        Main.StatusLineTimer--;
                        if (Main.StatusLineTimer == 0) {
                            Main.Statusline = "";
                            parentui.setLabelStatus(" ");
                        }
                    }

                    // Get the GPS position or the preset data
                    if (Main.gpsdata.getFixStatus()) {
                        parentui.setLatitude(Main.gpsdata.getLatitude());
                        parentui.setLongitude(Main.gpsdata.getLongitude());
                        if (Main.gpsdata.getSpeed().equals("")) {
                            parentui.setSpeed("0.0");
                        } else {
                            parentui.setSpeed(Main.gpsdata.getSpeed());
                        }
                        if (Main.gpsdata.getCourse().equals("")) {
                            parentui.setCourse("0.0");
                        } else {
                            parentui.setCourse(Main.gpsdata.getCourse());
                        }
                        parentui.setFixAt(Main.gpsdata.getFixTime());
                    }

                    // set progress bar
                    parentui.setProgressBarValue(Main.Progress);
                    if (Main.Progress > 0) {
                        parentui.setProgressBarStringPainted(true);
                    } else {
                        parentui.setProgressBarStringPainted(false);
                    }

                    // ... clock...

                    String formathour = "0" + Integer.toString(Hour);
                    formathour = formathour.substring(formathour.length() - 2);
                    String formatminute = "0" + Integer.toString(Minute);
                    formatminute = formatminute.substring(formatminute.length() - 2);
                    String formatsecond = "0" + Integer.toString(Second);
                    formatsecond = formatsecond.substring(formatsecond.length() - 2);

                    String clock = formathour + ":" + formatminute + ":" + formatsecond;
                    parentui.setClock(clock);

                    // display status field
                    if (Main.Bulletinmode) {
                        parentui.setStatusLabel(java.util.ResourceBundle.getBundle("javapskmail/mainpskmailui").getString("Bulletin"));
                        parentui.setStatusLabelForeground(Color.black);
                    } else if (Main.IACmode) {
                        parentui.setStatusLabel("FEC1");
                        parentui.setStatusLabelForeground(Color.black);
                    } else if (!oldstatus.equals(Main.Status)) {
                        oldstatus = Main.Status;
                        parentui.setStatusLabel(Main.Status);
                        parentui.setStatusLabelForeground(Color.black);
                        if (Main.Status.equals(mainpskmailui.getString("Connected"))) {
                            parentui.setConnectButtonText(java.util.ResourceBundle.getBundle("javapskmail/mainpskmailui").getString("QUIT"));
                            parentui.setFileConnectButtonText(java.util.ResourceBundle.getBundle("javapskmail/mainpskmailui").getString("QUIT"));
                            parentui.setStatusLabelForeground(Color.black);
                        } else {
                            parentui.setConnectButtonText(mainpskmailui.getString("Connect"));
                            parentui.setFileConnectButtonText(mainpskmailui.getString("Connect"));
                        }
                    } else if (Main.Status.equals(mainpskmailui.getString("Listening"))) {
                        parentui.setStatusLabel(mainpskmailui.getString("Listening"));
                        parentui.setStatusLabelForeground(Color.black);
                    } else if (Main.Connecting) {
                        parentui.setStatusLabel(mainpskmailui.getString("Connecting"));
                        parentui.setStatusLabelForeground(Color.RED);
                    } else if (Main.Status.equals(mainpskmailui.getString("Discon"))) {
                        parentui.setStatusLabelForeground(Color.RED);
                    }

                    // scanning connect ?

                    if (!Main.Connected & Main.Connecting) { // & (Minute % 5) == BeaconQrg) {
                        if (Second == 10 | Second == 20 | Second == 30 | Second == 40 | Second == 50) {
                            //System.out.println(myarq.get_txstatus());
                            try {
                                myarq.set_txstatus(txstatus.TXConnect);
                                myarq.send_frame("");

                                Main.Connecting = false;
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    // minute timer
                    if (Minute != oldminute & Main.Second == Second) {
                        oldminute = Minute;

                        // reset mode
                        if (!Main.Connected & !Main.Connecting) {
                            myarq.send_mode_command(Main.defaultmode);
                        }

                        int systemMinute = Minute % 5;
                        int i = 0;
                        try {
                            String Beaconqrg = myconfig.getPreference("BEACONQRG");
                            BeaconQrg = Integer.parseInt(Beaconqrg);
                            i = BeaconQrg;
                        } catch (Exception ex) {
                            i = 0;
                        }

                        if (!Main.Bulletinmode & !Main.Connected & !Main.IACmode) {
                            int linkPeriod = parentui.getLinkPeriod();

                            switch (linkPeriod) {
                                case 1:
                                    if (Minute == (i + 25) | Minute == (i + 45)) {
                                        // send link command
                                        bLinkActionPerformed(e);
//                                        if (!Main.Connected & !Main.Connecting & !Main.Bulletinmode & !Main.IACmode) {
//                                            if (Main.sending_link > 0) {
//                                                Main.sending_link--;
//                                                if (Main.sending_link > 2) {
//                                                    Main.linkmode = Main.modeprofile[1];
//                                                } else {
//                                                    Main.linkmode = Main.modeprofile[2];
//                                                }
//
//
//                                                if (!Main.TXActive) {
//                                                    try {
//                                                        myarq.Message(mainpskmailui.getString("Link_to_server"), 5);
//                                                        myarq.set_txstatus(txstatus.TXlinkreq);
//                                                        myarq.send_link();
//                                                    } catch (InterruptedException ex) {
//                                                        Logger.getLogger(mainpskmailui.class.getName()).log(Level.SEVERE, null, ex);
//                                                    }
//                                                }
//
//                                            }
//                                        }
                                    }
                                    break;
                                case 2:
                                    if (Minute == i + 50) {
                                        // send link command
                                        bLinkActionPerformed(e);
//                                        if (!Main.Connected & !Main.Connecting & !Main.Bulletinmode & !Main.IACmode) {
//                                            if (Main.sending_link > 0) {
//                                                Main.sending_link--;
//                                                if (Main.sending_link > 2) {
//                                                    Main.linkmode = Main.modeprofile[1];
//                                                } else {
//                                                    Main.linkmode = Main.modeprofile[2];
//                                                }
//
//
//                                                if (!Main.SendCommand.equals("") & !Main.TXActive) {
//                                                    try {
//                                                        myarq.Message(mainpskmailui.getString("Link_to_server"), 5);
//                                                        myarq.set_txstatus(txstatus.TXlinkreq);
//                                                        myarq.send_link();
//                                                    } catch (InterruptedException ex) {
//                                                        Logger.getLogger(mainpskmailui.class.getName()).log(Level.SEVERE, null, ex);
//                                                    }
//                                                }
//
//                                            }
//                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }

                        if (!Main.Bulletinmode & !Main.Connected & !Main.IACmode) {

                            String Period = parentui.getBeaconPeriod();
                            int iPeriod = Integer.parseInt(Period);

                            if (parentui.getChkBeacon()) {
//                                try {
                                if (iPeriod == 10) {
                                    if (Minute == (i + 10)
                                            | Minute == (i + 20)
                                            | Minute == (i + 30)
                                            | Minute == (i + 40)
                                            | Minute == (i + 50)) {
//                                        try {
//                                        if (!Main.sendLocked) {
                                        Main.theScheduler.offer(new SendCommand(
                                                myarq, SendCommand.CommandMode.BEACON, Main.modeprofile, ""));
//                                            Main.sendLocked = true;
//                                        }
                                        //myarq.send_beacon();

                                        myconfig.setPreference("LATITUDE", parentui.getLatitude());
                                        myconfig.setPreference("LONGITUDE", parentui.getLongitude());
//                                        } catch (InterruptedException ex) {
//                                            ex.printStackTrace();
//                                        }
                                    }
                                } else if (iPeriod == 30) {
                                    if (Minute == (i + 25) | Minute == (i + 45)) {
//                                        try {
//                                        if (!Main.sendLocked) {
                                        Main.theScheduler.offer(new SendCommand(
                                                myarq, SendCommand.CommandMode.BEACON, Main.modeprofile, ""));
//                                          //                                            Main.sendLocked = true;
//                                        }
                                        //myarq.send_beacon();
                                        myconfig.setPreference("LATITUDE", parentui.getLatitude());
                                        myconfig.setPreference("LONGITUDE", parentui.getLongitude());
//                                        } catch (InterruptedException ex) {
//                                            ex.printStackTrace();
//                                        }
                                    }
                                } else {    // 1 hour
                                    if (Minute == (i + 50)) {
//                                        try {
//                                        if (!Main.sendLocked) {
                                        Main.theScheduler.offer(new SendCommand(
                                                myarq, SendCommand.CommandMode.BEACON, Main.modeprofile, ""));
//                                          //                                           Main.sendLocked = true;
//                                        }
//                                            myarq.send_beacon();
                                        myconfig.setPreference("LATITUDE", parentui.getLatitude());
                                        myconfig.setPreference("LONGITUDE", parentui.getLongitude());
//                                        } catch (InterruptedException ex) {
//                                            ex.printStackTrace();
//                                        }
                                    }
                                }
                            }

                        } // minute timer end
                    }
                }

            }
        });
        t.start();
    }

    /**
     *
     * @return
     */
    public void mnuQuitActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
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
                myarq.Message(mainpskmailui.getString("Config_File_stored."), 10);
            }
        } catch (Exception e) {
            myarq.Message(mainpskmailui.getString("problem_writing_the_config_file"), 10);
        }

        parentui.dispose();
        System.exit(0); //calling the method is a must

    }

    public void mnuAboutActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            aboutform about = new aboutform();
            about.setLocationRelativeTo(null);
            about.setVisible(true);
        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Error_when_handling_about_window."), ex, true);
        }
    }

    public void bWXActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        parentui.getWXDialog().setLocationRelativeTo(null);
        parentui.getWXDialog().setVisible(true);
    }

    public void mnuPreferencesActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        try {
            if (!Main.intermar) {
                optionsDialog = new optionsdialog(parentui.getParentFrame(), true);
            } else {
                optionsDialog = new OperatorDataDialog(parentui.getParentFrame(), true);
            }

            optionsDialog.setCallsign(myconfig.getPreference("CALL"));
            optionsDialog.setServer(myconfig.getPreference("SERVER"));
            optionsDialog.setBeaconqrg(myconfig.getPreference("BEACONQRG"));

            // Center screen
            optionsDialog.setLocationRelativeTo(null);
            optionsDialog.setVisible(true);
            myconfig.setCallsign(optionsDialog.getCallsign());
            myarq.setCallsign(optionsDialog.getCallsign());
            String myServer = optionsDialog.getServer();
            myarq.setServer(myServer);
            parentui.setSelectedCboServer(myServer);
            //this.txtServer.setText(myServer);

            // Update the gui with these settings
            if (!Main.gpsport.curstate) {
                parentui.setLatitude(myconfig.getPreference("LATITUDE"));
                parentui.setLongitude(myconfig.getPreference("LONGITUDE"));
            }

            parentui.setCallsign(myconfig.getPreference("CALL"));
            parentui.setAPRSIcon(myconfig.getPreference("ICON"));

            parentui.setOperatorName(myconfig.getPreference("OPERATORNAME"));
            parentui.setPicturePath(myconfig.getPreference("PICTUREPATH"));
            parentui.setPictureName(myconfig.getPreference("PICTURENAME"));

            // Change options to use a spinner
            parentui.setSpnMinute(Integer.parseInt(optionsDialog.getBeaconqrg()));
            optionsDialog.dispose();
        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Error_when_handling_preferences!"), ex, true);
        }
    }

    public void txtMainEntryActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:

        String intext = parentui.getTxtMainEntry();
        if (Main.Connected) {
            Main.TX_Text += (intext + "\n");
            Main.mainwindow += "\n" + intext + "\n";
            parentui.setTxtMainEntry("");
        } else {
            if (intext.contains("@")) {
                myarq.set_txstatus(txstatus.TXUImessage);
                myarq.send_uimessage(intext);
            } else {
//                try {
//                    myarq.set_txstatus(txstatus.TXaprsmessage);
//                    myarq.send_aprsmessage(intext + myarq.getAPRSMessageNumber());
//                  Main.sendLocked = true;
                Main.APRSText = intext + myarq.getAPRSMessageNumber();
                Main.theScheduler.offer(new SendCommand(
                        myarq, SendCommand.CommandMode.APRS, Main.modeprofile, Main.APRSText, false));
//
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
            }
        }
    }

    public void bPingActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
        if (!Main.Connected & !Main.Connecting & !Main.Bulletinmode & !Main.IACmode) {
            try {
                myarq.Message(mainpskmailui.getString("send_ping"), 5);
                myarq.set_txstatus(txstatus.TXPing);
                myarq.send_ping();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void bLinkActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        Main.logFile.writeToLog("Connected: " + Main.Connected + ", Connecting: "
                + Main.Connecting + ", BulletinMode: " + Main.Bulletinmode
                + ", IACMode: " + Main.IACmode);

        if (!Main.Connected & !Main.Connecting & !Main.Bulletinmode & !Main.IACmode) {

            //myarq.Message(mainpskmailui.getString("Link_to_server"), 5);
            //myarq.set_txstatus(txstatus.TXlinkreq);
            //myarq.send_link();
//            if (!Main.sendLocked) {
            Main.theScheduler.offer(new SendCommand(
                    myarq, SendCommand.CommandMode.LINK, Main.modeprofile, ""));
//
//            Main.sendLocked = true;
//            }
            // turn only red if this is fourth or higher false try
            // otherwise higher the number of false trys
            if (Main.LinkTries > 2) {
                System.out.println("Tries outdone");
                parentui.setLinkIndicator(false);
                parentui.setLinkTime("");
            } else {
                Main.LinkTries++;
            }

        }
    }

    public void bBeaconActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
//        if (!Main.sendLocked) {
        Main.theScheduler.offer(new SendCommand(
                myarq, SendCommand.CommandMode.BEACON, Main.modeprofile, ""));
//
//            Main.sendLocked = true;
//        }
//        try {
//            myarq.Message(mainpskmailui.getString("Send_Beacon"), 5);
//            myarq.set_txstatus(txstatus.TXBeacon);
//            myarq.send_beacon();
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//myconfig.setPreference("LATITUDE", txtLatitude.getText());
//myconfig.setPreference("LONGITUDE", txtLongitude.getText());
    }

    public void PositButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
//        try {
        myconfig.setPreference("LATITUDE", parentui.getLatitude());
        myconfig.setPreference("LONGITUDE", parentui.getLongitude());
//            if (!Main.sendLocked) {
        Main.theScheduler.offer(new SendCommand(
                myarq, SendCommand.CommandMode.BEACON, Main.modeprofile, ""));
//                                            Main.sendLocked = true;
//            }
//            myarq.Message(mainpskmailui.getString("Send_Beacon"), 5);
//            myarq.set_txstatus(txstatus.TXBeacon);
//            myarq.send_beacon();
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
    }

    public void txtStatusKeyReleased(java.awt.event.KeyEvent evt) {
// TODO add your handling code here:
        String mystring;
        mystring = parentui.getStatus();
        myconfig.setPreference("STATUS", mystring);
        myarq.setTxtStatus(mystring);
    }

    public void setIcon(String instring) {
        this.Icon = instring;
    }

    ;

    public void cboAPRSIconActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        myarq.Message(mainpskmailui.getString("Icon_set..."), 5);
        myconfig.setPreference("ICON", parentui.getAPRSIcon());
        Main.Icon = parentui.getAPRSIcon();
    }

    public void chkBeaconStateChanged(javax.swing.event.ChangeEvent evt) {
        // TODO add your handling code here:

        if (parentui.getChkBeacon()) {
            myarq.Message(mainpskmailui.getString("Beacon_on"), 5);
            Main.configuration.SetBeacon("1");
        } else {
            myarq.Message(mainpskmailui.getString("Beacon_off"), 5);
            Main.configuration.SetBeacon("0");
        }
    }

    public void FileReadButtonActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
        myarq.Message(mainpskmailui.getString("Choose_File_to_read..."), 5);
        String myfile = "";
        if (evt.getSource() == parentui.getFileReadButton()) {
            File downloads = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator);

            JFileChooser chooser = new JFileChooser(downloads);
            int returnVal = chooser.showOpenDialog(chooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                myfile = chooser.getSelectedFile().getName();
            }


            try {
                File file = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator + myfile);
                if (file.isFile()) {
                    String Content = parentui.getContents(file);
                    parentui.setFilesTextArea(Content);
                }

            } catch (Exception e) {
                // dbd
            }
        }
        myarq.Message(mainpskmailui.getString("Reading_") + myfile, 5);
    }

    public void DeleteFileActionPerformed(java.awt.event.ActionEvent evt) {
        myarq.Message("Choose file to delete", 5);
        File myfile = null;

        JFileChooser chooser = new JFileChooser(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator);
        int returnVal = chooser.showOpenDialog(parentui.getParentFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            myfile = chooser.getSelectedFile();
        }

        try {
            if (myfile.getAbsolutePath().equals(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator + "bulletins")) {
                Main.bulletin = new FileWriter(myfile, false);
            } else {
                boolean success = myfile.delete();

                if (!success) {
                    System.out.println("no deletion could be done");
                }
            }
        } catch (Exception e) {
            System.out.println("Something wrong during deletion attempt");
        }

        myarq.Message("Am Löschen -", 5);
    }

    public void WriteFileActionPerformed(java.awt.event.ActionEvent evt) {
        myarq.Message("Choose file to write to ...", 5);
        File myfile = null;
        BufferedWriter testWriter = null;

        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        int returnVal = chooser.showOpenDialog(parentui.getParentFrame());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            myfile = chooser.getSelectedFile();
        }

        try {
            testWriter = new BufferedWriter(new FileWriter(myfile));
            testWriter.write(parentui.getPosReport());
        } catch (Exception e) {
            System.out.println("exception writing pos report");
        } finally {
            try {
                if (testWriter != null) {
                    testWriter.flush();
                    testWriter.close();
                }
            } catch (Exception e) {
                // do nothing
            }
        }

        myarq.Message("Am Schreiben -", 5);
    }

    public void ReadFileActionPerformed(java.awt.event.ActionEvent evt) {
        myarq.Message("Choose pos-file to read...", 5);
        File myfile = null;

        if (evt.getSource() == parentui.getPosReadButton()) {
            JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir")));
            int returnVal = chooser.showOpenDialog(parentui.getParentFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                myfile = chooser.getSelectedFile();
            }

            try {
                if (myfile.isFile()) {
                    TreeMap<String, TreeMap<String, PosData>> posTemp = (new ParsePosReportFile(myfile, 1)).getPosReport();
                    parentui.appendPosReport(posTemp);
                }
            } catch (Exception e) {
                // dbd
            }
        }

        myarq.Message("Am Lesen -", 5);
    }

    public void ReadTextActionPerformed(java.awt.event.ActionEvent evt) {
        myarq.Message("Choose pos-file to read...", 5);
        String myfile = "";

        if (evt.getSource() == parentui.getTextReadButton()) {
            File downloads = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator);

            JFileChooser chooser = new JFileChooser(downloads);
            int returnVal = chooser.showOpenDialog(chooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                myfile = chooser.getSelectedFile().getName();
            }

            try {
                File file = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator + myfile);
                if (file.isFile()) {
                    final BufferedReader fileScanner = new BufferedReader(
                            new FileReader(file));

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            int i = 0;
                            String line = null;
                            try {
                                while ((line = fileScanner.readLine()) != null) {
                                    parentui.appendMainWindow(line + "\n");
                                    i++;
                                    if (i > 10) {
                                        Thread.sleep(1);
                                        i = 0;
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }).start();
                    // to be implemented
                }
            } catch (Exception e) {
                // dbd
            }
        }

        myarq.Message("Am Lesen -", 5);
    }

    public void ReadSYNOPActionPerformed(java.awt.event.ActionEvent evt) {
        myarq.Message("Choose pos-file to read...", 5);
        String myfile = "";

        if (evt.getSource() == parentui.getSYNOPReadButton()) {
            File downloads = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator);

            JFileChooser chooser = new JFileChooser(downloads);
            int returnVal = chooser.showOpenDialog(chooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                myfile = chooser.getSelectedFile().getName();
            }

            try {
                File file = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator + myfile);
                if (file.isFile()) {
                    // to be implemented
                }
            } catch (Exception e) {
                // dbd
            }
        }

        myarq.Message("Am Lesen -", 5);
    }

    public void ReadGRIBActionPerformed(java.awt.event.ActionEvent evt) {
        myarq.Message("Choose pos-file to read...", 5);
        String myfile = "";

        if (evt.getSource() == parentui.getGRIBReadButton()) {
            File downloads = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator);

            JFileChooser chooser = new JFileChooser(downloads);
            int returnVal = chooser.showOpenDialog(chooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                myfile = chooser.getSelectedFile().getName();
            }

            try {
                File file = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator + myfile);
                if (file.isFile()) {
                    // to be implemented
                }
            } catch (Exception e) {
                // dbd
            }
        }

        myarq.Message("Am Lesen -", 5);
    }

    public void mnuBulletinsActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // TODO add your handling code here:
            myarq.Message(mainpskmailui.getString("Deleting_bulletin_file..."), 5);
            File fb = null;
            if (File.separator.equals("/")) {
                fb = new File(System.getProperty("user.home") + "/.pskmail", mainpskmailui.getString("bulletins"));
            } else {
                fb = new File(System.getProperty("user.home" + "\\pskmail\\"), mainpskmailui.getString("bulletins"));
            }
            parentui.deleteContent(fb);
            parentui.setFilesTextArea("");
        } catch (FileNotFoundException ex) {
            Main.log.writelog(mainpskmailui.getString("File_was_not_found!"), ex, true);
        } catch (IOException ex) {
            Main.log.writelog(mainpskmailui.getString("IO_Exception_when_accessing_file!"), ex, true);
        }
    }

    public void AbortButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        Main.Bulletinmode = false;
        Main.BulletinResetTimerToggle = false;
        Main.BulletinResetTimer = 0;
        Main.theScheduler.acknowledgeLastCommand();
        Main.Connecting = false;
        parentui.setStatusLabel(mainpskmailui.getString("Listening"));
        Main.Status = mainpskmailui.getString("Listening");
        Main.Connected = false;
//        Main.sending_connect = 0;
//        myarq.send_mode_command(Main.defaultmode);
//        Main.connectmode = Main.defaultmode;
//        Main.connect_ok = 0;
//
//        try {
//            myarq.send_txrsid_command("OFF");
//            Thread.sleep(500);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        myarq.Message(mainpskmailui.getString("Aborting..."), 5);
    }

    public void bConnectActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += ("~QUIT" + "\n");
            parentui.setStatusLabel(mainpskmailui.getString("Discon"));
            Main.Connecting = false;
        } else {
            if (parentui.getMnuMailScanning()) {
                Main.Connecting = true;
                parentui.setStatusLabel(mainpskmailui.getString("Connecting"));
                myarq.Message(mainpskmailui.getString("Connecting,_waiting_for_channel..."), 5);
            } else {
                //System.out.println(myarq.get_txstatus());
                try {

                    myarq.set_txstatus(txstatus.TXConnect);
                    myarq.send_frame("");
                    myarq.Message(mainpskmailui.getString("Sending_Connect_request..."), 5);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public void ReadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            String mailnr = "";
            mailnr = parentui.getTxtMainEntry();
            parentui.setTxtMainEntry("");
            mysession.sendRead(mailnr);
            myarq.Message(mainpskmailui.getString("Requesting_email_nr._") + mailnr, 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }
    }

    public void QTCButttonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            String mailnr = "";
            mailnr = mysession.getHeaderCount(mainpskmailui.getString("headers"));
            mysession.sendQTC(mailnr);
            myarq.Message(mainpskmailui.getString("Requesting_mail_headers_from_nr._") + mailnr, 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }
    }

    public void mnuGetTidestationsActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += "~GETTIDESTN\n";
            myarq.Message(mainpskmailui.getString("Requesting_list_of_tidal_reference_stations..."), 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
            threadFactory("~GETTIDESTN\n").start();
            bConnectActionPerformed(evt);
        }
        parentui.changeTabTo(3);
    }

    public void mnuGetTideActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            String tidestationnumber = "";
            // tidestationnumber = parentui.getTxtMainEntry();
            tidestationnumber = parentui.getPanelFour().getInfoscreen().getTableNumber();
            if (tidestationnumber.equals("")) {
                myarq.Message(mainpskmailui.getString("Need__number_of_the_station..."), 5);
            } else {
                Main.TX_Text += "~GETTIDE " + tidestationnumber + "\n";
                myarq.Message(mainpskmailui.getString("Requesting_tidal_information_for_atation_") + tidestationnumber, 5);
            }
        } else {
            String tidestationnumber = "";
            //tidestationnumber = parentui.getTxtMainEntry();
            tidestationnumber = parentui.getPanelFour().getInfoscreen().getTableNumber();
            if (tidestationnumber.equals("")) {
                myarq.Message(mainpskmailui.getString("Need__number_of_the_station..."), 5);
            } else {
                myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
                threadFactory("~GETTIDE " + tidestationnumber + "\n").start();
                bConnectActionPerformed(evt);
            }
        }
        parentui.changeTabTo(3);
    }

    public void mnuGetAPRSActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:

        if (Main.Connected) {
            Main.TX_Text += "~GETNEAR\n";
            myarq.Message(mainpskmailui.getString("Getting_APRS_stations_near_you..."), 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
            threadFactory("~GETNEAR\n").start();
            bConnectActionPerformed(evt);
        }
        parentui.changeTabTo(3);
    }

    public void mnuGetServerfqActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += "~GETSERVERS\n";
            myarq.Message(mainpskmailui.getString("Getting_list_of_servers_from_the_web..."), 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
            threadFactory("~GETSERVERS\n").start();
            bConnectActionPerformed(evt);
        }
        parentui.changeTabTo(3);
    }

    public void mnuGetPskmailNewsActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += "~GETNEWS\n";
            myarq.Message(mainpskmailui.getString("Trying_to_get_the_news_from_the_web..."), 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
            threadFactory("~GETNEWS\n").start();
            bConnectActionPerformed(evt);
        }
        parentui.changeTabTo(3);
    }

    public void mnuHeadersActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        parentui.setMailHeadersWindow("");
        mysession.deleteFile("headers");
        myarq.Message(mainpskmailui.getString("Delete_list_of_mail_headers..."), 5);
    }

    public void NewButtonActionPerformed(java.awt.event.ActionEvent evt) {
        NewMailDialog NewDialog;
        myarq.Message(mainpskmailui.getString("Write_new_email_message"), 5);

        try {
            NewDialog = new NewMailDialog(parentui.getParentFrame(), true);
            NewDialog.setLocationRelativeTo(null);
            NewDialog.setVisible(true);
            NewDialog.dispose();
        } catch (Exception e) {
            Main.log.writelog(mainpskmailui.getString("Error_when_handling_about_window."), e, true);
        }
    }

    public void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here
        if (Main.Connected) {
            FileReader out = null;
            myarq.Message(mainpskmailui.getString("Trying_to_send_your_email..."), 5);
            try {
                File dir = new File(Main.HomePath + Main.Dirprefix + "Outbox");
                File[] files = dir.listFiles();
                Main.Mailoutfile = files[0].getAbsolutePath();
                FileReader in = new FileReader(files[0]);
                BufferedReader br = new BufferedReader(in);
                String record = null;
                while ((record = br.readLine()) != null) {
                    Main.TX_Text += record + "\n";
                }

            } catch (IOException ex) {
                Logger.getLogger(mainpskmailui.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }


    }

    public void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            String numbers = parentui.getTxtMainEntry();
            if (numbers.length() > 0) {
                mysession.sendDelete(numbers);
                myarq.Message(mainpskmailui.getString("Trying_to_delete_mail_nr._") + numbers, 5);
            } else {
                myarq.Message(mainpskmailui.getString("Which_mail_numbers?"), 5);
            }
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }

    }

    public void ListFilesButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += "~LISTFILES\n";
            myarq.Message(mainpskmailui.getString("Requesting_files_list..."), 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }
    }

    public void DownloadButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            String file = parentui.getTxtMainEntry();
            if (file.length() > 0) {
                Main.TX_Text += "~GETBIN " + file + "\n";
                myarq.Message(mainpskmailui.getString("Requsting_file_") + file, 5);
            } else {
                myarq.Message(mainpskmailui.getString("Which_file_shall_I_get?"), 5);
            }
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }
    }

    public void FileConnectActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += ("~QUIT" + "\n");
            parentui.setStatusLabel(mainpskmailui.getString("Discon"));
            myarq.Message(mainpskmailui.getString("trying_to_disconnect..."), 5);
        } else {
            //System.out.println(myarq.get_txstatus());
            try {
                myarq.set_txstatus(txstatus.TXConnect);
                myarq.send_frame("");
                myarq.Message(mainpskmailui.getString("Choose_File_to_read..."), 5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void FileAbortButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        Main.Bulletinmode = false;
        Main.BulletinResetTimerToggle = false;
        Main.BulletinResetTimer = 0;
        Main.theScheduler.acknowledgeLastCommand();
        Main.Connecting = false;
        parentui.setStatusLabel(mainpskmailui.getString("Listening"));
        Main.Status = mainpskmailui.getString("Listening");
        Main.Connected = false;

        try {
            //myarq.send_txrsid_command("OFF");
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        myarq.Message(mainpskmailui.getString("Aborting..."), 5);
    }

    public void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (!Main.Connected) {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        } else {
            myarq.Message(mainpskmailui.getString("Choose_File_to_update..."), 5);
            if (evt.getSource() == parentui.getUpdateFileButton()) {
                File downloads = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator);
                String myfile = "";
                JFileChooser chooser = new JFileChooser(downloads);
                int returnVal = chooser.showOpenDialog(chooser);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    myfile = chooser.getSelectedFile().getName();
                }
                File dfile = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator + myfile);
                if (dfile.isFile()) {
                    Main.TX_Text += "~GETBIN " + myfile + "\n";
                    myarq.Message(mainpskmailui.getString("Updating_") + myfile, 5);
                }

            }
        }
    }

    public void mnuMboxListActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += "~LISTLOCAL\n";
            myarq.Message(mainpskmailui.getString("Requesting_list_of_local_mails_on_the_server"), 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }
    }

    public void mnuMboxReadActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            String number = parentui.getTxtMainEntry();
            if (number.length() > 0) {
                Main.TX_Text += "~READLOCAL " + number + "\n";
                myarq.Message(mainpskmailui.getString("Reading_local_mail_") + number + mainpskmailui.getString("_on_the_server"), 5);
            } else {
                myarq.Message(mainpskmailui.getString("Which_email?_(need_number...)"), 5);
            }
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }
    }

    public void mnuMboxDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            String number = parentui.getTxtMainEntry();
            if (number.length() > 0) {
                Main.TX_Text += "~DELETELOCAL " + number + "\n";
                myarq.Message(mainpskmailui.getString("Deleting_mail_nr._") + number + mainpskmailui.getString("on_the_server"), 5);
            }
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }
    }

    public void mnuGetCamperActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += "~GETCAMP " + parentui.getLatitude() + " " + parentui.getLongitude() + "\n";
            myarq.Message(mainpskmailui.getString("Requesting_list_of_camp_sites_from_the_web_(EU_only)"), 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
            threadFactory("~GETCAMP " + parentui.getLatitude() + " " + parentui.getLongitude() + "\n").start();
            bConnectActionPerformed(evt);
        }
        parentui.changeTabTo(3);
    }

    public void mnuGetWebPagesActionPerformed(java.awt.event.ActionEvent evt) {
        GetWebPageDialog WebDialog;
        // TODO add your handling code here:
        try {
            WebDialog = new GetWebPageDialog(parentui.getParentFrame(), true);

            WebDialog.dispose();
        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Error_when_opening_web_dialog!"), ex, true);
        }

    }

    public void mnuPSK63ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            modemmodeenum mymode = modemmodeenum.PSK63;
            parentui.updatemodeset(mymode);
            myarq.send_mode_command(mymode);
        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Encountered_problem_when_setting_mode."), ex, true);
        }
        myarq.Message(mainpskmailui.getString("Switching_modem_to_PSK63"), 5);
    }

    public void mnuPSK125ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            modemmodeenum mymode = modemmodeenum.PSK125;
            parentui.updatemodeset(mymode);
            myarq.send_mode_command(mymode);
        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Encountered_problem_when_setting_mode."), ex, true);
        }
        myarq.Message(mainpskmailui.getString("Switching_modem_to_PSK125"), 5);
    }

    public void mnuPSK250ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            modemmodeenum mymode = modemmodeenum.PSK250;
            parentui.updatemodeset(mymode);
            myarq.send_mode_command(mymode);
        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Encountered_problem_when_setting_mode."), ex, true);
        }
        myarq.Message(mainpskmailui.getString("Switching_modem_to_PSK250"), 5);
    }

    public void mnuMFSK64ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            modemmodeenum mymode = modemmodeenum.MFSK64;
            parentui.updatemodeset(mymode);
            myarq.send_mode_command(mymode);
        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Encountered_problem_when_setting_mode."), ex, true);
        }
        myarq.Message(mainpskmailui.getString("Switching_modem_to_MFSK64"), 5);
    }

    public void mnuTHOR22ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            modemmodeenum mymode = modemmodeenum.THOR22;
            parentui.updatemodeset(mymode);
            myarq.send_mode_command(mymode);
        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Encountered_problem_when_setting_mode."), ex, true);
        }
        myarq.Message(mainpskmailui.getString("Switching_modem_to_THOR22"), 5);
    }

    public void mnuModeQSYActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += "~QSY!\n";
            myarq.Message(mainpskmailui.getString("Asking_the_server_to_QSY"), 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
        }
    }

    public void mnuNewActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        NewMailDialog NewDialog;
        myarq.Message(mainpskmailui.getString("Write_new_email_message"), 5);

        try {
            NewDialog = new NewMailDialog(parentui.getParentFrame(), true);
            NewDialog.setLocationRelativeTo(null);
            NewDialog.setVisible(true);
            NewDialog.dispose();
        } catch (Exception e) {
            Main.log.writelog(mainpskmailui.getString("Error_when_handling_about_window."), e, true);
        }
    }

    public void mnuMailQueueActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        File path = new File(Main.HomePath + Main.Dirprefix + "Outbox" + Main.Separator);
        if (path.exists()) {
            File[] files = path.listFiles();

            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            myarq.Message(mainpskmailui.getString("Deleting_outgoing_mail"), 5);
        } else {
            myarq.Message(mainpskmailui.getString("No_messages_to_delete"), 5);
        }

    }

    public void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
// TODO add your handling code here:
    }

    public void menuMessagesActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += "~/~GETMSG\n";

            myarq.Message(mainpskmailui.getString("Getting_list_of_messages_from_the_web..."), 5);
        } else {
            myarq.Message(mainpskmailui.getString("You_need_to_connect_first..."), 5);
            threadFactory("~/~GETMSG\n").start();
            bConnectActionPerformed(evt);
        }
        parentui.changeTabTo(3);
    }

    public void mnuMailAPRS2ActionPerformed(java.awt.event.ActionEvent evt) {
        // Set the menu selected
        parentui.updatemailscanset(0);
    }

    public void mnuMailScanningActionPerformed(java.awt.event.ActionEvent evt) {
        parentui.updatemailscanset(1);
    }

    public void mnuTTY2ActionPerformed(java.awt.event.ActionEvent evt) {
        parentui.updatemailscanset(2);
    }

    public void GetGribActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String input = parentui.getTxtMainEntry();
        if (input.length() == 0) {
            String lat = parentui.getLatitude();
            String lon = parentui.getLongitude();
            String hilatstr = "N";
            String lolatstr = "N";
            String hilonstr = "E";
            String lolonstr = "E";
            float fl_lat = Float.valueOf(lat).floatValue();
            float fl_lon = Float.valueOf(lon).floatValue();
            int Intlat = (int) fl_lat;
            int Intlon = (int) fl_lon;
            int hilat = 0;
            int hilon = 0;
            int lolat = 0;
            int lolon = 0;

            hilat = Intlat + 5;
            if (hilat < 0) {
                hilatstr = "S";
                hilat = Math.abs(hilat);
            }
            lolat = Intlat - 5;
            if (lolat < 0) {
                lolatstr = "S";
                lolat = Math.abs(lolat);
            }
            hilon = Intlon + 5;
            if (hilon < 0) {
                hilonstr = "W";
                hilon = Math.abs(hilon);
            }
            lolon = Intlon - 5;
            if (lolon < 0) {
                lolonstr = "W";
                lolon = Math.abs(lolon);
            }
            input = Integer.toString(hilat) + hilatstr + "," + Integer.toString(lolat) + lolatstr + ","
                    + Integer.toString(hilon) + hilonstr + "," + Integer.toString(lolon) + lolonstr;
        }

        String gribsquare = "send gfs:" + input;
        Pattern pgs = Pattern.compile(".*(\\d+\\w,\\d+\\w,\\d+\\w,\\d+\\w).*");
        Matcher mgs = pgs.matcher(gribsquare);
        if (!mgs.find()) {
            myarq.Message(mainpskmailui.getString("Format_error:") + gribsquare, 10);
        } else {
            if (Main.Connected) {
                Main.TX_Text += "~SEND\nTo: query@saildocs.com\nSubject: none\n\n" + gribsquare + "\n.\n.\n";
            } else {
                myarq.Message(mainpskmailui.getString("Connect_first..."), 10);
                threadFactory("~SEND\nTo: query@saildocs.com\nSubject: none\n\n" + gribsquare + "\n.\n.\n").start();
                bConnectActionPerformed(evt);
            }
        }
        parentui.changeTabTo(4);
    }

    public void TwitterActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    public void Twitter_sendActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // TODO add your handling code here:
            String message = parentui.getTxtMainEntry();
            if (message.length() > 0) {
                message = "tweet@ttt " + message;
                myarq.set_txstatus(txstatus.TXUImessage);
                myarq.send_uimessage(message);
            } else {
                myarq.Message(mainpskmailui.getString("What?"), 10);
            }
        } catch (Exception e) {
            Main.log.writelog(mainpskmailui.getString("Problem_sending_tweet."), e, true);
        }
    }

    /**
     * Store the minute setting if changed by the user
     * @param evt
     */
    public void spnMinuteStateChanged(javax.swing.event.ChangeEvent evt) {
        try {
            Main.configuration.setBeaconqrg(parentui.getSpnMinute());
        } catch (Exception e) {
            Main.log.writelog(mainpskmailui.getString("Problem_changing_server_minute."), true);
        }
    }

    public void GetUpdatesmenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (Main.Connected) {
            Main.TX_Text += "~GETUPDATE\n";
        } else {
            myarq.Message(mainpskmailui.getString("Connect_first..."), 10);
        }
    }

    public void cboServerFocusLost(java.awt.event.FocusEvent evt) {
        try {
            String myServer = parentui.getSelectedCboServer();
            String OldServer = myarq.getServer();
            if (myServer.length() > 1 && !myServer.equals(OldServer)) {
                myarq.setServer(myServer);
                Main.configuration.setServer(myServer);
//            serverInput(myServer);
            }

        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Had_trouble_setting_the_server_to_link_to."), ex, true);
        }
    }

    /**
     * User wrote in the server combo, lets handle that shall we..
     * @param evt
     */
    public void cboServerActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String myServer = parentui.getSelectedCboServer();
            String OldServer = myarq.getServer();
            // Is it a a new and not empty thing?
            if (myServer.length() > 1 && !myServer.equals(OldServer)) {
                myarq.setServer(myServer);
                Main.configuration.setServer(myServer);
                // Update the server array and add item to drop down
                Main.AddServerToArray(myServer);
            }
        } catch (Exception ex) {
            Main.log.writelog(mainpskmailui.getString("Had_trouble_setting_the_server_to_link_to."), ex, true);
        }

    }

    public void deleteFile(File file, long lifeSpan, long timeSpan) {
        if (file.exists() && lifeSpan > timeSpan) {
            file.delete();
        }
    }

    public void deleteAllButNewest(File[] fileList, long timeSpan) {
        // System.out.println(fileList.length);

        if (fileList.length > 0) {
            long now = new Date().getTime();

            File youngestFile = fileList[0];
            long youngestFileLife = now - youngestFile.lastModified();

            for (int i = 1; i < fileList.length; i++) {

                long lifeSpan = now - fileList[i].lastModified();

                if (lifeSpan < youngestFileLife) {
                    deleteFile(youngestFile, youngestFileLife, timeSpan);
                    youngestFile = fileList[i];
                    youngestFileLife = lifeSpan;
                } else {
                    deleteFile(fileList[i], lifeSpan, timeSpan);
                }
            }
        }
    }

    public void ZyGribMapActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String fileName = "";

            java.io.FileFilter grbFleetFilter = new java.io.FileFilter() {

                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".grb");
                }
            };

            java.io.FileFilter fltFleetFilter = new java.io.FileFilter() {

                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".flt");
                }
            };

            File zyGribDirectory = new File(Main.weatherPath);

            File[] fleetFilesAvailable = zyGribDirectory.listFiles(fltFleetFilter);
            File[] gRIBFilesAvailable = zyGribDirectory.listFiles(grbFleetFilter);

            long now = new Date().getTime();

            /* ---- processing fleet files ---- */

            deleteAllButNewest(fleetFilesAvailable, 86400000);

            /* ---- processing GRIB files ---- */

            deleteAllButNewest(gRIBFilesAvailable, 86400000);

            /* ---- cleaned up the old reports ---- */

            JFileChooser chooser = new JFileChooser(new File(Main.weatherPath));
            chooser.setFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".grb")
                            || f.getName().toLowerCase().endsWith(".flt")
                            || f.getName().toLowerCase().equals("gribbroadcasted");
                }

                public String getDescription() {
                    return "GRIB or fleetcode Files";
                }
            });


            int r = chooser.showOpenDialog(parentui.getParentFrame());

            if (r == JFileChooser.APPROVE_OPTION) {
                fileName = chooser.getSelectedFile().getAbsolutePath();

                Process p = Runtime.getRuntime().exec("..\\zyGrib\\zyGrib.exe " + "\"" + fileName + "\"", new String[0], new File(new File("..\\zyGrib").getAbsolutePath()));

                p.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ServerUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        myarq.Message("Trying to send update...", 5);
        if (Main.Connected) {
            myarq.Message("Sending update...", 5);
            mysession.sendUpdate();
        } else {
            // parentui.setVisible(false);
            myarq.Message("You need to connect first...", 5);
        }
    }
}
