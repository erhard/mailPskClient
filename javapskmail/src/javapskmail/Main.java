/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javapskmail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javapskmail.SendCommand.CommandMode;
import javax.swing.UIManager;

/**
 *
 * @author per
 */
public class Main {

    static javax.swing.Timer callDelayTimer = null;
    static boolean enableSend = true;
    static SendScheduler theScheduler;
    static boolean intermar = true;
    static String application = "INTERMAR PSKmail V 1.5"; // Used to preset an empty status
    static String version = "version 1.5, 12-01-09, build 00";
    static String host = "localhost";
    static int port = 7322;
    static modemmodeenum Mymode = modemmodeenum.PSK250;
    static modemmodeenum[] modeprofile;
    // static int link_ok = 0;
    // static modemmodeenum linkmode = modemmodeenum.PSK250;
    static int modemnumber = 0;
    static modemmodeenum defaultmode = modemmodeenum.PSK250;
    //static int sending_link = 0;
    //static int sending_beacon = 0;
    //static int beacon_ok = 0;
    //static modemmodeenum beaconmode = modemmodeenum.PSK250;
    //static modemmodeenum APRSmode = modemmodeenum.PSK250;
    //static int sending_APRS = 0;
    //static int APRS_ok = 0;
    static String APRSText = "";
    static int callDelay = 20;
    //static boolean resetOperationModeToggle = false;
    //static int resetOperationModeCounter = 0;
    static String currentOperatingMode = "PSK250";
    //static boolean beaconloop = false;
    // boolean linkloop = false;
    //static boolean APRSloop = false;
    //static boolean emergencyloop = false;
    //static modemmodeenum emergencymode = modemmodeenum.PSK250;
    //static int sending_emergency = 0;
    //static int emergency_ok = 0;
    // static boolean sendLocked = false; // no lock at the beginning of the program
    static String HomePath = "";
    static String Dirprefix = "/.pskmail/";
    static String Separator = "/";
    static String Mailoutfile = "";
    static boolean Bulletinmode = false;
    static boolean IACmode = false;
    static boolean debug = false;
    static String Sendline = "";
    static String SendCommand = "";
    static int DCD = 0;
    static int MAXDCD = 3;
    static boolean BlockActive = false;
    static boolean TXActive = false;
    static int Second = 30;  // Beacon second
    // globals to pass info to gui windows
    static String monitor = "";
    static String mainwindow = "";
    static String MSGwindow = "";
    static String Mailheaderswindow = "";
    static String FilesTextArea = "";
    static String Status = "Listening";
    static String Statusline = "";
    static int StatusLineTimer;
    // globals for communication
    static String Icon;
    static int APRSMessageNumber;
    static String mycall;     // mycall from options
    static String myserver;    // myserver from options
    static long blockval = 0;
    static boolean Connected = false;
    static boolean Connecting = false;
    static String session; // present session
    static boolean validblock = true;
    static String myrxstatus = "   "; // last rx status
    static String TX_Text; // output queue
    static int Progress = 0;
    static String DataSize = "";
    static ArrayList<String> Servers = new ArrayList<String>();
    //static String Servers[] = {"", "", "", "", "", "", "", "", "", ""};
    // GPS handle
    static serialport gpsport;    // Serial port object
    static nmeaparser gpsdata;    // Parser for nmea data
    static arq q;
    // Config object
    static config configuration; // Static config object
    // Error handling and logging object
    static loggingclass log;
    // Our main window
    static boolean pollindicator;
    static boolean txblockerror;
    static boolean rxblockerror;
    // PSKmail main window
    //if (!intermar) then
    //        static mainpskmailintermar mainui;
    //else
    static mainui mainui;
    // Modem handle
    static private Modem m;
    // File handles
    static FileWriter bulletin = null;
    static FileReader hdr = null;
    // DCD
    static String DCDstr;
    // last recognized linked server (to evaluate indicator)
    static String linkedServer = "";
    static int LinkTries = 0;
    static BulletinParser bulletinParser = new BulletinParser();
    static String lastKnownFixAt = "";
    static long GPScounter = 0;
    static ServerDatabase pskServerDatabase;
    static SeaWeatherParser seaWeatherParser = new SeaWeatherParser();
    static DecodeFleetCode fleetDecoder = new DecodeFleetCode();
    static LogFile logFile = null;
    static boolean logAvailable = false;
    static String ShipDBPath = HomePath + Dirprefix + "ShipDB.zip";
    static String serverlistPath = Main.HomePath + Main.Dirprefix + "serverlist";
    static String wxPath = Main.HomePath + Main.Dirprefix + "wx";
    static String tempConfPath = HomePath + Dirprefix + "tempconfiguration.xml";
    static String posReportPath = HomePath + Dirprefix + "posreport/";
    static String weatherPath = HomePath + Dirprefix + "weatherfiles/";
    static String metareaPath = HomePath + Dirprefix + "metarea/";
    static InputSynchronizer mainInput = new InputSynchronizer();
    static TreeMap<Integer, MetareaCoords> metareaList;
    static boolean metareaTimerToggle = false;
    static int metareaTimer = 0;
    static MetareaRecorder metareaRecorder = new MetareaRecorder();
    static boolean gribTimerToggle = false;
    static int gribTimer = 0;
    static GRIBRecorder gribRecorder = new GRIBRecorder();
    static boolean emergencyToggle = false;
    static int emergencyTimer = 0;
    static String emergencyMessage = "";

