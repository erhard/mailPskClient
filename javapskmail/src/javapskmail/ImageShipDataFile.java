/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

/**
 *
 * @author sebastian_pohl
 */
public class ImageShipDataFile {

    private java.util.zip.ZipFile archive = null;
    private InputStream databaseStream = null;
    private BufferedImage shipImage = null;

    boolean decoded = false;

    public ImageShipDataFile(File file, String str) throws IOException, NullPointerException {
        archive = new ZipFile(file);
        ZipEntry tempEntry = archive.getEntry(str);
        databaseStream = archive.getInputStream(tempEntry);

        shipImage = ImageIO.read(databaseStream);

        archive.close();
    }

    public BufferedImage getShipImage() {
        return (BufferedImage) shipImage;
    }

    public static void main(String[] args) {
        try {
            ImageShipDataFile temp = new ImageShipDataFile(new File("ShipDB.zip"), "shipImages/pict.jpg");
        } catch(Exception e) {
            System.out.println("thrown up");
            e.printStackTrace();
        }
    }

}
