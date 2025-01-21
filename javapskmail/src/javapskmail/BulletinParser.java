/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javapskmail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sebastian_pohl
 */
public class BulletinParser {

    private String inline = "";
    private boolean posDataParsed = false;

    public BulletinParser() {
        inline = "";
    }

    public ArrayList<PosData> lookupPosData() {
        ArrayList<PosData> tempPos = new ArrayList<PosData>();
        // rebuild the inline string at this point
        String tempString = "";
        // cut \n (Zeilenvorschub)
        Pattern linebyline = Pattern.compile("(.*)\\n");
        Matcher mlinebyline = linebyline.matcher(inline);
        // rebuild string
        while (mlinebyline.find()) {
            if (mlinebyline.group(1).length() != 0) {
                tempString += mlinebyline.group(1) + " ";
            }
            inline = inline.substring(mlinebyline.end());
            mlinebyline = linebyline.matcher(inline);
        }

        //System.out.println("Temp String " + tempString);

        String tempString2 = "";
        // cut three space line
        Pattern cutthreespace = Pattern.compile("\\s{3}");
        Matcher mcutthreespace = cutthreespace.matcher(tempString);
        // delete three spaces in a row within the string
        tempString2 = mcutthreespace.replaceAll("");

        //System.out.println("tempString2 " + tempString2);

        String[] arrayString = tempString2.split("\\{linebreak\\}");

        //for (int i = 0; i < arrayString.length; i++)
        //    System.out.println(arrayString[i]);

        //System.out.println(arrayString.length);

        Pattern pgetposdataentries = Pattern.compile("(\\d+;[^;]+;[^;]+;[^;]+;[^;]+;[^;]+;.*)");


//        Matcher mgetposdataentries = pgetposdataentries.matcher(tempString2);
//
        ArrayList<String> tempData = new ArrayList<String>();
//
//        while (mgetposdataentries.find()) {
//            System.out.println("found one");
//            tempData.add(mgetposdataentries.group(1));
//            tempString2 = tempString2.substring(mgetposdataentries.end());
//            mgetposdataentries = pgetposdataentries.matcher(tempString2);
//        }

        for (int i = 0; i < arrayString.length; i++) {
            Matcher mgetposdataentries = pgetposdataentries.matcher(arrayString[i]);
            if (mgetposdataentries.find()) {
                tempData.add(mgetposdataentries.group(1));
            }
        }

        //System.out.println("Temp Data: " + tempData);

        for (int i = 0; i < tempData.size(); i++) {
            try {
                PosData p = new PosData(tempData.get(i), 1);
                tempPos.add(p);
            } catch (IOException ex) {
                System.out.println("something askew");
            } catch (NumberFormatException ex) {
                System.out.println("something askew");
            }
        }

        //System.out.println("Temp Pos: " + tempPos);

        return tempPos;
    }

//    public ArrayList<PosData> lookupPosData2() {
//        ArrayList<PosData> tempPos = new ArrayList<PosData>();
//        ArrayList<String> tempList = new ArrayList<String>();
//        String tempString = "";
//
//        // cut the inline into lines conforming to the \n chars delivered
//        Pattern linebyline = Pattern.compile("(.*)\\n");
//        Matcher m2 = linebyline.matcher(inline);
//        // cut it up with this loop, truncate inline as far as possible
//        while (m2.find()) {
//            tempList.add(m2.group(1));
//            tempString += m2.group(1) + " ";
//            //System.out.println(m2.group(1) + "\n" + m2.start() + "\n" + m2.end());
//            inline = inline.substring(m2.end());
//            m2 = linebyline.matcher(inline);
//        }
//        //  tempList has now saved all the lines from inline
//
//        //System.out.println(tempString);
//
//        ArrayList<String> tempList2 = new ArrayList<String>();
//
//        // now cut through the list of lines to find out where we have complete PosData
//        // using a regular expression
//        Pattern p = Pattern.compile("\\s{3}(\\d+;[^;]+;[^;]+;[^;]+;[^;]+;[^;]+;.*)");
//        Pattern p2 = Pattern.compile("NNNN");
//        Matcher m;
//        boolean started = false;
//        String current = "";
//
//        for (int i = 0; i < tempList.size(); i++) {
//            if (p2.matcher(tempList.get(i)).find()) {
//                tempList2.add(current);
//                break;
//            } else {
//                // get next element
//                m = p.matcher(tempList.get(i));
//                if (started) {
//                    if (m.matches()) {
//                        tempList2.add(current);
//                        current = m.group(1);
//                        started = true;
//                    } else {
//                        current += tempList.get(i).substring(2);
//                    }
//                } else {
//                    if (m.matches()) {
//                        started = true;
//                        current = m.group(1);
//                    }
//                }
//            }
//        }
    //System.out.println(tempList2);
//
//        for (int i = 0; i < tempList2.size(); i++) {
//            try {
//                tempPos.add(new PosData(tempList2.get(i), 1));
//            } catch (IOException ex) {
//                System.out.println("some IO Error, go further");
//            } catch (NumberFormatException ex) {
//                System.out.println("some number format error, go further");
//            }
//        }
//
//        return tempPos;
//    }
    public boolean getPosDataParsed() {
        return posDataParsed;
    }

