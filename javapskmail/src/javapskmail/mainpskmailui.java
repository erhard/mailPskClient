/*
 * mainpskmailui.java
 *
 * Created on den 25 november 2008, 21:55
 */
package javapskmail;

import java.awt.Color;
import java.io.*;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author  per
 */
public class mainpskmailui extends javax.swing.JFrame implements mainui {

    private WXSetupDialog wxDialog;
    private mainpskmaillogic pskmaillogic;
    private ResourceBundle mainpskmailui;
    private mapmanager mymapmanager;

    /** Creates new form mainpskmailui */
    public mainpskmailui() {

        // creates new logic
        pskmaillogic = new mainpskmaillogic(this);
        mainpskmailui = pskmaillogic.getResources();
        // Map handler
        mymapmanager = new mapmanager();
        mymapmanager.setmapobject(this.jxMapKit);
        initComponents();

        pskmaillogic.mainpskmaillogic_start();
        wxDialog = new WXSetupDialog(pskmaillogic, pskmaillogic.getParentUI().getParentFrame(), true);

    }

    public PosReportMapPanel getMainPosReportMapPanel() {
        return null;
    }

    public void changeTabTo(int index) {
        //no implementation here
    }

    /**
     * Fetch the entire contents of a text file, and return it in a String.
     * This style of implementation does not throw Exceptions to the caller.
     *
     * @param aFile is a file which already exists and can be read.
     */
    public String getContents(File aFile) {
        //...checks on aFile are elided
        StringBuilder contents = new StringBuilder();

        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(aFile));
            try {
                String line = null; //not declared within while loop
        /*
                 * readLine is a bit quirky :
                 * it returns the content of a line MINUS the newline.
                 * it returns null only for the END of the stream.
                 * it returns an empty String if two newlines appear in a row.
                 */
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }
    // delete content of file (used for emptying bulletins)

    public void deleteContent(File file) throws FileNotFoundException, IOException {
        FileOutputStream fos;
        DataOutputStream dos;
        fos = new FileOutputStream(file);
        dos = new DataOutputStream(fos);
        dos.writeChars("");
    }

    public void setStatus(String msg) {
        this.txtStatus.setText(msg);
    }

    public String getStatus() {
        return this.txtStatus.getText();
    }

    public void setLatitude(String msg) {
        this.txtLatitude.setText(msg);
    }

    public String getLatitude() {
        return this.txtLatitude.getText();
    }

    public void setLongitude(String msg) {
        this.txtLongitude.setText(msg);
    }

    public String getLongitude() {
        return this.txtLongitude.getText();
    }

    public void setChkBeacon(boolean state) {
        this.chkBeacon.setSelected(state);
    }

    public boolean getChkBeacon() {
        return this.chkBeacon.isSelected();
    }

    public void setSpnMinute(int i) {
        this.spnMinute.setValue(i);
    }

    public String getSpnMinute() {
        return this.spnMinute.getValue().toString();
    }

    public void removeAllCboServer() {
        this.cboServer.removeAllItems();
    }

    public void addCboServer(String server) {
        this.cboServer.addItem(server);
    }

    public void setSelectedCboServer(String server) {
        this.cboServer.setSelectedItem(server);
    }

    public String getSelectedCboServer() {
        return this.cboServer.getSelectedItem().toString();
    }

    public void setSelectedCboAPRSIcon(String instring) {
        this.cboAPRSIcon.setSelectedItem(instring);
    }

    public void appendFilesTxtArea(String instring) {
        this.FilesTxtArea.append(instring);
    }

    public void setLabelStatus(String instring) {
        this.StatusLabel.setText(instring);
    }

    public void setStatusLabel(String instring) {
        this.lblStatus.setText(instring);
    }

    public void setStatusLabelForeground(Color color) {
        this.lblStatus.setForeground(color);
    }

    public void setProgressBarValue(int n) {
        this.ProgressBar.setValue(n);
    }

    public void setProgressBarStringPainted(boolean b) {
        this.ProgressBar.setStringPainted(b);
    }

    public void setConnectButtonText(String text) {
        this.bConnect.setText(text);
    }

    public void setFileConnectButtonText(String text) {
        this.FileConnect.setText(text);
    }

    public String getBeaconPeriod() {
        return this.cboBeaconPeriod.getSelectedItem().toString();
    }

    public String getAPRSIcon() {
        pskmaillogic.setIcon(cboAPRSIcon.getSelectedItem().toString());
        return cboAPRSIcon.getSelectedItem().toString();
    }

    /**
     *
     * @param instring
     */
    public void setTxtSpeed(String instring) {
        this.txtSpeed.setText(instring);
    }

    public String getSpeed() {
        return txtSpeed.getText();
    }

    /**
     *
     * @param instring
     */
    public void setClock(String instring) {
        jTextField1.setText(instring);
    }

    /**
     * GPS Fix at.
     * @param instring
     */
    public void setFixAt(String instring) {
        this.txtFixTakenAt.setText(instring);
    }

    /**
     * Set the latitude text in the gps ui
     * @param instring
     */
    public void setLatitudeText(String instring) {
        this.txtLatitude.setText(instring);
    }

    /**
     * Set the longitude text in the gps ui
     * @param instring
     */
    public void setLongitudeText(String instring) {
        this.txtLongitude.setText(instring);
    }

    public void setCourseText(String instring) {
        this.txtCourse.setText(instring);
    }

    public void setCourse(String instring) {
        this.txtCourse.setText(instring);
    }

    public void setSpeed(String instring) {
        this.txtSpeed.setText(instring);
    }

    public String getCourse() {
        return this.txtCourse.getText();
    }

    public void setSpeedText(String instring) {
        this.txtSpeed.setText(instring);
    }

    /**
     *
     * @param instring
     */
    public void appendMSGWindow(String instring) {
        txtInMsgs.append(instring);
    }

    /**
     *
     * @param instring
     */
    public void appendTextArea3(String instring) {
        jTextArea3.append(instring);
    }

    /**
     *
     * @param instring
     */
    public void appendMainWindow(String instring) {
        jTextArea1.append(instring);
    }

    public void appendHeadersWindow(String instring) {
        MailHeadersWindow.append(instring);
    }

    public void setDCDColor(Color Color) {
        pnlStatusIndicator.setBackground(Color);
        pnlStatusIndicator.repaint();
    }

    public void addServer(String server) {
        cboServer.addItem(server);
    }

    public void setServer(String server) {
        cboServer.setSelectedItem(server);
    }