    static boolean BulletinResetTimerToggle = false;
    static int BulletinResetTimer = 0;

    /**
     * @param args the command line arguments
     */
    //@SuppressWarnings("empty-statement")
    public static void main(String[] args) throws InterruptedException {

        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // handle exception
            System.out.println("Problem setting look and feel.");
        }

        // Create error handling class
        log = new loggingclass(HomePath + Dirprefix + "jpskmaillog");

        javax.swing.ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        try {


            String Blockline = "";

            // Call the folder handling method
            handlefolderstructure();

            // Create config object
            configuration = new config(HomePath + Dirprefix);

            tempConfPath = HomePath + Dirprefix + "tempconfiguration.xml";

            ShipDBPath = HomePath + Dirprefix + "ShipDB.zip";

            serverlistPath = Main.HomePath + Main.Dirprefix + "serverlist";

            wxPath = Main.HomePath + Main.Dirprefix + "wx";

            posReportPath = HomePath + Dirprefix + "posreports/";

            weatherPath = HomePath + Dirprefix + "weatherfiles/";

            metareaPath = HomePath + Dirprefix + "metarea/";

            try {
                metareaList = new ParseMetareaCoords(new File("metarea/metareaCoords")).getMetareaList();
            } catch (Exception e) {
                metareaList = new TreeMap<Integer, MetareaCoords>();
            }

            try {
                File tempCheck = new File("manualDelay");
                if (tempCheck.exists()) {
                    Scanner tempScan = new Scanner(tempCheck);
                    String tempStr = null;
                    if (tempScan.hasNextLine()) {
                        tempStr = tempScan.nextLine();
                        Main.callDelay = new Integer(tempStr);
                    }
                }
            } catch (NumberFormatException e) {
                Main.callDelay = 14;
            } catch (Exception e) {
            }

            // System.out.println("call delay: " + Main.callDelay);

            try {
                File tempCheck = new File(wxPath);
                if (!tempCheck.exists()) {
                    tempCheck.createNewFile();
                }
            } catch (Exception e) {
            }

            try {
                File tempCheck = new File(posReportPath);
                if (!tempCheck.exists()) {
                    tempCheck.mkdir();
                }
                tempCheck = new File(posReportPath + "posreport.txt");
                if (!tempCheck.exists()) {
                    tempCheck.createNewFile();
                }
            } catch (Exception e) {
            }

            try {
                File tempCheck = new File(weatherPath);
                if (!tempCheck.exists()) {
                    tempCheck.mkdir();
                }
            } catch (Exception e) {
            }

            try {
                File tempCheck = new File(metareaPath);
                if (!tempCheck.exists()) {
                    tempCheck.mkdir();
                }
            } catch (Exception e) {
            }

            /* ------------------ */
            // ONLY TEMPORARY //
            seaWeatherParser = new SeaWeatherParser(new File(wxPath));
            /* ------------------ */

            try {
                logFile = new LogFile(HomePath + Dirprefix + "log");
                logAvailable = true;
            } catch (IOException e) {
                System.out.println("Problems with log file.");
            }

            // Make arq object
            q = new arq();

            // Get settings and initialize
            handleinitialization();

            // Handle GPS
            handlegps();

            // Make session object
            Session sm = new Session();  // session, class

            theScheduler = new SendScheduler(q);

            // Show the main window (center screen)
            if (!intermar) {
                mainui = new mainpskmailui();
            } else {
                mainui = new mainpskmailintermar();
            }

            mainui.setLocationRelativeTo(null);
            mainui.setVisible(true);

            // start the modem thread
            //     System.out.println("Connecting to the modem.");
            m = new Modem(host, port);
            Thread myThread = new Thread(m);
            // Start the modem thread
            myThread.setDaemon(true);
            myThread.start();
            q.Message(version, 10);

            q.send_rsid_command("ON");
            q.send_txrsid_command("ON");

//            int testI = 0; // for test injections
//            ArrayList<String> testarray = new ArrayList<String>();
//            BufferedReader fileScanner = new BufferedReader(
//                    new FileReader(new File("gribdatareceived")));
//            String templine;
//            String constructedline = "";
//            while ((templine = fileScanner.readLine()) != null) {
//                constructedline += templine + "\n";
//                //testarray.add(templine + "\n");
//            }
//            //testI = testarray.size();
//
//            String[] elements = constructedline.split("<SOH>");

            //Pattern p = Pattern.compile("(<SOH>[^(?:<SOH>)]*<EOT>)");
//            Pattern p = Pattern.compile("(<SOH>[^(?:<SOH>)]*<EOT>)",Pattern.DOTALL);
//            Matcher m1 = p.matcher(constructedline);
//
//            while (m1.find()) {
//
//                testarray.add(m1.group(1));
//                constructedline = constructedline.substring(m1.end(1));
//                m1 = p.matcher(constructedline);
//            }

//            for (String s: elements) {
//                if (s.contains("<EOT>")) {
//                    testarray.add("<SOH>" + s);
//                }
//            }
//
//            Iterator<String> iterstr = testarray.iterator();
//
//            while (iterstr.hasNext()) {
//                System.out.println("line: " + iterstr.next());
//            }

//            SendCommand test = new SendCommand(q, CommandMode.BEACON, modeprofile, "");
//
//            boolean btest = theScheduler.offer(test);
//
//            System.out.println(btest);

            while (true) {

//                if (!Main.Connected
//                        & !Main.Connecting
//                        & !Main.Bulletinmode
//                        & !Main.IACmode
//                        & sending_emergency >= 0
//                        & emergency_ok == 0) {
//
//                    switch (sending_emergency) {
//                        case 0:
//                            // Main.defaultmode = modemmodeenum.PSK250;
//                            Main.emergencymode = Main.defaultmode;
//                            break;
//                        case 1:
//                            if (Main.emergencyloop) {
//                                Main.emergencyloop = false;
//                                Main.resetOperationModeCounter = 0;
//                                Main.resetOperationModeToggle = true;
//                            }
//                            Main.emergencymode = modeprofile[2];
//                            break;
//                        case 2:
//                            Main.emergencymode = modeprofile[1];
//                            break;
//                        case 3:
//                            Main.emergencyloop = true;
//                        default:
//                            break;
//                    }
//
//                    // send link if TXActive false
//                    if (!Main.TXActive) {
//
//                        if (sending_emergency > 0) {
//                            // decrement sending_link
//                            sending_emergency--;
//
//                            try {
//                                // send emergency
////                                q.Message("Send beacon", 5);
//                                q.Message("Send emergency", 5);
//                                q.set_txstatus(txstatus.TXEmergency);
//                                q.send_emergency(Main.emergencyMessage);
////                                q.set_txstatus(txstatus.TXBeacon);
////                                q.send_beacon();
//                                Main.emergency_ok++;
//                            } catch (InterruptedException ex) {
//                                ex.printStackTrace();
//                            }
//
////                            if (sending_emergency == 0) {
////                                Main.sendLocked = false;
////                            }
//
//                        }
//                    }
//
//                }
//
//                if (!Main.Connected
//                        & !Main.Connecting
//                        & !Main.Bulletinmode
//                        & !Main.IACmode
//                        & sending_beacon >= 0
//                        & beacon_ok == 0) {
//
//                    switch (sending_beacon) {
//                        case 0:
//                            // Main.defaultmode = modemmodeenum.PSK250;
//                            Main.beaconmode = Main.defaultmode;
//                            break;
//                        case 1:
//                            if (Main.beaconloop) {
//                                Main.beaconloop = false;
//                                Main.resetOperationModeCounter = 0;
//                                Main.resetOperationModeToggle = true;
//                            }
//                            Main.beaconmode = modeprofile[2];
//                            break;
//                        case 2:
//                            Main.beaconmode = modeprofile[1];
//                            break;
//                        case 3:
//                            Main.beaconloop = true;
//                        default:
//                            break;
//                    }
//
//                    // send link if TXActive false
//                    if (!Main.TXActive) {
//
//                        if (sending_beacon > 0) {
//                            // decrement sending_link
//                            sending_beacon--;
//
//
//
//                            //if (!Main.TXActive) {
//                            try {
//                                q.Message("Send beacon", 5);
//                                q.set_txstatus(txstatus.TXBeacon);
//                                q.send_beacon();
//                                Main.beacon_ok++;
//                            } catch (InterruptedException ex) {
//                                ex.printStackTrace();
//                            }
//
////                            if (sending_beacon == 0) {
////                                Main.sendLocked = false;
////                            }
//
//                        }
//                    }
//
//                }
//
//                if (!Main.Connected & !Main.Connecting & !Main.Bulletinmode & !Main.IACmode & sending_link >= 0 & link_ok == 0) // check sending_link
//                {
//                    switch (sending_link) {
//                        case 0:
//                            // Main.defaultmode = modemmodeenum.PSK250;
//                            Main.linkmode = Main.defaultmode;
//                            break;
//                        case 1:
//                            if (Main.linkloop) {
//                                Main.linkloop = false;
//                                Main.resetOperationModeCounter = 0;
//                                Main.resetOperationModeToggle = true;
//                            }
//                            Main.linkmode = modeprofile[2];
//                            break;
//                        case 2:
//                            Main.linkmode = modeprofile[1];
//                            break;
//                        case 3:
//                            Main.linkloop = true;
//                        default:
//                            break;
//                    }
//
//                    // send link if TXActive false
//                    if (!Main.TXActive) {
//
//                        if (sending_link > 0) {
//                            // decrement sending_link
//                            sending_link--;
//
//                            try {
//                                q.Message("Link to server", 5);
//                                q.set_txstatus(txstatus.TXlinkreq);
//                                q.send_link();
//                                Main.link_ok++;
//                            } catch (InterruptedException ex) {
//                                ex.printStackTrace();
//                            }
//
////                            if (sending_link == 0) {
////                                Main.sendLocked = false;
////                            }
//
//                        }
//                    }
//                }
//
//                if (!Main.Connected & !Main.Bulletinmode & !Main.IACmode & sending_APRS >= 0 & APRS_ok == 0) // check sending_link
//                {
//                    switch (sending_APRS) {
//                        case 0:
//                            // Main.defaultmode = modemmodeenum.PSK250;
//                            Main.APRSmode = Main.defaultmode;
//                            break;
//                        case 1:
//                            if (Main.APRSloop) {
//                                Main.APRSloop = false;
//                                Main.resetOperationModeCounter = 0;
//                                Main.resetOperationModeToggle = true;
//                            }
//                            Main.APRSmode = modeprofile[2];
//                            break;
//                        case 2:
//                            Main.APRSmode = modeprofile[1];
//                            break;
//                        case 3:
//                            Main.APRSloop = true;
//                        default:
//                            break;
//                    }
//
//                    // send link if TXActive false
//                    if (!Main.TXActive) {
//
//                        if (sending_APRS > 0) {
//                            // decrement sending_link
//                            sending_APRS--;
//
//                            try {
//                                // perform APRS message
//                                q.set_txstatus((txstatus.TXaprsmessage));
//                                q.send_aprsmessage(Main.APRSText);
//
//                                Main.APRS_ok++;
//                            } catch (InterruptedException ex) {
//                                ex.printStackTrace();
//                            }
//
////                            if (sending_APRS == 0) {
////                                Main.sendLocked = false;
////                            }
//
//                        }
//                    }
//                }

                // Send a command
                if (!SendCommand.equals("")) {
                    while (Main.TXActive) {
                        Thread.sleep(500);
                    }

                    m.Sendln(SendCommand);
                    SendCommand = "";

                    Thread.sleep(50);
                }

                if (Sendline.length() > 0) {
                    Main.logFile.writeToLog("checking Sendline condition. "
                            + "TXActive: " + Main.TXActive + ", Main.DCD: " + Main.DCD);
                }
                // see if tx active and DCD is off
                if ((Sendline.length() > 0) & (!Main.TXActive) & (Main.DCD == 0)) {
                    // Main.logFile.writeToLog("Sendline needs to be put onto the modem line.");
                    try {
                        Main.logFile.writeToLog("Sending send_line");
                        m.Sendln(Sendline);
                        Main.logFile.writeToLog("Sent send_line");
                        Sendline = "";
                        Main.logFile.writeToLog("Sendline deleted.");
                        Main.TXActive = true;
                        Main.logFile.writeToLog("activating TYActive.");
                        if (callDelayTimer != null) {
                            if (callDelayTimer.isRunning()) {
                                callDelayTimer.stop();
                                callDelayTimer = null;
                                Main.logFile.writeToLog("stopping call delay timer.");
                            }
                        }

                        callDelayTimer =
                                new javax.swing.Timer(Main.callDelay * 1000, new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                Main.enableSend = true;
                                Main.logFile.writeToLog("enableSend enabled.");
                            }
                        });
                        callDelayTimer.setRepeats(false);
                        Main.logFile.writeToLog("Call Delay timer started.");
                        callDelayTimer.start();
                    } catch (Exception e) {
                        Main.monitor += ("\nModem problem. Is fldigi running?");
                        log.writelog("Modem problem. Is fldigi running?", e, true);
                    }
                } else if (Sendline.length() > 0) {
                    Main.logFile.writeToLog("Sendline denied.");
                }

//                mainui.setPosReport(mainui.getMainPosReport());

