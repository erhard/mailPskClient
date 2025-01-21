/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author sebastian_pohl
 */
public class ServerDatabase {

    private BufferedReader fileScanner;
    private TreeMap<String, Server> internalDatabase;
    private File internalFile;

    public ServerDatabase(File file) throws IOException {

        // open file and parse the serverlist
        if (file.exists()) {
            fileScanner = new BufferedReader(new FileReader(file));
        } else {
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("");
            out.close();
        }

        internalDatabase = new TreeMap<String, Server>();

        internalFile = file;

        parseThis();
        
    }

    public Set<String> getServerList() {
        return internalDatabase.keySet();
    }

    private void parseThis() throws IOException {
        String line;
        Server tempServer;

        while ((line = fileScanner.readLine()) != null) {

            Pattern p = Pattern.compile("([^;]*);([^;]*);([^;]*);([^;]*)");
            Matcher m = p.matcher(line);

            if (m.matches()) {
                tempServer = new Server(m.group(1), m.group(2), m.group(3), m.group(4));
                
                //System.out.println(tempServer + " parsed ok.");
                
                internalDatabase.put(tempServer.getCallSign(), tempServer);
                
                //System.out.println(m.group(1) + " " + m.group(4) + " turns out fine.");
            } else {
                throw new IOException("this line ("  + line + ") cannot be parsed.");
            }

        }

        //System.out.println("Ding Dong");
    }

    public void writeServerFile(File file) {
        BufferedWriter testWriter = null;

        try {
            testWriter = new BufferedWriter(new FileWriter(file));

            Iterator<String> iter = internalDatabase.keySet().iterator();

            while (iter.hasNext()) {
                testWriter.write(internalDatabase.get(iter.next()).toString() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (testWriter != null) {
                    testWriter.flush();
                    testWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public TableModel buildTableModel() {

        DefaultTableModel tempModel = new DefaultTableModel();

        Vector columnHeadVector = new Vector();

        columnHeadVector.add("Callsign");
        columnHeadVector.add("Country");
        columnHeadVector.add("Frequency");
        columnHeadVector.add("Position");

        Vector rowVector = new Vector();

        Iterator<String> iter = internalDatabase.keySet().iterator();

        while (iter.hasNext()) {
            rowVector.add(internalDatabase.get(iter.next()).buildVector());
        }

        tempModel.setDataVector(rowVector, columnHeadVector);

        return tempModel;

    }

    public void saveTableModel(TableModel m) {

        internalDatabase = new TreeMap<String, Server>();

        int rowCount = m.getRowCount();

        for (int r = 0; r < rowCount; r++) {
            internalDatabase.put((String) m.getValueAt(r, 0),
                    new Server((String) m.getValueAt(r,0),
                    (String) m.getValueAt(r,1),
                    (String) m.getValueAt(r,2),
                    (String) m.getValueAt(r,3)));
        }

        writeServerFile(internalFile);

    }

    public static void printTableModel(TableModel m) {
        for (int r = 0; r < m.getRowCount(); r++) {
            System.out.println(m.getValueAt(r,0) + "," + m.getValueAt(r,1) + ","
                    + m.getValueAt(r,2) + "," + m.getValueAt(r,3));         
        }
    }

    public static void main(String args[]) {

        try {
            ServerDatabase temp =  new ServerDatabase(new File("serverlist"));
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

}