    public void debug(String message) {
        System.out.println("Debug:" + message);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public java.awt.Frame getParentFrame() {
        return this;
    }

    public String getTxtMainEntry() {
        return this.txtMainEntry.getText();
    }

    public JButton getFileReadButton() {
        return this.FileReadButton;
    }

    public void setFilesTextArea(String instring) {
        this.FilesTxtArea.setText(instring);
    }

    public void setTxtMainEntry(String instring) {
        txtMainEntry.setText(instring);
    }

    public boolean getMnuMailScanning() {
        return this.mnuMailScanning.isSelected();
    }

    public void setMailHeadersWindow(String instring) {
        this.MailHeadersWindow.setText(instring);
    }

    public JButton getUpdateFileButton() {
        return this.UpdateButton;
    }

    public void setBoardWX(String str) {
        txtWX.setText(str);
    }

    ;

    public String getBoardWX() {
        return txtWX.getText();
    }

    public WXSetupDialog getWXDialog() {
        return wxDialog;
    }

    /**
     * Add a waypoint to the map
     */
    public void addWaypoint(String lat, String lon) {
        mymapmanager.addWaypoint(lat, lon);
    }

    /**
     * Paint the waypoints in the hash table
     */
    public void paintWaypoints() {
        mymapmanager.paintWaypoints();
    }

    public void clearWaypoints() {
        mymapmanager.clearWaypoints();
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

        tabMain = new javax.swing.JTabbedPane();
        tabTerminal = new javax.swing.JPanel();
        pnlTerminalButtons = new javax.swing.JPanel();
        bConnect = new javax.swing.JButton();
        AbortButton = new javax.swing.JButton();
        NewButton = new javax.swing.JButton();
        QTCButtton = new javax.swing.JButton();
        ReadButton = new javax.swing.JButton();
        SendButton = new javax.swing.JButton();
        DeleteButton = new javax.swing.JButton();
        PositButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        tabMailHeaders = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        MailHeadersWindow = new javax.swing.JTextArea();
        tabFiles = new javax.swing.JPanel();
        pnlFilesButtons = new javax.swing.JPanel();
        FileConnect = new javax.swing.JButton();
        FileAbortButton = new javax.swing.JButton();
        FileReadButton = new javax.swing.JButton();
        UpdateButton = new javax.swing.JButton();
        DownloadButton = new javax.swing.JButton();
        ListFilesButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        FilesTxtArea = new javax.swing.JTextArea();
        tabAPRS = new javax.swing.JPanel();
        pnlGPS = new javax.swing.JPanel();
        lblLatitude = new javax.swing.JLabel();
        txtLatitude = new javax.swing.JTextField();
        lblLongitude = new javax.swing.JLabel();
        txtLongitude = new javax.swing.JTextField();
        lblCourse = new javax.swing.JLabel();
        lblSpeed = new javax.swing.JLabel();
        txtCourse = new javax.swing.JTextField();
        txtSpeed = new javax.swing.JTextField();
        chkBeacon = new javax.swing.JCheckBox();
        txtFixTakenAt = new javax.swing.JTextField();
        lblFixat = new javax.swing.JLabel();
        pnlBeacon = new javax.swing.JPanel();
        lblAPRSIcon = new javax.swing.JLabel();
        cboAPRSIcon = new javax.swing.JComboBox();
        lblBeaconPeriod = new javax.swing.JLabel();
        cboBeaconPeriod = new javax.swing.JComboBox();
        lblInMsgs = new javax.swing.JLabel();
        scrInMsgs = new javax.swing.JScrollPane();
        txtInMsgs = new javax.swing.JTextArea();
        lblStatusMsg = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();
        lblWx = new javax.swing.JLabel();
        txtWX = new javax.swing.JTextField();
        bWX = new javax.swing.JButton();
        bPing = new javax.swing.JButton();
        bLink = new javax.swing.JButton();
        bBeacon = new javax.swing.JButton();
        tabMap = new javax.swing.JPanel();
        jxMapKit = new org.jdesktop.swingx.JXMapKit();
        pnlMainEntry = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        txtMainEntry = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();
        pnlStatusIndicator = new javax.swing.JPanel();
        pnlStatus = new javax.swing.JPanel();
        StatusLabel = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        ProgressBar = new javax.swing.JProgressBar();
        spnMinute = new javax.swing.JSpinner();
        cboServer = new javax.swing.JComboBox();
        jMenuBar3 = new javax.swing.JMenuBar();
        mnuFile2 = new javax.swing.JMenu();
        mnuNew2 = new javax.swing.JMenuItem();
        mnuOpenDraft2 = new javax.swing.JMenuItem();
        mnuSaveDraft2 = new javax.swing.JMenuItem();
        mnuClear2 = new javax.swing.JMenu();
        mnuMailQueue2 = new javax.swing.JMenuItem();
        mnuHeaders2 = new javax.swing.JMenuItem();
        mnuBulletins2 = new javax.swing.JMenuItem();
        mnuQuit2 = new javax.swing.JMenuItem();
        mnuEdit2 = new javax.swing.JMenu();
        mnuPreferences2 = new javax.swing.JMenuItem();
        mnuMode2 = new javax.swing.JMenu();
        mnuMailAPRS2 = new javax.swing.JRadioButtonMenuItem();
        mnuMailScanning = new javax.swing.JRadioButtonMenuItem();
        mnuTTY2 = new javax.swing.JRadioButtonMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        mnuPSK63 = new javax.swing.JRadioButtonMenuItem();
        mnuPSK125 = new javax.swing.JRadioButtonMenuItem();
        mnuPSK250 = new javax.swing.JRadioButtonMenuItem();
        mnuMFSK64 = new javax.swing.JRadioButtonMenuItem();
        mnuTHOR22 = new javax.swing.JRadioButtonMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        mnuModeQSY2 = new javax.swing.JMenuItem();
        mnuMbox2 = new javax.swing.JMenu();
        mnuMboxList2 = new javax.swing.JMenuItem();
        mnuMboxRead2 = new javax.swing.JMenuItem();
        mnuMboxDelete2 = new javax.swing.JMenuItem();
        mnuInfo2 = new javax.swing.JMenu();
        menuMessages = new javax.swing.JMenuItem();
        mnuGetTidestations2 = new javax.swing.JMenuItem();
        mnuGetTide2 = new javax.swing.JMenuItem();
        GetGrib = new javax.swing.JMenuItem();
        mnuGetAPRS2 = new javax.swing.JMenuItem();
        mnuGetCamper2 = new javax.swing.JMenuItem();
        mnuGetServerfq2 = new javax.swing.JMenuItem();
        mnuGetPskmailNews2 = new javax.swing.JMenuItem();
        mnuGetWebPages2 = new javax.swing.JMenuItem();
        Twitter = new javax.swing.JMenu();
        Twitter_send = new javax.swing.JMenuItem();
        GetUpdatesmenuItem = new javax.swing.JMenuItem();
        mnuHelpMain2 = new javax.swing.JMenu();
        mnuHelpFile2 = new javax.swing.JMenuItem();
        mnuAbout2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("jPSKMail v.0.3.7"); // NOI18N
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(740, 480));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        tabMain.setMaximumSize(new java.awt.Dimension(1400, 1024));
        tabMain.setMinimumSize(new java.awt.Dimension(725, 290));
        tabMain.setPreferredSize(new java.awt.Dimension(725, 291));

        tabTerminal.setMaximumSize(new java.awt.Dimension(1024, 1024));
        tabTerminal.setMinimumSize(new java.awt.Dimension(708, 306));
        tabTerminal.setLayout(new javax.swing.BoxLayout(tabTerminal, javax.swing.BoxLayout.PAGE_AXIS));

        pnlTerminalButtons.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnlTerminalButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        bConnect.setFont(new java.awt.Font("Metal", 1, 11));
        bConnect.setForeground(new java.awt.Color(0, 102, 51));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("javapskmail/mainpskmailui"); // NOI18N
        bConnect.setText(bundle.getString("mainpskmailui.bConnect.text")); // NOI18N
        bConnect.setMaximumSize(new java.awt.Dimension(100, 35));
        bConnect.setMinimumSize(new java.awt.Dimension(90, 35));
        bConnect.setPreferredSize(new java.awt.Dimension(90, 35));
        bConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bConnectActionPerformed(evt);
            }
        });
        pnlTerminalButtons.add(bConnect);

        AbortButton.setFont(new java.awt.Font("Metal", 1, 11));
        AbortButton.setForeground(new java.awt.Color(0, 102, 0));
        AbortButton.setText(bundle.getString("mainpskmailui.AbortButton.text")); // NOI18N
        AbortButton.setMinimumSize(new java.awt.Dimension(80, 35));
        AbortButton.setPreferredSize(new java.awt.Dimension(80, 35));
        AbortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AbortButtonActionPerformed(evt);
            }
        });
        pnlTerminalButtons.add(AbortButton);

        NewButton.setFont(new java.awt.Font("Metal", 1, 11));
        NewButton.setForeground(new java.awt.Color(0, 102, 0));
        NewButton.setText(bundle.getString("mainpskmailui.NewButton.text")); // NOI18N
        NewButton.setMinimumSize(new java.awt.Dimension(80, 35));
        NewButton.setPreferredSize(new java.awt.Dimension(80, 35));
        NewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewButtonActionPerformed(evt);
            }
        });
        pnlTerminalButtons.add(NewButton);

        QTCButtton.setFont(new java.awt.Font("Metal", 1, 11));
        QTCButtton.setForeground(new java.awt.Color(0, 102, 0));
        QTCButtton.setText(bundle.getString("mainpskmailui.QTCButtton.text")); // NOI18N
        QTCButtton.setMinimumSize(new java.awt.Dimension(80, 35));
        QTCButtton.setPreferredSize(new java.awt.Dimension(80, 35));
        QTCButtton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QTCButttonActionPerformed(evt);
            }
        });
        pnlTerminalButtons.add(QTCButtton);

        ReadButton.setFont(new java.awt.Font("Metal", 1, 11));
        ReadButton.setForeground(new java.awt.Color(0, 102, 0));
        ReadButton.setText(bundle.getString("mainpskmailui.ReadButton.text")); // NOI18N
        ReadButton.setMinimumSize(new java.awt.Dimension(80, 35));
        ReadButton.setPreferredSize(new java.awt.Dimension(80, 35));
        ReadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReadButtonActionPerformed(evt);
            }
        });
        pnlTerminalButtons.add(ReadButton);

        SendButton.setFont(new java.awt.Font("Metal", 1, 11));
        SendButton.setForeground(new java.awt.Color(0, 102, 0));
        SendButton.setText(bundle.getString("mainpskmailui.SendButton.text")); // NOI18N
        SendButton.setFocusPainted(false);
        SendButton.setMinimumSize(new java.awt.Dimension(80, 35));
        SendButton.setPreferredSize(new java.awt.Dimension(80, 35));
        SendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendButtonActionPerformed(evt);
            }
        });
        pnlTerminalButtons.add(SendButton);

        DeleteButton.setFont(new java.awt.Font("Metal", 1, 11));
        DeleteButton.setForeground(new java.awt.Color(0, 102, 0));
        DeleteButton.setText(bundle.getString("mainpskmailui.DeleteButton.text")); // NOI18N
        DeleteButton.setMinimumSize(new java.awt.Dimension(80, 35));
        DeleteButton.setPreferredSize(new java.awt.Dimension(80, 35));
        DeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteButtonActionPerformed(evt);
            }
        });
        pnlTerminalButtons.add(DeleteButton);

        PositButton.setFont(new java.awt.Font("Metal", 1, 11));
        PositButton.setForeground(new java.awt.Color(0, 102, 0));
        PositButton.setText(bundle.getString("mainpskmailui.PositButton.text")); // NOI18N
        PositButton.setMinimumSize(new java.awt.Dimension(80, 35));
        PositButton.setPreferredSize(new java.awt.Dimension(80, 35));
        PositButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PositButtonActionPerformed(evt);
            }
        });
        pnlTerminalButtons.add(PositButton);

        tabTerminal.add(pnlTerminalButtons);

        jTextArea1.setBackground(new java.awt.Color(255, 255, 235));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 12));
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextArea1);

        tabTerminal.add(jScrollPane1);

        tabMain.addTab(mainpskmailui.getString("Terminal"), tabTerminal); // NOI18N

        tabMailHeaders.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        MailHeadersWindow.setBackground(new java.awt.Color(255, 255, 230));
        MailHeadersWindow.setColumns(20);
        MailHeadersWindow.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 11));
        MailHeadersWindow.setRows(5);
        jScrollPane2.setViewportView(MailHeadersWindow);

        jPanel5.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        tabMailHeaders.add(jPanel5, java.awt.BorderLayout.CENTER);

        tabMain.addTab(mainpskmailui.getString("Mail_headers"), tabMailHeaders); // NOI18N

        tabFiles.setLayout(new javax.swing.BoxLayout(tabFiles, javax.swing.BoxLayout.PAGE_AXIS));

        pnlFilesButtons.setMaximumSize(new java.awt.Dimension(1280, 40));
        pnlFilesButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        FileConnect.setFont(new java.awt.Font("Metal", 1, 11));
        FileConnect.setForeground(new java.awt.Color(0, 102, 0));
        FileConnect.setText(bundle.getString("mainpskmailui.FileConnect.text")); // NOI18N
        FileConnect.setMaximumSize(new java.awt.Dimension(100, 35));
        FileConnect.setMinimumSize(new java.awt.Dimension(80, 35));
        FileConnect.setPreferredSize(new java.awt.Dimension(90, 35));
        FileConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileConnectActionPerformed(evt);
            }
        });
        pnlFilesButtons.add(FileConnect);

        FileAbortButton.setFont(new java.awt.Font("Metal", 1, 11));
        FileAbortButton.setForeground(new java.awt.Color(0, 102, 0));
        FileAbortButton.setText(bundle.getString("mainpskmailui.FileAbortButton.text")); // NOI18N
        FileAbortButton.setMaximumSize(new java.awt.Dimension(100, 35));
        FileAbortButton.setMinimumSize(new java.awt.Dimension(80, 35));
        FileAbortButton.setPreferredSize(new java.awt.Dimension(90, 35));
        FileAbortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileAbortButtonActionPerformed(evt);
            }
        });
        pnlFilesButtons.add(FileAbortButton);

        FileReadButton.setFont(new java.awt.Font("Metal", 1, 11));
        FileReadButton.setForeground(new java.awt.Color(0, 102, 0));
        FileReadButton.setText("Read"); // NOI18N
        FileReadButton.setMaximumSize(new java.awt.Dimension(100, 35));
        FileReadButton.setMinimumSize(new java.awt.Dimension(80, 35));
        FileReadButton.setPreferredSize(new java.awt.Dimension(90, 35));
        FileReadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileReadButtonActionPerformed(evt);
            }
        });
        pnlFilesButtons.add(FileReadButton);

        UpdateButton.setFont(new java.awt.Font("Metal", 1, 11));
        UpdateButton.setForeground(new java.awt.Color(0, 102, 0));
        UpdateButton.setText(bundle.getString("mainpskmailui.UpdateButton.text")); // NOI18N
        UpdateButton.setMaximumSize(new java.awt.Dimension(100, 35));
        UpdateButton.setMinimumSize(new java.awt.Dimension(80, 35));
        UpdateButton.setPreferredSize(new java.awt.Dimension(90, 35));
        UpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateButtonActionPerformed(evt);
            }
        });
        pnlFilesButtons.add(UpdateButton);

        DownloadButton.setFont(new java.awt.Font("Metal", 1, 11));
        DownloadButton.setForeground(new java.awt.Color(0, 102, 0));
        DownloadButton.setText(bundle.getString("mainpskmailui.DownloadButton.text")); // NOI18N
        DownloadButton.setMaximumSize(new java.awt.Dimension(100, 35));
        DownloadButton.setMinimumSize(new java.awt.Dimension(90, 35));
        DownloadButton.setPreferredSize(new java.awt.Dimension(100, 35));
        DownloadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DownloadButtonActionPerformed(evt);
            }
        });
        pnlFilesButtons.add(DownloadButton);

        ListFilesButton.setFont(new java.awt.Font("Metal", 1, 11));
        ListFilesButton.setForeground(new java.awt.Color(0, 102, 0));
        ListFilesButton.setText(bundle.getString("mainpskmailui.ListFilesButton.text")); // NOI18N
        ListFilesButton.setMaximumSize(new java.awt.Dimension(100, 35));
        ListFilesButton.setMinimumSize(new java.awt.Dimension(80, 35));
        ListFilesButton.setPreferredSize(new java.awt.Dimension(90, 35));
        ListFilesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ListFilesButtonActionPerformed(evt);
            }
        });
        pnlFilesButtons.add(ListFilesButton);

        tabFiles.add(pnlFilesButtons);

        FilesTxtArea.setBackground(new java.awt.Color(255, 255, 230));
        FilesTxtArea.setColumns(20);
        FilesTxtArea.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 12));
        FilesTxtArea.setRows(5);
        jScrollPane4.setViewportView(FilesTxtArea);

        tabFiles.add(jScrollPane4);

        tabMain.addTab(mainpskmailui.getString("Files"), tabFiles); // NOI18N

        tabAPRS.setMinimumSize(new java.awt.Dimension(670, 260));
        tabAPRS.setPreferredSize(new java.awt.Dimension(680, 260));
        tabAPRS.setLayout(new java.awt.GridBagLayout());

        pnlGPS.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("mainpskmailui.pnlGPS.border.title"))); // NOI18N
        pnlGPS.setMaximumSize(new java.awt.Dimension(250, 800));
        pnlGPS.setMinimumSize(new java.awt.Dimension(240, 250));
        pnlGPS.setPreferredSize(new java.awt.Dimension(240, 250));
        pnlGPS.setLayout(new java.awt.GridBagLayout());

        lblLatitude.setForeground(new java.awt.Color(0, 51, 204));
        lblLatitude.setText(bundle.getString("mainpskmailui.lblLatitude.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlGPS.add(lblLatitude, gridBagConstraints);

        txtLatitude.setBackground(new java.awt.Color(255, 255, 230));
        txtLatitude.setEditable(false);
        txtLatitude.setMinimumSize(new java.awt.Dimension(120, 27));
        txtLatitude.setPreferredSize(new java.awt.Dimension(120, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlGPS.add(txtLatitude, gridBagConstraints);

        lblLongitude.setForeground(new java.awt.Color(0, 51, 204));
        lblLongitude.setText(bundle.getString("mainpskmailui.lblLongitude.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlGPS.add(lblLongitude, gridBagConstraints);

        txtLongitude.setBackground(new java.awt.Color(255, 255, 230));
        txtLongitude.setEditable(false);
        txtLongitude.setMinimumSize(new java.awt.Dimension(120, 27));
        txtLongitude.setPreferredSize(new java.awt.Dimension(120, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlGPS.add(txtLongitude, gridBagConstraints);

        lblCourse.setForeground(new java.awt.Color(0, 51, 204));
        lblCourse.setText(bundle.getString("mainpskmailui.lblCourse.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlGPS.add(lblCourse, gridBagConstraints);

        lblSpeed.setForeground(new java.awt.Color(0, 51, 204));
        lblSpeed.setText(bundle.getString("mainpskmailui.lblSpeed.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlGPS.add(lblSpeed, gridBagConstraints);

        txtCourse.setBackground(new java.awt.Color(255, 255, 230));
        txtCourse.setEditable(false);
        txtCourse.setToolTipText(mainpskmailui.getString("Course_Made_Good,_degrees_true_")); // NOI18N
        txtCourse.setMinimumSize(new java.awt.Dimension(120, 27));
        txtCourse.setPreferredSize(new java.awt.Dimension(120, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlGPS.add(txtCourse, gridBagConstraints);

        txtSpeed.setBackground(new java.awt.Color(255, 255, 230));
        txtSpeed.setEditable(false);
        txtSpeed.setToolTipText(mainpskmailui.getString("Speed_over_ground_in_knots")); // NOI18N
        txtSpeed.setMinimumSize(new java.awt.Dimension(120, 27));
        txtSpeed.setPreferredSize(new java.awt.Dimension(120, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlGPS.add(txtSpeed, gridBagConstraints);

        chkBeacon.setForeground(new java.awt.Color(0, 51, 204));
        chkBeacon.setText(bundle.getString("mainpskmailui.chkBeacon.text")); // NOI18N
        chkBeacon.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkBeaconStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlGPS.add(chkBeacon, gridBagConstraints);

        txtFixTakenAt.setBackground(new java.awt.Color(255, 255, 230));
        txtFixTakenAt.setEditable(false);
        txtFixTakenAt.setToolTipText(mainpskmailui.getString("The_time_in_UTC_the_fix_was_taken")); // NOI18N
        txtFixTakenAt.setMinimumSize(new java.awt.Dimension(120, 27));
        txtFixTakenAt.setPreferredSize(new java.awt.Dimension(120, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlGPS.add(txtFixTakenAt, gridBagConstraints);

        lblFixat.setForeground(new java.awt.Color(0, 51, 204));
        lblFixat.setText(bundle.getString("mainpskmailui.lblFixat.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlGPS.add(lblFixat, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.5;
        tabAPRS.add(pnlGPS, gridBagConstraints);
        pnlGPS.getAccessibleContext().setAccessibleName(bundle.getString("mainpskmailui.pnlGPS.AccessibleContext.accessibleName")); // NOI18N

        pnlBeacon.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("mainpskmailui.pnlBeacon.border.title"))); // NOI18N
        pnlBeacon.setMinimumSize(new java.awt.Dimension(470, 250));
        pnlBeacon.setPreferredSize(new java.awt.Dimension(470, 250));
        pnlBeacon.setLayout(new java.awt.GridBagLayout());

        lblAPRSIcon.setForeground(new java.awt.Color(0, 51, 204));
        lblAPRSIcon.setText(bundle.getString("mainpskmailui.lblAPRSIcon.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlBeacon.add(lblAPRSIcon, gridBagConstraints);

        cboAPRSIcon.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "y", "-", "Y", "s", ">", "U", ";" }));
        cboAPRSIcon.setMinimumSize(new java.awt.Dimension(55, 27));
        cboAPRSIcon.setPreferredSize(new java.awt.Dimension(65, 27));
        cboAPRSIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAPRSIconActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlBeacon.add(cboAPRSIcon, gridBagConstraints);

        lblBeaconPeriod.setForeground(new java.awt.Color(0, 51, 204));
        lblBeaconPeriod.setText(bundle.getString("mainpskmailui.lblBeaconPeriod.text")); // NOI18N
        pnlBeacon.add(lblBeaconPeriod, new java.awt.GridBagConstraints());

        cboBeaconPeriod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10", "30", "60" }));
        cboBeaconPeriod.setMinimumSize(new java.awt.Dimension(60, 27));
        cboBeaconPeriod.setPreferredSize(new java.awt.Dimension(60, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlBeacon.add(cboBeaconPeriod, gridBagConstraints);

        lblInMsgs.setForeground(new java.awt.Color(0, 51, 204));
        lblInMsgs.setText(bundle.getString("mainpskmailui.lblInMsgs.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlBeacon.add(lblInMsgs, gridBagConstraints);

        scrInMsgs.setMinimumSize(new java.awt.Dimension(225, 120));
        scrInMsgs.setPreferredSize(new java.awt.Dimension(225, 120));

        txtInMsgs.setBackground(new java.awt.Color(255, 255, 230));
        txtInMsgs.setColumns(20);
        txtInMsgs.setFont(new java.awt.Font("Dialog", 0, 10));
        txtInMsgs.setRows(200);
        txtInMsgs.setWrapStyleWord(true);
        txtInMsgs.setMinimumSize(new java.awt.Dimension(200, 90));
        scrInMsgs.setViewportView(txtInMsgs);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlBeacon.add(scrInMsgs, gridBagConstraints);

        lblStatusMsg.setForeground(new java.awt.Color(0, 51, 204));
        lblStatusMsg.setText(bundle.getString("mainpskmailui.lblStatusMsg.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlBeacon.add(lblStatusMsg, gridBagConstraints);

        txtStatus.setMinimumSize(new java.awt.Dimension(210, 24));
        txtStatus.setPreferredSize(new java.awt.Dimension(210, 27));
        txtStatus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtStatusKeyReleased(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 6, 5);
        pnlBeacon.add(txtStatus, gridBagConstraints);

        lblWx.setForeground(new java.awt.Color(0, 51, 204));
        lblWx.setText(bundle.getString("mainpskmailui.lblWx.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlBeacon.add(lblWx, gridBagConstraints);

        txtWX.setMinimumSize(new java.awt.Dimension(225, 24));
        txtWX.setPreferredSize(new java.awt.Dimension(225, 27));
        txtWX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWXActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlBeacon.add(txtWX, gridBagConstraints);

        bWX.setText(" WX "); // NOI18N
        bWX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bWXActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        pnlBeacon.add(bWX, gridBagConstraints);

        bPing.setFont(new java.awt.Font("Metal", 1, 11));
        bPing.setForeground(new java.awt.Color(0, 102, 0));
        bPing.setText(bundle.getString("mainpskmailui.bPing.text")); // NOI18N
        bPing.setMinimumSize(new java.awt.Dimension(90, 29));
        bPing.setPreferredSize(new java.awt.Dimension(90, 29));
        bPing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPingActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        pnlBeacon.add(bPing, gridBagConstraints);

        bLink.setFont(new java.awt.Font("Metal", 1, 11));
        bLink.setForeground(new java.awt.Color(0, 102, 0));
        bLink.setText(bundle.getString("mainpskmailui.bLink.text")); // NOI18N
        bLink.setMinimumSize(new java.awt.Dimension(90, 29));
        bLink.setPreferredSize(new java.awt.Dimension(90, 29));
        bLink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bLinkActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        pnlBeacon.add(bLink, gridBagConstraints);

        bBeacon.setFont(new java.awt.Font("Metal", 1, 11));
        bBeacon.setForeground(new java.awt.Color(0, 102, 0));
        bBeacon.setText(bundle.getString("mainpskmailui.bBeacon.text")); // NOI18N
        bBeacon.setMinimumSize(new java.awt.Dimension(90, 29));
        bBeacon.setPreferredSize(new java.awt.Dimension(90, 29));
        bBeacon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBeaconActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlBeacon.add(bBeacon, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        tabAPRS.add(pnlBeacon, gridBagConstraints);
        pnlBeacon.getAccessibleContext().setAccessibleName(bundle.getString("mainpskmailui.pnlBeacon.AccessibleContext.accessibleName")); // NOI18N

        tabMain.addTab(bundle.getString("mainpskmailui.tabAPRS.TabConstraints.tabTitle"), tabAPRS); // NOI18N

        jxMapKit.setDefaultProvider(org.jdesktop.swingx.JXMapKit.DefaultProviders.OpenStreetMaps);
        jxMapKit.setPreferredSize(new java.awt.Dimension(705, 235));
        tabMap.add(jxMapKit);

        tabMain.addTab(bundle.getString("mainpskmailui.tabMap.TabConstraints.tabTitle"), tabMap); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(tabMain, gridBagConstraints);

        pnlMainEntry.setMaximumSize(new java.awt.Dimension(1400, 900));
        pnlMainEntry.setMinimumSize(new java.awt.Dimension(710, 149));
        pnlMainEntry.setPreferredSize(new java.awt.Dimension(710, 149));
        pnlMainEntry.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setMaximumSize(new java.awt.Dimension(1400, 410));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(700, 100));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(700, 200));

        jTextArea3.setBackground(new java.awt.Color(255, 255, 234));
        jTextArea3.setColumns(20);
        jTextArea3.setFont(new java.awt.Font("FreeMono", 1, 12));
        jTextArea3.setForeground(new java.awt.Color(0, 51, 153));
        jTextArea3.setLineWrap(true);
        jTextArea3.setRows(10000);
        jTextArea3.setMaximumSize(new java.awt.Dimension(1400, 400));
        jTextArea3.setMinimumSize(new java.awt.Dimension(650, 100));
        jScrollPane3.setViewportView(jTextArea3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 5, 1);
        pnlMainEntry.add(jScrollPane3, gridBagConstraints);

        txtMainEntry.setMaximumSize(new java.awt.Dimension(1400, 27));
        txtMainEntry.setMinimumSize(new java.awt.Dimension(400, 27));
        txtMainEntry.setPreferredSize(new java.awt.Dimension(400, 27));
        txtMainEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMainEntryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlMainEntry.add(txtMainEntry, gridBagConstraints);

        lblStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStatus.setText("Listening"); // NOI18N
        lblStatus.setMaximumSize(new java.awt.Dimension(120, 17));
        lblStatus.setMinimumSize(new java.awt.Dimension(95, 17));
        lblStatus.setPreferredSize(new java.awt.Dimension(100, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 1);
        pnlMainEntry.add(lblStatus, gridBagConstraints);

        pnlStatusIndicator.setBackground(new java.awt.Color(255, 255, 255));
        pnlStatusIndicator.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlStatusIndicator.setMaximumSize(new java.awt.Dimension(15, 15));
        pnlStatusIndicator.setMinimumSize(new java.awt.Dimension(15, 15));
        pnlStatusIndicator.setPreferredSize(new java.awt.Dimension(15, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        pnlMainEntry.add(pnlStatusIndicator, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(pnlMainEntry, gridBagConstraints);

        pnlStatus.setMaximumSize(new java.awt.Dimension(1500, 30));
        pnlStatus.setMinimumSize(new java.awt.Dimension(400, 30));
        pnlStatus.setPreferredSize(new java.awt.Dimension(710, 30));
        pnlStatus.setLayout(new java.awt.GridBagLayout());

        StatusLabel.setMaximumSize(new java.awt.Dimension(1400, 25));
        StatusLabel.setMinimumSize(new java.awt.Dimension(320, 20));
        StatusLabel.setPreferredSize(new java.awt.Dimension(680, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        pnlStatus.add(StatusLabel, gridBagConstraints);

        jTextField1.setBackground(new java.awt.Color(230, 255, 230));
        jTextField1.setFont(new java.awt.Font("SansSerif", 0, 12));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setMaximumSize(new java.awt.Dimension(100, 26));
        jTextField1.setMinimumSize(new java.awt.Dimension(100, 26));
        jTextField1.setPreferredSize(new java.awt.Dimension(100, 27));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 2, 3);
        pnlStatus.add(jTextField1, gridBagConstraints);

        ProgressBar.setMaximumSize(new java.awt.Dimension(120, 20));
        ProgressBar.setMinimumSize(new java.awt.Dimension(100, 20));
        ProgressBar.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 2, 6);
        pnlStatus.add(ProgressBar, gridBagConstraints);

        spnMinute.setModel(new SpinnerNumberModel(0,0,4,1));
        spnMinute.setToolTipText(mainpskmailui.getString("During_what_minute_(0-4)_will_the_server_listen_and_the_client_transmit?")); // NOI18N
        spnMinute.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnMinuteStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        pnlStatus.add(spnMinute, gridBagConstraints);

        cboServer.setEditable(true);
        cboServer.setMinimumSize(new java.awt.Dimension(130, 27));
        cboServer.setPreferredSize(new java.awt.Dimension(150, 27));
        cboServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboServerActionPerformed(evt);
            }
        });
        cboServer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboServerFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        pnlStatus.add(cboServer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(pnlStatus, gridBagConstraints);

        mnuFile2.setText(bundle.getString("mainpskmailui.mnuFile2.text")); // NOI18N

        mnuNew2.setText(bundle.getString("mainpskmailui.mnuNew2.text")); // NOI18N
        mnuNew2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNewActionPerformed(evt);
            }
        });
        mnuFile2.add(mnuNew2);

        mnuOpenDraft2.setText(bundle.getString("mainpskmailui.mnuOpenDraft2.text")); // NOI18N
        mnuOpenDraft2.setEnabled(false);
        mnuFile2.add(mnuOpenDraft2);

        mnuSaveDraft2.setText(bundle.getString("mainpskmailui.mnuSaveDraft2.text")); // NOI18N
        mnuSaveDraft2.setEnabled(false);
        mnuFile2.add(mnuSaveDraft2);

        mnuClear2.setText(bundle.getString("mainpskmailui.mnuClear2.text")); // NOI18N

        mnuMailQueue2.setText(bundle.getString("mainpskmailui.mnuMailQueue2.text")); // NOI18N
        mnuMailQueue2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMailQueueActionPerformed(evt);
            }
        });
        mnuClear2.add(mnuMailQueue2);

        mnuHeaders2.setText(bundle.getString("mainpskmailui.mnuHeaders2.text")); // NOI18N
        mnuHeaders2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHeadersActionPerformed(evt);
            }
        });
        mnuClear2.add(mnuHeaders2);

        mnuBulletins2.setText(bundle.getString("mainpskmailui.mnuBulletins2.text")); // NOI18N
        mnuBulletins2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuBulletinsActionPerformed(evt);
            }
        });
        mnuClear2.add(mnuBulletins2);

        mnuFile2.add(mnuClear2);

        mnuQuit2.setText(bundle.getString("mainpskmailui.mnuQuit2.text")); // NOI18N
        mnuQuit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuQuitActionPerformed(evt);
            }
        });
        mnuFile2.add(mnuQuit2);

        jMenuBar3.add(mnuFile2);

        mnuEdit2.setText(bundle.getString("mainpskmailui.mnuEdit2.text")); // NOI18N

        mnuPreferences2.setText(bundle.getString("mainpskmailui.mnuPreferences2.text")); // NOI18N
        mnuPreferences2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPreferencesActionPerformed(evt);
            }
        });
        mnuEdit2.add(mnuPreferences2);

        jMenuBar3.add(mnuEdit2);

        mnuMode2.setText(bundle.getString("mainpskmailui.mnuMode2.text")); // NOI18N

        mnuMailAPRS2.setSelected(true);
        mnuMailAPRS2.setText(bundle.getString("mainpskmailui.mnuMailAPRS2.text")); // NOI18N
        mnuMailAPRS2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMailAPRS2ActionPerformed(evt);
            }
        });
        mnuMode2.add(mnuMailAPRS2);

        mnuMailScanning.setText(bundle.getString("mainpskmailui.mnuMailScanning.text")); // NOI18N
        mnuMailScanning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMailScanningActionPerformed(evt);
            }
        });
        mnuMode2.add(mnuMailScanning);

        mnuTTY2.setText(bundle.getString("mainpskmailui.mnuTTY2.text")); // NOI18N
        mnuTTY2.setEnabled(false);
        mnuTTY2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTTY2ActionPerformed(evt);
            }
        });
        mnuMode2.add(mnuTTY2);
        mnuMode2.add(jSeparator6);

        mnuPSK63.setText("PSK63"); // NOI18N
        mnuPSK63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPSK63ActionPerformed(evt);
            }
        });
        mnuMode2.add(mnuPSK63);

        mnuPSK125.setText("PSK125"); // NOI18N
        mnuPSK125.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPSK125ActionPerformed(evt);
            }
        });
        mnuMode2.add(mnuPSK125);

        mnuPSK250.setSelected(true);
        mnuPSK250.setText("PSK250"); // NOI18N
        mnuPSK250.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuPSK250ActionPerformed(evt);
            }
        });
        mnuMode2.add(mnuPSK250);

        mnuMFSK64.setText("MFSK64"); // NOI18N
        mnuMFSK64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMFSK64ActionPerformed(evt);
            }
        });
        mnuMode2.add(mnuMFSK64);

        mnuTHOR22.setText("THOR22"); // NOI18N
        mnuTHOR22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTHOR22ActionPerformed(evt);
            }
        });
        mnuMode2.add(mnuTHOR22);
        mnuMode2.add(jSeparator7);

        mnuModeQSY2.setText(bundle.getString("mainpskmailui.mnuModeQSY2.text")); // NOI18N
        mnuModeQSY2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuModeQSYActionPerformed(evt);
            }
        });
        mnuMode2.add(mnuModeQSY2);

        jMenuBar3.add(mnuMode2);

        mnuMbox2.setText(bundle.getString("mainpskmailui.mnuMbox2.text")); // NOI18N

        mnuMboxList2.setText(bundle.getString("mainpskmailui.mnuMboxList2.text")); // NOI18N
        mnuMboxList2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMboxListActionPerformed(evt);
            }
        });
        mnuMbox2.add(mnuMboxList2);

        mnuMboxRead2.setText(bundle.getString("mainpskmailui.mnuMboxRead2.text")); // NOI18N
        mnuMboxRead2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMboxReadActionPerformed(evt);
            }
        });
        mnuMbox2.add(mnuMboxRead2);

        mnuMboxDelete2.setText(bundle.getString("mainpskmailui.mnuMboxDelete2.text")); // NOI18N
        mnuMboxDelete2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMboxDeleteActionPerformed(evt);
            }
        });
        mnuMbox2.add(mnuMboxDelete2);

        jMenuBar3.add(mnuMbox2);

        mnuInfo2.setText(bundle.getString("mainpskmailui.mnuInfo2.text")); // NOI18N

        menuMessages.setText(mainpskmailui.getString("Get_Messages")); // NOI18N
        menuMessages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMessagesActionPerformed(evt);
            }
        });
        mnuInfo2.add(menuMessages);

        mnuGetTidestations2.setText(bundle.getString("mainpskmailui.mnuGetTidestations2.text")); // NOI18N
        mnuGetTidestations2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGetTidestationsActionPerformed(evt);
            }
        });
        mnuInfo2.add(mnuGetTidestations2);

        mnuGetTide2.setText(bundle.getString("mainpskmailui.mnuGetTide2.text")); // NOI18N
        mnuGetTide2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGetTideActionPerformed(evt);
            }
        });
        mnuInfo2.add(mnuGetTide2);

        GetGrib.setText(mainpskmailui.getString("Get_Grib_file")); // NOI18N
        GetGrib.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetGribActionPerformed(evt);
            }
        });
        mnuInfo2.add(GetGrib);

        mnuGetAPRS2.setText(bundle.getString("mainpskmailui.mnuGetAPRS2.text")); // NOI18N
        mnuGetAPRS2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGetAPRSActionPerformed(evt);
            }
        });
        mnuInfo2.add(mnuGetAPRS2);

        mnuGetCamper2.setText(bundle.getString("mainpskmailui.mnuGetCamper2.text")); // NOI18N
        mnuGetCamper2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGetCamperActionPerformed(evt);
            }
        });
        mnuInfo2.add(mnuGetCamper2);

        mnuGetServerfq2.setText(bundle.getString("mainpskmailui.mnuGetServerfq2.text")); // NOI18N
        mnuGetServerfq2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGetServerfqActionPerformed(evt);
            }
        });
        mnuInfo2.add(mnuGetServerfq2);

        mnuGetPskmailNews2.setText(bundle.getString("mainpskmailui.mnuGetPskmailNews2.text")); // NOI18N
        mnuGetPskmailNews2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGetPskmailNewsActionPerformed(evt);
            }
        });
        mnuInfo2.add(mnuGetPskmailNews2);

        mnuGetWebPages2.setText(bundle.getString("mainpskmailui.mnuGetWebPages2.text")); // NOI18N
        mnuGetWebPages2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuGetWebPagesActionPerformed(evt);
            }
        });
        mnuInfo2.add(mnuGetWebPages2);

        jMenuBar3.add(mnuInfo2);

        Twitter.setText(mainpskmailui.getString("Twitter")); // NOI18N
        Twitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TwitterActionPerformed(evt);
            }
        });

        Twitter_send.setText(mainpskmailui.getString("Send_msg.")); // NOI18N
        Twitter_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Twitter_sendActionPerformed(evt);
            }
        });
        Twitter.add(Twitter_send);

        GetUpdatesmenuItem.setText(bundle.getString("mainpskmailui.GetUpdatesmenuItem.text")); // NOI18N
        GetUpdatesmenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GetUpdatesmenuItemActionPerformed(evt);
            }
        });
        Twitter.add(GetUpdatesmenuItem);

        jMenuBar3.add(Twitter);

        mnuHelpMain2.setText(bundle.getString("mainpskmailui.mnuHelpMain2.text")); // NOI18N

        mnuHelpFile2.setText(bundle.getString("mainpskmailui.mnuHelpFile2.text")); // NOI18N
        mnuHelpFile2.setEnabled(false);
        mnuHelpMain2.add(mnuHelpFile2);

        mnuAbout2.setText(bundle.getString("mainpskmailui.mnuAbout2.text")); // NOI18N
        mnuAbout2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAboutActionPerformed(evt);
            }
        });
        mnuHelpMain2.add(mnuAbout2);

        jMenuBar3.add(mnuHelpMain2);

        setJMenuBar(jMenuBar3);

        getAccessibleContext().setAccessibleName("javaPskMail"); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void mnuQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuQuitActionPerformed
