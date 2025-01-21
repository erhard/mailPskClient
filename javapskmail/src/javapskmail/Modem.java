/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javapskmail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.InputStream;
//import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rein PA0R
 */

/*  public methods:
 * SendLine(String) : send a block
 * (String) GetMessage : get a block from the queue
 *      returns "none" if queue is empty 
 * (boolean) checkBlock : returns true is message in queue
 */
public class Modem implements Runnable {

    public String outLine = "";
    public PrintWriter pout = null;
    public InputStream in = null;
    static final int MAXQUEUE = 8;
    private Vector<String> messages = new Vector<String>();
    private String BlockString;
    private boolean connected = false; // Use this to suppress errors when the port is closed
    private String rxIDstart = "<cmd><rsid>";
    private String rxIDend = "</rsid></cmd>";
    private String txIDstart = "<cmd><txrsid>";
    private String txIDend = "</txrsid><cmd>";
    private long blockstart;
    private String host;
    private int port;
//   
//  public int MAXDCD = 3;

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return this.connected;
    }

    Modem(String host, int port) {

        //try {
        //make the socket objects
//            Socket sock = new Socket(host, port);
//            OutputStream out = sock.getOutputStream();
//            in = sock.getInputStream();
//            pout = new PrintWriter(out, true);

        this.host = host;
        this.port = port;
        // initialize modem
        char SOC = (char) 26; // Start of command
        char EOC = (char) 27; // End of command
        String pskmailon = SOC + "MULTIPSK-OFF" + EOC;

//         System.out.println("Modem initialized.");       
//        } catch (IOException e) {
//            e.printStackTrace();
//            Main.log.writelog("Error when connecting to modem.", e, true);
//        }
    }

    // Send routine
    public void Sendln(String outLine) {
        try {
            if (connected) {
                pout.println(outLine);
                // System.out.println(outLine);
            }
//            javax.swing.Timer callDelayTimer =
//                    new javax.swing.Timer(Main.callDelay * 1000, new ActionListener() {
//
//                public void actionPerformed(ActionEvent e) {
//                    Main.enableSend = true;
//                    System.out.println("enableSend enabled.");
//                }
//            });
//            callDelayTimer.setRepeats(false);
//            System.out.println("Call Delay timer started.");
//            callDelayTimer.start();
            
        } catch (Exception ex) {
            Main.logFile.writeToLog("Error sending to modem. " + ex);
            Main.logFile.writeToLog("Modem not connected.");
            connected = false;

            // Main.log.writelog("Could not send frame, fldigi not running or busy?", ex, true);
        }
    }

    public void Set_rxID() {
        Main.SendCommand = rxIDstart + "ON" + rxIDend;
    }

    public void Unset_rxID() {
        Main.SendCommand = rxIDstart + "OFF" + rxIDend;
    }

    public void Set_txID() {
        Main.SendCommand = txIDstart + "ON" + txIDend;
    }

    public void Unset_txID() {
        Main.SendCommand = txIDstart + "OFF" + txIDend;
    }

    private char GetByte() {
        char myChar;
        // read a byte
        try {
            if (connected) {
                byte back = (byte) in.read();
                myChar = (char) back;
                return myChar;
            }
        } catch (IOException e) {
            Main.logFile.writeToLog("Error reading from modem. " + e);
            Main.logFile.writeToLog("Modem disconnected.");
            connected = false;
        }

        return '\0';  // should not happen.
    }

    private void GetBlock() {
        int timer = 0;
        try {
            char inChar = '\0';
            boolean BlockActive = false;
            while (true) {
                // if there is no modem connection over the socket
                // try establishing one
                if (!connected) {
                    try {
                        Socket sock = new Socket(host, port);
                        OutputStream out = sock.getOutputStream();
                        in = sock.getInputStream();
                        pout = new PrintWriter(out, true);
                        connected = true;
                        BlockString = "";
                        outLine = "<cmd>server</cmd>";
                        Sendln(outLine);
                        Main.logFile.writeToLog("Modem (re)connected");
                        timer = 0;
                        Thread.sleep(100);
                    } catch (IOException ex) {
                        if (timer >= 10) {
                            Main.logFile.writeToLog("not connected.");
                            timer = 0;
                        }
                        Thread.sleep(1000);
                        timer++;
                    }
                } else {
                    inChar = GetByte();
                    Main.DCD = Main.MAXDCD;
                    if (inChar > 127) {     // todo: unicode encoding
                        inChar = 0;
                    }
                    switch (inChar) {
                        case 0:
                            break; // do nothing
                        case 1:
                            blockstart = System.currentTimeMillis();
                            WriteToMonitor("<SOH>");
                            if (BlockActive == false) {
                                BlockActive = true;
                                Main.BlockActive = true;
                                BlockString = "<SOH>";
                            } else {
                                BlockString += "<SOH>";
//                System.out.println(BlockString);
                                try {
                                    putMessage(BlockString);
                                    BlockString = "<SOH>";
                                } catch (InterruptedException e) {
                                }
                            }
                            Main.DCD = 0;
                            break;
                        case 4:
                            Main.blockval = System.currentTimeMillis() - blockstart;

                            if (Main.blockval < 10) {
                            }

                            WriteToMonitor("<EOT>\n");
                            if (BlockActive == true) {
                                BlockString += "<EOT>";
                                try {
                                    putMessage(BlockString);
                                } catch (InterruptedException e) {
                                }

                                BlockString = "";
                            }
                            if (Main.BlockActive) {
                                BlockActive = false;
                                Main.BlockActive = false;
                                Main.DCD = 2;
                            } else {
                                Main.DCD = 0;
                            }
                            break;
                        case 6:
//             System.out.println("<RDY>"); 
                            Main.DCD = 0;
                            Main.TXActive = false;
                            break;
                        case 31:
                            WriteToMonitor("<US>");
                            break;
                        case 10:
                        case 13:
                            WriteToMonitor("\n");
                            if (BlockActive == true) {
                                BlockString += inChar;
                            }
                            Main.DCD = 0;
                            break;
                        default:
                            if (inChar > 31) {
                                WriteToMonitor(inChar);
                            }
                            if (BlockActive == true) {
                                BlockString += inChar;
                            }
                            break;
                    }

                    // sleep some...
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } // end while
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    } // end GetBlock

    private void WriteToMonitor(char inchar) {
        Main.monitor += Character.toString(inchar);
    }

    public void WriteToMonitor(String instr) {
        Main.monitor += instr;
    }

    private void WriteToMain(String instr) {
        Pattern pcnv = Pattern.compile("&(\\d\\d\\d);");
        Matcher mcvt = pcnv.matcher(instr);
        if (mcvt.lookingAt()) {
            int chrst = Integer.valueOf(mcvt.group(1));
            String str = Integer.toString(chrst);
            String fnd = "&" + mcvt.group(1) + ";";
            int found = instr.indexOf(fnd);
            String strt = instr.substring(0, found);
            String chrstring = instr.substring(found, found + 5);
            String endst = instr.substring(found + 5);
            instr = strt + chrstring + endst;
        }
        Main.mainwindow += instr;
    }

    private synchronized void putMessage(String BlockString)
            throws InterruptedException {

        Main.logFile.writeToLog("Entering putting Message (class Modem).");

        while (messages.size() == MAXQUEUE) {
            wait();
        }
        messages.addElement(BlockString);
        notify();

        Main.logFile.writeToLog("Leaving putting Message (class Modem).");
    }

    public synchronized String getMessage()
            throws InterruptedException {
//        notify( );

        Main.logFile.writeToLog("Entering getting Message (class Modem).");

        if (messages.size() == 0) {
            return "none";
        }
        String message = (String) messages.firstElement();
        messages.removeElement(message);

        Main.logFile.writeToLog("Leaving getting Message (class Modem).");

        return message;
    }

    public synchronized boolean checkBlock() {
        if (messages.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void run() {
        GetBlock();
    }
}  // end Modem

