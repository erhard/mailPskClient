/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Timer;

/**
 *
 * @author sebastian_pohl
 */

public class DecodeFleetCode implements ActionListener {

    private String output = "";

    private String region = null;

    private TreeMap<Integer,String> dataTree = new TreeMap<Integer,String>();

    public void actionPerformed(ActionEvent e) {
        switch(currentState) {

            case AWAITING_ZFZF: // do nothing
                break;

            default: System.out.println("Time OUT");
                currentState = IACFleetState.AWAITING_GETOUTPUT;
                decodeDataTree();
                break;

        }
    }

    public enum IACFleetState {
        AWAITING_ZFZF,      // await ZFZF for the begin of IAC Fleet
        AWAITING_REGION,    // await region behaviour -> go to scan
        SCANNING_DATA,      // scanning data till NNNN
        AWAITING_GETOUTPUT  // await getting output
    }

    private Timer timeOut = new Timer(30000, this);

    private IACFleetState currentState = IACFleetState.AWAITING_ZFZF;

    public String getOutput() {
        return output;
    }

    public String getRegion() {
        return region;
    }

    public IACFleetState getState() {
        return currentState;
    }

    public TreeMap<Integer, String> getDataTree() {
        return dataTree;
    }

    private int maxLineNumber() {
        Collection<Integer> keys = dataTree.keySet();
        Iterator<Integer> iter = keys.iterator();
        int max = 0;
        while (iter.hasNext()) {
            int temp = iter.next();
            if (temp > max)
                max = temp;
        }

        return max;
    }

    private boolean tryNNNN(String str) {
        Pattern p = Pattern.compile("NNNN");
        Matcher m = p.matcher(str);

        return m.find();
    }

    private boolean tryZFZF(String str) {
        Pattern p = Pattern.compile("ZFZF");
        Matcher m = p.matcher(str);

        return m.find();
    }

    private boolean tryRegion(String str) {
        Pattern p = Pattern.compile("<SOH>(\\w{4}\\d{2} \\w{4} \\d{6})");
        Matcher m = p.matcher(str);

        if (m.find()) {
            region = m.group(1);
            return true;
        }

        return false;
    }

    private boolean tryData(String str) {
        Pattern p = Pattern.compile("<SOH>(\\p{Lower}{2}) (\\p{Lower}{8} \\p{Lower}{8} \\p{Lower}{8} \\p{Lower}{8})");
        Matcher m = p.matcher(str);

        if (m.find()) {
            try {
                int lineNumber = 0;
                char[] lineNumberArray = m.group(1).toCharArray();
                for (int i = 0; i < lineNumberArray.length; i++) {
                    switch (i) {
                        case 0:
                            lineNumber += decodeLetter(lineNumberArray[i]) * 16;
                            break;
                        case 1:
                            lineNumber += decodeLetter(lineNumberArray[i]);
                    }
                }
                dataTree.put(lineNumber, m.group(2));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }            
        }
        return false;
    }

    public DecodeFleetCode() { }

    public DecodeFleetCode(File file) throws IOException {

        BufferedReader fileScanner = new BufferedReader(
                new FileReader(file));

        String line;

        while ((line = fileScanner.readLine()) != null) {
            inject(line + "\n");
        }

    }

    public void inject(String str) {

        switch(currentState) {

            case AWAITING_ZFZF:
                if (tryZFZF(str)) {
                    currentState = IACFleetState.AWAITING_REGION;
                    timeOut.restart();
                    break;
                }
                break;

            case AWAITING_REGION:
                if (tryRegion(str)) {
                    currentState = IACFleetState.SCANNING_DATA;
                    timeOut.restart();
                    break;
                }
                if (tryNNNN(str)) {
                    currentState = IACFleetState.AWAITING_ZFZF;
                    timeOut.restart();
                    break;
                }
                break;

            case SCANNING_DATA:
                if (tryData(str)) {
                    timeOut.restart();
                    break;
                }
                if (tryNNNN(str)) {
                    //System.out.println("max line: " + maxLineNumber());
                    decodeDataTree();
                    currentState = IACFleetState.AWAITING_GETOUTPUT;
                    timeOut.stop();
                    break;
                }

            case AWAITING_GETOUTPUT:
                break;

        }
    }

