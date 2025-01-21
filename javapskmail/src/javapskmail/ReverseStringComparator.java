/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.util.Comparator;

/**
 *
 * @author sebastian_pohl
 */
public class ReverseStringComparator implements Comparator<String> {

    public int compare(String o1, String o2) {
        return o1.compareTo(o2) * -1;
    }
    
}
