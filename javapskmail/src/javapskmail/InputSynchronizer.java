/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javapskmail;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author sebastian_pohl
 */
public class InputSynchronizer implements KeyListener, ItemListener {

    private ArrayList<JTextField> synchronizedObjects = null;
    private JComboBox synchronizedBox = null;
    private Component boxEditorComponent = null;

    public InputSynchronizer() {
        synchronizedObjects = new ArrayList<JTextField>();
    }

    public void addTextField(JTextField textField) {
        synchronizedObjects.add(textField);
        textField.addKeyListener(this);
    }

    public void setBox(JComboBox comboBox) {
        synchronizedBox = comboBox;
        boxEditorComponent = comboBox.getEditor().getEditorComponent();
        comboBox.getEditor().getEditorComponent().addKeyListener(this);
        comboBox.addItemListener(this);
    }

    public void keyTyped(KeyEvent e) {
        
    }

    public void keyPressed(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void keyReleased(KeyEvent e) {
        if (!e.getSource().equals(boxEditorComponent)) {
            String text = ((JTextField) e.getSource()).getText();

            //System.out.println(text);

            Iterator<JTextField> iter = synchronizedObjects.iterator();

            while (iter.hasNext()) {
                iter.next().setText(text);
            }//throw new UnsupportedOperationException("Not supported yet.");

            synchronizedBox.getEditor().setItem(text);

            //System.out.println("textfield changed");
        } else {
            String text = "";

            if (boxEditorComponent instanceof JTextField) {
                text = ((JTextField) boxEditorComponent).getText();
            }

            Iterator<JTextField> iter = synchronizedObjects.iterator();

            while (iter.hasNext()) {
                //System.out.println("next textfield");
                iter.next().setText(text);
            }

            //System.out.println("combo box changed");
        }
    }

    public void itemStateChanged(ItemEvent e) {
       
            if (e.getSource().equals(synchronizedBox)) {
            // String text = ((JTextField) ((JComboBox) e.getSource()).getEditor().getEditorComponent()).getText();

            String text = (String) synchronizedBox.getSelectedItem();

            //System.out.println(text);

            Iterator<JTextField> iter = synchronizedObjects.iterator();

            while (iter.hasNext()) {
                iter.next().setText(text);
            }//throw new UnsupportedOperationException("Not supported yet.");

            // synchronizedBox.getEditor().setItem(text);

            //System.out.println("textfield changed");
            }
        
    }

}