// TODO add your handling code here:
    pskmaillogic.mnuQuitActionPerformed(evt);
}//GEN-LAST:event_mnuQuitActionPerformed

private void mnuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAboutActionPerformed
        try{//GEN-LAST:event_mnuAboutActionPerformed
            //aboutform about = new aboutform();
            //about.setLocationRelativeTo(null);
            //about.setVisible(true);
        } catch (Exception ex) {
            //Main.log.writelog(mainpskmailui.getString("Error_when_handling_about_window."), ex, true);
        }
        pskmaillogic.mnuAboutActionPerformed(evt);
    }

private void bWXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bWXActionPerformed
// TODO add your handling code here:
    pskmaillogic.bWXActionPerformed(evt);
}//GEN-LAST:event_bWXActionPerformed

private void mnuPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPreferencesActionPerformed
// TODO add your handling code here:
    pskmaillogic.mnuPreferencesActionPerformed(evt);
}//GEN-LAST:event_mnuPreferencesActionPerformed

private void txtMainEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMainEntryActionPerformed
// TODO add your handling code here:
    pskmaillogic.txtMainEntryActionPerformed(evt);
}//GEN-LAST:event_txtMainEntryActionPerformed

private void bPingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPingActionPerformed
// TODO add your handling code here:
    pskmaillogic.bPingActionPerformed(evt);
}//GEN-LAST:event_bPingActionPerformed

