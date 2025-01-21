/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javapskmail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdesktop.swingx.ws.yahoo.search.Utils;


/**
 *
 * @author rein
 */
public class Session {

    public String mycall;
    public String myserver;
    private static String blocklength;
    public String session_id;
    private String Lineoutstring;
    // service flags
    private static int Blocklength;
    public boolean Headers = false;
    public boolean FileDownload = false;
    public boolean WWWDownload = false;
    public String ThisFile = "";
    public String ThisFileLength = "";
    public boolean MsgDownload = false;
    public boolean base64attachment = false;
    public String attachmentFilename = null;
    //rx is my machine, tx is the other machine
    public static String tx_lastsent;    //Last block sent to me
    public static String tx_missing; //List of repeats  I need to resend.
    public static String tx_ok;  //last block received conseq ok at other end.
    public static String tx_lastreceived;  // at other end
    private static String rx_lastsent;    // Last block I sent
    private static String rx_ok;   //  Text o.k  until this one
    private static String rx_lastreceived;   // Last block I received
    public static String rx_missing; //List of repeat requests I need to send to other side
    private static boolean rx_lastBlock; // Flag for end of frame
    private static String[] rxbuffer = new String[64];
    private static int beginblock;
    private static int goodblock;
    private static int thisblock;
    private static int lastblock;
    public static int lastdisplayed = 0;
    public static boolean gooddata;
    public static String[] txbuffer = new String[64];
    private static int lastqueued;
    // progress bar values
    private static int DataReceived = 0;
    private static int DataSize = 0;
    private FileWriter headers = null;
    private FileWriter dlFile = null;
    private FileWriter tmpmessage = null;
    private FileWriter inbox = null;
    private arq a;
    private BufferedWriter iacout;
    private fastfec f;

    //  private  String lastqueued;  //Last block in my send queue
    public Session() {
        String path = Main.HomePath + Main.Dirprefix;
        config cf = new config(path);
        a = new arq();
        f = new fastfec();
        myserver = cf.getServer();
        blocklength = "3";
        try {
            blocklength = cf.getBlocklength();
        } catch (Exception e) {
            blocklength = "3";
        }
        Blocklength = 5;
        Main.TX_Text = "";

        initSession();
    }

    public void sendUpdate() {

        String pophost = Main.configuration.getPreference("POPHOST");
        String popuser = Main.configuration.getPreference("POPUSER");
        String poppass = Main.configuration.getPreference("POPPASS");
        String returnaddr = Main.configuration.getPreference("RETURNADDRESS");
        String recinfo = pophost + "," + popuser + "," + poppass + "," + returnaddr + ",none";

        String record = "~RECx" + base_64.base64Encode(recinfo);
        int eol_loc = -1;
        String frst = null;
        String secnd = null;
        eol_loc = record.indexOf(10);
        if (eol_loc != -1) {
            frst = record.substring(0, eol_loc - 1);
            secnd = record.substring(eol_loc + 1, record.length());
            record = frst + secnd;
        }

        //System.out.println("Update record put on TX_Text: " + record);

        Main.TX_Text += record + "\n";
    }

    public void RXStatus(String text) {
        if (text.length() > 2) {
            tx_lastsent = text.substring(0, 1);
            tx_lastreceived = text.substring(1, 2);
            tx_ok = text.substring(2, 3);
            tx_missing = text.substring(3);
        }
    }

    public String getTXStatus() {
        rx_missing = "";
        int endblock = 0;
        lastblock = (int) (tx_lastsent.charAt(0) - 32);

        if (lastblock < lastdisplayed + 1 & lastdisplayed - lastblock > 49) {
            endblock = lastblock + 64;
        } else {
            endblock = lastblock;
        }
        int i = 0;
        int index = 0;

        for (i = lastdisplayed; i <= endblock; i++) {
            index = i % 64;

            if (rxbuffer[index].equals("")) {
                char m = (char) (index + 32);
                rx_missing += Character.toString(m);
            } else {
                if (rx_missing.length() == 0) {
                    goodblock = index;
                }
                lastblock = index;
            }
        }
        // generate the status block
        char last = (char) (lastblock + 32);
        rx_lastreceived = Character.toString(last);
        char ok = (char) (goodblock + 32);
        rx_ok = Character.toString(ok);

        String outstr = rx_lastsent + rx_ok + rx_lastreceived + rx_missing;
        return outstr;
    }

