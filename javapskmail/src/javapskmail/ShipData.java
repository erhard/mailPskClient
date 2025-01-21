/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sebastian_pohl
 */
public class ShipData {

    private String call = "";
    private String name = "";
    private String country = "";
    private String crew = "";
    private String ship = "";
    private String websitephoto = "";
    private String remark = "";

    private boolean goodData = false;

    public ShipData(String linein) {

//        Pattern p = Pattern.compile("\\s*([^\\t]+)\\t" +
//                "\\s*([^\\t]+)\\t" +
//                "\\s*([^\\t]+)\\t" +
//                "\\s*([^\\t]+)\\t" +
//                "\\s*([^\\t]+)\\t" +
//                "\\s*([^\\t]+)\\t" +
//                "\\s*([^\\t]+)" +
//                ".*");
//
//        Matcher m = p.matcher(linein);

        // linein.replace("\t\t", "\t");
        String[] tempSplit = linein.split("\\t");

//        if (m.matches()) {
//            this.call = m.group(1);
//            this.name = m.group(2);
//            this.country = m.group(3);
//            this.crew = m.group(4);
//            this.ship = m.group(5);
//            this.websitephoto = m.group(6);
//            this.remark = m.group(7);
        //if (tempSplit.length >= 6) {
        if (tempSplit.length > 0) {
            int adjustedLength = tempSplit.length;
            if (adjustedLength > 7) {
                adjustedLength = 7;
            }

            switch(adjustedLength) {
                case 7: this.remark = tempSplit[6];
                case 6: this.websitephoto = tempSplit[5];
                case 5: this.ship = tempSplit[4];
                case 4: this.crew = tempSplit[3];
                case 3: this.country = tempSplit[2];
                case 2: this.name = tempSplit[1];
                case 1: this.call = tempSplit[0];
                default: goodData = true;
                    break;
            }
        } else {
            goodData = false;
        }

    }

    @Override
    public String toString() {
        return this.getCall() + ","  + this.getName() + "," + this.getCountry() + "," +
                this.getCrew() + "," + this.getShip() + "," + this.getWebsitephoto() + "," +  this.getRemark();
    }

    public static void main(String args[]) {

        ShipData test = new ShipData(" KF4PNX	 NATHAN B	USA	 NATHAN B HOPE	SY-MEERCAT	./shipImages/pict12410.jpg	Fahrtensegler");

        System.out.println(test);

    }

    /**
     * @return the call
     */
    public String getCall() {
        return call;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the crew
     */
    public String getCrew() {
        return crew;
    }

    /**
     * @return the ship
     */
    public String getShip() {
        return ship;
    }

    /**
     * @return the websitephoto
     */
    public String getWebsitephoto() {
        return websitephoto;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    public boolean isGoodData() {
        return goodData;
    }

}