private void bLinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLinkActionPerformed
    // TODO add your handling code here:
    pskmaillogic.bLinkActionPerformed(evt);
}//GEN-LAST:event_bLinkActionPerformed

private void bBeaconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBeaconActionPerformed
    // TODO add your handling code here:
    pskmaillogic.bBeaconActionPerformed(evt);
}//GEN-LAST:event_bBeaconActionPerformed

private void PositButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PositButtonActionPerformed
    // TODO add your handling code here:
    pskmaillogic.PositButtonActionPerformed(evt);
}//GEN-LAST:event_PositButtonActionPerformed

private void txtStatusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStatusKeyReleased
// TODO add your handling code here:
    pskmaillogic.txtStatusKeyReleased(evt);
}//GEN-LAST:event_txtStatusKeyReleased

private void cboAPRSIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAPRSIconActionPerformed
    // TODO add your handling code here:
    pskmaillogic.cboAPRSIconActionPerformed(evt);
}//GEN-LAST:event_cboAPRSIconActionPerformed

private void chkBeaconStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkBeaconStateChanged
    // TODO add your handling code here:

    if (this.chkBeacon.isSelected()){//GEN-LAST:event_chkBeaconStateChanged
        }
        pskmaillogic.chkBeaconStateChanged(evt);
    }

