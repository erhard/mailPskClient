/*
 * optionsdalog.java  
 *   
 * Copyright (C) 2008 Pär Crusefalk (SM0RWO)  
 *   
 * This program is distributed in the hope that it will be useful,  
 * but WITHOUT ANY WARRANTY; without even the implied warranty of  
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
 * GNU General Public License for more details.  
 *   
 * You should have received a copy of the GNU General Public License  
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.  
 */

package javapskmail;

import java.awt.Component;
import javax.swing.SpinnerNumberModel;
import java.text.NumberFormat;      // For masked edit field for position
import java.util.Locale;            // For format in masked edit

/**
 *
 * @author  per
 */
public class optionsdialog extends javax.swing.JDialog implements optionsui {

    private optionslogic optlogic;

    public void setAPRSIcon(String str) {
        // nothing
    }

    public String getAPRSIcon() {
        return "APRSIcon";
    }

    public void setPosTime(String str) {
        // nothing
    }

    public String getPosTime() {
        return "PosTime";
    }

    public void setOperatorName(String str) {
        // nothing
    }

    public String getOperatorName() {
        return "operator";
    }

    public void setPicturePath(String str) {
        // nothing
    }

    public String getPicturePath() {
        return "picturepath";
    }

    public void setPictureName(String str) {
        // nothing
    }

    public String getPictureName() {
        return "picturename";
    }

    /**
     * 
     * @param call
     */
    public void setCallsign(String call) {
        this.txtCallsign.setText(call);
    }

    public String getCallsign() {
        return this.txtCallsign.getText();
    }

    /**
     * 
     * @param beacon
     */
    public void setBeaconqrg(String beacon) {
        this.spinBeaconMinute.setValue(Integer.parseInt(beacon));
    }

    public String getBeaconqrg() {
        return this.spinBeaconMinute.getValue().toString();
    };

    /**
     * 
     * @param call
     */
    public void setServer(String call) {
        this.txtLinkto.setText(call);
    }

    public String getServer() {
        return this.txtLinkto.getText();
    }
       /**
        * 
        * @param string
        */
    public void setLatitude(String string) {
        txtLatitude.setText(string);
    }

    public String getLatitude() {
        return this.txtLatitude.getText();
    }

    /**
     * 
     * @param string
     */
    public void setLongitude(String string) {
        this.txtLongitude.setText(string);
    }

    public String getLongitude() {
        return this.txtLongitude.getText();
    }

    /**
     * 
     * @param string
     */
    public void setBlockLength(String string) {
        this.txtBlocklength.setText(string);
    }

    public String getBlockLength() {
       return this.txtBlocklength.getText();
    }

    public Object getSpinOffsetSecond() {
        return this.spinOffsetSeconds.getValue();
    }

    public Object getDCDSpinner() {
        return this.DCDSpinner.getValue();
    }

    public void setDCDSpinner(Object o) {
        this.DCDSpinner.setValue(o);
    }

    public void removeAllGPSSerialPort() {
        this.cboGPSSerialPort.removeAllItems();
    }

    public void addGPSSerialPort(Object o) {
        this.cboGPSSerialPort.addItem(o);
    }

    public boolean GPSConnectionSelected() {
        return this.chkGPSConnection.isSelected();
    };

    public void setGPSConnection(boolean b) {
        this.chkGPSConnection.setSelected(b);
    }

    public void GPSSpeedEnabled(boolean set) {
        this.cboGPSSpeed.setEnabled(set);
    };

    public void selectGPSSpeed(Object o) {
        this.cboGPSSpeed.setSelectedItem(o);
    }

    public void GPSSerialPortEnabled(boolean set) {
        this.cboGPSSerialPort.setEnabled(set);
    };

    public void selectGPSSerialPort(Object o) {
        this.cboGPSSerialPort.setSelectedItem(o);
    };

    public Object getSpinBeaconMinute() {
        return this.spinBeaconMinute.getValue();
    };

    public void setSpinRetries(Object o) {
        this.spinRetries.setValue(o);
    };

