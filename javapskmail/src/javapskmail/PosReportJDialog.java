/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PosReportJDialog.java
 *
 * Created on 20.08.2009, 09:02:28
 */

package javapskmail;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author sebastian_pohl
 */
public class PosReportJDialog extends javax.swing.JDialog implements ListSelectionListener {
    // temporary link to the posReport to be shown
    TreeMap<String, TreeMap<String, PosData>> posTemp = null;

    TreeMap<String, TreeMap<String, PosData>> dictionary = null;

    /** Creates new form PosReportJDialog */
    public PosReportJDialog(TreeMap<String, 
            TreeMap<String,PosData>> posDataList,
            java.awt.Frame parent,
            boolean modal) {
        super(parent, modal);
        posTemp = posDataList;

        initComponents();

        listCallSigns.addListSelectionListener(this);
        listDates.addListSelectionListener(this);

        buildPosReport();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(listCallSigns)) {
            listDates.setListData(posTemp.get((String) listCallSigns.getSelectedValue()).keySet().toArray());
            //listDates.setSelectedIndex(1);
//            PosData temp = posTemp.get((String) listCallSigns.getSelectedValue()).get((String) listDates.getSelectedValue());
//            String tempString = temp.getCallsign() + "\n" +
//                temp.getDate() + "," + temp.getTime() + "\n" +
//                temp.getLatitude().getGeodesic() + "," + temp.getLongitude().getGeodesic();
//
//            if (temp.isOptionalFlag()) {
//                tempString += "\n" + temp.getOptional();
//            }
//
//            txtPosData.setText(tempString);
        }

        if (e.getSource().equals(listDates)) {
            PosData temp = posTemp.get((String) listCallSigns.getSelectedValue()).get((String) listDates.getSelectedValue());
            String tempString = temp.getCallsign() + "\n" + temp.getAprs() + "\n" +
                temp.getDate() + " " + temp.getTime() + "\n" +
                temp.getLatitude().getGeodesic() + "," + temp.getLongitude().getGeodesic();

            if (temp.isOptionalFlag()) {
                tempString += "\n" + temp.getOptional();
            }

            txtPosData.setText(tempString);

            //System.out.println(listDates.getSelectedIndices());
        }

        //System.out.println(e.getFirstIndex() + "," + e.getLastIndex());
        //System.out.println(listCallSigns.getSelectedValue());
        //System.out.println(dictionary.get((String) listCallSigns.getSelectedValue()));
    }

    private void buildPosReport() {
        //dictionary = new TreeMap<String, TreeMap<String, PosData>>();
        //Iterator<String> tempIter = posTemp.keySet().iterator();
        //while (tempIter.hasNext()) {
        //    String temp = tempIter.next();
        //    dictionary.put(temp, posTemp.get(temp));
        //}
        listCallSigns.setListData(posTemp.keySet().toArray());
        //listDates.setListData(posTemp.get((String) listCallSigns.getSelectedValue())
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOK = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtPosData = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        listCallSigns = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        listDates = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        txtPosData.setColumns(20);
        txtPosData.setEditable(false);
        txtPosData.setRows(5);
        jScrollPane2.setViewportView(txtPosData);

        listCallSigns.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(listCallSigns);

        listDates.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(listDates);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOK))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOK)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        setVisible(false);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnOKActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PosReportJDialog dialog = new PosReportJDialog(null, new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList listCallSigns;
    private javax.swing.JList listDates;
    private javax.swing.JTextArea txtPosData;
    // End of variables declaration//GEN-END:variables

}
