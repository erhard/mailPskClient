/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author sebastian_pohl
 */
public class DownloadProgress extends JProgressBar {

    private int length;
    private int current;

    public DownloadProgress(int length) {

        super(0 ,100);
        this.length = length;
        this.current = 0;
        setValue(0);
        setStringPainted(true);

    }

    public void update(int newCurrent) {
        this.current = newCurrent;

        if (this.length >= 1) {
            setIndeterminate(false);
            setString(null);
            setValue((int) ((double) current / (double) length * 100));
        } else {
            setIndeterminate(true);
            setString("downloading");
        }
    }

    public static void main(String[] args) {

        JFrame test = new JFrame("Test progress");

        JPanel test2 = new JPanel();

        DownloadProgress progress = new DownloadProgress(27000000);
        
        test2.add(progress);

        test.add(test2);

        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setSize(300, 300);
        test.setLocationRelativeTo(null);

        test.setVisible(true);

        progress.update(5000000);

    }

}