    public void setSpinIdleTime(Object o) {
        this.spinIdleTime.setValue(o);
    };

    public void setSpinTXdelay(Object o){
        this.spinTXdelay.setValue(o);
    };

    public void setSpinOffsetMinute(Object o) {
        this.spinOffsetMinute.setValue(o);
    };

    public void setSpinOffsetSecond(Object o) {
        this.spinOffsetSeconds.setValue(o);
    }

    public void setTxtPophost(String str) {
        this.txtPophost.setText(str);
    };

    public String getTxtPophost() {
        return this.txtPophost.getText();
    };

    public void setTxtPopUser(String str) {
        this.txtPopUser.setText(str);
    };

    public String getTxtPopUser() {
        return this.txtPopUser.getText();
    }

    public void setTxtPopPassword(String str) {
        this.txtPopPassword.setText(str);
    };

    public String getTxtPopPassword() {
        return this.txtPopPassword.getText();
    }

    public void setTxtReplyTo(String str) {
        this.txtReplyto.setText(str);
    };

    public String getTxtReplyTo() {
        return this.txtReplyto.getText();
    }

    public Object getSpinOffsetMinute() {
        return this.spinOffsetMinute.getValue();
    }

    public Object getGPSSerialPort() {
        return this.cboGPSSerialPort.getSelectedItem();
    }

    public Object getGPSSpeed() {
        return this.cboGPSSpeed.getSelectedItem();
    }

    public Object getSpinRetries() {
        return this.spinRetries.getValue();
    }

    public Object getSpinIdleTime() {
        return this.spinIdleTime.getValue();
    }

    public Object getSpinTXdelay() {
        return this.spinTXdelay.getValue();
    }

    public void dispose() {
        super.dispose();
    }

    public void setLocationRelativeTo(Component c) {
        super.setLocationRelativeTo(c);
    }

    /** Creates new form optionsdialog
         * @param parent 
         * @param modal
         */
    public optionsdialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        optlogic = new optionslogic(this);
        initComponents();
        optlogic.optionslogic_start();
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