private void FileReadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileReadButtonActionPerformed
// TODO add your handling code here:
    pskmaillogic.FileReadButtonActionPerformed(evt);
}//GEN-LAST:event_FileReadButtonActionPerformed

private void mnuBulletinsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuBulletinsActionPerformed
    File fb = null;
    try {
        if (false)
            deleteContent(fb);//GEN-LAST:event_mnuBulletinsActionPerformed
        } catch (Exception e) {
        }
        pskmaillogic.mnuBulletinsActionPerformed(evt);
    }
private void AbortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbortButtonActionPerformed
    // TODO add your handling code here:
    pskmaillogic.AbortButtonActionPerformed(evt);
}//GEN-LAST:event_AbortButtonActionPerformed

            private void bConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bConnectActionPerformed
                // TODO add your handling code here:
                pskmaillogic.bConnectActionPerformed(evt);
            }//GEN-LAST:event_bConnectActionPerformed

            private void ReadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReadButtonActionPerformed
                // TODO add your handling code here:
                pskmaillogic.ReadButtonActionPerformed(evt);
}//GEN-LAST:event_ReadButtonActionPerformed

            private void QTCButttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QTCButttonActionPerformed
                // TODO add your handling code here:
                pskmaillogic.QTCButttonActionPerformed(evt);
            }//GEN-LAST:event_QTCButttonActionPerformed

            private void mnuGetTidestationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGetTidestationsActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuGetTidestationsActionPerformed(evt);
            }//GEN-LAST:event_mnuGetTidestationsActionPerformed

            private void mnuGetTideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGetTideActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuGetTideActionPerformed(evt);
            }//GEN-LAST:event_mnuGetTideActionPerformed

            private void mnuGetAPRSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGetAPRSActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuGetAPRSActionPerformed(evt);
            };//GEN-LAST:event_mnuGetAPRSActionPerformed

            private void mnuGetServerfqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGetServerfqActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuGetServerfqActionPerformed(evt);
            }//GEN-LAST:event_mnuGetServerfqActionPerformed

            private void mnuGetPskmailNewsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGetPskmailNewsActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuGetPskmailNewsActionPerformed(evt);
            }//GEN-LAST:event_mnuGetPskmailNewsActionPerformed

            private void mnuHeadersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHeadersActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuHeadersActionPerformed(evt);
            }//GEN-LAST:event_mnuHeadersActionPerformed

            private void NewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewButtonActionPerformed
                pskmaillogic.NewButtonActionPerformed(evt);
}//GEN-LAST:event_NewButtonActionPerformed

            private void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendButtonActionPerformed
                // TODO add your handling code here
                pskmaillogic.SendButtonActionPerformed(evt);
}//GEN-LAST:event_SendButtonActionPerformed

            private void DeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteButtonActionPerformed
                // TODO add your handling code here:
                pskmaillogic.DeleteButtonActionPerformed(evt);
}//GEN-LAST:event_DeleteButtonActionPerformed

            private void ListFilesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ListFilesButtonActionPerformed
                // TODO add your handling code here:
                pskmaillogic.ListFilesButtonActionPerformed(evt);
            }//GEN-LAST:event_ListFilesButtonActionPerformed

            private void DownloadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DownloadButtonActionPerformed
                // TODO add your handling code here:
                pskmaillogic.DownloadButtonActionPerformed(evt);
            }//GEN-LAST:event_DownloadButtonActionPerformed

            private void FileConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileConnectActionPerformed
                // TODO add your handling code here:
                pskmaillogic.FileConnectActionPerformed(evt);
            }//GEN-LAST:event_FileConnectActionPerformed

            private void FileAbortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileAbortButtonActionPerformed
                // TODO add your handling code here:
                pskmaillogic.FileAbortButtonActionPerformed(evt);
            }//GEN-LAST:event_FileAbortButtonActionPerformed

            private void UpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateButtonActionPerformed
                // TODO add your handling code here:
                pskmaillogic.UpdateButtonActionPerformed(evt);
            }//GEN-LAST:event_UpdateButtonActionPerformed

            private void mnuMboxListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMboxListActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuMboxListActionPerformed(evt);
            }//GEN-LAST:event_mnuMboxListActionPerformed

            private void mnuMboxReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMboxReadActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuMboxReadActionPerformed(evt);
            }//GEN-LAST:event_mnuMboxReadActionPerformed

            private void mnuMboxDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMboxDeleteActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuMboxDeleteActionPerformed(evt);
            }//GEN-LAST:event_mnuMboxDeleteActionPerformed

            private void mnuGetCamperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGetCamperActionPerformed
                // TODO add your handling code here:
                pskmaillogic.mnuGetCamperActionPerformed(evt);
            }//GEN-LAST:event_mnuGetCamperActionPerformed

            private void mnuGetWebPagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuGetWebPagesActionPerformed
                pskmaillogic.mnuGetWebPagesActionPerformed(evt);
            }//GEN-LAST:event_mnuGetWebPagesActionPerformed

    /**
     * Make sure the mode buttons are set properly
     * @param what
     * 0 = Mail/APRS
     * 1 = MAIL/Scanning
     * 2 = TTY
     */
    public void updatemailscanset(Integer what) {
        switch (what) {
            case 0: // Mail/ APRS
                this.mnuMailAPRS2.setSelected(true);
                this.mnuMailScanning.setSelected(false);
                this.mnuTTY2.setSelected(false);
                break;
            case 1: // Mail / Scanning
                this.mnuMailAPRS2.setSelected(false);
                this.mnuMailScanning.setSelected(true);
                this.mnuTTY2.setSelected(false);
                break;
            case 2: // TTY
                this.mnuMailAPRS2.setSelected(false);
                this.mnuMailScanning.setSelected(false);
                this.mnuTTY2.setSelected(true);
                break;
        }
    }

    /**
     * Update the mode menus, just make the right one selected
     * @param mymode
     */
    public void updatemodeset(modemmodeenum mymode) {
        switch (mymode) {
            case PSK63:
                mnuPSK63.setSelected(true);
                mnuPSK125.setSelected(false);
                mnuPSK250.setSelected(false);
                mnuMFSK64.setSelected(false);
                mnuTHOR22.setSelected(false);
                break;
            case PSK125:
                mnuPSK63.setSelected(false);
                mnuPSK125.setSelected(true);
                mnuPSK250.setSelected(false);
                mnuMFSK64.setSelected(false);
                mnuTHOR22.setSelected(false);
                break;
            case PSK250:
                mnuPSK63.setSelected(false);
                mnuPSK125.setSelected(false);
                mnuPSK250.setSelected(true);
                mnuMFSK64.setSelected(false);
                mnuTHOR22.setSelected(false);
                break;
            case MFSK64:
                mnuPSK63.setSelected(false);
                mnuPSK125.setSelected(false);
                mnuPSK250.setSelected(false);
                mnuMFSK64.setSelected(true);
                mnuTHOR22.setSelected(false);
                break;
            case THOR22:
                mnuPSK63.setSelected(false);
                mnuPSK125.setSelected(false);
                mnuPSK250.setSelected(false);
                mnuMFSK64.setSelected(false);
                mnuTHOR22.setSelected(true);
                break;
        }

    }