                try {

//                      Object[] testInput = (Object[]) testarray.toArray();

//                    String[] testInput = {"<SOH>ZCZC\nADDC<EOT>",
//                    "<SOH>QTC de DK4XI-80\nE035<EOT>",
//                    "<SOH>Fr 20. Nov 15:03:03 2009\n3EEA<EOT>",
//                    "<SOH>   METAREA_VIII_bi0owebfowbfowbfoiwbf    \n96AA<EOT>",
//                    "<SOH>NNNN96AA<EOT>"};

//                    String[] testInput = {"<SOH>ZCZC\n",
//                        "ADDC<EOT><SOH>QTC de DK4XI-80\n",
//                        "E035<EOT><SOH>Fr 20. Nov 15:03:03009\n",
//                        "3EEA<EOT><SOH>\n",
//                        "0780<EOT><SOH>\n",
//                        "0780<EOT><SOH>NNNN\n",
//                        "96AA<EOT><SOH>NNNN"};

//                    String[] testInput = {"<SOH>ZFZF",
//                            "31DF<EOT>",
//                            "<SOH>FSXX21 EGRR 280000",
//                            "BECA<EOT>",
//                            "<SOH>eo rlllreii iooeoaco oeoooanm dddoomct smhllmod",
//                            "B3F8<EOT>",
//                            "<SOH>et oslersin aeemctoc celdiiae emctodre fncnfioh",
//                            "520A<EOT>",
//                            "<SOH>ea niirceem ctoosesi aoreemct otneisoc pnrlnnet",
//                            "5356<EOT>",
//                            "<SOH>ei adiocseh paeheoeh foefnduu fnlaiuuf mmtnshei",
//                            "F71A<EOT>",
//                            "<SOH>NNNN",
//                            "96AA<EOT>"};
//
                    if (m.checkBlock()) {
//                        if (m.checkBlock() || testI < testInput.length) {
//
//                        if (testI < testInput.length) {
//                            Blockline = testInput[testI].toString();
//                            testI++;
//                        } else
                        Blockline = m.getMessage();

                        Main.logFile.writeToLog("fleet Decoder is in state: " + Main.fleetDecoder.getState().toString());

                        //System.out.println("new blockline: " + Blockline);

                        RXBlock rxb = new RXBlock(Blockline);

                        if (!rxb.valid) {
                            validblock = false;
                        } else {
                            validblock = true;
                        }

                        if (!Bulletinmode & !IACmode) {
                            ArrayList<PosData> tempPos = Main.bulletinParser.lookupPosData();
                            //System.out.println("newly parsed: " + tempPos);
                            mainui.addPosReport(tempPos);
                            //System.out.println(Main.bulletinParser.lookupPosData());

                            pollindicator = false;
                            if (!Connected & Blockline.contains("00u") & Blockline.contains("<>") & Blockline.contains(q.callsign)) {
                                Pattern psc = Pattern.compile(".*00u(\\S+)<>(\\S+)\\s([0123456789ABCDEF]{4})");

                                Matcher msc = psc.matcher(Blockline);
                                String scall = "";
                                String callsign = "";
                                String pCheck = "";
                                if (msc.lookingAt()) {
                                    scall = msc.group(1);
                                    callsign = msc.group(2);
                                    pCheck = msc.group(3);
                                }

                                char soh = 1;
                                String sohstr = Character.toString(soh);
                                String checkstring = sohstr + "00u" + scall + "<>" + q.callsign;
                                String check = q.checksum(checkstring);

                                //System.out.println(check);

                                // got link answer from a server , flash it green
                                // reset false tries
                                linkedServer = scall;
                                mainui.setLinkIndicator(true);
                                Main.LinkTries = 0;
                                //System.out.println("Acknowledging link answer.");
                                Main.theScheduler.acknowledgeLastCommand();
                                //System.out.println("Link answer acknowledged.");
                                //Main.sending_link = 0;
//                                Main.sendLocked = false;
                                q.send_mode_command(Main.defaultmode);
                                //Main.linkmode = Main.defaultmode;
                                //Main.link_ok = 0;
                                mainui.setLinkTime(mainui.getClock());
                                try {
                                    //q.send_txrsid_command("OFF");
                                    Thread.sleep(500);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (!Connected & Blockline.contains("QSL") & Blockline.contains(q.callsign)) {
                                //                               System.out.println(Blockline);
                                // Disengage emergency message upon receiving a QSL with your callsign
                                Main.emergencyToggle = false;
                                Main.emergencyTimer = 0;
                                Main.emergencyMessage = "";

                                Main.theScheduler.acknowledgeLastCommand();
                                //Main.sending_emergency = 0;
//                                Main.sendLocked = false;
                                //Main.emergency_ok = 0;
                                //Main.emergencymode = Main.defaultmode;
                                // ---------------------------

                                String pCheck = "";
                                Pattern psc = Pattern.compile(".*QSL\\s(\\S+)\\sde\\s(\\S+)\\s([0123456789ABCDEF]{4})");
                                Matcher msc = psc.matcher(Blockline);
                                String scall = "";
                                String callsign = "";
                                if (msc.lookingAt()) {
                                    callsign = msc.group(1);
                                    scall = msc.group(2);
                                    pCheck = msc.group(3);
                                    //q.send_txrsid_command("OFF");
                                    Thread.sleep(500);
                                    q.send_mode_command(defaultmode);
                                }

                                char soh = 1;
                                String sohstr = Character.toString(soh);
                                String checkstring = sohstr + "QSL " + q.callsign + " de " + scall;
                                String check = q.checksum(checkstring);
                                if (check.equals(pCheck)) {

                                    // set indicator red if pos beacon got answered by different server
                                    // set indicator green if pos beacon got answered by already linked server
                                    if (scall.equals(linkedServer)) {
                                        // mainui.setLinkIndicator(true);
                                    } else {
                                        mainui.setLinkIndicator(false);
                                        mainui.setLinkTime("");
                                    }
                                    /*if (scall.equals(linkedServer)) {
                                    mainui.setLinkTime(mainui.getClock());
                                    } else {
                                    mainui.setLinkTime("");
                                    }*/

                                    int i;
                                    boolean knownserver = false;
                                    Iterator<String> iter = Servers.iterator();
                                    while (iter.hasNext()) {
                                        String temp = iter.next();
                                        if (scall.equals(temp)) {
                                            knownserver = true;
                                            break;
                                        }
                                    }
                                    if (!knownserver) {
                                        Servers.add(scall);
                                        mainui.addServer(scall);
                                        mainui.setServer(scall);
                                    } else {
                                        mainui.setServer(scall);
                                    }

                                    Main.theScheduler.acknowledgeLastCommand();

                                    //q.send_txrsid_command("OFF");
                                    Thread.sleep(500);
                                }
                            }

                            String s = Blockline + "\n";
                            // unproto packet
                            if (rxb.type.equals("u")) {
                                if (rxb.port.equals("26")) {
                                    if (rxb.call.equals(configuration.getPreference("CALL"))) {
                                        //q.send_txrsid_command("OFF");
                                        Thread.sleep(500);
                                        MSGwindow += rxb.from + ": " + rxb.msgtext + "\n";
                                    }
                                    // make newsgroups available too
                                    if (configuration.getPreference("NEWSGROUPENABLED").equals("true")
                                            && rxb.call.equals("PSKAPRS")) {
                                        //q.send_txrsid_command("OFF");
                                        Thread.sleep(500);
                                        MSGwindow += rxb.from + ": " + rxb.msgtext + "\n";
                                    }
                                    // -----------------------------
                                }
                                // connect_ack
                            } else if (rxb.type.equals("k") & rxb.valid) {  // connect ack

                                Pattern pk = Pattern.compile("^(\\S+):\\d+\\s(\\S+):\\d+\\s(\\d)$");
                                Matcher mk = pk.matcher(rxb.payload);
                                if (mk.lookingAt()) {
                                    rxb.server = mk.group(1);
                                    rxb.call = mk.group(2);
                                    rxb.serverBlocklength = mk.group(3);
                                }
                                // are we  connected?
                                if (rxb.call.equals(rxb.mycall) & rxb.server.equals(configuration.getPreference("SERVER"))) {
                                    //q.send_txrsid_command("OFF");
                                    Thread.sleep(500);
                                    Status = "Connected";
                                    //Status = "Verbunden";
                                    Connected = true;
                                    Connecting = false;

//                                    Main.sending_connect = 0;
//                                    Main.connectmode = Main.defaultmode;
//                                    Main.connect_ok = 0;

                                    // reset tx queue
                                    TX_Text = "";

                                    sm.initSession();
                                    session = rxb.session;
                                    sm.session_id = rxb.session;
                                    sm.myserver = rxb.server;
                                }
                                // poll packet
                            } else if (Connected & (rxb.type.equals("p"))
                                    & rxb.valid & rxb.session.equals(session)) {
                                sm.RXStatus(rxb.payload);   // parse incoming status packet

                                myrxstatus = sm.getTXStatus();
                                q.send_status(myrxstatus);  // send our status

                                pollindicator = true;
                                if (Session.tx_missing.length() > 0) {
                                    txblockerror = true;
                                } else {
                                    txblockerror = false;
                                }
                                if (Session.rx_missing.length() > 0) {
                                    rxblockerror = true;
                                } else {
                                    rxblockerror = false;
                                }

                                // status packet
                            } else if (Connected & (rxb.type.equals("s"))
                                    & rxb.valid & rxb.session.equals(session)) {

                                sm.RXStatus(rxb.payload);   // parse incoming status packet

                                // get the tx status
                                myrxstatus = sm.getTXStatus();

                                if (Session.tx_missing.length() > 0 | Main.TX_Text.length() > 0) {
                                    String outstr = sm.doTXbuffer();
                                    //System.out.println("outstr: " + outstr);
                                    q.send_data(outstr);
                                } else {
                                    myrxstatus = sm.getTXStatus();
                                    q.send_status(myrxstatus);  // send our status
                                }
                                Main.validblock = true;
                                pollindicator = false;
                                if (Session.tx_missing.length() > 0) {
                                    txblockerror = true;
                                } else {
                                    txblockerror = false;
                                }
                                if (Session.rx_missing.length() > 0) {
                                    rxblockerror = true;
                                } else {
                                    rxblockerror = false;
                                }

                                // disconnect request
                            } else if (Connected & rxb.session.equals(session) & rxb.type.equals("d")) {
                                Status = "Listening";
                                Connected = false;

//                                q.send_mode_command(Main.defaultmode);
//                                try {
//                                    q.send_txrsid_command("OFF");
//                                    Thread.sleep(500);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }

                                session = "";
                                pollindicator = false;
                                // ident block
                            } else if (Connected & rxb.session.equals(session) & rxb.type.equals("i")) {
                                // discard
                                // data block
                            } else if (Connected & rxb.valid & rxb.session.equals(session)) {
                                myrxstatus = sm.doRXBuffer(rxb.payload, rxb.type);
                            } else if (Connected & rxb.session.equals(session)) {
                                myrxstatus = sm.doRXBuffer("", rxb.type);
                            }
                        } else if (Main.Bulletinmode) {
                            pollindicator = false;
                            // Bulletin mode

                            //System.out.println("Bulletin Mode engaged.");

                            Blockline = Blockline.substring(5);
                            if (Blockline.length() > 9) {
                                Blockline = Blockline.substring(0, Blockline.length() - 9);
                            }
                            Pattern pb = Pattern.compile("NNNN");
                            Matcher mb = pb.matcher(Blockline);
                            if (mb.find()) {
                                //System.out.println("Bulletin mode found NNNN");

                                Blockline = "\n----------\n";

                                bulletin.write(Blockline);
                                Main.Bulletinmode = false;
                                Main.BulletinResetTimerToggle = false;
                                Main.BulletinResetTimer = 0;
                                Main.Status = "Listening";
                            }

                            //System.out.println("adding to main window.");

                            mainwindow += Blockline;

                            //System.out.println("finally" + Blockline);
                            bulletinParser.inject(Blockline); // build up bulletin string

                            //System.out.println("blockline for inject.");
                            seaWeatherParser.inject(Blockline);
                            seaWeatherParser.getInternalDatabase().writeFile(new File(Main.wxPath));

                            // inject metareaData
                            if (MetareaRecorder.checkMetarea(Blockline) && !metareaTimerToggle) {
                                Main.metareaRecorder.reset();
                                Main.metareaRecorder.inject(Blockline);
                                Main.metareaTimer = 0;
                                Main.metareaTimerToggle = true;
                            } else {

                                if (Main.metareaTimerToggle) {
                                    Main.metareaRecorder.inject(Blockline);
                                    Main.metareaTimer = 0;
                                }
                            }

                            if (Main.BulletinResetTimerToggle) {
                                Main.BulletinResetTimer = 0;
                            }
                            // inject GRIB data
                            // check for start GRIB
                            if (GRIBRecorder.checkStartGRIB(Blockline)) {
                                Main.gribRecorder.reset();
                                Main.gribTimer = 0;
                                Main.gribTimerToggle = true;
                            } else {
                                // check for end GRIB
                                if (GRIBRecorder.checkEndGRIB(Blockline)) {
                                    //Main.gribRecorder.writeFile(new File(Main.weatherPath + "/" +
                                    //        Main.gribRecorder.getFileName() + "2"));
                                    Main.gribRecorder.saveToFile(Main.weatherPath + "/"
                                            + Main.gribRecorder.getFileName());
                                    Main.gribRecorder.reset();
                                    Main.gribTimer = 0;
                                    Main.gribTimerToggle = false;
                                }
                                // check for timer toggle
                                if (gribTimerToggle) {
                                    Main.gribRecorder.inject(Blockline);
                                    Main.gribTimer = 0;
                                }
                            }

                            // write to bulletins file...
                            bulletin.write(Blockline);
                            bulletin.flush();

                        } else if (Main.IACmode) {
                            sm.parseInput(Blockline);
                        }


                        if (debug) {
                            System.out.println(rxb.test);
                            System.out.println(rxb.protocol);
                            System.out.println(rxb.session);
                            System.out.println(rxb.type);
                            System.out.println(rxb.crc);
                            System.out.println(rxb.port);

                            if (rxb.valid == true) {
                                System.out.println("valid");
                            }
                        }
                    }
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    log.writelog("Program flow interrupted!", ex, true);
                }
            } // end while
        } catch (IOException ex) {
            log.writelog("IO Exception encountered!", ex, true);
        } finally {
            try {
                if (!(bulletin == null)) {
                    bulletin.close();
                }
            } catch (IOException ex) {
                log.writelog("IO Exception when closing bulletins!", ex, true);
            }

        }
    } // end Main

    /**
     * Add a server to the array of known servers, for instance as written by the user
     * @param MyServer
     */
    public static void AddServerToArray(String myServer) {
        try {
            //int i;
            boolean knownserver = false;

            Iterator<String> iter = Servers.iterator();

            while (iter.hasNext()) {
                String temp = iter.next();

                if (myServer.equals(temp)) {
                    knownserver = true;
                    break;
                }
            }
//            for (i = 0; i < 10; i++) {
//                if (myServer.equals(Servers[i])) {
//                    knownserver = true;
//                    break;
//                }
//            }

            if (!knownserver) {
                Servers.add(myServer);
                mainui.addServer(myServer);
//                for (i = 0; i < 10; i++) {
//                    if (Servers[i].equals("")) {
//                        Servers[i] = myServer;
//                        mainui.addServer(myServer);
//                        break;
//                    }
//       7         }
            }
        } catch (Exception e) {
            log.writelog("Had problem adding server to array, full?", e, true);
        }
    }

    /**
     * Create or check the necessary folder structure (.pskmail)
     */
    private static void handlefolderstructure() {
        try {
            HomePath = System.getProperty("user.home");
            if (File.separator.equals("/")) {
                Dirprefix = "/.pskmail/";
            } else {
                Dirprefix = "\\pskmail\\";
            }


            //Check if pskmail directory exists, create if not
            File dir = new File(HomePath + Dirprefix);
            if (!dir.isDirectory()) {
                dir.mkdir();
            }
            //Check if Outbox directory exists, create if not
            if (File.separator.equals("/")) {
                Separator = "/";
            } else {
                Separator = "\\";
            }
            File outbox = new File(HomePath + Dirprefix + "Outbox" + Separator);
            if (!outbox.isDirectory()) {
                outbox.mkdir();
            }

            //Check if Downloads directory exists, create if not
            if (File.separator.equals("/")) {
                Separator = "/";
            } else {
                Separator = "\\";
            }
            File downloads = new File(HomePath + Dirprefix + "Downloads" + Separator);
            if (!downloads.isDirectory()) {
                downloads.mkdir();
            }

            // Check if bulletin file  exists, create if not
            File fFile = new File(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator + "bulletins");
            if (!fFile.exists()) {
                fFile.createNewFile();
            }


            bulletin = new FileWriter(fFile, true);

            // check if headers file exists, and read in contents
            File fh = new File(HomePath + Dirprefix + "headers");
            if (!fh.exists()) {
                fh.createNewFile();
            }

            hdr = new FileReader(fh);
            BufferedReader br = new BufferedReader(hdr);
            String s;
            while ((s = br.readLine()) != null) {
                String fl = s + "\n";
                Mailheaderswindow += fl;
            }
            br.close();
        } catch (Exception ex) {
            log.writelog("Problem when handling pskmail folder structure.", ex, true);
        }
    }

    private static void handleinitialization() {
        try {
            // copy the config file if present

            File f1 = new File(Main.HomePath + Main.Dirprefix + "configuration.xml");
            File f2 = new File(Main.tempConfPath);

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
                q.Message("Config File copied.", 10);
            }

            // try to initialize MAXDCD from Prefs
            DCDstr = configuration.getPreference("DCD");
            MAXDCD = Integer.parseInt(DCDstr);
            // try to initialize Icon from Prefs
            Icon = configuration.getPreference("ICON");
            // Initialize APRSMessageNumber
            APRSMessageNumber = 0;
            // Initialize send queue
            TX_Text = "";

            String profile = configuration.getPreference("BLOCKLENGTH");
            int profilenr = Integer.parseInt(profile);
            modeprofile = new modemmodeenum[10];

            switch (profilenr) {
                case 9:
                    modeprofile[0] = modemmodeenum.PSK500;
                    modeprofile[1] = modemmodeenum.PSK500R;
                    modeprofile[2] = modemmodeenum.PSK125R;
                    break;
                case 8:
                    modeprofile[0] = modemmodeenum.PSK500;
                    modeprofile[1] = modemmodeenum.PSK250R;
                    modeprofile[2] = modemmodeenum.PSK125R;
                    break;
                case 7:
                    modeprofile[0] = modemmodeenum.PSK500;
                    modeprofile[1] = modemmodeenum.MFSK32;
                    modeprofile[2] = modemmodeenum.PSK125R;
                    break;
                case 6:
                    modeprofile[0] = modemmodeenum.PSK500;
                    modeprofile[1] = modemmodeenum.THOR22;
                    modeprofile[2] = modemmodeenum.PSK125R;
                    break;
                case 5:
                    modeprofile[0] = modemmodeenum.PSK250;
                    modeprofile[1] = modemmodeenum.PSK250R;
                    modeprofile[2] = modemmodeenum.PSK125R;
                    break;
                case 4:
                    modeprofile[0] = modemmodeenum.PSK500R;
                    modeprofile[1] = modemmodeenum.MFSK32;
                    modeprofile[2] = modemmodeenum.PSK125R;
                    break;
                case 3:
                    modeprofile[0] = modemmodeenum.PSK500R;
                    modeprofile[1] = modemmodeenum.THOR22;
                    modeprofile[2] = modemmodeenum.PSK125R;
                    break;
                case 2:
                    modeprofile[0] = modemmodeenum.PSK250R;
                    modeprofile[1] = modemmodeenum.PSK125R;
                    modeprofile[2] = modemmodeenum.PSK125R;
                    break;
                case 1:
                    modeprofile[0] = modemmodeenum.MFSK32;
                    modeprofile[1] = modemmodeenum.MFSK16;
                    modeprofile[2] = modemmodeenum.PSK125R;
                    break;
                case 0:
                    modeprofile[0] = modemmodeenum.PSK250;
                    modeprofile[1] = modemmodeenum.PSK250;
                    modeprofile[2] = modemmodeenum.PSK250;
            }

            Main.defaultmode = modeprofile[0];
//            Main.linkmode = modeprofile[0];
//            Main.beaconmode = modeprofile[0];
//            Main.APRSmode = modeprofile[0];
        } catch (Exception e) {
            MAXDCD = 3;
            Icon = "y";
            log.writelog("Problems with config parameter.", e, true);
        }

        // Send Link request -----------------
        // q.set_txstatus(txstatus.TXlinkreq);
        // q.send_link();
        // -----------------------------------

        // Servers[0] = configuration.getPreference("SERVER");
        Servers.clear();
        Servers.add(configuration.getPreference("SERVER"));
    }

    /**
     * Open a GPS connection, if that should be used
     */
    private static void handlegps() {
        // GPS
        gpsport = new serialport();       // Serial port object
        gpsdata = new nmeaparser();     // Parser for nmea data
        String portforgps = configuration.getPreference("GPSPORT");
        if (configuration.getPreference("GPSENABLED").equals("yes")) {
            try {
                String speedforgps = configuration.getPreference("GPSSPEED");
                int speedygps = Integer.parseInt(speedforgps);

                gpsport.connect(portforgps, speedygps);
                // Check if the port is open
                if (!gpsport.curstate) {
                    // Disconnect and set it off
                    gpsport.disconnect();
                    configuration.setPreference("GPSENABLED", "no");
                }
                /*if (portforgps.contains("USB"))
                // Here is the code for getting a gps out of sirf mode
                gpsdata.writehexsirfmsg("8102010100010101050101010001000100010001000112c0"); //Set 4800 bps nmea*/
            } catch (Exception ex) {
                log.writelog("Error when trying to connect to the GPS.", ex, true);
            }
        }
    }
} // end Main class



