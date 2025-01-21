/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * OperatorDataDialog.java
 *
 * Created on 14.05.2009, 08:26:27
 */

package javapskmail;

import java.awt.Component;
import java.util.Locale;

/**
 *
 * @author sebastian_pohl
 */
public class OperatorDataDialog extends javax.swing.JDialog implements optionsui {

    private optionslogic optlogic;

    private String beaconqrg;
    private String DCDSpinner;
    private String latitude;
    private String longitude;
    private String spinOffsetSecond;
    private String spinOffsetMinute;
    private String spinRetries;
    
    private String spinIdleTime;
    private String spinTXdelay;
    private String linkServer;

    //private String pictureName;
    //private String picturePath;
    //private String operatorName;

    /** Creates new form OperatorDataDialog */
    public OperatorDataDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        optlogic = new optionslogic(this);
        initComponents();

        pnlAbout = new AboutDialog(parent, true);
        pnlAbout.setLocationRelativeTo(null);
        pnlEmailOption = new EmailOptionDialog(parent, true);
        pnlEmailOption.setLocationRelativeTo(null);
        pnlOperatorOption = new OperatorOptionDialog(parent, true);
        pnlOperatorOption.setLocationRelativeTo(null);
        pnlGPSOption = new GPSOptionDialog(parent, true);
        pnlGPSOption.setLocationRelativeTo(null);

        optlogic.optionslogic_start();
    }

    public void setAPRSIcon(String str) {
        this.pnlOperatorOption.setAPRSIcon(str);
    }

    public String getAPRSIcon() {
        return this.pnlOperatorOption.getAPRSIcon();
    }

    public void setPosTime(String str) {
        this.pnlOperatorOption.setPosTime(str);
    }

    public String getPosTime() {
        return this.pnlOperatorOption.getPosTime();            
    }

    public void setOperatorName(String str) {
        this.pnlOperatorOption.setOperatorName(str);
    }

    public String getOperatorName() {
        return this.pnlOperatorOption.getOperatorName();
    }

    public void setPicturePath(String str) {
        this.pnlOperatorOption.setPicturePath(str);
    }

    public String getPicturePath() {
        return this.pnlOperatorOption.getPicturePath();
    }

    public void setNewsgroup(String str) {
        this.pnlOperatorOption.setNewsgroup(str);
    }

    public String getNewsgroup() {
        return this.pnlOperatorOption.getNewsgroup();
    }

    public void setPictureName(String str) {
        this.pnlOperatorOption.setPictureName(str);
    }

    public String getPictureName() {
        return this.pnlOperatorOption.getPictureName();
    }

    public void setCallsign(String call) {
        pnlOperatorOption.setCallsign(call);
    }

    public String getCallsign() {
        return pnlOperatorOption.getCallsign();
    }

    public void setBeaconqrg(String beacon) {
        this.beaconqrg = beacon;
    }

    public Object getSpinBeaconMinute() {
        return this.beaconqrg;
    }

    public String getBeaconqrg() {
        return this.beaconqrg;        
    }

    public void setServer(String call) {
       this.linkServer = call;
    }

    public String getServer() {
        return this.linkServer;
    }

    public void setLatitude(String string) {
        this.latitude = string;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLongitude(String string) {
        this.longitude = string;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setBlockLength(String string) {
        pnlOperatorOption.setBlockLength(string);
    }

    public String getBlockLength() {
        return pnlOperatorOption.getBlockLength();
    }

    public Object getSpinOffsetSecond() {
        return this.spinOffsetSecond;
    }

    public Object getSpinOffsetMinute() {
        return this.spinOffsetMinute;
    }

    public Object getDCDSpinner() {
        return this.DCDSpinner;
    }

    public void setDCDSpinner(Object o) {
        this.DCDSpinner = o.toString();
    }

    public void removeAllGPSSerialPort() {
        this.pnlGPSOption.removeAllItemsGPSSerialPort();
    }

    public void addGPSSerialPort(Object o) {
        this.pnlGPSOption.addItemGPSSerialPort(o);
    }

    public Object getGPSSerialPort() {
        return this.pnlGPSOption.getSelectedGPSSerialPort();
    }

    public boolean GPSConnectionSelected() {
        return this.pnlGPSOption.getGPSconnected();
    }

    public void setGPSConnection(boolean b) {
        this.pnlGPSOption.setGPSconnected(b);
    }

    public void GPSSpeedEnabled(boolean set) {
        this.pnlGPSOption.setEnabledGPSSpeed(set);
    }

    public void selectGPSSpeed(Object o) {
        this.pnlGPSOption.setSelectedGPSSpeed(o);
    }

    public Object getGPSSpeed() {
        return this.pnlGPSOption.getSelectedGPSSpeed();
    }

    public void GPSSerialPortEnabled(boolean set) {
        this.pnlGPSOption.setEnabledGPSSerialPort(set);
    }

    public void selectGPSSerialPort(Object o) {
        this.pnlGPSOption.setSelectedGPSSerialPort(o);
    }

    public void setSpinRetries(Object o) {
        this.spinRetries = o.toString();
    }

    public Object getSpinRetries() {
        return this.spinRetries;
    }

    public void setSpinIdleTime(Object o) {
        this.spinIdleTime = o.toString();
    }

    public Object getSpinIdleTime() {
        return this.spinIdleTime;
    }

    public void setSpinTXdelay(Object o) {
        this.spinTXdelay = o.toString();
    }

    public Object getSpinTXdelay() {
        return this.spinTXdelay;
    }

    public void setSpinOffsetMinute(Object o) {
        this.spinOffsetMinute = o.toString();
    }

    public void setSpinOffsetSecond(Object o) {
        this.spinOffsetSecond = o.toString();
    }

    public void setTxtPophost(String str) {
        this.pnlEmailOption.setPopHost(str);
    }

    public String getTxtPophost() {
        return this.pnlEmailOption.getPopHost();
    }

    public void setTxtPopUser(String str) {
        this.pnlEmailOption.setPopUser(str);
    }

    public String getTxtPopUser() {
        return this.pnlEmailOption.getPopUser();
    }

    public void setTxtPopPassword(String str) {
        this.pnlEmailOption.setPopPassword(str);
    }

    public String getTxtPopPassword() {
        return this.pnlEmailOption.getPopPassword();
    }

    public void setTxtReplyTo(String str) {
        this.pnlEmailOption.setReplyTo(str);
    }

    public String getTxtReplyTo() {
        return this.pnlEmailOption.getReplyTo();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void setLocationRelativeTo(Component c) {
        super.setLocationRelativeTo(c);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOperatorOperatorOptions = new javax.swing.JButton();
        btnOperatorEmailOptions = new javax.swing.JButton();
        btnOperatorGPSOptions = new javax.swing.JButton();
        btnOperatorAbout = new javax.swing.JButton();
        btnOperatorManual = new javax.swing.JButton();
        lblOperatorOperatorData = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnOperatorOperatorOptions.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("javapskmail/OperatorDataDialog"); // NOI18N
        btnOperatorOperatorOptions.setText(bundle.getString("OPERATOR_-_OPTIONS")); // NOI18N
        btnOperatorOperatorOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorOperatorOptionsActionPerformed(evt);
            }
        });

        btnOperatorEmailOptions.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnOperatorEmailOptions.setText(bundle.getString("EMAIL_-_OPTIONS")); // NOI18N
        btnOperatorEmailOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorEmailOptionsActionPerformed(evt);
            }
        });

        btnOperatorGPSOptions.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnOperatorGPSOptions.setText(bundle.getString("GPS_-_OPTIONS")); // NOI18N
        btnOperatorGPSOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorGPSOptionsActionPerformed(evt);
            }
        });

        btnOperatorAbout.setFont(new java.awt.Font("Arial", 0, 16));
        btnOperatorAbout.setText(bundle.getString("ABOUT")); // NOI18N
        btnOperatorAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorAboutActionPerformed(evt);
            }
        });

        btnOperatorManual.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        btnOperatorManual.setText(bundle.getString("MANUAL")); // NOI18N
        btnOperatorManual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorManualActionPerformed(evt);
            }
        });

        lblOperatorOperatorData.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblOperatorOperatorData.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOperatorOperatorData.setText(bundle.getString("OPERATOR_-_DATA")); // NOI18N

        btnCancel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnCancel.setText(bundle.getString("CANCEL")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnSave.setText(bundle.getString("SAVE")); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnOperatorOperatorOptions, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(btnOperatorEmailOptions, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(btnOperatorGPSOptions, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(btnOperatorAbout, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(btnOperatorManual, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .addComponent(lblOperatorOperatorData)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblOperatorOperatorData)
                .addGap(17, 17, 17)
                .addComponent(btnOperatorOperatorOptions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOperatorEmailOptions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOperatorGPSOptions)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOperatorAbout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOperatorManual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnSave))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOperatorEmailOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorEmailOptionsActionPerformed
        // TODO add your handling code here:
        pnlEmailOption.setVisible(true);
}//GEN-LAST:event_btnOperatorEmailOptionsActionPerformed

    private void btnOperatorAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorAboutActionPerformed
        // TODO add your handling code here:
        pnlAbout.setVisible(true);
}//GEN-LAST:event_btnOperatorAboutActionPerformed

    private void btnOperatorGPSOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorGPSOptionsActionPerformed
        // TODO add your handling code here:
        pnlGPSOption.setVisible(true);
    }//GEN-LAST:event_btnOperatorGPSOptionsActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        optlogic.bOKActionPerformed(evt);
        setVisible(false);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOperatorOperatorOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorOperatorOptionsActionPerformed
        // TODO add your handling code here:
        pnlOperatorOption.setVisible(true);
    }//GEN-LAST:event_btnOperatorOperatorOptionsActionPerformed

    private void btnOperatorManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorManualActionPerformed
        // TODO add your handling code here:
