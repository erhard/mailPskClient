/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sebastian_pohl
 */
public class ParseMetareaCoords {

    private TreeMap<Integer,MetareaCoords> internalList;

    private Scanner scanner;

    public ParseMetareaCoords(File file) throws FileNotFoundException {
        scanner = new Scanner(file);

        internalList = new TreeMap<Integer,MetareaCoords>();

        this.parseIt();
    }

    private void parseIt() {

        String temp = "";

        while (scanner.hasNextLine()) {

            temp = scanner.nextLine();

            // System.out.println("parsing: " + temp);

            Pattern p = Pattern.compile("(\\d+),(\\d{1,3})\\.(\\d{1,2})([N,S]{1}),(\\d{1,3})\\.(\\d{1,2})([E,W]{1})");
            Matcher m = p.matcher(temp);

            if (m.find()) {

                internalList.put(new Integer(m.group(1)),
                        new MetareaCoords(new Integer(m.group(1)),
                        new MapCoordinate(new Integer(m.group(5)), new Integer(m.group(6)), 0, m.group(7).charAt(0)),
                        new MapCoordinate(new Integer(m.group(2)), new Integer(m.group(3)), 0, m.group(4).charAt(0))));

            } else {
                // System.out.println("parse unsuccessful.");
            }

        }

    }

    public TreeMap<Integer, MetareaCoords> getMetareaList() {
        return internalList;
    }

    public static void main(String args[]) {
        try {
            ParseMetareaCoords temp = new ParseMetareaCoords(new File("metarea/metareaCoords"));

            TreeMap<Integer,MetareaCoords> tempList = temp.getMetareaList();

            Iterator<Integer> iter;

            iter = tempList.keySet().iterator();

            while (iter.hasNext()) {
                Integer tempInt = iter.next();

                System.out.println(tempList.get(tempInt));
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ParseMetareaCoords.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