    public String getRXmissing() {
        rx_missing = "";
        int i = 0;
        int end = thisblock;
        if (thisblock < lastdisplayed + 1 & lastdisplayed - thisblock > 49) {
            end = thisblock + 64;
        }

        for (i = lastdisplayed + 1; i < end; i++) {
            char m = (char) ((i % 64) + 32);
            rx_missing += Character.toString(m);
        }

        return rx_missing;
    }

    public void initSession() {
        tx_lastsent = " ";
        tx_lastreceived = " ";
        tx_ok = " ";
        tx_missing = "";
        rx_lastsent = " ";
        rx_ok = " ";
        rx_lastreceived = " ";
        rx_missing = "";
        rx_lastBlock = false;
        beginblock = 0;
        goodblock = 0;
        thisblock = 0;
        lastblock = 0;
        lastqueued = 0;
        lastdisplayed = 0;
        gooddata = true;
        for (int i = 0; i < 64; i++) {
            rxbuffer[i] = "";
            txbuffer[i] = "";
        }
        Lineoutstring = " ";
//                tkn = new StringTokenizer(Lineoutstring, "\n", false);

    }

    // handles the rx buffer and calculates the new TXStatus after every Block
    public String doRXBuffer(String block, String index) throws FileNotFoundException, IOException {

        if (block.length() > 0) { //valid block
            thisblock = (int) (index.charAt(0) - 32) % 64;
            rxbuffer[thisblock] = block;

            if (lastdisplayed > 63) {
                lastdisplayed = 0;
            }

            while (!rxbuffer[(lastdisplayed + 1) % 64].equals("")) {
                // display this block
                lastdisplayed++;
                if (lastdisplayed > 63) {
                    lastdisplayed = 0;
                }

                // parse commands
                Lineoutstring += rxbuffer[lastdisplayed];
                int Linebreak = -1;
                while (Lineoutstring.indexOf("\n") >= 0) {
                    Linebreak = Lineoutstring.indexOf("\n");
                    if (Linebreak >= 0) {
                        String fullLine = Lineoutstring.substring(0, Linebreak);
                        Lineoutstring = Lineoutstring.substring(Linebreak + 1);
                        parseInput(fullLine);
                    }
                }

                // output to main window
                Main.mainwindow += rxbuffer[lastdisplayed];
                // output to mail headers window
                if (Headers) {
//                        Main.Mailheaderswindow += rxbuffer[lastdisplayed];
                    }
            }
            // make room for more data
            int i = 0;
            for (i = thisblock + 16; i < thisblock + 24; i++) {
                rxbuffer[i % 64] = "";
            }
            lastblock = thisblock;
        }
//          return getTXStatus();
        return "";
    }

//    private class GRBFilter implements FileFilter {
//
//        public boolean accept(File pathName) {
//            if (pathName.isDirectory()) {
//                return true;
//            }
//
//            String extension = Utils.getExtension(pathName);
//            if (extension != null) {
//                if (extension.equals(Utils.tiff));
//            }
//
//            return false;
//        }
//    }

