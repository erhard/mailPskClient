/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a map.inf file with a certain syntax
 * for the leftupper und rightlower coordinates of the map
 * and it's name
 *
 * @author sebastian_pohl
 */
public class ParseMapInfFile {

    private MapCoordinate left;
    private MapCoordinate right;

    private MapCoordinate upper;
    private MapCoordinate lower;

    private String mapName;

    private Scanner fileScanner;

    @Override
    public String toString() {
        return "Map Name: " + mapName + "\n" +
            "Left: " + left + " Upper: " + upper + "\n" +
            "Right: " + right + " Lower: " + lower + "\n";
    }

    /**
     * @return the left
     */
    public MapCoordinate getLeft() {
        return left;
    }

    /**
     * @return the right
     */
    public MapCoordinate getRight() {
        return right;
    }

    /**
     * @return the upper
     */
    public MapCoordinate getUpper() {
        return upper;
    }

    /**
     * @return the lower
     */
    public MapCoordinate getLower() {
        return lower;
    }

    /**
     * @return the mapName
     */
    public String getMapName() {
        return mapName;
    }

    private class EncapsulateMapCoordinate {

        private MapCoordinate content;

        public EncapsulateMapCoordinate(MapCoordinate content) {
            this.content = content;
        }

        public void setContent(MapCoordinate content) {
            this.content = content;
        }

        public MapCoordinate getContent() {
            return this.content;
        }

    }

    public ParseMapInfFile(File file) throws FileNotFoundException, IOException {
        fileScanner = new Scanner(file);

        this.parseFile();
    }

    private void parseCoordinateLine(String line, EncapsulateMapCoordinate mx, EncapsulateMapCoordinate my)
        throws IOException {
        Pattern p;
        Matcher m;

        p = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)([E,W]),\\s*(\\d+)\\.(\\d+)\\.(\\d+)([N,S]).*");
        m = p.matcher(line);

        if (m.lookingAt()) {
            mx.setContent(new MapCoordinate((new Integer( m.group(1))).intValue(),
                    (new Integer(m.group(2)).intValue()),
                    (new Integer(m.group(3)).intValue()),
                    m.group(4).charAt(0)));
            
            my.setContent(new MapCoordinate((new Integer( m.group(5))).intValue(),
                    (new Integer(m.group(6)).intValue()),
                    (new Integer(m.group(7)).intValue()),
                    m.group(8).charAt(0)));
            return;
        } //else {
            //throw new IOException("wrong data format");
        //}

        p = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)([N,S]),\\s*(\\d+)\\.(\\d+)\\.(\\d+)([E,W]).*");
        m = p.matcher(line);

        if (m.lookingAt()) {
            my.setContent(new MapCoordinate((new Integer( m.group(1))).intValue(),
                    (new Integer(m.group(2)).intValue()),
                    (new Integer(m.group(3)).intValue()),
                    m.group(4).charAt(0)));

            mx.setContent(new MapCoordinate((new Integer( m.group(5))).intValue(),
                    (new Integer(m.group(6)).intValue()),
                    (new Integer(m.group(7)).intValue()),
                    m.group(8).charAt(0)));
            return;
        } else {
            throw new IOException("wrong data format");
        }
    }

    // we expect three lines
    private void parseFile() throws IOException {
        
        if (fileScanner.hasNextLine()) {
            String line1 = fileScanner.nextLine();
            try {
                EncapsulateMapCoordinate leftEncapsulate = new EncapsulateMapCoordinate(getLeft());
                EncapsulateMapCoordinate upperEncapsulate = new EncapsulateMapCoordinate(getUpper());
                parseCoordinateLine(line1, 
                        leftEncapsulate,
                        upperEncapsulate);
                left = leftEncapsulate.getContent();
                upper = upperEncapsulate.getContent();
            } catch(IOException e) {
                throw(e);
            }
        } else {
            throw new IOException("wrong data format");
        }
        if (fileScanner.hasNextLine()) {
            String line2 = fileScanner.nextLine();
            try {
                EncapsulateMapCoordinate rightEncapsulate = new EncapsulateMapCoordinate(getRight());
                EncapsulateMapCoordinate lowerEncapsulate = new EncapsulateMapCoordinate(getLower());
                parseCoordinateLine(line2,
                        rightEncapsulate,
                        lowerEncapsulate);
                right = rightEncapsulate.getContent();
                lower = lowerEncapsulate.getContent();
            } catch (IOException e) {
                throw(e);
            }
        } else {
            throw new IOException("wrong data format");
        }
        if (fileScanner.hasNextLine()) {
            String line3 = fileScanner.nextLine();
            this.mapName = line3;
        } else {
            throw new IOException("wrong data format");
        }
    }

    public static void main(String args[]) {
        try {
            ParseMapInfFile test = new ParseMapInfFile(new File("00N000E.inf"));
            System.out.println(test.getLeft());
            System.out.println(test.getUpper());
            System.out.println(test.getRight());
            System.out.println(test.getLower());
            System.out.println(test.getMapName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

