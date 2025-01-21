/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class to parse a pos report file made in PSKmail standard
 * provides an arraylist of the parsed PosData elements
 *
 * @author sebastian_pohl
 */
public class ParsePosReportFile {

    private BufferedReader fileScanner;
    private TreeMap<String, TreeMap<String,PosData>> posReport;

    public ParsePosReportFile(File file, int mode) throws FileNotFoundException, IOException {
        fileScanner = new BufferedReader(new FileReader(file));
        posReport = new TreeMap<String, TreeMap<String,PosData>>(); // empty ArrayList

        this.parseFile(mode);
    }

    private void parseFile(int mode) throws IOException {
        String line;

        while ((line = fileScanner.readLine()) != null) {
            //System.out.println(line);

            // cut blanks from the beginning and the end of the line
            Pattern p = Pattern.compile("\\s*(.*)\\s*");
            Matcher m = p.matcher(line);

            if (m.matches()) {
                line = m.group(1);
            } else {
                throw new IOException("Data input is corrupt.");
            }

            try {
                //System.out.println(line + " mode: " + mode);
                PosData temp = new PosData(line, mode);
                // if this goes through , add the PosData to the internal PosReport
                // check emptiness, so we know if we have to create a new TreeMap for this CallSign
                if (posReport.get(temp.getCallsign()) != null) {
                    posReport.get(temp.getCallsign()).put(temp.getDate() + " " + temp.getTime(), temp);
                } else {
                    // create new TreeMap for this callSign (since empty)
                    posReport.put(temp.getCallsign(), new TreeMap<String,PosData>(new ReverseStringComparator()));
                    posReport.get(temp.getCallsign()).put(temp.getDate() + " " + temp.getTime(), temp);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("failed: " + line);
                // data line is corrupt
            }
        }
    }

    @Override
    public String toString() {
        String temp = "";
        Iterator<TreeMap<String,PosData>> iter = posReport.values().iterator();

        while (iter.hasNext()) {
            PosData temp2 = iter.next().firstEntry().getValue();
            temp += temp2 + "\n";
        }

        return temp;
    }

    public TreeMap<String, TreeMap<String, PosData>> getPosReport() {
        return posReport;
    }

    public static void main(String args[]) {
        try {
            ParsePosReportFile report = new ParsePosReportFile(new File("posreport2.txt"),1);
            System.out.println(report);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Input Output exception. Boom.");
            ex.printStackTrace();
        }
    }
}
