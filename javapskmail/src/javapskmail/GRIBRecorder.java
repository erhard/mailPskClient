/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sebastian_pohl
 */
public class GRIBRecorder {

    public String internal = "";

    public GRIBRecorder() {
        internal = "";
    }

    public void reset() {
        internal = "";
    }

    public void inject(String str) {
        internal += str;
    }

    public String getFileName() {
        return "GRIBbroadcasted";
    }

    public static boolean checkStartGRIB(String str) {
        Pattern p = Pattern.compile("start of GRIB");
        Matcher m = p.matcher(str);

        return m.find();
    }

    public static boolean checkEndGRIB(String str) {
        Pattern p = Pattern.compile("end of GRIB");
        Matcher m = p.matcher(str);

        return m.find();
    }

    public void saveToFile(String fileName) {
        try {
            Base64.decodeToFile(internal.replaceAll("[^a-zA-Z0-9+/]",""), fileName);
        } catch (Exception e) {
            System.out.println("problems with base64 decoding");
            e.printStackTrace();
        }
    }

    public void writeFile(File file) {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                return;
            }
        }

        BufferedWriter fileWriter = null;

        try {
            FileWriter fstream = new FileWriter(file);
            fileWriter = new BufferedWriter(fstream);
            fileWriter.write(internal.replaceAll("\\s",""));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (Exception e) {
            }
        }
    }

}