private void mnuPSK63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPSK63ActionPerformed
    pskmaillogic.mnuPSK63ActionPerformed(evt);
}//GEN-LAST:event_mnuPSK63ActionPerformed

private void mnuPSK125ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPSK125ActionPerformed
    pskmaillogic.mnuPSK125ActionPerformed(evt);
}//GEN-LAST:event_mnuPSK125ActionPerformed

private void mnuPSK250ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuPSK250ActionPerformed
    pskmaillogic.mnuPSK250ActionPerformed(evt);
}//GEN-LAST:event_mnuPSK250ActionPerformed

private void mnuMFSK64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMFSK64ActionPerformed
    pskmaillogic.mnuMFSK64ActionPerformed(evt);
}//GEN-LAST:event_mnuMFSK64ActionPerformed

private void mnuTHOR22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTHOR22ActionPerformed
    pskmaillogic.mnuTHOR22ActionPerformed(evt);
}//GEN-LAST:event_mnuTHOR22ActionPerformed

private void mnuModeQSYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuModeQSYActionPerformed
    // TODO add your handling code here:
    pskmaillogic.mnuModeQSYActionPerformed(evt);
}//GEN-LAST:event_mnuModeQSYActionPerformed

private void mnuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNewActionPerformed
    // TODO add your handling code here:
    pskmaillogic.mnuNewActionPerformed(evt);
}//GEN-LAST:event_mnuNewActionPerformed