    private String decodeBlock(char[] str) {
        String temp = "";

        for (int i = 0; i < str.length; i++) {
            try {
                temp += decodeInterim(decodeLetter(str[i]));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return temp;
    }

    private String decodeLine(String str) {
        String temp = "";

        Pattern p = Pattern.compile("(\\p{Lower}{8}) (\\p{Lower}{8}) (\\p{Lower}{8}) (\\p{Lower}{8})");
        Matcher m = p.matcher(str);

        if (m.find()) {
            temp += decodeBlock(m.group(1).toCharArray());
            temp += decodeBlock(m.group(2).toCharArray());
            temp += decodeBlock(m.group(3).toCharArray());
            temp += decodeBlock(m.group(4).toCharArray());
        }

        return temp;
    }

    private void decodeDataTree() {
        output += region + "\n";

        for (int i = 1; i + 3 <= maxLineNumber(); i += 4) {
            int numbersBlocksThere = 0;
            for (int j = i; j < i + 4; j++) {
                if (dataTree.containsKey(j)) {
                    numbersBlocksThere++;
                }
            }
            //if (numbersBlocksThere == 4) {
            if (dataTree.containsKey(i))
                output += decodeLine(dataTree.get(i));
            if (dataTree.containsKey(i+1))
                output += decodeLine(dataTree.get(i + 1));
            if (dataTree.containsKey(i+2))
                output += decodeLine(dataTree.get(i + 2));
            //}

//            System.out.println("Decoding line " + i + ": " + decodeLine(dataTree.get(i)) + "\n" +
//                    "Decoding line " + (i+1) + ": " + decodeLine(dataTree.get(i + 1)) + "\n" +
//                    "Decoding line " + (i+2) + ": " + decodeLine(dataTree.get(i + 2)));
        }
    }

    private char decodeInterim(byte b) throws IOException {

        switch (b) {
            case 0: return ' ';
            case 1: return '0';
            case 2: return '1';
            case 3: return '2';
            case 4: return '3';
            case 5: return '4';
            case 6: return '5';
            case 7: return '6';
            case 8: return '7';
            case 9: return '8';
            case 10: return '9';
            case 11: return '/';
            case 12: return '=';
            case 13: return '\n';
            default: throw new IOException("Wrong byte value: " + b);
        }
    }

    private byte decodeLetter(char ch) throws IOException {
        switch (ch) {
            case 'e': return 0;
            case 'o': return 1;
            case 't': return 2;
            case 'a': return 3;
            case 'i': return 4;
            case 'n': return 5;
            case 'l': return 6;
            case 'r': return 7;
            case 's': return 8;
            case 'c': return 9;
            case 'd': return 10;
            case 'f': return 11;
            case 'h': return 12;
            case 'm': return 13;
            case 'p': return 14;
            case 'u': return 15;
            default: throw new IOException("There is a wrong letter, decoding impossible:" + ch);
        }
    }

    public static void main(String[] args) {

        DecodeFleetCode test = new DecodeFleetCode();
        
        test.inject("<SOH>ZFZF");

        System.out.println(test.getState());

        test.inject("<SOH>FSXX21 EGRR 280000");
        
        System.out.println(test.getState());

        test.inject("<SOH>eo rlllreii iooeoaco oeoooanm dddoomct smhllmod");
        test.inject("<SOH>et oslersin aeemctoc celdiiae emctodre fncnfioh");
        //test.inject("<SOH>ea niirceem ctoosesi aoreemct otneisoc pnrlnnet");
        test.inject("<SOH>ea mmheeeee eeeeeeee eeeeeeee eeeeeeee eeeeeeee");
        test.inject("<SOH>ei adiocseh paeheoeh foefnduu fnlaiuuf mmtnshei");
        
        test.inject("<SOH>en ceemctot selcaace emcloate noisoeem ihfddtdu");


        System.out.println(test.getState());

        test.inject("<SOH>NNNN");

        System.out.println(test.getOutput());
        try {
            DecodeFleetCode test2 = new DecodeFleetCode(new File("F:/codedfleet"));

            System.out.println(test2.getState());

            System.out.println(test2.getDataTree().size());

            System.out.println(test2.getOutput());

            File fleetFile = new File("..\\zyGrib\\fleet");
            boolean tempBool;
            BufferedWriter fleetWriter = null;
            if (!fleetFile.exists()) {
                tempBool = fleetFile.createNewFile();
            }
            try {
                fleetWriter = new BufferedWriter(new FileWriter(fleetFile));
                fleetWriter.write(test2.getOutput());
            } catch (Exception except) {
                except.printStackTrace();
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



        } catch (IOException ex) {
            Logger.getLogger(DecodeFleetCode.class.getName()).log(Level.SEVERE, null, ex);
        }

}

}
