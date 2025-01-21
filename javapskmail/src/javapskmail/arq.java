/*
 * arq.java
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

import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Per Crusefalk <per@crusefalk.se>
 */
public class arq {

    // Frame handling variables
    char Unproto = 'u';
    char Connect = 'c';
    char Status = 's';
    char SOC = (char) 26; // Start of command
    char EOC = (char) 27; // End of command
    char NUL = (char) 0; // Null character
    char StartHeader = (char) 1;                    // <soh>, start of frame
    String FrameEnd = "" + (char) 4 + (char) 10 + "    ";     // <eot> + lf, end of frame
    char sendstring = (char) 31;                    // <us>, used instead of dcd. Sent first
    // Common objects
    String path = Main.HomePath + Main.Dirprefix;
    config cf = new config(path); // Configuration object
    public String callsign = cf.getCallsign();
    public String servercall = cf.getServer();
    public String statustxt = cf.getStatus();
    public String wxtxt = cf.getWX();
    String Modem = "PSK250";    // Current modem mode
    modemmodeenum mode = modemmodeenum.PSK250;

    /**
     *
     * @param incall
     */
    public void setCallsign(String incall) {
        callsign = incall;
    }

    /**
     *
     * @param server
     */
    public void setServer(String server) {
        servercall = server;
    }

    /**
     * Get the current server to link to
     * @return server callsign
     */
    public String getServer() {
        return servercall;
    }

    /**
     *
     * @param intext
     */
    public void setTxtStatus(String intext) {
        statustxt = intext;
    }

    public void setBoardWX(String intext) {
        wxtxt = intext;
    }

    /* Status enums */
    txstatus txserverstatus;

    /** /
     * Set the tx status, extremly important this gets set the right way
     * @param tx
     */
    public void set_txstatus(txstatus tx) {
        this.txserverstatus = tx;
    }

    public txstatus get_txstatus() {
        return this.txserverstatus;
    }

    public void Message(String msg, int time) {
        Main.Statusline = msg;
        Main.StatusLineTimer = time;
    }

    /**
     *
     */
    public arq() {
        String mypath = Main.HomePath + Main.Dirprefix;
        config ca = new config(mypath);
        callsign = ca.getCallsign();
    }

    public String getAPRSMessageNumber() {
        Main.APRSMessageNumber++;
        if (Main.APRSMessageNumber > 99) {
            Main.APRSMessageNumber = 0;
        }
        String outnumber = Integer.toString(Main.APRSMessageNumber);
        if (Main.APRSMessageNumber < 10) {
            outnumber = "0" + outnumber;
        }
        return "{" + outnumber;
    }

    public String getLastAPRSMessageNumber() {
        String outnumber = Integer.toString(Main.APRSMessageNumber);
        if (Main.APRSMessageNumber < 10) {
            outnumber = "0" + outnumber;
        }
        return "{" + outnumber;
    }