        tabOptions = new javax.swing.JTabbedPane();
        pnlUserData = new javax.swing.JPanel();
        lblCallsign = new javax.swing.JLabel();
        lblLinkto = new javax.swing.JLabel();
        lblBlocklength = new javax.swing.JLabel();
        lblLatitude = new javax.swing.JLabel();
        lblLongitude = new javax.swing.JLabel();
        lblBeaconQRG = new javax.swing.JLabel();
        txtCallsign = new javax.swing.JTextField();
        txtLinkto = new javax.swing.JTextField();
        txtBlocklength = new javax.swing.JTextField();
        // Setup masked boxes
        NumberFormat nfLonLat;
        nfLonLat = NumberFormat.getInstance(Locale.US);
        nfLonLat.setMinimumFractionDigits(4);
        nfLonLat.setMaximumFractionDigits(4);
        nfLonLat.setMaximumIntegerDigits(3);
        nfLonLat.setMinimumIntegerDigits(3);
        txtLongitude = new javax.swing.JFormattedTextField(nfLonLat);
        // Setup masked boxes
        NumberFormat nfLat;
        nfLat = NumberFormat.getInstance(Locale.US);
        nfLat.setMinimumFractionDigits(4);
        nfLat.setMaximumFractionDigits(4);
        nfLat.setMaximumIntegerDigits(2);
        nfLat.setMinimumIntegerDigits(2);
        txtLatitude = new javax.swing.JFormattedTextField(nfLat);
        spinBeaconMinute = new javax.swing.JSpinner();
        pnlEmail = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtPophost = new javax.swing.JTextField();
        txtPopUser = new javax.swing.JTextField();
        txtPopPassword = new javax.swing.JTextField();
        txtReplyto = new javax.swing.JTextField();
        ServerUpdateButton = new javax.swing.JButton();
        pnlConfiguration = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtLogFile = new javax.swing.JTextField();
        lblRetries = new javax.swing.JLabel();
        lblIdle = new javax.swing.JLabel();
        lblTXdelay = new javax.swing.JLabel();
        lblOffsetmin = new javax.swing.JLabel();
        spinRetries = new javax.swing.JSpinner();
        spinIdleTime = new javax.swing.JSpinner();
        spinTXdelay = new javax.swing.JSpinner();
        lblSecond = new javax.swing.JLabel();
        spinOffsetMinute = new javax.swing.JSpinner();
        spinOffsetSeconds = new javax.swing.JSpinner();
        DCDSpinner = new javax.swing.JSpinner();
        lblDCD = new javax.swing.JLabel();
        pnlGPS = new javax.swing.JPanel();
        frGPS = new javax.swing.JPanel();
        chkGPSConnection = new javax.swing.JCheckBox();
        lblPortscbo = new javax.swing.JLabel();
        lblSpeed = new javax.swing.JLabel();
        cboGPSSpeed = new javax.swing.JComboBox();
        cboGPSSerialPort = new javax.swing.JComboBox();
        pnlButtons = new javax.swing.JPanel();
        bOK = new javax.swing.JButton();
        bCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(410, 289));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        pnlUserData.setLayout(new java.awt.GridBagLayout());

        lblCallsign.setText("Callsign");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlUserData.add(lblCallsign, gridBagConstraints);

        lblLinkto.setText("Link to");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlUserData.add(lblLinkto, gridBagConstraints);

        lblBlocklength.setText("Block length");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlUserData.add(lblBlocklength, gridBagConstraints);

        lblLatitude.setText("Latitude");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlUserData.add(lblLatitude, gridBagConstraints);

        lblLongitude.setText("Longitude");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlUserData.add(lblLongitude, gridBagConstraints);

        lblBeaconQRG.setText("Beacon QRG nr");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        pnlUserData.add(lblBeaconQRG, gridBagConstraints);

        txtCallsign.setMinimumSize(new java.awt.Dimension(150, 27));
        txtCallsign.setPreferredSize(new java.awt.Dimension(150, 27));
        txtCallsign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCallsignActionPerformed(evt);
            }
        });
        txtCallsign.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCallsignFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlUserData.add(txtCallsign, gridBagConstraints);

        txtLinkto.setToolTipText("Enter the callsign of a server to link up with");
        txtLinkto.setMinimumSize(new java.awt.Dimension(150, 27));
        txtLinkto.setPreferredSize(new java.awt.Dimension(150, 27));
        txtLinkto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLinktoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlUserData.add(txtLinkto, gridBagConstraints);

        txtBlocklength.setMinimumSize(new java.awt.Dimension(150, 27));
        txtBlocklength.setPreferredSize(new java.awt.Dimension(150, 27));
        txtBlocklength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBlocklengthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlUserData.add(txtBlocklength, gridBagConstraints);

        txtLongitude.setToolTipText("Enter longitude as decimal degrees without W&E. For instance like -017.0010 (west is negative)");
        txtLongitude.setMinimumSize(new java.awt.Dimension(150, 27));
        txtLongitude.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlUserData.add(txtLongitude, gridBagConstraints);

        txtLatitude.setToolTipText("Enter latitude as decimal degrees without N&S. For instance like -59.0010 (south is negative)");
        txtLatitude.setMinimumSize(new java.awt.Dimension(150, 27));
        txtLatitude.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlUserData.add(txtLatitude, gridBagConstraints);

        spinBeaconMinute.setModel(new SpinnerNumberModel(0, 0, 4, 1));
        spinBeaconMinute.setToolTipText("The minute to transmit to a scanning server, when is it listening at the current fq?");
        spinBeaconMinute.setMinimumSize(new java.awt.Dimension(45, 28));
        spinBeaconMinute.setPreferredSize(new java.awt.Dimension(50, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlUserData.add(spinBeaconMinute, gridBagConstraints);

        tabOptions.addTab("User data", pnlUserData);

        pnlEmail.setLayout(new java.awt.GridBagLayout());

        jLabel7.setText("Pop host");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEmail.add(jLabel7, gridBagConstraints);

        jLabel8.setText("Pop user");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEmail.add(jLabel8, gridBagConstraints);

        jLabel9.setText("Pop password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEmail.add(jLabel9, gridBagConstraints);

        jLabel10.setText("Reply to");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEmail.add(jLabel10, gridBagConstraints);

        txtPophost.setMinimumSize(new java.awt.Dimension(100, 27));
        txtPophost.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlEmail.add(txtPophost, gridBagConstraints);

        txtPopUser.setMinimumSize(new java.awt.Dimension(100, 27));
        txtPopUser.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlEmail.add(txtPopUser, gridBagConstraints);

        txtPopPassword.setMinimumSize(new java.awt.Dimension(100, 27));
        txtPopPassword.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlEmail.add(txtPopPassword, gridBagConstraints);

        txtReplyto.setMinimumSize(new java.awt.Dimension(100, 27));
        txtReplyto.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlEmail.add(txtReplyto, gridBagConstraints);

        ServerUpdateButton.setText("Update server");
        ServerUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServerUpdateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlEmail.add(ServerUpdateButton, gridBagConstraints);

        tabOptions.addTab("Email settings", pnlEmail);

        pnlConfiguration.setLayout(new java.awt.GridBagLayout());

        jLabel12.setText("Log File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlConfiguration.add(jLabel12, gridBagConstraints);

        txtLogFile.setText("client.log");
        txtLogFile.setMinimumSize(new java.awt.Dimension(150, 27));
        txtLogFile.setPreferredSize(new java.awt.Dimension(200, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlConfiguration.add(txtLogFile, gridBagConstraints);

        lblRetries.setText("Max retries");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlConfiguration.add(lblRetries, gridBagConstraints);

        lblIdle.setText("Idle time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlConfiguration.add(lblIdle, gridBagConstraints);

        lblTXdelay.setText("TX delay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlConfiguration.add(lblTXdelay, gridBagConstraints);

        lblOffsetmin.setText("Offset minute");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlConfiguration.add(lblOffsetmin, gridBagConstraints);

        spinRetries.setModel(new SpinnerNumberModel(16, 5, 50, 1));
        spinRetries.setMinimumSize(new java.awt.Dimension(45, 24));
        spinRetries.setPreferredSize(new java.awt.Dimension(50, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlConfiguration.add(spinRetries, gridBagConstraints);

        spinIdleTime.setModel(new SpinnerNumberModel(15, 1, 20, 1));
        spinIdleTime.setMinimumSize(new java.awt.Dimension(45, 24));
        spinIdleTime.setPreferredSize(new java.awt.Dimension(50, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlConfiguration.add(spinIdleTime, gridBagConstraints);

        spinTXdelay.setModel(new SpinnerNumberModel(0, 0, 3, 1));
        spinTXdelay.setMinimumSize(new java.awt.Dimension(45, 24));
        spinTXdelay.setPreferredSize(new java.awt.Dimension(50, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlConfiguration.add(spinTXdelay, gridBagConstraints);

        lblSecond.setText("Second");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlConfiguration.add(lblSecond, gridBagConstraints);

        spinOffsetMinute.setModel(new SpinnerNumberModel(0, 0, 4, 1));
        spinOffsetMinute.setMinimumSize(new java.awt.Dimension(45, 24));
        spinOffsetMinute.setPreferredSize(new java.awt.Dimension(50, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlConfiguration.add(spinOffsetMinute, gridBagConstraints);

        spinOffsetSeconds.setModel(new SpinnerNumberModel(0, 0, 50, 1));
        spinOffsetSeconds.setMinimumSize(new java.awt.Dimension(45, 24));
        spinOffsetSeconds.setPreferredSize(new java.awt.Dimension(50, 24));
        spinOffsetSeconds.setValue(30);
        spinOffsetSeconds.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinOffsetSecondsStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlConfiguration.add(spinOffsetSeconds, gridBagConstraints);
        spinOffsetSeconds.setModel(new SpinnerNumberModel(30, 0, 50, 10));

        DCDSpinner.setModel(new SpinnerNumberModel(2, 0, 10, 1));
        DCDSpinner.setMinimumSize(new java.awt.Dimension(45, 24));
        DCDSpinner.setPreferredSize(new java.awt.Dimension(50, 24));
        DCDSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                DCDSpinnerStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlConfiguration.add(DCDSpinner, gridBagConstraints);
        DCDSpinner.setModel(new SpinnerNumberModel(3,0,9,1));

        lblDCD.setText("DCD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlConfiguration.add(lblDCD, gridBagConstraints);

        tabOptions.addTab("Configuration", pnlConfiguration);

        pnlGPS.setLayout(new java.awt.BorderLayout());

        frGPS.setBorder(javax.swing.BorderFactory.createTitledBorder("GPS settings"));
        frGPS.setLayout(new java.awt.GridBagLayout());

        chkGPSConnection.setText("GPS is connected");
        chkGPSConnection.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkGPSConnectionStateChanged(evt);
            }
        });
        chkGPSConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkGPSConnectionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        frGPS.add(chkGPSConnection, gridBagConstraints);

        lblPortscbo.setText("Serial port");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        frGPS.add(lblPortscbo, gridBagConstraints);

        lblSpeed.setText("Speed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        frGPS.add(lblSpeed, gridBagConstraints);

        cboGPSSpeed.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1200", "2400", "4800", "9600", "19200" }));
        cboGPSSpeed.setMinimumSize(new java.awt.Dimension(150, 27));
        cboGPSSpeed.setPreferredSize(new java.awt.Dimension(150, 27));
        cboGPSSpeed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGPSSpeedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        frGPS.add(cboGPSSpeed, gridBagConstraints);

        cboGPSSerialPort.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboGPSSerialPort.setMinimumSize(new java.awt.Dimension(150, 27));
        cboGPSSerialPort.setPreferredSize(new java.awt.Dimension(150, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        frGPS.add(cboGPSSerialPort, gridBagConstraints);

        pnlGPS.add(frGPS, java.awt.BorderLayout.CENTER);

        tabOptions.addTab("Devices", pnlGPS);

        getContentPane().add(tabOptions);

        pnlButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        bOK.setText("OK");
        bOK.setMaximumSize(new java.awt.Dimension(100, 35));
        bOK.setMinimumSize(new java.awt.Dimension(80, 30));
        bOK.setPreferredSize(new java.awt.Dimension(80, 30));
        bOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOKActionPerformed(evt);
            }
        });
        pnlButtons.add(bOK);

        bCancel.setText("Cancel");
        bCancel.setMaximumSize(new java.awt.Dimension(100, 35));
        bCancel.setMinimumSize(new java.awt.Dimension(80, 30));
        bCancel.setPreferredSize(new java.awt.Dimension(80, 30));
        bCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelActionPerformed(evt);
            }
        });
        pnlButtons.add(bCancel);

        getContentPane().add(pnlButtons);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void txtLinktoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLinktoActionPerformed
// TODO add your handling code here:
    optlogic.txtLinktoActionPerformed(evt);
}//GEN-LAST:event_txtLinktoActionPerformed

private void bOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOKActionPerformed
    // TODO add your handling code here:
    // Update the config object and have it save itself
    optlogic.bOKActionPerformed(evt);
    this.setVisible(false);
}//GEN-LAST:event_bOKActionPerformed

/**
 * Enable or disable the gps controls
 * @param set
 */
private void enablegpscontrols(Boolean set){
    optlogic.enablegpscontrols(set);
}

/**
 * Open the port if its selected and not open before
 */
private void handlegpsupdown(){
    // Take care of GPS settings and enable/disable it
    optlogic.handlegpsupdown();
}

private void txtCallsignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCallsignActionPerformed
    // TODO add your handling code here:
    optlogic.txtCallsignActionPerformed(evt);
}//GEN-LAST:event_txtCallsignActionPerformed

private void txtCallsignFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCallsignFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtCallsignFocusLost

private void bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelActionPerformed
// TODO add your handling code here:
    this.setVisible(false);
}//GEN-LAST:event_bCancelActionPerformed

private void txtBlocklengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBlocklengthActionPerformed
    // TODO add your handling code here:
    optlogic.txtBlocklengthActionPerformed(evt);
}//GEN-LAST:event_txtBlocklengthActionPerformed

private void chkGPSConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkGPSConnectionActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_chkGPSConnectionActionPerformed

private void cboGPSSpeedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGPSSpeedActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_cboGPSSpeedActionPerformed

private void chkGPSConnectionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkGPSConnectionStateChanged
    // Get the checkbox state, too tired to remember how now (need sleep)
    
}//GEN-LAST:event_chkGPSConnectionStateChanged

private void spinOffsetSecondsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinOffsetSecondsStateChanged
    // TODO add your handling code here:
    optlogic.spinOffsetSecondsStateChanged(evt);
}//GEN-LAST:event_spinOffsetSecondsStateChanged

private void DCDSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_DCDSpinnerStateChanged
    // TODO add your handling code here:
    optlogic.DCDSpinnerStateChanged(evt);
}//GEN-LAST:event_DCDSpinnerStateChanged

private void ServerUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ServerUpdateButtonActionPerformed
    // TODO add your handling code here:
    optlogic.ServerUpdateButtonActionPerformed(evt);
}//GEN-LAST:event_ServerUpdateButtonActionPerformed



/**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                optionsdialog dialog = new optionsdialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JSpinner DCDSpinner;
    private javax.swing.JButton ServerUpdateButton;
    private javax.swing.JButton bCancel;
    private javax.swing.JButton bOK;
    private javax.swing.JComboBox cboGPSSerialPort;
    private javax.swing.JComboBox cboGPSSpeed;
    private javax.swing.JCheckBox chkGPSConnection;
    private javax.swing.JPanel frGPS;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lblBeaconQRG;
    private javax.swing.JLabel lblBlocklength;
    private javax.swing.JLabel lblCallsign;
    private javax.swing.JLabel lblDCD;
    private javax.swing.JLabel lblIdle;
    private javax.swing.JLabel lblLatitude;
    private javax.swing.JLabel lblLinkto;
    private javax.swing.JLabel lblLongitude;
    private javax.swing.JLabel lblOffsetmin;
    private javax.swing.JLabel lblPortscbo;
    private javax.swing.JLabel lblRetries;
    private javax.swing.JLabel lblSecond;
    private javax.swing.JLabel lblSpeed;
    private javax.swing.JLabel lblTXdelay;
    private javax.swing.JPanel pnlButtons;
    private javax.swing.JPanel pnlConfiguration;
    private javax.swing.JPanel pnlEmail;
    private javax.swing.JPanel pnlGPS;
    private javax.swing.JPanel pnlUserData;
    private javax.swing.JSpinner spinBeaconMinute;
    private javax.swing.JSpinner spinIdleTime;
    private javax.swing.JSpinner spinOffsetMinute;
    public javax.swing.JSpinner spinOffsetSeconds;
    private javax.swing.JSpinner spinRetries;
    private javax.swing.JSpinner spinTXdelay;
    private javax.swing.JTabbedPane tabOptions;
    private javax.swing.JTextField txtBlocklength;
    private javax.swing.JTextField txtCallsign;
    private javax.swing.JFormattedTextField txtLatitude;
    private javax.swing.JTextField txtLinkto;
    private javax.swing.JTextField txtLogFile;
    private javax.swing.JFormattedTextField txtLongitude;
    private javax.swing.JTextField txtPopPassword;
    private javax.swing.JTextField txtPopUser;
    private javax.swing.JTextField txtPophost;
    private javax.swing.JTextField txtReplyto;
    // End of variables declaration//GEN-END:variables

    public void setNewsgroup(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNewsgroup() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNewsgroupenabled(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNewsgroupenabled() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