    public void parseInput(String str) throws FileNotFoundException, IOException {
        boolean Firstline = false;

        // mail headers
        Pattern pm = Pattern.compile("^\\s*(Your\\smail:)");
        Matcher mm = pm.matcher(str);
        if (mm.lookingAt()) {
            if (mm.group(1).equals("Your mail:")) {
                Headers = true;
                Firstline = true;
                try {
                    this.headers = new FileWriter(Main.HomePath + Main.Dirprefix + "headers", true);
                } catch (Exception e) {
                    Main.log.writelog("Error when trying to open the headers file.", e, true);
                }
            }
        }
        // IAC fleetcodes
        if (!Main.Connected) {
            Pattern pc = Pattern.compile(".*<SOH>(ZFZF)");
            Matcher mc = pc.matcher(str);
            if (mc.lookingAt()) {
                if (mc.group(1).equals("ZFZF")) {
                    Main.logFile.writeToLog("IACMode set to true, within parseInput.");
                    Main.IACmode = true;
                    Firstline = true;
                    try {
                        iacout = new BufferedWriter(new FileWriter(Main.HomePath + Main.Dirprefix + "iactemp", true));
                        a.Message("Receiving IAC fleetcode file", 15);
                    } catch (Exception e) {
                        Main.log.writelog("Error when trying to open the iac file.", e, true);
                        a.Message("Error opening file...", 15);
                    }
                }
            }
        }
        // file download
        Pattern pf = Pattern.compile("^\\s*(Your\\sfile:)(.*)\\s(\\d+)");
        Matcher mf = pf.matcher(str);
        if (mf.lookingAt()) {
            if (mf.group(1).equals("Your file:")) {
                FileDownload = true;
                Firstline = true;
                ThisFile = mf.group(2);
                // set progress indicator here...
                ThisFileLength = mf.group(3);
                DataSize = Integer.parseInt(ThisFileLength);
                DataReceived = 0;
                Main.DataSize = Integer.toString(DataSize);
                try {
                    this.dlFile = new FileWriter("TempFile", true);
                } catch (Exception e) {
                    Main.log.writelog("Error when trying to open the download file.", e, true);
                }
            }
        }
        // message receive
        Pattern pmsg = Pattern.compile("^\\s*(Your\\smsg:)\\s(\\d+)");
        Matcher mmsg = pmsg.matcher(str);
        if (mmsg.lookingAt()) {
            if (mmsg.group(1).equals("Your msg:")) {
                MsgDownload = true;
                Firstline = true;
                try {
                    this.tmpmessage = new FileWriter("tmpmessage", true);
                } catch (Exception e) {
                    Main.log.writelog("Error when trying to open the headers file.", e, true);
                }
                // set progress indicator here...
                ThisFileLength = mmsg.group(2);
                DataSize = Integer.parseInt(ThisFileLength);
                DataReceived = 0;
                Main.DataSize = Integer.toString(DataSize);
            }
        }
        // web page receive
        Pattern pw = Pattern.compile("^\\s*(Your\\swwwpage:)\\s(\\d+)");
        Matcher mw = pw.matcher(str);
        if (mw.lookingAt()) {
            if (mw.group(1).equals("Your wwwpage:")) {
                WWWDownload = true;
                Firstline = true;
                // set progress indicator here...
                ThisFileLength = mw.group(2);
                DataSize = Integer.parseInt(ThisFileLength);
                DataReceived = 0;
                Main.DataSize = Integer.toString(DataSize);
            }
        }
        // Message sent...
        Pattern ps = Pattern.compile("^\\s*(Message sent\\.\\.\\.)");
        Matcher ms = ps.matcher(str);
        if (ms.lookingAt()) {
            if (ms.group(1).equals("Message sent...")) {
//                                            System.out.println(Main.Mailoutfile);
                try {
                    File fd = new File(Main.Mailoutfile);
                    fd.delete();
                } catch (Exception e) {
                    Main.log.writelog("Error deleting mail file.", e, true);
                }
            }
        }
        // -end- command
        Pattern pe = Pattern.compile("^\\s*(\\S+)");
        Matcher me = pe.matcher(str);
        if (me.lookingAt()) {
            if (me.group(1).equals("-end-")) {
                if (Headers) {
                    Headers = false;
                    try {
                        this.headers.close();
                    } catch (IOException ex) {
                        Main.log.writelog("Error when trying to close the headers file.", ex, true);
                    }
                }
                if (FileDownload) {
                    FileDownload = false;
                    try {
                        this.dlFile.close();
                    } catch (IOException ex) {
                        Main.log.writelog("Error when trying to close the downoad file.", ex, true);
                    }
                    try {
                        Base64.decodeFileToFile("TempFile", Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator + ThisFile);
                        Unzip.Unzip(Main.HomePath + Main.Dirprefix + "Downloads" + Main.Separator + ThisFile);
                        File tmp = new File("TempFile");
                        tmp.delete();
                    } catch (Exception exc) {
                        Main.log.writelog("Error when trying to decode the downoad file.", exc, true);
                    }
                }
                // messages  download      - append tmpmessage to Inbox in mbox format
                if (MsgDownload) {
                    MsgDownload = false;
                    this.tmpmessage.close();
                    // append to Inbox file
                    FileReader fr = new FileReader("tmpmessage");
                    BufferedReader br = new BufferedReader(fr);
                    // all local stuff
                    String s;
                    String From = null;
                    String Date = null;
                    String Sub = null;
                    String outstr = "";
                    String attachment = "";
                    base64attachment = false;
                    // read tmpmessage line by line
                    while ((s = br.readLine()) != null) {
                        // compile some patterns and set up the matchers
                        Pattern pfrm = Pattern.compile("^\\s*(From:)\\s(.*)");
                        Matcher mfrm = pfrm.matcher(s);
                        Pattern pdate = Pattern.compile("^\\s*(Date:)\\s(\\w{3})\\,*\\s+(\\d+)\\s(\\w{3})\\s(\\d{4})\\s(\\d\\d:\\d\\d:\\d\\d)");
                        Matcher mdate = pdate.matcher(s);
                        Pattern pdate2 = Pattern.compile("^\\s*(Date:)\\s(\\d+)\\s+(\\w{3})\\s+(\\d{4}\\s\\d\\d:\\d\\d:\\d\\d)");
                        Matcher mdate2 = pdate2.matcher(s);
                        Pattern psub = Pattern.compile("^\\s*(Subject:)\\s(.*)");
                        Matcher msub = psub.matcher(s);
                        Pattern p64 = Pattern.compile("^\\s*(content-transfer-encoding: base64)");
                        Matcher m64 = p64.matcher(s.toLowerCase());
                        Pattern pnm = Pattern.compile(".*(filename=)(.*)");
                        Matcher mnm = pnm.matcher(s);
                        if (mfrm.lookingAt()) {
                            if (mfrm.group(1).equals("From:")) {
                                From = mfrm.group(2);
                            }
                        } else if (mdate.lookingAt()) {
                            if (mdate.group(1).equals("Date:")) {
                                Date = mdate.group(2) + " " + mdate.group(3) + " " +
                                        mdate.group(4) + " " + mdate.group(5) + " " +
                                        mdate.group(6);
                            }

                        } else if (mdate2.lookingAt()) {
                            if (mdate2.group(1).equals("Date:")) {
                                Date = mdate2.group(2) + " " + mdate2.group(3) + " " +
                                        mdate2.group(4);
                            }

                        } else if (msub.lookingAt()) {
                            if (msub.group(1).equals("Subject:")) {
                                Sub = msub.group(2);
                            }

                        } else if (m64.lookingAt()) {
                            if (m64.group(1).equals("content-transfer-encoding: base64")) {
                                // there is an attachment...
                                base64attachment = true;
//                                                                  debug ("Attachment");
                            }
                        } else if (mnm.lookingAt()) {
                            if (mnm.group(1).equals("filename=")) {
                                // get the file name
                                attachmentFilename = mnm.group(2);
                                attachmentFilename = attachmentFilename.substring(1, attachmentFilename.length() - 1);
                            }
                        } else {
                            if (base64attachment) {
                                if (!s.equals("")) {
                                    if (!s.startsWith("--")) {
                                        attachment += s + "\n";
                                    } else {
                                        base64attachment = false;

                                        System.out.println("attachment: \n" + attachment);

                                        if (attachment.length() > 10 & attachmentFilename != null) {
                                            // is it a grib file?
                                            if (attachmentFilename.endsWith(".grb")) {
                                                // are we on linux?
                                                // if (Main.Separator.equals("/")) {
                                                    try {
                                                        // File f1 = new File("/opt/zyGrib/grib/");
                                                        File f1 = new File(Main.weatherPath);
                                                        // is zygrib installed?
                                                        if (f1.isDirectory()) {
                                                            attachmentFilename = Main.weatherPath + attachmentFilename;
                                                        } //else {
                                                            // no, put it in the temp directory
                                                            //attachmentFilename = Main.HomePath + Main.Dirprefix + attachmentFilename;
                                                        //}
                                                    } catch (Exception e) {
                                                        a.Message("IO problem", 10);
                                                    }
                                                // }
                                            } else {
                                                // no grib file
                                                attachmentFilename = Main.HomePath + Main.Dirprefix + attachmentFilename;
                                            }
                                            try {
//                                                                                debug (attachmentFilename);
                                                Base64.decodeToFile(attachment, attachmentFilename);
                                                a.Message("File stored", 10);
                                            } catch (Exception e) {
                                                a.Message("Problem with decoding", 10);
                                            }

                                        }
                                    }
                                }
                            } else {                    // no attachment, body text for Inbox
                                outstr += s + "\n";
                                //                                                                debug ("Out=" + s);
                            }
                        }

                    } // end while

                    fr.close();
                    this.inbox = new FileWriter(Main.HomePath + Main.Dirprefix + "Inbox", true);
                    inbox.write("From " + From + " " + Date + "\n");
                    inbox.write("From: " + From + "\n");
                    if (Date != null) {
                        inbox.write("Date: " + Date + "\n");
                    }
                    inbox.write("Subject: " + Sub + "\n");
                    // write message body
                    if (outstr != null) {
                        inbox.write(outstr + "\n");
                    }
                    inbox.flush();
                    inbox.close();

                    File fl = new File("tmpmessage");
                    if (fl.exists()) {
                        fl.delete();
                    }

                    a.Message("Added to mbox queue", 10);
                }
                // web pages   download
                if (WWWDownload) {
                    WWWDownload = false;
                }
                a.Message("done...", 10);
                Main.Progress = 0;
            }

        }
        // NNNN
// fleetcodes
        if (!Main.Connected & Main.IACmode) {
            Pattern pn = Pattern.compile("<SOH>(NNNN)");
            Matcher mn = pn.matcher(str);
            if (mn.lookingAt()) {
                if (mn.group(1).equals("NNNN")) {
                    Main.logFile.writeToLog("NNNN detected during IACMode (parseInput).");
                    Main.IACmode = false;
                    Main.logFile.writeToLog("Switching off IAC Mode.");
                    // inject the quadruple NNNN
                    Main.fleetDecoder.inject(str);
                    Main.logFile.writeToLog("injected NNNN into the fleet decoder.");
                    // write the output generated to file fleet
                    //System.out.println("Trying to write." +
                    //        "..\\zyGrib\\" + Main.fleetDecoder.getRegion().replaceAll(" ", "_") + ".flt");

                    Main.logFile.writeToLog("writing fleetcode file now.");
                    File fleetFile = new File(Main.weatherPath +
                            Main.fleetDecoder.getRegion().replaceAll(" ", "_") + ".flt");


                    //File fleetFile = new File("..\\zyGrib\\fleet");


                    //System.out.println("File loaded.");

                    BufferedWriter fleetWriter = null;
                    if (!fleetFile.exists()) {
                        //System.out.println("Creating new file.");
                        fleetFile.createNewFile();
                        //System.out.println("New File created");
                    }
                    try {
                        //System.out.println("Opening BufferedWriter.");
                        fleetWriter = new BufferedWriter(new FileWriter(fleetFile));
                        //System.out.println("Opened BufferedWriter.");
                        fleetWriter.write(Main.fleetDecoder.getOutput());
                        Main.logFile.writeToLog("Fleet code file written.");
                    } catch (Exception except) {
                        except.printStackTrace();
                        Main.logFile.writeToLog("Error writing the fleet code file." +
                                except.getMessage());
                    } finally {
                        try {
                            if (fleetWriter != null) {
                                fleetWriter.flush();
                                fleetWriter.close();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    Main.logFile.writeToLog("Fleet code ended." +
                            " IAC mode = " + Main.IACmode);

                    // -----------------
                    a.Message("End of code...", 10);
                    Main.Status = "Listening";
                    try {
                        iacout.close();
                    } catch (Exception e) {
                        Main.log.writelog("Error closing the iac file.", e, true);
                    }
                    try {
                        f.fastfec2(Main.HomePath + Main.Dirprefix + "iactemp", "");
                    } catch (Exception e) {
                    }
                    deleteFile("iactemp");
                }
            }
        }
        // bulletin
        if (!Main.Connected & Main.Bulletinmode) {
            Pattern pn = Pattern.compile("<SOH>(NNNN)");
            Matcher mn = pn.matcher(str);
            if (mn.lookingAt()) {
                if (mn.group(1).equals("NNNN")) {
                    Main.Bulletinmode = false;
                    Main.BulletinResetTimerToggle = false;
                    Main.BulletinResetTimer = 0;
                    a.Message("End of bulletin...", 2);
                }
            }
        }


        // write headers
        if (Headers & !Firstline) {
            Pattern phd = Pattern.compile("^(\\s*\\d+.*)");
            Matcher mhd = phd.matcher(str);
            if (mhd.lookingAt()) {
                String outToWindow = mhd.group(1) + "\n";
                Main.Mailheaderswindow += outToWindow;
            }

            try {
                this.headers.write(str + "\n");
            } catch (IOException ex) {
                Main.log.writelog("Error when trying to write to headers file.", ex, true);
            }
        }
        // write file
        if (FileDownload & !Firstline) {
            Main.FilesTextArea += str + "\n";
            DataReceived += str.length();

//                                        double ProGress = 100 * DataReceived / DataSize;
            Main.Progress = (int) (100 * DataReceived / DataSize);
            try {
                this.dlFile.write(str + "\n");
            } catch (IOException ex) {
                Main.log.writelog("Error when trying to write to download file.", ex, true);
            }
        }
// messages                                    
        if (MsgDownload & !Firstline) {
            DataReceived += str.length();
            Main.Progress = (int) (100 * DataReceived / DataSize);
            this.tmpmessage.write(str + "\n");
        }
        // www pages       WWWDownload
        if (WWWDownload & !Firstline) {
            DataReceived += str.length();
//                                        double ProGress = 100 * DataReceived / DataSize;
            Main.Progress = (int) (100 * DataReceived / DataSize);
        }
        // iac fleetcode file
//                                    debug (Integer.toString(str.length()));

        if (!Main.Connected & Main.IACmode & str.length() > 0) {
            try {
                // fleet Decoder injection
                Main.logFile.writeToLog("string injected into fleet code (parseInput)");
                Main.fleetDecoder.inject(str);
                //
                iacout.write(str + "\n");
                iacout.flush();
                Main.mainwindow += str + "\n";
            } catch (IOException exc) {
                Main.log.writelog("Error when trying to write to download file.", exc, true);
            }
        }
    }

    public String doTXbuffer() {

        //System.out.println("doTXbuffer");

        String Outbuffer = "";
        int nr_missing = tx_missing.length();
        int b[] = new int[nr_missing];  // array of missing blocks
        int i;
        for (i = 0; i < nr_missing; i++) {
            b[i] = (int) tx_missing.substring(i, i + 1).charAt(0) - 32;
        }

        if (tx_missing.length() > 2) {
            if (Blocklength > 3) {
                Blocklength--;
            }
        } else if (tx_missing.length() < 1) {
            if (Blocklength < 6) {
                Blocklength++;
            }
        } else {
            Blocklength = 5;
        }

        // add missing blocks
        if (nr_missing > 0) {
            for (i = 0; i < nr_missing; i++) {
                String block = TX_addblock(b[i]);
                Outbuffer += block;
            }
        }

        i = 0;
        // int Blocklength = Integer.parseInt(blocklength);
        while (i < (8 - nr_missing) & Main.TX_Text.length() > 0) {
            String newstring = "";

            if (Blocklength < 4) {
                Blocklength = 4;
            } else if (Blocklength > 6) {
                Blocklength = 6;
            }

            double bl = Math.pow(2, Blocklength);
            int queuelen = Main.TX_Text.length();

            //System.out.print("TX_Text: " + Main.TX_Text);

            if (queuelen > 0) {
                if (queuelen < (int) bl) {
                    newstring = Main.TX_Text;
                    Main.TX_Text = "";
                } else {
                    newstring = Main.TX_Text.substring(0, (int) bl);
                    Main.TX_Text = Main.TX_Text.substring((int) bl);
                }

                //System.out.print(", newatring: " + newstring);

                lastqueued += 1;
                if (lastqueued > 63) {
                    lastqueued = 0;
                }
                txbuffer[lastqueued] = newstring;

                for (int j = lastqueued + 17; j < lastqueued + 25; j++) {
                    txbuffer[j % 64] = "";
                }
            }

            String block = TX_addblock(lastqueued);

            //System.out.print(", block: " + block);

            char lasttxchar = (char) (lastqueued + 32);
            rx_lastsent = Character.toString(lasttxchar);
            Main.myrxstatus = getTXStatus();
            Outbuffer += block;
            i++;
        }

        //System.out.println("Outbuffer: " + Outbuffer);

        return Outbuffer;
    }

    public String TX_addblock(int nr) {
        char c = (char) (nr + 32);
        String accum = "0";
        accum += Main.session;
        accum += Character.toString(c);
        accum += Session.txbuffer[nr];
        String blcheck = a.checksum(accum);
        accum += blcheck;
        return accum + (char) 1;
    }

    void sendQTC(String mailnr) {
        Main.TX_Text += "~QTC " + mailnr + "+\n";
    }

    void sendDelete(String numbers) {
        Main.TX_Text += "~DELETE " + numbers + "\n";
    }

    void sendRead(String mailnr) {
        Main.TX_Text += "~READ " + mailnr + "\n";
    }

    public void deleteFile(String filename) {
        String fileName = Main.HomePath + Main.Dirprefix + filename;
        // A File object to represent the filename
        File fl = new File(fileName);

        // Make sure the file or directory exists and isn't write protected
        try {
            if (!fl.exists()) {
                throw new IllegalArgumentException("Delete: Does not exist: " + fileName);
            }


            if (!fl.canWrite()) {
                throw new IllegalArgumentException("Delete: write protected: " + fileName);
            }

            // If it is a directory, make sure it is empty
            if (fl.isDirectory()) {
                String[] files = fl.list();
                if (files.length > 0) {
                    throw new IllegalArgumentException(
                            "Delete: directory not empty: " + fileName);
                }
            }

            // Attempt to delete it
            boolean success = fl.delete();

            if (!success) {
                throw new IllegalArgumentException("Delete: deletion failed");
            }
        } catch (IllegalArgumentException e) {
            Main.log.writelog("Error deleting headers file:", e, true);
        }

    }
// read last header number from headers file    

    public String getHeaderCount(String filename) {
        FileReader hdr = null;
        String Countstr = "0";
        File fh = new File(Main.HomePath + Main.Dirprefix + filename);
        if (!fh.exists()) {
            return "1";
        }
        try {
            hdr = new FileReader(fh);
            BufferedReader br = new BufferedReader(hdr);
            String s;
            while ((s = br.readLine()) != null) {
                //===================================
                Pattern ph = Pattern.compile("^\\s*(\\d+)");
                Matcher mh = ph.matcher(s);
                if (mh.lookingAt()) {
                    Countstr = mh.group(1);
                    int Count = Integer.parseInt(Countstr);
                    Count++;
                    Countstr = Integer.toString(Count);
                }
//=====================================                        
            }
            br.close();
        } catch (IOException e) {
            Main.log.writelog("Error when trying to read the headers file.", e, true);
        }
        return Countstr;
    }

    public void debug(String message) {
        System.out.println("Debug:" + message);
    }
} // end of class


