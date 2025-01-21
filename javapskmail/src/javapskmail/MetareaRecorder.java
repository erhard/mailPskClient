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
public class MetareaRecorder {

    public String internal = "";

    public MetareaRecorder() {
        this.internal = "";
    }

    public void reset() {
        internal = "";
    }

    public void inject(String str) {
        //System.out.println("injected: " + str);

        internal += str;
    }

    public int getArea(String str, String str2) {

        //System.out.println("area decoded: " + str);

        if (str.equals("I")) return 1;
        if (str.equals("II")) return 2;
        if (str.equals("III")) {
            if (str2.equals("E")) {
                return 3;
            }
            if (str2.equals("W")) {
                return 18;
            }
        }
        if (str.equals("IV")) return 4;
        if (str.equals("V")) return 5;
        if (str.equals("VI")) return 6;
        if (str.equals("VII")) return 7;
        if (str.equals("VIII")) {
            if (str2.equals("S"))
                return 8;
            if (str2.equals("N"))
                return 17;
        }
        //if (str.equals("VIII")) return 17;
        if (str.equals("IX")) return 9;
        if (str.equals("X")) return 10;
        if (str.equals("XI")) return 11;
        if (str.equals("XII")) return 12;
        if (str.equals("XIII")) return 13;
        if (str.equals("XIV")) return 14;
        if (str.equals("XV")) return 15;
        if (str.equals("XVI")) return 16;

        return -1;
    }

    public String getFilename() {

        //System.out.println("internal string: " + internal);

        Pattern p = Pattern.compile(".*METAREA_([^_]+)_([^_])");
        Matcher m = p.matcher(internal);

        int area = 0;

        if (m.find()) {
            area = getArea(m.group(1), m.group(2));
        }

        return "metarea" + area + ".txt";

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
            fileWriter.write(internal);
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

    public static boolean checkMetarea(String str) {

        Pattern p = Pattern.compile(".*METAREA.*");
        Matcher m = p.matcher(str);

        return m.find();

    }

    public static void main(String[] args) {

        MetareaRecorder temp = new MetareaRecorder();

        String test = "   METAREA_III_W_HIGHSEA wbhfbw0obfw0obfw0p   ";

        if (MetareaRecorder.checkMetarea(test)) {

            temp.inject(test);

            temp.writeFile(new File(temp.getFilename()));

        }

    }

}
