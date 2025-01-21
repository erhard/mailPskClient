/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javapskmail;

import java.io.*;
import java.util.*;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
//import gnu.io.CommPort;
//import gnu.io.CommPortIdentifier;
//import gnu.io.SerialPort;
//import gnu.io.SerialPortEvent;
//import gnu.io.SerialPortEventListener;

/**
 *
 * @author per
 */
public class serialport {

    static Enumeration portList;
    InputStream inputStream;
    OutputStream outputStream;
    Thread readThread;
    BufferedReader reader;
    SerialReader thereader;
    boolean curstate = false;

    /**
     * Seralport constructor, only calls its superclass constructor for now.
     */
    public serialport() {
        super();
    }

    /**
     * Connect to the GPS and try to get data, update nmeaparser when data is received
     * @param portName
     * @param speed
     * @throws java.lang.Exception
     */
    void connect(String portName, int speed) throws Exception {
        try {
                    InputStream in = IOUtils.toInputStream("some test data for my input stream");
                    BufferedReader bufread = new BufferedReader(new InputStreamReader(in));
                     IOUtils.write("data outPutStream", outputStream, "UTF-8");
                   
                    thereader = new SerialReader(bufread, in);
                    // Save the state
                    curstate = true;
            
        } catch (Exception ex) {
            Main.log.writelog("Could not connect to GPS port.", true);
        }
    }

    /**
     * Disconnect the port
     */
    void disconnect() {
        
    }

    /**
     * Return the port status
     * @return
     */
    boolean getconnectstate() {
        return curstate;
    }

    /**
     * Write a string to the serial port
     * @param command What to send
     */
    public void writestring(String command) {
        try {
            // Add line end
            command = command + "\r\n";
            outputStream.write(command.getBytes());
        } catch (Exception e) {
            Main.log.writelog("Error when writing data to serial port", e, true);
        }
    }

    /**
     * Write a character to the serial port
     * @param command What to send
     */
    public void writechar(int chr) {
        try {
            // Add line end
            outputStream.write(chr);
        } catch (Exception e) {
            Main.log.writelog("Error when writing data to serial port", e, true);
        }
    }

    /**
     * Have the serialport class get the available ports on this system
     * using the current operating system.
     * @return
     */
    public ArrayList getCommports() {
        ArrayList<String> myarr = new ArrayList<String>();
            myarr.add("COMMMM1");
            return myarr;
         }

    static String getPortTypeName(int portType) {
        return "Fake Serial";
    }

    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example. 
     */
    public static class SerialReader implements EventListener {

        private InputStream in;
        BufferedReader inreader;
        private byte[] buffer = new byte[1024];
        private String mytest = "";

        public SerialReader(InputStream in) {
            this.in = in;
        }

        public SerialReader(BufferedReader reader, InputStream in) {
            this.inreader = reader;
            this.in = in;
        }

        public void serialEvent(String arg0) {
           
        }

        /**
         * Get one of the wanted nmea lines and parse it
         * @param nmeadata Raw nmea data string
         */
        public void parsenmeadata(String nmeadata) {
            Main.gpsdata.newdata(nmeadata);
            if (Main.gpsdata.getFixStatus()) {
                Main.configuration.setLatitude(Main.gpsdata.getLatitude());
                Main.configuration.setLongitude(Main.gpsdata.getLongitude());
                Main.configuration.setSpeed(Main.gpsdata.getSpeed());
                Main.configuration.setCourse(Main.gpsdata.getCourse());
            }
        }
    }
}
