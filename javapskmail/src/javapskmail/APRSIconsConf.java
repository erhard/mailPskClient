/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 *
 * @author sebastian_pohl
 */
public class APRSIconsConf {

    // saves the map for APRS Icon to Icon File
    private Properties settings = null;

    public APRSIconsConf() {
        super();
    }

    public APRSIconsConf(String filepath) throws FileNotFoundException, IOException {
        FileInputStream tempFile = new FileInputStream(filepath);

        settings = new Properties();
        settings.loadFromXML(tempFile);
    }

    public String getOverlay(String key) {

        char k1 = key.charAt(0);

        if (k1 != '/' && k1 != '\\') {
            return String.valueOf(k1);
        }

        return null;
    }

    public String getFileName(String key) {
        //Stack<String> tempStack = new Stack<String>();
        //if (settings != null) {
        //    tempStack.push(settings.getProperty(key, "icons/i.gif"));
        //} else {
        //    tempStack.push("icons/i.gif");
        //}

        char k1;
        char k2;

        try {
            k1 = key.charAt(0);
            k2 = key.charAt(1);
        } catch (Exception e) {
            k1 = '/';
            k2 = 'Y';
        }
        Integer i;
        String str;
        switch(k1) {
            // primary table
            case '/': i = new Integer((int) k2);
                str = i.toString();
                switch (str.length()) {
                    case 1: str = "00" + str + ".gif";
                        break;
                    case 2: str = "0" + str + ".gif";
                        break;
                    default: str = str + ".gif";
                        break;
                }
                return "icons/pri/" + str;
            // secondary table
            case '\\':
                i = new Integer((int) k2);
                str = i.toString();
                switch (str.length()) {
                    case 1: str = "00" + str + ".gif";
                        break;
                    case 2: str = "0" + str + ".gif";
                        break;
                    default: str = str + ".gif";
                        break;
                }
                return "icons/alt/" + str;
            // overlay
            default:
                // symbol to overlay
                //str = String.valueOf(k1);
                //tempStack.push(str);
                // underlying symbol is ON TOP of the stack
                i = new Integer((int) k2);
                str = i.toString();
                switch (str.length()) {
                    case 1: str = "00" + str + ".gif";
                        break;
                    case 2: str = "0" + str + ".gif";
                        break;
                    default: str = str + ".gif";
                        break;
                }
                return "icons/alt/" + str;
        }        
    }

    public ImageIcon getImageIcon(String key, int width, int height) {
        String fileName = getFileName(key);
        //Stack<ImageIcon> tempIcons = new Stack<ImageIcon>();

        //while (!fileName.empty()) {
        ImageIcon temp = new ImageIcon((new ImageIcon(fileName)).getImage().getScaledInstance(width, height, 40));
        //}

        return temp;
    }

    public static void main(String args[]) {
        try {
            APRSIconsConf temp = new APRSIconsConf("aprsicons.xml");

            System.out.println(temp.getFileName("/y"));

            System.out.println(temp.getFileName("\\-"));

            System.out.println(temp.getFileName("P&"));

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
