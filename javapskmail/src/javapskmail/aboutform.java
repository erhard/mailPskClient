/*
 * aboutform.java
 *
 * Created on den 26 november 2008, 15:23
 */

package javapskmail;

/**
 *
 * @author  per
 */
public class aboutform extends javax.swing.JFrame {

    /** Creates new form aboutform */
    public aboutform() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(470, 300));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jLabel1.setText("About JPSKmail");
        jPanel1.add(jLabel1);

        getContentPane().add(jPanel1, new java.awt.GridBagConstraints());

        jScrollPane1.setMinimumSize(new java.awt.Dimension(460, 200));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(480, 230));

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("DejaVu Sans", 0, 12));
        jTextArea1.setRows(5);
        jTextArea1.setText("Welcome to JPSKmail, PSKmail client v0.3.6 (beta)\n(c) Copyright 2009 Pär Crusefalk (SM0RWO) and Rein Couperus (PA0R)\n\nThis is a cross-platform client for PSKmail developed in java.\nThis client is built with the intention that it will mature into the pskmail\nclient of the future, Some features from the stable linux client are\nat the moment missing, one such feature is compressed downloads.\nWe are working hard to make this client complete, updates will follow.\n\nDistributed under the GNU General Public License version 2 or later.\nThis is free software: you are free to change and redistribute it.\nThere is NO WARRANTY, to the extent permitted by law.\n");
        jTextArea1.setMinimumSize(new java.awt.Dimension(450, 200));
        jTextArea1.setPreferredSize(new java.awt.Dimension(450, 200));
        jScrollPane1.setViewportView(jTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton1.setText(" OK ");
        jButton1.setMaximumSize(new java.awt.Dimension(80, 29));
        jButton1.setMinimumSize(new java.awt.Dimension(60, 29));
        jButton1.setPreferredSize(new java.awt.Dimension(70, 29));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        getContentPane().add(jPanel2, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// TODO add your handling code here:
    this.setVisible(false);
}//GEN-LAST:event_jButton1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new aboutform().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