    /**
     *
     * @param outmessage
     */
    public void sendit(String outmessage) {
        Main.logFile.writeToLog("inside send_it.");
        String sendtext;
        try {
            sendtext = "";
            sendtext += outmessage;
            Main.Sendline = sendtext;
            Main.logFile.writeToLog("test put on Main.Sendline: " + sendtext +
                    " Bulletinmode: " + Main.Bulletinmode);
            String montext = sendtext.substring(1, sendtext.length() - 6);
            Main.monitor += "\n*TX*  " + "<SOH>" + montext + "<EOT>\n";
        } catch (Exception ex) {
            Logger.getLogger(arq.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** /
     * Create UI message, used for pskaprs email among things
     * @param intext
     * @return
     */
    private String ui_messageblock(String intext) {
        String returnframe = "";
        returnframe = "00" + Unproto + callsign + ":25 " + intext + "\n";
        return returnframe;
    }

    private String ui_aprsblock(String intext) {
        String returnframe = "";
        returnframe = "0" + "0" + Unproto + callsign + ":26 " + intext;
        return returnframe;
    }

    private String pingblock() {
        String returnframe = "";
        // Fix stream id, this is wrong
        callsign = Main.configuration.getPreference("CALL");
        returnframe = "00" + Unproto + callsign + ":7 ";
        return returnframe;
    }

    private String ui_linkblock() {
        String returnframe = "";
        // Fix stream id, this is wrong
        callsign = Main.configuration.getPreference("CALL");
        servercall = Main.configuration.getPreference("SERVER");
        returnframe = "00" + Unproto + callsign + "><" + servercall + " ";
        return returnframe;
    }

    private String ui_emergencyblock(String str) {
        String returnframe = "";
        try {
            String latstring = "0000.00";
            String lonstring = "00000.00";
            float latnum = 0;
            float lonnum = 0;
            String latsign = "N";
            String lonsign = "E";
            String course = "0";
            String speed = "0";

            // Get the GPS position data or the preset data
            if (Main.gpsdata.getFixStatus()) {
                latstring = Main.gpsdata.getLatitude();
                lonstring = Main.gpsdata.getLongitude();
                course = Main.gpsdata.getCourse();
                speed = Main.gpsdata.getSpeed();
            } else {
                // Preset data
                latstring = Main.configuration.getPreference("LATITUDE");
                lonstring = Main.configuration.getPreference("LONGITUDE");
            }
            latnum = Float.parseFloat(latstring);
            lonnum = Float.parseFloat(lonstring);
            if (latnum < 0) {
                latnum = Math.abs(latnum);
                latsign = "S";
            }
            if (lonnum < 0) {
                lonnum = Math.abs(lonnum);
                lonsign = "W";
            }

            DecimalFormat twoPlaces = new DecimalFormat("##0.00");
            int latint = (int) latnum;
            int lonint = (int) lonnum;

            latnum = ((latnum - latint) * 60) + latint * 100;
            latstring = twoPlaces.format(latnum);
            latstring = "0000" + latstring;
            int len = latstring.length();
            if (len > 6) {
                latstring = latstring.substring(len - 7, len);
            }

            // Make sure there is a period in there
            latstring = latstring.replace(",", ".");

            lonnum = ((lonnum - lonint) * 60) + lonint * 100;
            lonstring = twoPlaces.format(lonnum);
            lonstring = "00000" + lonstring;

            len = lonstring.length();
            if (len > 7) {
                lonstring = lonstring.substring(len - 8, len);
            }

            //make sure we have a period there
            lonstring = lonstring.replace(",", ".");

            // Fix stream id, this is wrong
            callsign = Main.configuration.getPreference("CALL");
            statustxt = str;
            wxtxt = Main.configuration.getPreference("WX");

            char iconPart1 = '\\';
            char iconPart2 = '!';

//            try {
//                iconPart1 = Main.Icon.charAt(0);
//                iconPart2 = Main.Icon.charAt(1);
//            } catch (Exception e) {
//                iconPart1 = '/';
//                iconPart2 = 'y';
//            }

            if (Main.gpsdata.getFixStatus()) {
                returnframe = "00" + Unproto + callsign + ":26 " + "!" + latstring +
                        latsign + iconPart1 + lonstring + lonsign + iconPart2 +
                        "/" + statustxt + "/C:" + course + "/S:" + speed + wxtxt;
            } else {
                returnframe = "00" + Unproto + callsign + ":26 " + "!" + latstring +
                        latsign + iconPart1 + lonstring + lonsign + iconPart2 +
                        statustxt + wxtxt;
            }

            return returnframe;
        } catch (Exception ex) {
            Main.log.writelog("Error when creating emergency block", ex, true);
        }

        return returnframe;
    }

    private String ui_beaconblock() {
        String returnframe = "";
        try {
            String latstring = "0000.00";
            String lonstring = "00000.00";
            float latnum = 0;
            float lonnum = 0;
            String latsign = "N";
            String lonsign = "E";
            String course = "0";
            String speed = "0";

            // Get the GPS position data or the preset data
            if (Main.gpsdata.getFixStatus()) {
                latstring = Main.gpsdata.getLatitude();
                lonstring = Main.gpsdata.getLongitude();
                course = Main.gpsdata.getCourse();
                speed = Main.gpsdata.getSpeed();
            } else {
                // Preset data
                latstring = Main.configuration.getPreference("LATITUDE");
                lonstring = Main.configuration.getPreference("LONGITUDE");
            }
            latnum = Float.parseFloat(latstring);
            lonnum = Float.parseFloat(lonstring);
            if (latnum < 0) {
                latnum = Math.abs(latnum);
                latsign = "S";
            }
            if (lonnum < 0) {
                lonnum = Math.abs(lonnum);
                lonsign = "W";
            }

            DecimalFormat twoPlaces = new DecimalFormat("##0.00");
            int latint = (int) latnum;
            int lonint = (int) lonnum;

            latnum = ((latnum - latint) * 60) + latint * 100;
            latstring = twoPlaces.format(latnum);
            latstring = "0000" + latstring;
            int len = latstring.length();
            if (len > 6) {
                latstring = latstring.substring(len - 7, len);
            }

            // Make sure there is a period in there
            latstring = latstring.replace(",", ".");

            lonnum = ((lonnum - lonint) * 60) + lonint * 100;
            lonstring = twoPlaces.format(lonnum);
            lonstring = "00000" + lonstring;

            len = lonstring.length();
            if (len > 7) {
                lonstring = lonstring.substring(len - 8, len);
            }

            //make sure we have a period there
            lonstring = lonstring.replace(",", ".");

            // Fix stream id, this is wrong
            callsign = Main.configuration.getPreference("CALL");
            statustxt = Main.configuration.getPreference("STATUS");
            wxtxt = Main.configuration.getPreference("WX");

            char iconPart1 = ' ';
            char iconPart2 = ' ';

            try {
                iconPart1 = Main.Icon.charAt(0);
                iconPart2 = Main.Icon.charAt(1);
            } catch (Exception e) {
                iconPart1 = '/';
                iconPart2 = 'y';
            }

            if (Main.gpsdata.getFixStatus()) {
                returnframe = "00" + Unproto + callsign + ":26 " + "!" + latstring +
                        latsign + iconPart1 + lonstring + lonsign + iconPart2 +
                        "/" + statustxt + "/C:" + course + "/S:" + speed + wxtxt;
            } else {
                returnframe = "00" + Unproto + callsign + ":26 " + "!" + latstring +
                        latsign + iconPart1 + lonstring + lonsign + iconPart2 +
                        statustxt + wxtxt;
            }

            return returnframe;
        } catch (Exception ex) {
            Main.log.writelog("Error when creating beaconblock", ex, true);
        }

        return returnframe;
    }

    private String connectblock() {
        String returnframe = "";
        // Fix stream id, this is wrong
        callsign = Main.configuration.getPreference("CALL");
        servercall = Main.configuration.getPreference("SERVER");
        returnframe = "00" + Connect + callsign + ":1024 " + servercall + ":24 5";
        return returnframe;
    }

    private String statusblock(String status) {
        String returnframe = "";
        returnframe = "0" + Main.session + Character.toString(Status) + status;
        return returnframe;
    }

    /**
     *
     * @param payload
     */
    public void send_frame(String payload) throws InterruptedException {
        String info = "";
        String outstring = "";
        int Lastblockinframe = 1;

        switch (this.txserverstatus) {
            case TXUImessage:
                // Only UI messages at this point
                //send_txrsid_command("ON");
                Thread.sleep(500);
                info = ui_messageblock(payload);
                Lastblockinframe = 1;
                outstring = make_block(info) + FrameEnd;
                break;
            case TXPing:
                info = pingblock();
                Lastblockinframe = 1;
                outstring = make_block(info) + FrameEnd;
                break;
            case TXaprsmessage:
                //send_txrsid_command("ON");
//                Thread.sleep(500);
//                send_mode_command(Main.APRSmode);
//                Thread.sleep(500);
                info = ui_aprsblock(payload);
                Lastblockinframe = 1;
                outstring = make_block(info) + FrameEnd;
                break;
            case TXlinkreq:
                //send_txrsid_command("ON");
//                Thread.sleep(500);
//                send_mode_command(Main.linkmode);
//                Thread.sleep(500);
                info = ui_linkblock();
                outstring = make_block(info) + FrameEnd;
                break;
            case TXBeacon:
                //send_txrsid_command("ON");
//                Thread.sleep(500);
//                send_mode_command(Main.beaconmode);
//                Thread.sleep(500);
                Main.logFile.writeToLog("beacon mode inside send_frame.");
                info = ui_beaconblock();
                outstring = make_block(info) + FrameEnd;
                break;
            case TXEmergency:
                //send_txrsid_command("ON");
//                Thread.sleep(500);
//                send_mode_command(Main.emergencymode);
//                Thread.sleep(500);
                info = ui_emergencyblock(payload);
                outstring = make_block(info) + FrameEnd;
                break;
            case TXConnect:
//                send_txrsid_command("ON");
//                Thread.sleep(500);
//                send_mode_command(Main.connectmode);
//                Thread.sleep(500);
                info = connectblock();
                outstring = make_block(info) + FrameEnd;
                break;
            case TXStat:
                info = statusblock(Main.myrxstatus);
                outstring = make_block(info) + FrameEnd;
                break;
            case TXTraffic:
                outstring = "";
                info = payload;
                outstring = StartHeader + info;

                info = statusblock(Main.myrxstatus);
                outstring += make_block(info) + FrameEnd;
                break;
        }
        sendit(outstring);
    }

    /** /
     * Send UI, unnumbered information, message
     * @param msg
     */
    public void send_uimessage(String msg) {
        try {
            this.txserverstatus = txstatus.TXUImessage;
            send_frame(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** /
     * Send a simple ping, using port 7
     */
    public void send_ping() throws InterruptedException {
        this.txserverstatus = txstatus.TXPing;
        send_frame("");
    }

    public void send_rsid_command(String s) {
        String rsidstart = "<cmd><rsid>";
        String rsidend = "</rsid></cmd>";
        if (!s.equals("")) {
            Main.SendCommand += rsidstart + s + rsidend;
        }
    }

    public void send_txrsid_command(String s) {
        String txrsidstart = "<cmd><txrsid>";
        String txrsidend = "</txrsid></cmd>";
        if (!s.equals("")) {
            Main.SendCommand += txrsidstart + s + txrsidend;
        }
    }

    public void send_txrxonoff_command() {
        Main.SendCommand += "<cmd><txrxrsid>ON</txrsid><rsid>OFF</rsid></cmd>";
    }

    /**
     * Send a mode command to the modem
     */
    public void send_mode_command(modemmodeenum mode){
        // System.out.println("inside mode command.");

        String modestart="<cmd><mode>";
        String modeend="</mode></cmd>";
        String modeset="";
        String connstr="";
        switch(mode){
            case PSK31:
                modeset="PSK31";
                break;
            case QPSK31:
                break;
            case PSK63:
                modeset="PSK63";
                connstr="~SPEED63!";
                break;
            case PSK63R:
                modeset="PSK63R";
                break;
            case PSK125:
                modeset="PSK125";
                connstr="~SPEED125!";
                break;
            case PSK125R:
                modeset="PSK125R";
                break;
            case PSK250:
                modeset="PSK250";
                connstr="~SPEED250!";
                break;
            case PSK250R:
                modeset="PSK250R";
                break;
            case PSK500:
                modeset="PSK500";
                connstr="~SPEED500!";
                break;
            case PSK500R:
                modeset="PSK500R";
                break;
            case DOMINOEX4:
                break;
            case DOMINOEX5:
                break;
            case DOMINOEX8:
                break;
            case DOMINOEX11:
                break;
            case DOMINOEX16:
                break;
            case DOMINOEX22:
                break;
            case MFSK4:
                break;
            case MFSK8:
                break;
            case MFSK16:
                modeset="MFSK16";
                connstr="~SPEEDMFSK16!";
                break;
            case MFSK22:
                modeset="MFSK22";
                connstr="~SPEEDMFSK22!";
                break;
            case MFSK31:
                break;
            case MFSK32:
                modeset="MFSK32";
                connstr="~SPEEDMFSK32!";
                break;
            case MFSK64:
                modeset="MFSK64";
                connstr="~SPEEDMFSK64!";
                break;
            case THOR4:
                break;
            case THOR5:
                break;
            case THOR8:
                 modeset="THOR8";
                connstr="~SPEEDTHOR8!";
               break;
            case THOR11:
                modeset="THOR11";
                connstr="~SPEEDTHOR11!";
                break;
            case THOR16:
                break;
            case THOR22:
                modeset="THOR22";
                connstr="~SPEEDTHOR22!";
                break;
        }

        if (!modeset.equals("")){
            if (Main.Status.equals("Connected") && !Modem.equals(modeset)) {
                // During connected state
                Main.TX_Text += connstr + "\n";
            }
            else
            {
                // Order a mode switch (could be second press too)
                Main.SendCommand += modestart+modeset+modeend;
            }

            // Save the new mode
            Modem = modeset;

            Main.currentOperatingMode = modeset;
            this.mode = mode;

//            if (mode == Main.defaultmode) {
//                Main.resetOperationModeToggle = false;
//                Main.resetOperationModeCounter = 0;
//                // System.out.println("Counter reset.");
//            } else {
////                Main.resetOperationModeCounter = 0;
////                Main.resetOperationModeToggle = true;
////                System.out.println("Counter started");
//            }

            // lock enableSwitch, keep Track of ANY send_mode_command
            // make sure by contract the lock if lifted at some point
        }        
    }

    /**
     *
     */
    public void send_link() throws InterruptedException {
        this.txserverstatus = txstatus.TXlinkreq;
        send_frame("");
    }

    /**
     *
     */
    public void send_beacon() throws InterruptedException {
        Main.logFile.writeToLog("inside send_beacon");
        if (!Main.Connected) {
            this.txserverstatus = txstatus.TXBeacon;
            Main.logFile.writeToLog("Send frame inside send_beacon");
            send_frame("");
        }
    }

    public void send_emergency(String message) throws InterruptedException {
        Main.logFile.writeToLog("inside send_emergency");

        this.txserverstatus = txstatus.TXEmergency;
        send_frame(message);
    }

    /**
     *
     * @param msg
     */
    public void send_aprsmessage(String msg) throws InterruptedException {
        this.txserverstatus = txstatus.TXaprsmessage;
        send_frame(msg);
    }

    public void send_status(String txt) throws InterruptedException {
        this.txserverstatus = txstatus.TXStat;
        send_frame(txt);
    }

    public void send_data(String outstr) throws InterruptedException {
        this.txserverstatus = txstatus.TXTraffic;
        send_frame(outstr);
    }

//    public String getModem() {
//        // System.out.println(this.Modem + " test");
//
//        return this.Modem;
//    }

    public String TX_addblock(int nr) {
        char c = (char) nr;
        String accum = "0";
        accum += Main.session;
        accum += Character.toString(c);
        accum += Session.txbuffer[nr];
        String blcheck = checksum(accum);
        accum += blcheck;

        return make_block(accum);
    }

    /** /
     * Adds SOH and checksum
     * e.g.: '<SOH>00jThis is data for'akj0
     * @param info
     * @return
     */
    public String make_block(String info) {
        String check = "";
        if (info.length() > 0) {
            check = checksum(info);
        }
        return StartHeader + info + check;
    }

    public modemmodeenum getMode() {
        return mode;
    }

    /*
    ############################################################
    # Checksum of header + block
    # Time + password + header + block
    ############################################################
     */
    /**
     *
     * @param intext
     * @return
     */
    public String checksum(String intext) {
        String Encrypted = "0000";

        int[] table = {
            0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
            0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
            0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
            0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
            0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
            0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
            0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
            0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
            0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
            0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
            0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
            0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
            0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
            0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
            0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
            0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
            0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
            0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
            0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
            0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
            0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
            0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
            0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
            0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
            0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
            0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
            0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
            0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
            0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
            0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
            0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
            0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040,};


        byte[] bytes = intext.getBytes();
        int crc = 0x0000;
        for (byte b : bytes) {
            crc = (crc >>> 8) ^ table[(crc ^ b) & 0xff];
        }

        Encrypted += Integer.toHexString(crc).toUpperCase();
        return Encrypted.substring(Encrypted.length() - 4);
    }
}