private void mnuMailQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMailQueueActionPerformed
    // TODO add your handling code here:
    pskmaillogic.mnuMailQueueActionPerformed(evt);
}//GEN-LAST:event_mnuMailQueueActionPerformed

private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
// TODO add your handling code here:
    pskmaillogic.jTextField1ActionPerformed(evt);
}//GEN-LAST:event_jTextField1ActionPerformed

private void menuMessagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuMessagesActionPerformed
    // TODO add your handling code here:
    pskmaillogic.menuMessagesActionPerformed(evt);
}//GEN-LAST:event_menuMessagesActionPerformed

private void mnuMailAPRS2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMailAPRS2ActionPerformed
    // Set the menu selected
    pskmaillogic.mnuMailAPRS2ActionPerformed(evt);
}//GEN-LAST:event_mnuMailAPRS2ActionPerformed

private void mnuMailScanningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMailScanningActionPerformed
    pskmaillogic.mnuMailScanningActionPerformed(evt);
}//GEN-LAST:event_mnuMailScanningActionPerformed

private void mnuTTY2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTTY2ActionPerformed
    pskmaillogic.mnuTTY2ActionPerformed(evt);
}//GEN-LAST:event_mnuTTY2ActionPerformed

private void GetGribActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetGribActionPerformed
    // TODO add your handling code here:
    pskmaillogic.GetGribActionPerformed(evt);
}//GEN-LAST:event_GetGribActionPerformed

private void TwitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TwitterActionPerformed
    // TODO add your handling code here:
    pskmaillogic.TwitterActionPerformed(evt);
}//GEN-LAST:event_TwitterActionPerformed