//        try {
//            Process p =
//                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"manual_pskmail_1_4en.pdf\"");
//            p.waitFor();
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
        String currentLanguage = Locale.getDefault().getLanguage();

        if (!currentLanguage.equals("de") && !currentLanguage.equals("fr")) {
            currentLanguage = "en";
        }

        // System.out.println(Locale.getDefault().getLanguage());

        startupManual(currentLanguage);
    }//GEN-LAST:event_btnOperatorManualActionPerformed

    public OperatorOptionDialog getOperatorOption() {
        return pnlOperatorOption;
    }

    private void startupManual(String str) {
        try {
            Process p =
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"manual_pskmail_1_5" + str + ".pdf\"");
            p.waitFor();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OperatorDataDialog dialog = new OperatorDataDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOperatorAbout;
    private javax.swing.JButton btnOperatorEmailOptions;
    private javax.swing.JButton btnOperatorGPSOptions;
    private javax.swing.JButton btnOperatorManual;
    private javax.swing.JButton btnOperatorOperatorOptions;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel lblOperatorOperatorData;
    // End of variables declaration//GEN-END:variables

    private AboutDialog pnlAbout;
    private EmailOptionDialog pnlEmailOption;
    private OperatorOptionDialog pnlOperatorOption;
    private GPSOptionDialog pnlGPSOption;

    public void setNewsgroupenabled(String str) {
        this.pnlOperatorOption.setNewsgroupenabled(str);
    }

    public String getNewsgroupenabled() {
        return this.pnlOperatorOption.getNewsgroupenabled();
    }

}
