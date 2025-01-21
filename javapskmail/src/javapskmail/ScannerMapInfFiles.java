/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;

/**
 *
 * @author sebastian_pohl
 */
public class ScannerMapInfFiles {

    JComboBox reference;

    public ScannerMapInfFiles(JComboBox parent) {
        reference = parent;
    }

    private ArrayList<String> filter(String[] temp, String string) {
        ArrayList<String> tempStorage = new ArrayList<String>();
        //Pattern p = Pattern.compile("([^.]).inf");
        Pattern p = Pattern.compile(string);
        //Pattern p = Pattern.compile("([^\\.]*)\\.inf");
        
        for (int i = 0; i < temp.length; i++) {
            //System.out.println(temp[i]);
            
            Matcher m = p.matcher(temp[i]);
            if (m.matches()) {
                //System.out.println(" matches");
                tempStorage.add(m.group(1));
            }
        }
        return tempStorage;
    }

    public ArrayList<String> getInfFileList() {
        File f = new File(".");
        String[] files = f.list();
        return filter(files, "([^\\.]*)\\.inf");
    }

//    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
//        File f = new File(".");
//        String[] files = f.list();
//
//        System.out.println(files.length);
//
//        ArrayList<String> filesFiltered = filter(files, "([^\\.]*)\\.inf");
//
//        reference.removeAllItems();
//
//        Iterator<String> iter = filesFiltered.iterator();
//
//        while (iter.hasNext()) {
//            reference.addItem(iter.next());
//            //    System.out.println(iter.next());
//        }
//    }

    public static void main(String[] args) {
        Pattern p = Pattern.compile("(.)");
        Matcher m = p.matcher("hasbro.inf");

        System.out.println(m.matches());
    }

}