    public String getInline() {
        return inline;
    }

    public void inject(String str) {
        inline += str;
    }

    public static void main(String[] args) {
        BulletinParser temp = new BulletinParser();

        ///temp.inject("ZCZC\n"+
        //    "QTC de DK4XI-0\n"+
        //    "Fr 4. Sep 09:46:08 UTC 2009\n"+
        //    "\n"+
        //    "\n");
        //temp.inject("   1;RAUTAUOMA;25.0815;60.1562;/s;2009-09-03 22:50:01; http://kartta.hmpy.fi\n");
        //temp.inject("   additional comment {linebreaker}\n");
        //temp.inject("   2;PA0WKM-5;3.85017;51.7382;/Y;2009-09-03 22:48:24;{linebreaker}\n");
        //temp.inject("   3;KB1MAD;101.725;-13.8687;/Y;2009-09-03 22:31:00; Islands, Indian Ocean.{linebreaker}\n");
        //temp.inject("   NNNN\n");

        temp.inject("ZCZC QTC de DK4XI-0  Mi 9. Sep 12:46:03 UTC 2009    1;PH0DF;108.536;-2.4915;/Y;2009-09-09 08:49:00;this is status;with a semicolon{linebreak}    2;F5YD;2.90517;43.2663;/y;2009-09-09    08:41:20;4315.98N/00254.31Ey{linebreak}    3;WA6CZL;118.385;-8.33117;/Y;2009-09-09 08:33:00; Sumbawa, Ind {linebreak}    4;KI4CIT;115.239;-8.71883;/Y;2009-09-09 08:06:00; north of Benoa harbour,    fast trip down. {linebreak} 5;VK8HF;130.9;-12.4;/Y;2009-09-09    08:04:00;{linebreak} 6;ON3LMD;-4.79967;48.2603;/Y;2009-09-09    08:03:00;{linebreak} 7;VA2FCL;26.7663;37.2947;/Y;2009-09-09 07:29:00;    attendant accalmie {linebreak} 8;DG1HWS;-7.0715;37.211;/Y;2009-09-09    07:24:00; el rompido {linebreak} 9;OE1AES;15.2845;43.8197;/Y;2009-09-09    07:22:00; Steg beim Wirten; {linebreak}    10;VK2APE-8;151.482;-33.108;/s;2009-09-09 07:20:00;{linebreak}    11;WIDE2-2;9.36733;47.6628;/Y;2009-09-09    07:18:21;/071811h4739.77N/00922.04EY081/000/A=001321 12.8V 26C HDOP00.9    SATS08Rene unterwegs mit Segelyacht QRV 145.250{linebreak}    12;DL8HB;167.431;-14.3133;/Y;2009-09-09 07:02:00; Amwindkurs ruhige Lakona    Bay, Gaua Island {linebreak} 13;PE1PRX;5;52;/y;2009-09-09    07:01:39;0.3.7{linebreak} 14;JA2DME-8;132.323;34.3032;/Y;2009-09-09    07:00:07;{linebreak} 15;KD6SAE;-1.14433;37.1102;/Y;2009-09-09 06:52:00; to    Gibraltar. {linebreak} 16;SY-LAMEDI;-5.68167;46.2257;/Y;2009-09-09    06:44:00;{linebreak} 17;W7BRI-7;-144.336;-27.6167;/s;2009-09-09    06:34:23;`w7bri@w7bri.com Join me in Rapa!_{linebreak}    18;IZ5IVV;25.7515;35.5452;/Y;2009-09-09 06:27:00; nodi, non e' cosa da    gentleman! {linebreak} 19;KG4PRC;2.63533;39.5657;/Y;2009-09-09    06:07:00;{linebreak} NNNN \n");

        //temp.inject("1;PH0DF;108.536;-2.4915;/Y;2009-09-09 08:49:00;this is status;with a semicolon{linebreak}\n");

        System.out.println(temp.lookupPosData());

    }
}
