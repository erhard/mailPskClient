/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * fourbuttons.java
 *
 * Created on 05.03.2009, 08:15:06
 */

package javapskmail;

/**
 *
 * @author sebastian_pohl
 */
public class fourbuttons extends javax.swing.JPanel {

    // private mainpskmailintermar parent;
    private mainpskmaillogic parentlogic;

    private APRSInfoDialog infoscreen = null;

    private EmergencyMessageDialog emergencyscreen = null;

    /** Creates new form fourbuttons */
    public fourbuttons(mainpskmaillogic currentlogic) {
        //this.parent = parent;
        initComponents();
        parentlogic = currentlogic;
    }

    public APRSInfoDialog getInfoscreen() {
        return infoscreen;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnPosBacon = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnLink = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnAPRSinfo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnEmergency = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(212, 208, 200), 8));
        setMaximumSize(new java.awt.Dimension(10000, 10000));
        setMinimumSize(new java.awt.Dimension(512, 50));
        setPreferredSize(new java.awt.Dimension(512, 50));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(512, 50));
        jPanel1.setMinimumSize(new java.awt.Dimension(512, 50));
        jPanel1.setPreferredSize(new java.awt.Dimension(512, 50));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 5));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));

        btnPosBacon.setBackground(new java.awt.Color(204, 0, 0));
        btnPosBacon.setFont(new java.awt.Font("Tahoma", 1, 13));
        btnPosBacon.setForeground(new java.awt.Color(255, 255, 255));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("javapskmail/fourbuttons"); // NOI18N
        btnPosBacon.setText(bundle.getString("POS-BEACON")); // NOI18N
        btnPosBacon.setToolTipText(bundle.getString("SENDET_POSITIONSREPORT_INKLUSIVE_BEACONCOMMENT,_KURS,_SPEED,_BORDWETTER.")); // NOI18N
        btnPosBacon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPosBaconActionPerformed(evt);
            }
        });
        jPanel5.add(btnPosBacon);

        jPanel1.add(jPanel5);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        btnLink.setBackground(new java.awt.Color(255, 255, 204));
        btnLink.setFont(new java.awt.Font("Tahoma", 1, 13));
        btnLink.setText(bundle.getString("LINK")); // NOI18N
        btnLink.setToolTipText("Sendet eine Link-Information zum \"Server actual\". Server listet den User, APRS-Mails und Messagen können dann zugestellt werden. (Unbedingt erforderlich!)");
        btnLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLinkActionPerformed(evt);
            }
        });
        jPanel3.add(btnLink);

        jPanel1.add(jPanel3);

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        btnAPRSinfo.setBackground(new java.awt.Color(255, 204, 255));
        btnAPRSinfo.setFont(new java.awt.Font("Tahoma", 1, 13));
        btnAPRSinfo.setText(bundle.getString("APRS-INFO")); // NOI18N
        btnAPRSinfo.setToolTipText(bundle.getString("ÖFFNET_FENSTER_ZUR_AUTOMATISCHEN_ABFRAGE_VON_APRS-INFORMATIONEN.")); // NOI18N
        btnAPRSinfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAPRSinfoActionPerformed(evt);
            }
        });
        jPanel4.add(btnAPRSinfo);

        jPanel1.add(jPanel4);

        jPanel2.setBackground(new java.awt.Color(204, 0, 0));

        btnEmergency.setBackground(new java.awt.Color(255, 153, 0));
        btnEmergency.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnEmergency.setForeground(new java.awt.Color(255, 255, 255));
        btnEmergency.setText(bundle.getString("EMERGENCY")); // NOI18N
        btnEmergency.setToolTipText(bundle.getString("SENDET_NOT-RUF;_VORHER_TEXT_EINGEBEN")); // NOI18N
        btnEmergency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmergencyActionPerformed(evt);
            }
        });
        jPanel2.add(btnEmergency);

        jPanel1.add(jPanel2);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnPosBaconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPosBaconActionPerformed
        // TODO add your handling code here:
        //System.out.println("BeaconAction");
        parentlogic.PositButtonActionPerformed(evt);
    }//GEN-LAST:event_btnPosBaconActionPerformed

    private void btnLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLinkActionPerformed
        // TODO add your handling code here:
        parentlogic.bLinkActionPerformed(evt);
}//GEN-LAST:event_btnLinkActionPerformed

    private void btnAPRSinfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAPRSinfoActionPerformed
        // TODO add your handling code here:
        //frmAPRSInfo infoscreen = new frmAPRSInfo(parentlogic);
        infoscreen = new APRSInfoDialog(parentlogic, parentlogic.getParentUI().getParentFrame(), true);
        infoscreen.setLocationRelativeTo(null);
        infoscreen.setVisible(true);

        infoscreen.dispose();
        infoscreen = null;
    }//GEN-LAST:event_btnAPRSinfoActionPerformed

    private void btnEmergencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmergencyActionPerformed
        // TODO add your handling code here:
        emergencyscreen = new EmergencyMessageDialog(parentlogic, parentlogic.getParentUI().getParentFrame(), true);
        emergencyscreen.setLocationRelativeTo(null);
        emergencyscreen.setVisible(true);

        emergencyscreen.dispose();
        emergencyscreen = null;
    }//GEN-LAST:event_btnEmergencyActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAPRSinfo;
    private javax.swing.JButton btnEmergency;
    private javax.swing.JButton btnLink;
    private javax.swing.JButton btnPosBacon;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    // End of variables declaration//GEN-END:variables

}
