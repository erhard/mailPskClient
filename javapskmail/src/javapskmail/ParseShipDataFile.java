/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.TreeMap;
import java.util.zip.ZipFile;

/**
 *
 * @author sebastian_pohl
 */
public class ParseShipDataFile {

    private java.util.zip.ZipFile archive;
    private InputStream databasereader;
    private BufferedReader databasescanner;
    private TreeMap<String,ShipData> database;

    //private TreeMap<String,ShipData> boogies;

    public ParseShipDataFile(File file) throws IOException {
        // System.out.println(file);

        try {
            archive = new ZipFile(file);
        }catch(Exception e) {
            e.printStackTrace();
        }
        database = new TreeMap<String, ShipData>();
        //boogies = new TreeMap<String, ShipData>();

        // System.out.println(archive.size());

        databasereader = archive.getInputStream(archive.getEntry("Database.txt"));
        databasescanner = new BufferedReader(new InputStreamReader(databasereader, "ISO-8859-1"));
        int i = 0;
        int j = 0;
        int k = 0;
        String line;

        while ((line = databasescanner.readLine()) != null) {
            // System.out.println(line);
            ShipData temp = new ShipData(line);

            if (temp != null && temp.isGoodData()) {
                i++;
                database.put(temp.getCall(), temp);

                // System.out.println(temp);
            }

            if (temp.equals(null)) {
                j++;
            }

            if (!temp.isGoodData()) {
                k++;
                System.out.println(line);
            }

        }
        Main.logFile.writeToLog(j + " nulls, successfully retrieved " + i + " entries, " + k + " unsuccessful retrieves");

        //for (String iter : database.keySet()) {
        //   System.out.println(database.get(iter));
        //}

        archive.close();
    }

    public TreeMap<String,ShipData> getDataBase() {
        return database;
    }

    public static void main(String[] args) {
        try {
            ParseShipDataFile temp = new ParseShipDataFile(new File("ShipDB.zip"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