private void Twitter_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Twitter_sendActionPerformed
    pskmaillogic.Twitter_sendActionPerformed(evt);
}//GEN-LAST:event_Twitter_sendActionPerformed
    /**
     * Store the minute setting if changed by the user
     * @param evt
     */
private void spnMinuteStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnMinuteStateChanged
    pskmaillogic.spnMinuteStateChanged(evt);
}//GEN-LAST:event_spnMinuteStateChanged

private void GetUpdatesmenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GetUpdatesmenuItemActionPerformed
    // TODO add your handling code here:
    pskmaillogic.GetUpdatesmenuItemActionPerformed(evt);
}//GEN-LAST:event_GetUpdatesmenuItemActionPerformed

private void cboServerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboServerFocusLost
    pskmaillogic.cboServerFocusLost(evt);
}//GEN-LAST:event_cboServerFocusLost

    /**
     * User wrote in the server combo, lets handle that shall we..
     * @param evt
     */
private void cboServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboServerActionPerformed
    pskmaillogic.cboServerActionPerformed(evt);
}//GEN-LAST:event_cboServerActionPerformed

private void txtWXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWXActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_txtWXActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new mainpskmailui().setVisible(true);
            }
        });
    }
    private javax.swing.ButtonGroup buttonGroup2;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AbortButton;
    private javax.swing.JButton DeleteButton;
    private javax.swing.JButton DownloadButton;
    private javax.swing.JButton FileAbortButton;
    private javax.swing.JButton FileConnect;
    private javax.swing.JButton FileReadButton;
    private javax.swing.JTextArea FilesTxtArea;
    private javax.swing.JMenuItem GetGrib;
    private javax.swing.JMenuItem GetUpdatesmenuItem;
    private javax.swing.JButton ListFilesButton;
    private javax.swing.JTextArea MailHeadersWindow;
    private javax.swing.JButton NewButton;
    private javax.swing.JButton PositButton;
    private javax.swing.JProgressBar ProgressBar;
    private javax.swing.JButton QTCButtton;
    private javax.swing.JButton ReadButton;
    private javax.swing.JButton SendButton;
    private javax.swing.JLabel StatusLabel;
    private javax.swing.JMenu Twitter;
    private javax.swing.JMenuItem Twitter_send;
    private javax.swing.JButton UpdateButton;
    private javax.swing.JButton bBeacon;
    private javax.swing.JButton bConnect;
    private javax.swing.JButton bLink;
    private javax.swing.JButton bPing;
    private javax.swing.JButton bWX;
    private javax.swing.JComboBox cboAPRSIcon;
    private javax.swing.JComboBox cboBeaconPeriod;
    private javax.swing.JComboBox cboServer;
    private javax.swing.JCheckBox chkBeacon;
    private javax.swing.JMenuBar jMenuBar3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JTextArea jTextArea1;
    public javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField1;
    private org.jdesktop.swingx.JXMapKit jxMapKit;
    private javax.swing.JLabel lblAPRSIcon;
    private javax.swing.JLabel lblBeaconPeriod;
    private javax.swing.JLabel lblCourse;
    private javax.swing.JLabel lblFixat;
    private javax.swing.JLabel lblInMsgs;
    private javax.swing.JLabel lblLatitude;
    private javax.swing.JLabel lblLongitude;
    private javax.swing.JLabel lblSpeed;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatusMsg;
    private javax.swing.JLabel lblWx;
    private javax.swing.JMenuItem menuMessages;
    private javax.swing.JMenuItem mnuAbout2;
    private javax.swing.JMenuItem mnuBulletins2;
    private javax.swing.JMenu mnuClear2;
    private javax.swing.JMenu mnuEdit2;
    private javax.swing.JMenu mnuFile2;
    private javax.swing.JMenuItem mnuGetAPRS2;
    private javax.swing.JMenuItem mnuGetCamper2;
    private javax.swing.JMenuItem mnuGetPskmailNews2;
    private javax.swing.JMenuItem mnuGetServerfq2;
    private javax.swing.JMenuItem mnuGetTide2;
    private javax.swing.JMenuItem mnuGetTidestations2;
    private javax.swing.JMenuItem mnuGetWebPages2;
    private javax.swing.JMenuItem mnuHeaders2;
    private javax.swing.JMenuItem mnuHelpFile2;
    private javax.swing.JMenu mnuHelpMain2;
    private javax.swing.JMenu mnuInfo2;
    private javax.swing.JRadioButtonMenuItem mnuMFSK64;
    private javax.swing.JRadioButtonMenuItem mnuMailAPRS2;
    private javax.swing.JMenuItem mnuMailQueue2;
    private javax.swing.JRadioButtonMenuItem mnuMailScanning;
    private javax.swing.JMenu mnuMbox2;
    private javax.swing.JMenuItem mnuMboxDelete2;
    private javax.swing.JMenuItem mnuMboxList2;
    private javax.swing.JMenuItem mnuMboxRead2;
    private javax.swing.JMenu mnuMode2;
    private javax.swing.JMenuItem mnuModeQSY2;
    private javax.swing.JMenuItem mnuNew2;
    private javax.swing.JMenuItem mnuOpenDraft2;
    private javax.swing.JRadioButtonMenuItem mnuPSK125;
    private javax.swing.JRadioButtonMenuItem mnuPSK250;
    private javax.swing.JRadioButtonMenuItem mnuPSK63;
    private javax.swing.JMenuItem mnuPreferences2;
    private javax.swing.JMenuItem mnuQuit2;
    private javax.swing.JMenuItem mnuSaveDraft2;
    private javax.swing.JRadioButtonMenuItem mnuTHOR22;
    private javax.swing.JRadioButtonMenuItem mnuTTY2;
    private javax.swing.JPanel pnlBeacon;
    private javax.swing.JPanel pnlFilesButtons;
    private javax.swing.JPanel pnlGPS;
    private javax.swing.JPanel pnlMainEntry;
    private javax.swing.JPanel pnlStatus;
    private javax.swing.JPanel pnlStatusIndicator;
    private javax.swing.JPanel pnlTerminalButtons;
    private javax.swing.JScrollPane scrInMsgs;
    private javax.swing.JSpinner spnMinute;
    private javax.swing.JPanel tabAPRS;
    private javax.swing.JPanel tabFiles;
    private javax.swing.JPanel tabMailHeaders;
    private javax.swing.JTabbedPane tabMain;
    private javax.swing.JPanel tabMap;
    private javax.swing.JPanel tabTerminal;
    private javax.swing.JTextField txtCourse;
    private javax.swing.JTextField txtFixTakenAt;
    private javax.swing.JTextArea txtInMsgs;
    private javax.swing.JTextField txtLatitude;
    private javax.swing.JTextField txtLongitude;
    private javax.swing.JTextField txtMainEntry;
    private javax.swing.JTextField txtSpeed;
    private javax.swing.JTextField txtStatus;
    private javax.swing.JTextField txtWX;
    // End of variables declaration//GEN-END:variables

    public void setOperatorName(String str) {
        // nothing
    }

    public String getOperatorName() {
        return "";
    }

    public void setPicturePath(String str) {
        // nothing
    }

    public String getPicturePath() {
        return "";
    }

    public void setPictureName(String str) {
        // nothing
    }

    public String getPictureName() {
        return "";
    }

    public void setCallsign(String str) {
        // nothing
    }

    public void setAPRSIcon(String str) {
        // nothing
    }

    public JButton getPosReadButton() {
        return ReadButton;
    }

    ;

    public JButton getTextReadButton() {
        return ReadButton;
    }

    public JButton getSYNOPReadButton() {
        return ReadButton;
    }

    public JButton getGRIBReadButton() {
        return ReadButton;
    }

    public void setLinkPeriod(int l) {
    }

    public int getLinkPeriod() {
        return 0;
    }

    public void setLinkIndicator(boolean b) {
        // do nothing in thie UI
    }

    public void setLinkTime(String str) {
        // do nothing in this UI
    }

    public String getClock() {
        // implement this if needed
        return "";
    }

    public TreeMap<String, TreeMap<String, PosData>> getMainPosReport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPosReport(TreeMap<String, TreeMap<String, PosData>> posReport) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addPosReport(TreeMap<String, PosData> posReport) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addPosReport(Collection<PosData> posReport) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addPosReport(PosData p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPosReport(String str) {
    }

    public void appendPosReport(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void appendPosReport(TreeMap<String, TreeMap<String, PosData>> posReport) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPosReport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setGPSIndicator(boolean b) {
    }

    public fourbuttons getPanelFour() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setOperatingMode(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public modemmodeenum[] getModeProfile() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
