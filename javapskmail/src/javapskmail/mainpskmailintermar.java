/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * mainpskmailintermar.java
 *
 * Created on 03.03.2009, 15:18:21
 */
package javapskmail;

/* Package from mainpskmailui */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 *
 * @author sebastian_pohl
 */
public class mainpskmailintermar extends javax.swing.JFrame implements mainui,
        PopupMenuListener, java.awt.event.ItemListener, ActionListener,
        ListSelectionListener {

    private mainpskmaillogic pskmaillogic;
    private PosReportMapPanel nPanel;
    private MouseInputListener mListener;
    private ScannerMapInfFiles mapInfFileScanner;
    private TreeMap<String, TreeMap<String, PosData>> mainPosReport;

    public void appendPosFile(String str) {
        BufferedWriter testWriter = null;

        try {
            testWriter = new BufferedWriter(new FileWriter(Main.posReportPath + "posreport.txt", true));
            testWriter.write(str);
        } catch (Exception e) {
            System.out.println("exception writing pos report");
        } finally {
            try {
                if (testWriter != null) {
                    testWriter.flush();
                    testWriter.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void addPosReport(PosData p) {
        if (mainPosReport.get(p.getCallsign()) != null) {
            if (mainPosReport.get(p.getCallsign()) != null) {
                mainPosReport.get(p.getCallsign()).put(p.getDate() + " " + p.getTime(), p);
            } else {
                TreeMap<String, PosData> tempTree = new TreeMap<String, PosData>(new ReverseStringComparator());
                tempTree.put(p.getDate() + " " + p.getTime(), p);
                mainPosReport.put(p.getCallsign(), tempTree);
            }
        } else {
            TreeMap<String, PosData> tempTree = new TreeMap<String, PosData>(new ReverseStringComparator());
            tempTree.put(p.getDate() + " " + p.getTime(), p);
            mainPosReport.put(p.getCallsign(), tempTree);
        }
        // add posdata to the posReportWindow
        appendPosReport(p.toString() + "\n");
        // add posdata to the internal map
        nPanel.addPosData(p);
        // append posdata to the external file posreport.txt in dir posreport
        appendPosFile(p.toString() + "\n");
    }

    public void addPosReport(Collection<PosData> posReport) {
        Iterator<PosData> iter1 = posReport.iterator();
        while (iter1.hasNext()) {
            PosData p = iter1.next();

            addPosReport(p);
        }
        //appendPosReportWindow(posReport);
        //nPanel.addPosReport(posReport);
    }

    public void addPosReport(TreeMap<String, PosData> posReport) {
        //System.out.println(posReport);

//        Iterator<PosData> iter1 = posReport.values().iterator();
//        while (iter1.hasNext()) {
//            PosData p = iter1.next();
//
//            addPosReport(p);
//        }
//        appendPosReportWindow((ArrayList) posReport.values());
//        nPanel.addPosReport(posReport);
        addPosReport(posReport.values());
    }

    public void appendPosReport(TreeMap<String, TreeMap<String, PosData>> posReport) {
        //System.out.println("size: " + posReport.size());

        Collection<TreeMap<String, PosData>> setOfKeys = posReport.values();

        //System.out.println("key set size: " + setOfKeys.size() + "\n" + setOfKeys);

        Iterator<TreeMap<String, PosData>> iter1 = setOfKeys.iterator();

        while (iter1.hasNext()) {
            TreeMap<String, PosData> temp1 = iter1.next();

            Iterator<PosData> iter2 = temp1.values().iterator();
            while (iter2.hasNext()) {
                addPosReport(iter2.next());
            }
        }
    }

    ;

    public void setPosReport(TreeMap<String, TreeMap<String, PosData>> posReport) {
        if (posReport != null) {
            mainPosReport = posReport;
        } else {
            mainPosReport = new TreeMap<String, TreeMap<String, PosData>>();
        }
        setPosReportWindow(posReport);
        nPanel.setPosReport(mainPosReport);
    }

    public TreeMap<String, TreeMap<String, PosData>> getMainPosReport() {
        return mainPosReport;
    }

    public PosReportMapPanel getMainPosReportMapPanel() {
        return nPanel;
    }

    /** Creates new form mainpskmailintermar */
    public mainpskmailintermar() {

        pskmaillogic = new mainpskmaillogic(this);
        initComponents();

        // setExtendedState(MAXIMIZED_BOTH);

        pnlSeaWeather.setVisible(false);

        Main.mainInput.addTextField(txtNr1);
        Main.mainInput.addTextField(txtNr2);
        Main.mainInput.addTextField(txtNr3);

        txtAreaWx2.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                doit();
            }

            public void removeUpdate(DocumentEvent e) {
                doit();
            }

            public void changedUpdate(DocumentEvent e) {
                doit();
            }

            private void doit() {
                MailHeadersWindow2.setText(txtAreaWx2.getText());
                txtAreaMbox2.setText(txtAreaWx2.getText());
                //txtPosReport2.setText(txtAreaWx2.getText());
            }
        });

        double prop = 0.5;

        APRSSplitPane.setDividerLocation(prop);
        EmailSplitPane.setDividerLocation(prop);
        //FilesSplitPane.setDividerLocation(prop);
        MboxSplitPane.setDividerLocation(prop);
        PosReportPane.setDividerLocation(prop);
        WXSplitPane.setDividerLocation(prop);
        //WebSplitPane.setDividerLocation(prop);

        // internal panels to add on the frame
        pnlPSKmailTraffic = new pskmailtrafficpanel(pskmaillogic);

        mapInfFileScanner = new ScannerMapInfFiles(cboMapChoice);

        cboMapChoice.removeAllItems();

        Iterator<String> iter = mapInfFileScanner.getInfFileList().iterator();
        while (iter.hasNext()) {
            cboMapChoice.addItem(iter.next());
        }

        cboMapChoice.setSelectedItem("physWorld");

        try {
            mainPosReport = (new ParsePosReportFile(new File(Main.posReportPath + "posreport.txt"), 1)).getPosReport();
            changePosReportMap("physWorld.inf", "physWorld.gif", mainPosReport);
        } catch (Exception e) {
            mainPosReport = new TreeMap<String, TreeMap<String, PosData>>();
            changePosReportMap("physWorld.inf", "physWorld.gif", mainPosReport);
        }

        appendPosReportWindow(mainPosReport);

        pnlFourButtons = new fourbuttons(pskmaillogic);
        pnlAPRS = new APRSpanel(pskmaillogic);

        pnlAPRS.setAPRSIcon("/Y");

        // set APRS/Mail mode as default
        pskmaillogic.mnuMailAPRS2ActionPerformed(null);

        // internal panels to pop up on the frame
        pnlOperatorData = new OperatorDataDialog(this, true);

        // assigning APRS tab
        APRSLeftLower.add(pnlFourButtons);
        APRSRight.add(pnlPSKmailTraffic);

        // assigning Pos Report tab
        APRSLeftUpper.add(pnlAPRS);

        // change listener for tabbed pane to find out
        // if the tab has changed and to place Traffic Panel there
        APRStabPane.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent e) {
                // get tabbed pane object
                javax.swing.JTabbedPane tp = (javax.swing.JTabbedPane) e.getSource();
                // get selected index
                int index = tp.getSelectedIndex();
                // place Traffic Panel on newly selected pad
                // and repaint Panel
                switch (index) {
                    case 0:
                        APRSRight.add(pnlPSKmailTraffic);
                        APRSLeftLower.add(pnlFourButtons);
                        jPanel1.repaint();
                        break;
                    case 1:
                        PosReportRight.add(pnlPSKmailTraffic);
                        //txtPosReport2.setText(txtAreaWx2.getText());
                        PosReportLeftLower.add(pnlFourButtons);
                        jPanel1.repaint();
                        break;
                    case 3:
                        WXRight.add(pnlPSKmailTraffic);
                        // nothing to do
                        WXLeftLower.add(pnlFourButtons);
                        jPanel1.repaint();
                        break;
                    //case 3: FilesRight.add(pnlPSKmailTraffic);
                    //    FilesLeftLower.add(pnlFourButtons);
                    //    break;
                    case 4:
                        EmailRight.add(pnlPSKmailTraffic);
                        //MailHeadersWindow2.setText(txtAreaWx2.getText());
                        EmailLeftLower.add(pnlFourButtons);
                        break;
                    case 5:
                        MBoxRight.add(pnlPSKmailTraffic);
                        //txtAreaMbox2.setText(txtAreaWx2.getText());
                        MBoxLeftLower.add(pnlFourButtons);
                        break;
                    //case 6: WebRight.add(pnlPSKmailTraffic);
                    //    WebLeftLower.add(pnlFourButtons);
                    default:
                        break;
                }
            }
        });

        setCourse("0.0");
        setSpeed("0.0");

        mListener = new MouseInputListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }

            public void mouseMoved(MouseEvent e) {
                MapCoordinate tempX = nPanel.constructX(e.getX());
                MapCoordinate tempY = nPanel.constructY(e.getY());
                java.text.DecimalFormat formatter = new java.text.DecimalFormat("#.00");
                //System.out.println("Mouse moved");
                lblStatusMapPanel.setText(formatter.format(tempX.getDecimal()) + "" + tempX.getDirection() + "," + formatter.format(tempY.getDecimal()) + "" + tempY.getDirection());
            }
        };

        nPanel.addMouseMotionListener(mListener);
        nPanel.addMouseListener(mListener);
        nPanel.setSeaWeatherDraw(0);
        nPanel.setSeaWeatherTimeStamp("");

        cboMapChoice.addPopupMenuListener(this);
        cboMapChoice.addItemListener(this);

        cboTimeMode.addItemListener(this);

        cboSeaWeather.addActionListener(this);

        lstWeatherData.addListSelectionListener(this);

        Thread download = new Thread(new Runnable() {

            public void run() {
                try {
                    downloadFileCopyTo("http://www.intermar-ev.de/karte/ShipDB.zip", Main.ShipDBPath);

                    System.out.println("download finished");

                    //replaceFile("_ShipDB.zip", "ShipDB.zip");
                } catch (IOException ex) {
                    Logger.getLogger(mainpskmailintermar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        if (!(new File(Main.ShipDBPath).exists())) {
            download.start();
        }
        try {
            if (!new File(Main.serverlistPath).exists()) {
                BufferedInputStream in = new BufferedInputStream(
                        new FileInputStream(new File("serverlist")));

                BufferedOutputStream bout = new BufferedOutputStream(
                        new FileOutputStream(Main.serverlistPath), 1024);

                int i;

                while ((i = in.read()) != -1) {
                    bout.write(i);
                }

                bout.flush();
                bout.close();
                in.close();
            }

            Main.pskServerDatabase = new ServerDatabase(new File(Main.serverlistPath));

            Iterator<String> iter3 = Main.pskServerDatabase.getServerList().iterator();

            while (iter3.hasNext()) {

                String temp = iter3.next();
                Iterator<String> iter2 = Main.Servers.iterator();
                boolean contained = false;

                while (iter2.hasNext()) {
                    if (temp.equals(iter2.next())) {
                        contained = true;
                    }
                }

                if (!contained) {
                    Main.Servers.add(temp);
                }
            }

            //Main.Servers.addAll(Main.pskServerDatabase.getServerList());
        } catch (IOException ex) {
            System.out.println("There was a problem.");
            ex.printStackTrace();
            Main.pskServerDatabase = null;
        }

        pskmaillogic.mainpskmaillogic_start();
    }

//    public void replaceFile(String source, String destination) throws IOException {
//
//        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(source)));
//
//        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(destination), 1024);
//
//        //byte data[] = new byte[1024];
//        int i;
//
//        //while (in.read(data, 0, 1024) >= 0) {
//        //    bout.write(data);
//        //}
//
//        while((i = in.read()) != -1) {
//            bout.write(i);
//        }
//
//        bout.flush();
//        bout.close();
//        in.close();
//    }

    public void downloadFileCopyTo(String urlPath, String fileName) throws IOException {
        try {
            URL url = new URL(urlPath);
            URLConnection urlc = url.openConnection();

            // System.out.println("Encoding: "  + urlc.getContentEncoding() + ", Length: " +
            //        urlc.getContentLength() + ", Type: " + urlc.getContentType());

            DownloadProgress progressBar = new DownloadProgress(urlc.getContentLength());

            pnlProgress.add(progressBar);

            repaint();

            BufferedInputStream in = new BufferedInputStream(urlc.getInputStream());

            FileOutputStream fos = new FileOutputStream(fileName);

            BufferedOutputStream bout = new BufferedOutputStream(fos);

            //byte data[] = new byte[1024];
            int counter = 0;

            int i;

            //while (in.read(data, 0, 1024) != -1) {
            //   bout.write(data);
            //}

            while ((i = in.read()) != -1) {
                progressBar.update(counter++);
                bout.write(i);
            }

            bout.flush();
            bout.close();
            in.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pnlProgress.removeAll();
            pnlProgress.repaint();
        }

    }

    private void changePosReportMap(String fileInf, String fileGif, TreeMap<String, TreeMap<String, PosData>> PosReport) {
        //String filePosReport, int mode) {
        nPanel = new PosReportMapPanel(this,
                new File(fileInf), new ImageIcon(fileGif), PosReport);

        nPanel.addMouseMotionListener(mListener);
        nPanel.addMouseListener(mListener);

        newPosReportMapPanel.removeAll();
        newPosReportMapPanel.add(nPanel);
    }

    public void changeTabTo(int index) {
        APRStabPane.setSelectedIndex(index);
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
        APRStabPane = new javax.swing.JTabbedPane();
        APRStab = new javax.swing.JPanel();
        APRSbuttons = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        btnOperatorData = new javax.swing.JButton();
        btnEmailAbort1 = new javax.swing.JButton();
        btnQuit = new javax.swing.JButton();
        APRSSplitPane = new javax.swing.JSplitPane();
        APRSLeft = new javax.swing.JPanel();
        APRSLeftUpper = new javax.swing.JPanel();
        APRSLeftLower = new javax.swing.JPanel();
        APRSRight = new javax.swing.JPanel();
        PosReporttab = new javax.swing.JPanel();
        PosReportButtons = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        btnReadFile = new javax.swing.JButton();
        btnSavePosReport = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        btnClearPosReportWindow = new javax.swing.JButton();
        PosReportPane = new javax.swing.JSplitPane();
        PosReportLeft = new javax.swing.JPanel();
        PosReportLeftUpper = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtPosReport2 = new javax.swing.JTextArea();
        PosReportLeftLower = new javax.swing.JPanel();
        PosReportRight = new javax.swing.JPanel();
        pnlPosReportMap = new javax.swing.JPanel();
        PosReportMapButtons = new javax.swing.JPanel();
        btnClearMap = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        lblTimeInterval = new javax.swing.JLabel();
        cboTimeMode = new javax.swing.JComboBox();
        lblModeFilter = new javax.swing.JLabel();
        cboModeFilter = new javax.swing.JComboBox();
        btnCallFinder = new javax.swing.JButton();
        txtCallFinder = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cboMapChoice = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        newPosReportMapPanel = new javax.swing.JPanel();
        pnlSeaWeather = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstWeatherData = new javax.swing.JList();
        txtStatusMapPanel = new javax.swing.JPanel();
        lblStatusMapPanel = new javax.swing.JLabel();
        pnlProgress = new javax.swing.JPanel();
        btnMetarea = new javax.swing.JButton();
        cboSeaWeather = new javax.swing.JComboBox();
        btnZoomGrid = new javax.swing.JButton();
        btnUpdateShipDB = new javax.swing.JButton();
        WXtab = new javax.swing.JPanel();
        WXButtons = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        btnFilesConnect = new javax.swing.JButton();
        btnFilesAbort = new javax.swing.JButton();
        btnWeatherUpdate = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnFilesList = new javax.swing.JButton();
        btnFilesDownload = new javax.swing.JButton();
        txtNr1 = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btnReadFILE = new javax.swing.JButton();
        btnDeleteTEXT = new javax.swing.JButton();
        btnDeleteFileWindow = new javax.swing.JButton();
        btnMapTEXT = new javax.swing.JButton();
        WXSplitPane = new javax.swing.JSplitPane();
        WXLeft = new javax.swing.JPanel();
        WxLeftUpper2 = new javax.swing.JScrollPane();
        txtAreaWx2 = new javax.swing.JTextArea();
        WXLeftLower = new javax.swing.JPanel();
        WXRight = new javax.swing.JPanel();
        Emailtab = new javax.swing.JPanel();
        EmailButtons = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        btnEmailConnect = new javax.swing.JButton();
        btnEmailAbort = new javax.swing.JButton();
        btnEmailUpdate = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        btnEmailQTC = new javax.swing.JButton();
        btnEmailRead = new javax.swing.JButton();
        btnEmailDelete = new javax.swing.JButton();
        txtNr2 = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        btnEmailInBox = new javax.swing.JButton();
        btlEmailClearQueue = new javax.swing.JButton();
        btnEmailClearHeaders = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        btnEmailNew = new javax.swing.JButton();
        btnEmailSend = new javax.swing.JButton();
        btnEmailOutBox = new javax.swing.JButton();
        EmailSplitPane = new javax.swing.JSplitPane();
        EmailLeft = new javax.swing.JPanel();
        EmailLeftUpper2 = new javax.swing.JScrollPane();
        MailHeadersWindow2 = new javax.swing.JTextArea();
        EmailLeftLower = new javax.swing.JPanel();
        EmailRight = new javax.swing.JPanel();
        Mboxtab = new javax.swing.JPanel();
        MboxButtons = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        btnMBoxConnect = new javax.swing.JButton();
        btnMBoxAbort = new javax.swing.JButton();
        btnMBoxUpdate = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        btnMBoxList = new javax.swing.JButton();
        btnMBoxRead = new javax.swing.JButton();
        btnMBoxDelete = new javax.swing.JButton();
        txtNr3 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        btnMBoxNew = new javax.swing.JButton();
        btnMboxSend = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        btnMboxInBox = new javax.swing.JButton();
        btnMboxOutBox = new javax.swing.JButton();
        MboxSplitPane = new javax.swing.JSplitPane();
        MBoxLeft = new javax.swing.JPanel();
        MBoxLeftUpper2 = new javax.swing.JScrollPane();
        txtAreaMbox2 = new javax.swing.JTextArea();
        MBoxLeftLower = new javax.swing.JPanel();
        MBoxRight = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("javapskmail/mainpskmailintermar"); // NOI18N
        setTitle(bundle.getString("INTERMAR_PSKMAIL_CLIENT_WINV1.3_")); // NOI18N
        setMinimumSize(new java.awt.Dimension(1024, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(1024, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(1024, 600));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        APRStabPane.setBackground(new java.awt.Color(204, 204, 204));
        APRStabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        APRStabPane.setToolTipText(bundle.getString("BRICHT_PROGRAMM_AB,_NUR_IM_NOTFALL_VERWENDEN!")); // NOI18N
        APRStabPane.setFont(new java.awt.Font("Tahoma", 1, 18));
        APRStabPane.setMaximumSize(new java.awt.Dimension(500, 50));
        APRStabPane.setMinimumSize(new java.awt.Dimension(1089, 637));
        APRStabPane.setPreferredSize(new java.awt.Dimension(1006, 637));
        APRStabPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabHighlight(evt);
            }
        });

        APRStab.setLayout(new java.awt.GridBagLayout());

        APRSbuttons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        btnOperatorData.setBackground(new java.awt.Color(102, 204, 255));
        btnOperatorData.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnOperatorData.setText(bundle.getString("OPERATOR-DATA")); // NOI18N
        btnOperatorData.setToolTipText(bundle.getString("ZUM_ABSPEICHERN/EINGEBEN_DER_EIGENEN_DATEN.")); // NOI18N
        btnOperatorData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOperatorDataActionPerformed(evt);
            }
        });
        jPanel9.add(btnOperatorData);

        btnEmailAbort1.setBackground(new java.awt.Color(204, 204, 204));
        btnEmailAbort1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEmailAbort1.setForeground(new java.awt.Color(255, 255, 255));
        btnEmailAbort1.setText(bundle.getString("ABORT")); // NOI18N
        btnEmailAbort1.setToolTipText(bundle.getString("BEENDEN_(NUR_IM_NOTFALL_BENUTZEN),_ANSONSTEN_QUIT_BENUTZEN.")); // NOI18N
        btnEmailAbort1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailAbort1ActionPerformed(evt);
            }
        });
        jPanel9.add(btnEmailAbort1);

        btnQuit.setBackground(new java.awt.Color(0, 204, 0));
        btnQuit.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnQuit.setForeground(new java.awt.Color(255, 255, 255));
        btnQuit.setText(bundle.getString("QUIT")); // NOI18N
        btnQuit.setToolTipText(bundle.getString("BEENDET_UND_SPEICHERT_DAS_PROGRAMM.")); // NOI18N
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitActionPerformed(evt);
            }
        });
        jPanel9.add(btnQuit);

        APRSbuttons.add(jPanel9);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        APRStab.add(APRSbuttons, gridBagConstraints);

        APRSSplitPane.setDividerLocation(350);
        APRSSplitPane.setResizeWeight(0.5);
        APRSSplitPane.setLastDividerLocation(350);
        APRSSplitPane.setMinimumSize(new java.awt.Dimension(691, 562));
        APRSSplitPane.setPreferredSize(new java.awt.Dimension(691, 562));

        APRSLeft.setLayout(new java.awt.GridBagLayout());

        APRSLeftUpper.setMaximumSize(new java.awt.Dimension(500, 550));
        APRSLeftUpper.setMinimumSize(new java.awt.Dimension(500, 550));
        APRSLeftUpper.setPreferredSize(new java.awt.Dimension(500, 550));
        APRSLeftUpper.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        APRSLeft.add(APRSLeftUpper, gridBagConstraints);

        APRSLeftLower.setMaximumSize(new java.awt.Dimension(500, 50));
        APRSLeftLower.setMinimumSize(new java.awt.Dimension(500, 50));
        APRSLeftLower.setPreferredSize(new java.awt.Dimension(500, 50));
        APRSLeftLower.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        APRSLeft.add(APRSLeftLower, gridBagConstraints);

        APRSSplitPane.setLeftComponent(APRSLeft);

        APRSRight.setLayout(new javax.swing.BoxLayout(APRSRight, javax.swing.BoxLayout.LINE_AXIS));
        APRSSplitPane.setRightComponent(APRSRight);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        APRStab.add(APRSSplitPane, gridBagConstraints);

        APRStabPane.addTab("APRS", APRStab);

        PosReporttab.setLayout(new java.awt.GridBagLayout());

        PosReportButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        btnReadFile.setBackground(new java.awt.Color(0, 0, 204));
        btnReadFile.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnReadFile.setForeground(new java.awt.Color(255, 255, 255));
        btnReadFile.setText(bundle.getString("READ_POSFILE")); // NOI18N
        btnReadFile.setToolTipText(bundle.getString("ÖFFNET_DATEIORDNER_POSITIONSREPORT_ZUM_IMPORTIEREN_DER_DATEN")); // NOI18N
        btnReadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadFileActionPerformed(evt);
            }
        });
        jPanel8.add(btnReadFile);

        btnSavePosReport.setBackground(new java.awt.Color(0, 0, 204));
        btnSavePosReport.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnSavePosReport.setForeground(new java.awt.Color(255, 255, 255));
        btnSavePosReport.setText(bundle.getString("SAVE_POSFILE")); // NOI18N
        btnSavePosReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePosReportActionPerformed(evt);
            }
        });
        jPanel8.add(btnSavePosReport);

        btnDelete.setBackground(new java.awt.Color(43, 2, 2));
        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText(bundle.getString("DELETE_POSFILE")); // NOI18N
        btnDelete.setToolTipText(bundle.getString("LÖSCHT_POSITIONSDATEN_NACH_EINSTELLUNG_(SCROLLBALKEN)")); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        jPanel8.add(btnDelete);

        PosReportButtons.add(jPanel8);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        btnClearPosReportWindow.setBackground(new java.awt.Color(43, 2, 2));
        btnClearPosReportWindow.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnClearPosReportWindow.setForeground(new java.awt.Color(255, 255, 255));
        btnClearPosReportWindow.setText(bundle.getString("CLEAR_POSWINDOW")); // NOI18N
        btnClearPosReportWindow.setToolTipText(bundle.getString("LÖSCHT_POSREPORT_WINDOW_(KARTE_BLEIBT_ERHALTEN).")); // NOI18N
        btnClearPosReportWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearPosReportWindowActionPerformed(evt);
            }
        });
        jPanel21.add(btnClearPosReportWindow);

        PosReportButtons.add(jPanel21);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        PosReporttab.add(PosReportButtons, gridBagConstraints);

        PosReportPane.setDividerLocation(350);
        PosReportPane.setResizeWeight(0.5);
        PosReportPane.setLastDividerLocation(350);
        PosReportPane.setMinimumSize(new java.awt.Dimension(691, 562));
        PosReportPane.setPreferredSize(new java.awt.Dimension(691, 562));

        PosReportLeft.setLayout(new java.awt.GridBagLayout());

        PosReportLeftUpper.setMaximumSize(new java.awt.Dimension(500, 550));
        PosReportLeftUpper.setMinimumSize(new java.awt.Dimension(500, 550));
        PosReportLeftUpper.setPreferredSize(new java.awt.Dimension(500, 550));
        PosReportLeftUpper.setLayout(new java.awt.GridLayout(1, 1));

        txtPosReport2.setColumns(20);
        txtPosReport2.setRows(5);
        jScrollPane2.setViewportView(txtPosReport2);

        PosReportLeftUpper.add(jScrollPane2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        PosReportLeft.add(PosReportLeftUpper, gridBagConstraints);

        PosReportLeftLower.setMaximumSize(new java.awt.Dimension(500, 50));
        PosReportLeftLower.setMinimumSize(new java.awt.Dimension(500, 50));
        PosReportLeftLower.setPreferredSize(new java.awt.Dimension(500, 50));
        PosReportLeftLower.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        PosReportLeft.add(PosReportLeftLower, gridBagConstraints);

        PosReportPane.setLeftComponent(PosReportLeft);

        PosReportRight.setLayout(new javax.swing.BoxLayout(PosReportRight, javax.swing.BoxLayout.LINE_AXIS));
        PosReportPane.setRightComponent(PosReportRight);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        PosReporttab.add(PosReportPane, gridBagConstraints);

        APRStabPane.addTab("Pos-Report", PosReporttab);

        pnlPosReportMap.setMinimumSize(new java.awt.Dimension(600, 600));
        pnlPosReportMap.setLayout(new java.awt.GridBagLayout());

        PosReportMapButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnClearMap.setBackground(new java.awt.Color(255, 255, 204));
        btnClearMap.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnClearMap.setText(bundle.getString("CLEAR_MAP")); // NOI18N
        btnClearMap.setToolTipText(bundle.getString("LÖSCHT_KARTE_UND_POSREPORT_WINDOW")); // NOI18N
        btnClearMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearMapActionPerformed(evt);
            }
        });
        PosReportMapButtons.add(btnClearMap);

        jButton1.setBackground(new java.awt.Color(255, 255, 204));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton1.setText(bundle.getString("REPAINT")); // NOI18N
        jButton1.setToolTipText(bundle.getString("ZEICHNET_KARTE_NEU,_FALLS_FEHLER_IN_DER_ZEICHNUNG.")); // NOI18N
        PosReportMapButtons.add(jButton1);

        lblTimeInterval.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblTimeInterval.setText(bundle.getString("TIME_INTERVAL")); // NOI18N
        lblTimeInterval.setToolTipText(bundle.getString("POSITIONSDATENANZEIGEZEIT_(POSITIONSDATEN_BLEIBEN_IN_DER_DATENBANK_ERHALTEN)_ZUR_BESSEREN_ÜBERSICHT")); // NOI18N
        PosReportMapButtons.add(lblTimeInterval);

        cboTimeMode.setFont(new java.awt.Font("Tahoma", 1, 12));
        cboTimeMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "infinite", "1 day", "3 days", "7 days", "14 days" }));
        cboTimeMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTimeModeActionPerformed(evt);
            }
        });
        PosReportMapButtons.add(cboTimeMode);

        lblModeFilter.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblModeFilter.setText(bundle.getString("FILTER")); // NOI18N
        lblModeFilter.setToolTipText(bundle.getString("FILTERT_DIE_ANZEIGE_NACH_POSITIONSBARKEN_UND_POSITIONREPORTS.")); // NOI18N
        PosReportMapButtons.add(lblModeFilter);

        cboModeFilter.setFont(new java.awt.Font("Tahoma", 1, 12));
        cboModeFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none", "only posreport", "only pos beacons", "both" }));
        cboModeFilter.setSelectedIndex(3);
        cboModeFilter.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboModeFilterItemStateChanged(evt);
            }
        });
        PosReportMapButtons.add(cboModeFilter);

        btnCallFinder.setBackground(new java.awt.Color(102, 204, 255));
        btnCallFinder.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnCallFinder.setText(bundle.getString("CALLFINDER")); // NOI18N
        btnCallFinder.setToolTipText(bundle.getString("CALLSIGN_SUCHER_(NICHT_CASE-SENTITIVE),_CALLSIGN_EINGEBEN_UND_ENTER_DRÜCKEN.")); // NOI18N
        btnCallFinder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCallFinderActionPerformed(evt);
            }
        });
        PosReportMapButtons.add(btnCallFinder);

        txtCallFinder.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtCallFinder.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCallFinder.setPreferredSize(new java.awt.Dimension(70, 21));
        txtCallFinder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCallFinderKeyPressed(evt);
            }
        });
        PosReportMapButtons.add(txtCallFinder);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel1.setText(bundle.getString("MAP")); // NOI18N
        jLabel1.setToolTipText(bundle.getString("KARTENAUSWAHL_DER_DARZUSTELLENDE_KARTE")); // NOI18N
        PosReportMapButtons.add(jLabel1);

        cboMapChoice.setToolTipText(bundle.getString("KARTENAUSWAHL_DER_DARZUSTELLENDE_KARTE")); // NOI18N
        cboMapChoice.setMinimumSize(new java.awt.Dimension(70, 21));
        cboMapChoice.setPreferredSize(new java.awt.Dimension(120, 21));
        PosReportMapButtons.add(cboMapChoice);

        jButton2.setBackground(new java.awt.Color(255, 255, 204));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton2.setText(bundle.getString("ABOUT_MAP")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        PosReportMapButtons.add(jButton2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        pnlPosReportMap.add(PosReportMapButtons, gridBagConstraints);

        jPanel4.setPreferredSize(new java.awt.Dimension(600, 600));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        newPosReportMapPanel.setMinimumSize(new java.awt.Dimension(874, 500));
        newPosReportMapPanel.setPreferredSize(new java.awt.Dimension(924, 500));
        newPosReportMapPanel.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        jPanel4.add(newPosReportMapPanel, gridBagConstraints);

        pnlSeaWeather.setMaximumSize(new java.awt.Dimension(100, 500));
        pnlSeaWeather.setMinimumSize(new java.awt.Dimension(0, 0));
        pnlSeaWeather.setPreferredSize(new java.awt.Dimension(100, 500));
        pnlSeaWeather.setLayout(new java.awt.GridLayout(1, 1));

        jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(100, 500));

        jScrollPane1.setViewportView(lstWeatherData);

        pnlSeaWeather.add(jScrollPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        jPanel4.add(pnlSeaWeather, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        pnlPosReportMap.add(jPanel4, gridBagConstraints);

        txtStatusMapPanel.setLayout(new java.awt.GridBagLayout());

        lblStatusMapPanel.setMaximumSize(new java.awt.Dimension(299, 25));
        lblStatusMapPanel.setMinimumSize(new java.awt.Dimension(299, 25));
        lblStatusMapPanel.setPreferredSize(new java.awt.Dimension(299, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        txtStatusMapPanel.add(lblStatusMapPanel, gridBagConstraints);

        pnlProgress.setMaximumSize(new java.awt.Dimension(150, 25));
        pnlProgress.setMinimumSize(new java.awt.Dimension(150, 25));
        pnlProgress.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        txtStatusMapPanel.add(pnlProgress, gridBagConstraints);

        btnMetarea.setBackground(new java.awt.Color(255, 255, 204));
        btnMetarea.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMetarea.setText("Metarea");
        btnMetarea.setMaximumSize(new java.awt.Dimension(150, 25));
        btnMetarea.setMinimumSize(new java.awt.Dimension(150, 25));
        btnMetarea.setPreferredSize(new java.awt.Dimension(150, 25));
        btnMetarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMetareaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        txtStatusMapPanel.add(btnMetarea, gridBagConstraints);

        cboSeaWeather.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "no weather", "wind speed", "gust speed", "wave height" }));
        cboSeaWeather.setMaximumSize(new java.awt.Dimension(150, 25));
        cboSeaWeather.setMinimumSize(new java.awt.Dimension(150, 25));
        cboSeaWeather.setPreferredSize(new java.awt.Dimension(150, 25));
        cboSeaWeather.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboSeaWeatherItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        txtStatusMapPanel.add(cboSeaWeather, gridBagConstraints);

        btnZoomGrid.setBackground(new java.awt.Color(255, 255, 204));
        btnZoomGrid.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnZoomGrid.setText(bundle.getString("TOGGLE_ZOOM_GRID")); // NOI18N
        btnZoomGrid.setToolTipText(bundle.getString("DRAWS_COORDINATE_AXIS_OVER_THE_MAP._CHOOSE_MAP_ACCORDINGLY_FROM_LIST.")); // NOI18N
        btnZoomGrid.setMaximumSize(new java.awt.Dimension(150, 25));
        btnZoomGrid.setMinimumSize(new java.awt.Dimension(150, 25));
        btnZoomGrid.setPreferredSize(new java.awt.Dimension(150, 25));
        btnZoomGrid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnZoomGridActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        txtStatusMapPanel.add(btnZoomGrid, gridBagConstraints);

        btnUpdateShipDB.setBackground(new java.awt.Color(255, 255, 204));
        btnUpdateShipDB.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnUpdateShipDB.setText(bundle.getString("UPDATE_SHIPDB")); // NOI18N
        btnUpdateShipDB.setToolTipText(bundle.getString("DOWNLOAD_VIA_INTERNET_OF_CURRENT_PSKMAIL_YACHT_DATABASE.")); // NOI18N
        btnUpdateShipDB.setMaximumSize(new java.awt.Dimension(150, 25));
        btnUpdateShipDB.setMinimumSize(new java.awt.Dimension(150, 25));
        btnUpdateShipDB.setPreferredSize(new java.awt.Dimension(150, 25));
        btnUpdateShipDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateShipDBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.1;
        txtStatusMapPanel.add(btnUpdateShipDB, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        pnlPosReportMap.add(txtStatusMapPanel, gridBagConstraints);

        APRStabPane.addTab("Pos-Report-Map", pnlPosReportMap);

        WXtab.setLayout(new java.awt.GridBagLayout());

        WXButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnFilesConnect.setBackground(new java.awt.Color(204, 0, 0));
        btnFilesConnect.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnFilesConnect.setForeground(new java.awt.Color(255, 255, 255));
        btnFilesConnect.setToolTipText(bundle.getString("CONNECTED_PSKMAIL_SERVER_ACTUAL.")); // NOI18N
        btnFilesConnect.setLabel(bundle.getString("CONNECT")); // NOI18N
        btnFilesConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilesConnectActionPerformed(evt);
            }
        });
        jPanel2.add(btnFilesConnect);

        btnFilesAbort.setBackground(new java.awt.Color(204, 204, 204));
        btnFilesAbort.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnFilesAbort.setForeground(new java.awt.Color(255, 255, 255));
        btnFilesAbort.setText(bundle.getString("ABORT")); // NOI18N
        btnFilesAbort.setToolTipText(bundle.getString("BEENDEN_(NUR_IM_NOTFALL_BENUTZEN),_ANSONSTEN_QUIT_BENUTZEN.")); // NOI18N
        btnFilesAbort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilesAbortActionPerformed(evt);
            }
        });
        jPanel2.add(btnFilesAbort);

        btnWeatherUpdate.setBackground(new java.awt.Color(102, 204, 255));
        btnWeatherUpdate.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnWeatherUpdate.setToolTipText(bundle.getString("SENDET_EIGENE_EMAIL_DATEN_(POPBOX,_ADRESSE,_PASSWORT)_ZUM_SERVER.")); // NOI18N
        btnWeatherUpdate.setLabel(bundle.getString("SERVER_UPDATE")); // NOI18N
        btnWeatherUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWeatherUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(btnWeatherUpdate);

        WXButtons.add(jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btnFilesList.setBackground(new java.awt.Color(255, 255, 204));
        btnFilesList.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnFilesList.setText(bundle.getString("GET_FILE_LIST")); // NOI18N
        btnFilesList.setToolTipText(bundle.getString("GIBT_LISTE_DER_AUF_DEM_SERVER_LIEGENDEN_FILES.")); // NOI18N
        btnFilesList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilesListActionPerformed(evt);
            }
        });
        jPanel3.add(btnFilesList);

        btnFilesDownload.setBackground(new java.awt.Color(255, 255, 204));
        btnFilesDownload.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnFilesDownload.setText(bundle.getString("DOWNLOAD_FILE")); // NOI18N
        btnFilesDownload.setToolTipText("Nach Eingabe Filename in Sendefenster, Taste \"Download File\", File wird übertragen.");
        btnFilesDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilesDownloadActionPerformed(evt);
            }
        });
        jPanel3.add(btnFilesDownload);

        txtNr1.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtNr1.setMinimumSize(new java.awt.Dimension(120, 21));
        txtNr1.setPreferredSize(new java.awt.Dimension(120, 21));
        txtNr1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNr1ActionPerformed(evt);
            }
        });
        jPanel3.add(txtNr1);

        WXButtons.add(jPanel3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        WXtab.add(WXButtons, gridBagConstraints);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnReadFILE.setBackground(new java.awt.Color(0, 0, 204));
        btnReadFILE.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnReadFILE.setForeground(new java.awt.Color(255, 255, 255));
        btnReadFILE.setText(bundle.getString("READ_FILE")); // NOI18N
        btnReadFILE.setToolTipText(bundle.getString("ÖFFNET_DATEI-ORDNER.")); // NOI18N
        btnReadFILE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReadFILEActionPerformed(evt);
            }
        });
        jPanel5.add(btnReadFILE);

        btnDeleteTEXT.setBackground(new java.awt.Color(43, 2, 2));
        btnDeleteTEXT.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnDeleteTEXT.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteTEXT.setText(bundle.getString("DELETE_FILE")); // NOI18N
        btnDeleteTEXT.setToolTipText(bundle.getString("LÖSCHT_DATEI")); // NOI18N
        btnDeleteTEXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTEXTActionPerformed(evt);
            }
        });
        jPanel5.add(btnDeleteTEXT);

        btnDeleteFileWindow.setBackground(new java.awt.Color(43, 2, 2));
        btnDeleteFileWindow.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnDeleteFileWindow.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteFileWindow.setText(bundle.getString("DELETE_FILE_WINDOW")); // NOI18N
        btnDeleteFileWindow.setToolTipText(bundle.getString("LÖSCHT_DAS_FENSTER_(NUR_INTERN)")); // NOI18N
        btnDeleteFileWindow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFileWindowActionPerformed(evt);
            }
        });
        jPanel5.add(btnDeleteFileWindow);

        btnMapTEXT.setBackground(new java.awt.Color(241, 249, 216));
        btnMapTEXT.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMapTEXT.setToolTipText(bundle.getString("ÖFFNET_KARTE.")); // NOI18N
        btnMapTEXT.setLabel(bundle.getString("MAP")); // NOI18N
        btnMapTEXT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMapTEXTActionPerformed(evt);
            }
        });
        jPanel5.add(btnMapTEXT);

        jPanel18.add(jPanel5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        WXtab.add(jPanel18, gridBagConstraints);

        WXSplitPane.setDividerLocation(350);
        WXSplitPane.setResizeWeight(0.5);
        WXSplitPane.setLastDividerLocation(350);

        WXLeft.setLayout(new java.awt.GridBagLayout());

        txtAreaWx2.setColumns(20);
        txtAreaWx2.setRows(5);
        WxLeftUpper2.setViewportView(txtAreaWx2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        WXLeft.add(WxLeftUpper2, gridBagConstraints);

        WXLeftLower.setMaximumSize(new java.awt.Dimension(500, 50));
        WXLeftLower.setMinimumSize(new java.awt.Dimension(500, 50));
        WXLeftLower.setPreferredSize(new java.awt.Dimension(500, 50));
        WXLeftLower.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        WXLeft.add(WXLeftLower, gridBagConstraints);

        WXSplitPane.setLeftComponent(WXLeft);

        WXRight.setLayout(new javax.swing.BoxLayout(WXRight, javax.swing.BoxLayout.LINE_AXIS));
        WXSplitPane.setRightComponent(WXRight);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        WXtab.add(WXSplitPane, gridBagConstraints);

        APRStabPane.addTab("Weather-Files", WXtab);

        Emailtab.setLayout(new java.awt.GridBagLayout());

        EmailButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        btnEmailConnect.setBackground(new java.awt.Color(204, 0, 0));
        btnEmailConnect.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailConnect.setForeground(new java.awt.Color(255, 255, 255));
        btnEmailConnect.setToolTipText(bundle.getString("CONNECTED_PSKMAIL_SERVER_ACTUAL.")); // NOI18N
        btnEmailConnect.setLabel(bundle.getString("CONNECT")); // NOI18N
        btnEmailConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailConnectActionPerformed(evt);
            }
        });
        jPanel10.add(btnEmailConnect);

        btnEmailAbort.setBackground(new java.awt.Color(204, 204, 204));
        btnEmailAbort.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEmailAbort.setForeground(new java.awt.Color(255, 255, 255));
        btnEmailAbort.setText(bundle.getString("ABORT")); // NOI18N
        btnEmailAbort.setToolTipText(bundle.getString("BEENDEN_(NUR_IM_NOTFALL_BENUTZEN),_ANSONSTEN_QUIT_BENUTZEN.")); // NOI18N
        btnEmailAbort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailAbortActionPerformed(evt);
            }
        });
        jPanel10.add(btnEmailAbort);

        btnEmailUpdate.setBackground(new java.awt.Color(102, 204, 255));
        btnEmailUpdate.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailUpdate.setToolTipText(bundle.getString("SENDET_EIGENE_EMAIL_DATEN_(POPBOX,_ADRESSE,_PASSWORT)_ZUM_SERVER.")); // NOI18N
        btnEmailUpdate.setLabel(bundle.getString("SERVER_UPDATE")); // NOI18N
        btnEmailUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailUpdateActionPerformed(evt);
            }
        });
        jPanel10.add(btnEmailUpdate);

        EmailButtons.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        btnEmailQTC.setBackground(new java.awt.Color(255, 255, 204));
        btnEmailQTC.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailQTC.setText(bundle.getString("GET_EMAIL_LIST")); // NOI18N
        btnEmailQTC.setToolTipText(bundle.getString("RUFT_AKTUELLE_EMAILLISTE_1_BIS_X_VOM_EMAIL-PROVIDER_AB._(EINGANGSEMAILS)")); // NOI18N
        btnEmailQTC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailQTCActionPerformed(evt);
            }
        });
        jPanel11.add(btnEmailQTC);

        btnEmailRead.setBackground(new java.awt.Color(255, 255, 204));
        btnEmailRead.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailRead.setText(bundle.getString("READ_EMAIL")); // NOI18N
        btnEmailRead.setToolTipText("Bei Eingabe der Nummer Email-Liste ins Sendefeld, Taste \"Read Mail\", wird die gewünschte Email übertragen.");
        btnEmailRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailReadActionPerformed(evt);
            }
        });
        jPanel11.add(btnEmailRead);

        btnEmailDelete.setBackground(new java.awt.Color(43, 2, 2));
        btnEmailDelete.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnEmailDelete.setText(bundle.getString("DELETE_EMAIL")); // NOI18N
        btnEmailDelete.setToolTipText("Bei Eingabe der Nummer Email-Liste in Sendefeld, Taste \"Delete Mail\" wird die Email beim Email Provider gelöscht.");
        btnEmailDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailDeleteActionPerformed(evt);
            }
        });
        jPanel11.add(btnEmailDelete);

        txtNr2.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtNr2.setMinimumSize(new java.awt.Dimension(120, 21));
        txtNr2.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel11.add(txtNr2);

        EmailButtons.add(jPanel11);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        Emailtab.add(EmailButtons, gridBagConstraints);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        btnEmailInBox.setBackground(new java.awt.Color(0, 0, 204));
        btnEmailInBox.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailInBox.setForeground(new java.awt.Color(255, 255, 255));
        btnEmailInBox.setText(bundle.getString("INBOX")); // NOI18N
        btnEmailInBox.setToolTipText(bundle.getString("ZUGANG_ZU_GESPEICHERTEN_UND_EMPFANGENEN_EMAIL_IM_NOTEBOOK.")); // NOI18N
        jPanel12.add(btnEmailInBox);

        btlEmailClearQueue.setBackground(new java.awt.Color(43, 2, 2));
        btlEmailClearQueue.setFont(new java.awt.Font("Tahoma", 1, 12));
        btlEmailClearQueue.setForeground(new java.awt.Color(255, 255, 255));
        btlEmailClearQueue.setText(bundle.getString("DELETE_OUTBOX")); // NOI18N
        btlEmailClearQueue.setToolTipText(bundle.getString("MAILS,_WELCHE_IN_DER_OUTBOX_SIND,_WERDEN_GELÖSCHT._(NOTEBOOK-INTERN)")); // NOI18N
        btlEmailClearQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btlEmailClearQueueActionPerformed(evt);
            }
        });
        jPanel12.add(btlEmailClearQueue);

        btnEmailClearHeaders.setBackground(new java.awt.Color(43, 2, 2));
        btnEmailClearHeaders.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailClearHeaders.setForeground(new java.awt.Color(255, 255, 255));
        btnEmailClearHeaders.setText(bundle.getString("DELETE_MAIL-LIST")); // NOI18N
        btnEmailClearHeaders.setToolTipText(bundle.getString("LÖSCHT_DIE_INTERN_ABGERUFENE_MAILLISTE,_UM_NEUE_MAILS_ABRUFEN_ZU_KÖNNEN._(NOTEBOOK-INTERN)")); // NOI18N
        btnEmailClearHeaders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailClearHeadersActionPerformed(evt);
            }
        });
        jPanel12.add(btnEmailClearHeaders);

        jPanel19.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        btnEmailNew.setBackground(new java.awt.Color(0, 0, 204));
        btnEmailNew.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailNew.setForeground(new java.awt.Color(255, 255, 255));
        btnEmailNew.setText(bundle.getString("NEW_MAIL")); // NOI18N
        btnEmailNew.setToolTipText(bundle.getString("EINGABEFELD_ZUM_SCHREIBEN_EINER_NEUEN_AUSGANGS-EMAIL.")); // NOI18N
        btnEmailNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailNewActionPerformed(evt);
            }
        });
        jPanel13.add(btnEmailNew);

        btnEmailSend.setBackground(new java.awt.Color(204, 0, 0));
        btnEmailSend.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailSend.setForeground(new java.awt.Color(255, 255, 255));
        btnEmailSend.setText(bundle.getString("SEND_MAIL")); // NOI18N
        btnEmailSend.setToolTipText(bundle.getString("SENDET_EMAIL_AUS_OUTBOX")); // NOI18N
        btnEmailSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailSendActionPerformed(evt);
            }
        });
        jPanel13.add(btnEmailSend);

        btnEmailOutBox.setBackground(new java.awt.Color(0, 0, 204));
        btnEmailOutBox.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnEmailOutBox.setForeground(new java.awt.Color(255, 255, 255));
        btnEmailOutBox.setText(bundle.getString("OUTBOX")); // NOI18N
        btnEmailOutBox.setToolTipText(bundle.getString("ZUGANG_ZU_GESPEICHERTEN_AUSGANGS-EMAILS.")); // NOI18N
        jPanel13.add(btnEmailOutBox);

        jPanel19.add(jPanel13);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        Emailtab.add(jPanel19, gridBagConstraints);

        EmailSplitPane.setDividerLocation(350);
        EmailSplitPane.setResizeWeight(0.5);
        EmailSplitPane.setLastDividerLocation(350);

        EmailLeft.setLayout(new java.awt.GridBagLayout());

        MailHeadersWindow2.setColumns(20);
        MailHeadersWindow2.setRows(5);
        EmailLeftUpper2.setViewportView(MailHeadersWindow2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        EmailLeft.add(EmailLeftUpper2, gridBagConstraints);

        EmailLeftLower.setMaximumSize(new java.awt.Dimension(500, 50));
        EmailLeftLower.setMinimumSize(new java.awt.Dimension(500, 50));
        EmailLeftLower.setPreferredSize(new java.awt.Dimension(500, 50));
        EmailLeftLower.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        EmailLeft.add(EmailLeftLower, gridBagConstraints);

        EmailSplitPane.setLeftComponent(EmailLeft);

        EmailRight.setLayout(new javax.swing.BoxLayout(EmailRight, javax.swing.BoxLayout.LINE_AXIS));
        EmailSplitPane.setRightComponent(EmailRight);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Emailtab.add(EmailSplitPane, gridBagConstraints);

        APRStabPane.addTab("Inet-Email", Emailtab);

        Mboxtab.setLayout(new java.awt.GridBagLayout());

        MboxButtons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));

        btnMBoxConnect.setBackground(new java.awt.Color(204, 0, 0));
        btnMBoxConnect.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMBoxConnect.setForeground(new java.awt.Color(255, 255, 255));
        btnMBoxConnect.setToolTipText(bundle.getString("CONNECTED_PSKMAIL_SERVER_ACTUAL.")); // NOI18N
        btnMBoxConnect.setLabel(bundle.getString("CONNECT")); // NOI18N
        btnMBoxConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMBoxConnectActionPerformed(evt);
            }
        });
        jPanel14.add(btnMBoxConnect);

        btnMBoxAbort.setBackground(new java.awt.Color(204, 204, 204));
        btnMBoxAbort.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnMBoxAbort.setForeground(new java.awt.Color(255, 255, 255));
        btnMBoxAbort.setText(bundle.getString("ABORT")); // NOI18N
        btnMBoxAbort.setToolTipText(bundle.getString("BEENDEN_(NUR_IM_NOTFALL_BENUTZEN),_ANSONSTEN_QUIT_BENUTZEN.")); // NOI18N
        btnMBoxAbort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMBoxAbortActionPerformed(evt);
            }
        });
        jPanel14.add(btnMBoxAbort);

        btnMBoxUpdate.setBackground(new java.awt.Color(102, 204, 255));
        btnMBoxUpdate.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMBoxUpdate.setToolTipText(bundle.getString("SENDET_EIGENE_EMAIL_DATEN_(POPBOX,_ADRESSE,_PASSWORT)_ZUM_SERVER.")); // NOI18N
        btnMBoxUpdate.setLabel(bundle.getString("SERVER_UPDATE")); // NOI18N
        btnMBoxUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMBoxUpdateActionPerformed(evt);
            }
        });
        jPanel14.add(btnMBoxUpdate);

        MboxButtons.add(jPanel14);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));

        btnMBoxList.setBackground(new java.awt.Color(255, 255, 204));
        btnMBoxList.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMBoxList.setText(bundle.getString("GETMAILLIST")); // NOI18N
        btnMBoxList.setToolTipText(bundle.getString("RUFT_AKTUELLE_EMAILLISTE_1_BIS_X_VOM_EMAIL-PROVIDER_AB._(EINGANGSEMAILS)")); // NOI18N
        btnMBoxList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMBoxListActionPerformed(evt);
            }
        });
        jPanel15.add(btnMBoxList);

        btnMBoxRead.setBackground(new java.awt.Color(255, 255, 204));
        btnMBoxRead.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMBoxRead.setText(bundle.getString("READ_MAIL")); // NOI18N
        btnMBoxRead.setToolTipText("Bei Eingabe der Nummer Email-Liste ins Sendefeld, Taste \"Read Mail\", wird die gewünschte Email übertragen.");
        btnMBoxRead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMBoxReadActionPerformed(evt);
            }
        });
        jPanel15.add(btnMBoxRead);

        btnMBoxDelete.setBackground(new java.awt.Color(43, 2, 2));
        btnMBoxDelete.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMBoxDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnMBoxDelete.setText(bundle.getString("DELETE_MAIL")); // NOI18N
        btnMBoxDelete.setToolTipText("Bei Eingabe der Nummer Email-Liste in Sendefeld, Taste \"Delete Mail\" wird die Email beim Email Provider gelöscht.");
        btnMBoxDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMBoxDeleteActionPerformed(evt);
            }
        });
        jPanel15.add(btnMBoxDelete);

        txtNr3.setFont(new java.awt.Font("Tahoma", 1, 12));
        txtNr3.setMinimumSize(new java.awt.Dimension(120, 21));
        txtNr3.setPreferredSize(new java.awt.Dimension(120, 21));
        jPanel15.add(txtNr3);

        MboxButtons.add(jPanel15);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        Mboxtab.add(MboxButtons, gridBagConstraints);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        btnMBoxNew.setBackground(new java.awt.Color(0, 0, 204));
        btnMBoxNew.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMBoxNew.setForeground(new java.awt.Color(255, 255, 255));
        btnMBoxNew.setText(bundle.getString("NEW_MAIL")); // NOI18N
        btnMBoxNew.setToolTipText(bundle.getString("EINGABEFELD_ZUM_SCHREIBEN_EINER_NEUEN_AUSGANGS-EMAIL.")); // NOI18N
        btnMBoxNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMBoxNewActionPerformed(evt);
            }
        });
        jPanel16.add(btnMBoxNew);

        btnMboxSend.setBackground(new java.awt.Color(204, 0, 0));
        btnMboxSend.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMboxSend.setForeground(new java.awt.Color(255, 255, 255));
        btnMboxSend.setText(bundle.getString("SEND_MAIL")); // NOI18N
        btnMboxSend.setToolTipText(bundle.getString("SENDET_EMAIL_AUS_OUTBOX")); // NOI18N
        jPanel16.add(btnMboxSend);

        jPanel20.add(jPanel16);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        btnMboxInBox.setBackground(new java.awt.Color(0, 0, 204));
        btnMboxInBox.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMboxInBox.setForeground(new java.awt.Color(255, 255, 255));
        btnMboxInBox.setText(bundle.getString("INBOX")); // NOI18N
        btnMboxInBox.setToolTipText(bundle.getString("ZUGANG_ZU_GESPEICHERTEN_UND_EMPFANGENEN_EMAIL_IM_NOTEBOOK")); // NOI18N
        jPanel17.add(btnMboxInBox);

        btnMboxOutBox.setBackground(new java.awt.Color(0, 0, 204));
        btnMboxOutBox.setFont(new java.awt.Font("Tahoma", 1, 12));
        btnMboxOutBox.setForeground(new java.awt.Color(255, 255, 255));
        btnMboxOutBox.setText(bundle.getString("OUTBOX")); // NOI18N
        btnMboxOutBox.setToolTipText(bundle.getString("ZUGANG_ZU_GESPEICHERTEN_AUSGANGS-EMAILS.")); // NOI18N
        jPanel17.add(btnMboxOutBox);

        jPanel20.add(jPanel17);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        Mboxtab.add(jPanel20, gridBagConstraints);

        MboxSplitPane.setDividerLocation(350);
        MboxSplitPane.setResizeWeight(0.5);
        MboxSplitPane.setLastDividerLocation(350);

        MBoxLeft.setLayout(new java.awt.GridBagLayout());

        txtAreaMbox2.setColumns(20);
        txtAreaMbox2.setRows(5);
        MBoxLeftUpper2.setViewportView(txtAreaMbox2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        MBoxLeft.add(MBoxLeftUpper2, gridBagConstraints);

        MBoxLeftLower.setMaximumSize(new java.awt.Dimension(500, 50));
        MBoxLeftLower.setMinimumSize(new java.awt.Dimension(500, 50));
        MBoxLeftLower.setPreferredSize(new java.awt.Dimension(500, 50));
        MBoxLeftLower.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        MBoxLeft.add(MBoxLeftLower, gridBagConstraints);

        MboxSplitPane.setLeftComponent(MBoxLeft);

        MBoxRight.setLayout(new javax.swing.BoxLayout(MBoxRight, javax.swing.BoxLayout.LINE_AXIS));
        MboxSplitPane.setRightComponent(MBoxRight);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        Mboxtab.add(MboxSplitPane, gridBagConstraints);

        APRStabPane.addTab("PSK-Mailbox", Mboxtab);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(APRStabPane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        getAccessibleContext().setAccessibleName("INTERMAR PSKmail WinV1.1");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnMBoxDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMBoxDeleteActionPerformed
        // TODO add your handling code here:
        pskmaillogic.mnuMboxDeleteActionPerformed(evt);
}//GEN-LAST:event_btnMBoxDeleteActionPerformed

    private void btnMBoxReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMBoxReadActionPerformed
        // TODO add your handling code here:
        pskmaillogic.mnuMboxReadActionPerformed(evt);
}//GEN-LAST:event_btnMBoxReadActionPerformed

    private void btnMBoxListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMBoxListActionPerformed
        // TODO add your handling code here:
        pskmaillogic.mnuMboxListActionPerformed(evt);
}//GEN-LAST:event_btnMBoxListActionPerformed

    private void btnMBoxConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMBoxConnectActionPerformed
        // TODO add your handling code here:
        pskmaillogic.bConnectActionPerformed(evt);
}//GEN-LAST:event_btnMBoxConnectActionPerformed

    private void btnEmailSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailSendActionPerformed
        // TODO add your handling code here:
        pskmaillogic.SendButtonActionPerformed(evt);
}//GEN-LAST:event_btnEmailSendActionPerformed

    private void btnEmailNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailNewActionPerformed
        // TODO add your handling code here:
        pskmaillogic.mnuNewActionPerformed(evt);
}//GEN-LAST:event_btnEmailNewActionPerformed

    private void btnEmailClearHeadersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailClearHeadersActionPerformed
        // TODO add your handling code here:
        pskmaillogic.mnuHeadersActionPerformed(evt);
}//GEN-LAST:event_btnEmailClearHeadersActionPerformed

    private void btlEmailClearQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btlEmailClearQueueActionPerformed
        // TODO add your handling code here:
        pskmaillogic.mnuMailQueueActionPerformed(evt);
}//GEN-LAST:event_btlEmailClearQueueActionPerformed

    private void btnEmailDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailDeleteActionPerformed
        // TODO add your handling code here:
        pskmaillogic.DeleteButtonActionPerformed(evt);
}//GEN-LAST:event_btnEmailDeleteActionPerformed

    private void btnEmailConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailConnectActionPerformed
        // TODO add your handling code here:
        pskmaillogic.bConnectActionPerformed(evt);
}//GEN-LAST:event_btnEmailConnectActionPerformed

    private void btnFilesDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilesDownloadActionPerformed
        // TODO add your handling code here:
        pskmaillogic.DownloadButtonActionPerformed(evt);
}//GEN-LAST:event_btnFilesDownloadActionPerformed

    private void btnFilesListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilesListActionPerformed
        // TODO add your handling code here:
        pskmaillogic.ListFilesButtonActionPerformed(evt);
}//GEN-LAST:event_btnFilesListActionPerformed

    private void btnFilesAbortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilesAbortActionPerformed
        // TODO add your handling code here:
        pskmaillogic.FileAbortButtonActionPerformed(evt);
}//GEN-LAST:event_btnFilesAbortActionPerformed

    private void btnFilesConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilesConnectActionPerformed
        // TODO add your handling code here:
        pskmaillogic.bConnectActionPerformed(evt);
}//GEN-LAST:event_btnFilesConnectActionPerformed

    private void btnQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitActionPerformed
        // TODO add your handling code here:
        pskmaillogic.mnuQuitActionPerformed(evt);
}//GEN-LAST:event_btnQuitActionPerformed

    private void btnOperatorDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOperatorDataActionPerformed
        // TODO add your handling code here:
        //pnlOperatorData.setVisible(true);
        pskmaillogic.mnuPreferencesActionPerformed(evt);
}//GEN-LAST:event_btnOperatorDataActionPerformed

    private void TabHighlight(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabHighlight
        // TODO add your handling code here:
        javax.swing.JTabbedPane temp = (javax.swing.JTabbedPane) evt.getSource();

        int selected = temp.getSelectedIndex();

        for (int i = 0; i < temp.getTabCount(); i++) {
            if (i != selected) {
                temp.setBackgroundAt(i, new Color(236, 235, 235));
                temp.setForegroundAt(i, new Color(0, 0, 0));
            } else {
                temp.setBackgroundAt(i, new Color(0, 0, 0));
                temp.setForegroundAt(i, new Color(236, 235, 235));
            }
        }
    }//GEN-LAST:event_TabHighlight

    private void btnReadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadFileActionPerformed
        // TODO add your handling code here:
        pskmaillogic.ReadFileActionPerformed(evt);
    }//GEN-LAST:event_btnReadFileActionPerformed

    private void btnEmailAbortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailAbortActionPerformed
        // TODO add your handling code here:
        pskmaillogic.AbortButtonActionPerformed(evt);
    }//GEN-LAST:event_btnEmailAbortActionPerformed

    private void btnMBoxAbortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMBoxAbortActionPerformed
        // TODO add your handling code here:
        pskmaillogic.AbortButtonActionPerformed(evt);
    }//GEN-LAST:event_btnMBoxAbortActionPerformed

    private void btnEmailUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailUpdateActionPerformed
        // TODO add your handling code here:
        pskmaillogic.ServerUpdateButtonActionPerformed(evt);
    }//GEN-LAST:event_btnEmailUpdateActionPerformed

    private void btnMBoxUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMBoxUpdateActionPerformed
        // TODO add your handling code here:
        pskmaillogic.ServerUpdateButtonActionPerformed(evt);
    }//GEN-LAST:event_btnMBoxUpdateActionPerformed

    private void btnMapTEXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMapTEXTActionPerformed
        // TODO add your handling code here:
        pskmaillogic.ZyGribMapActionPerformed(evt);
    }//GEN-LAST:event_btnMapTEXTActionPerformed

    private void btnReadFILEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReadFILEActionPerformed
        // TODO add your handling code here:
        pskmaillogic.ReadTextActionPerformed(evt);
    }//GEN-LAST:event_btnReadFILEActionPerformed

    private void btnMBoxNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMBoxNewActionPerformed
        // TODO add your handling code here:
        pskmaillogic.mnuNewActionPerformed(evt);
    }//GEN-LAST:event_btnMBoxNewActionPerformed

    private void btnClearPosReportWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPosReportWindowActionPerformed
        // TODO add your handling code here:
        setPosReportWindow(new TreeMap<String, TreeMap<String, PosData>>());
    }//GEN-LAST:event_btnClearPosReportWindowActionPerformed

    private void btnEmailQTCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailQTCActionPerformed
        // TODO add your handling code here:
        pskmaillogic.QTCButttonActionPerformed(evt);
    }//GEN-LAST:event_btnEmailQTCActionPerformed

    private void btnEmailReadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailReadActionPerformed
        // TODO add your handling code here:
        pskmaillogic.ReadButtonActionPerformed(evt);
    }//GEN-LAST:event_btnEmailReadActionPerformed

    private void btnDeleteTEXTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTEXTActionPerformed
        // TODO add your handling code here:
        pskmaillogic.DeleteFileActionPerformed(evt);
    }//GEN-LAST:event_btnDeleteTEXTActionPerformed

    private void cboModeFilterItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboModeFilterItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            //System.out.println(cboModeFilter.getSelectedIndex());
            nPanel.setModeFilter(cboModeFilter.getSelectedIndex());
            nPanel.repaint();
        }
    }//GEN-LAST:event_cboModeFilterItemStateChanged

    private void btnCallFinderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCallFinderActionPerformed
        // TODO add your handling code here:
        String temp = txtCallFinder.getText();
        if (!temp.isEmpty()) {
            temp = temp.toUpperCase();
            java.util.TreeMap<String, PosData> tempReport = nPanel.lookUpCallSign(temp);
            if (tempReport != null) {
                if (!tempReport.isEmpty()) {
                    java.util.TreeMap<String, java.util.TreeMap<String, PosData>> tempTree =
                            new java.util.TreeMap<String, java.util.TreeMap<String, PosData>>();
                    tempTree.put(temp, tempReport);
                    PosReportChooser posDataReport = new PosReportChooser(
                            tempTree.keySet(),
                            this,
                            true);

                    posDataReport.setLocationRelativeTo(null);

                    posDataReport.setVisible(true);

                    BoardPanel boardPanel = new BoardPanel(temp, tempReport);

                    boardPanel.setLocationRelativeTo(null);

                    boardPanel.setVisible(true);
                }
            } else {
                System.out.println("not found");
            }
        }
    }//GEN-LAST:event_btnCallFinderActionPerformed

    private void txtCallFinderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCallFinderKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            btnCallFinderActionPerformed(null);
        }
    }//GEN-LAST:event_txtCallFinderKeyPressed

    private void btnSavePosReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePosReportActionPerformed
        // TODO add your handling code here:
        pskmaillogic.WriteFileActionPerformed(evt);
    }//GEN-LAST:event_btnSavePosReportActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        pskmaillogic.DeleteFileActionPerformed(evt);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnWeatherUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWeatherUpdateActionPerformed
        // TODO add your handling code here:
        pskmaillogic.ServerUpdateButtonActionPerformed(evt);
    }//GEN-LAST:event_btnWeatherUpdateActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        AboutMap tempAbout = new AboutMap(this, true);
        tempAbout.setLocationRelativeTo(null);
        tempAbout.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnClearMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearMapActionPerformed
        // TODO add your handling code here:
        setPosReport((TreeMap<String, TreeMap<String, PosData>>) null);
    }//GEN-LAST:event_btnClearMapActionPerformed

    private void btnZoomGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnZoomGridActionPerformed
        // TODO add your handling code here:
        nPanel.toggleOverlayZoomGrid();
    }//GEN-LAST:event_btnZoomGridActionPerformed

    private void btnUpdateShipDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateShipDBActionPerformed
        // TODO add your handling code here:
        Thread download = new Thread(new Runnable() {

            public void run() {
                try {
                    downloadFileCopyTo("http://www.intermar-ev.de/karte/ShipDB.zip", Main.ShipDBPath);

                    //System.out.println("download finished");

                    //replaceFile("_ShipDB.zip", "ShipDB.zip");
                } catch (IOException ex) {
                    Logger.getLogger(mainpskmailintermar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        download.start();
    }//GEN-LAST:event_btnUpdateShipDBActionPerformed

    private void cboTimeModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTimeModeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboTimeModeActionPerformed

    private void cboSeaWeatherItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboSeaWeatherItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSeaWeatherItemStateChanged

    private void btnDeleteFileWindowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFileWindowActionPerformed
        // TODO add your handling code here:
        setFilesTextArea("");
    }//GEN-LAST:event_btnDeleteFileWindowActionPerformed

    private void btnMetareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMetareaActionPerformed
        // TODO add your handling code here:
        nPanel.toggleOverlayMetarea();
    }//GEN-LAST:event_btnMetareaActionPerformed

    private void txtNr1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNr1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNr1ActionPerformed

    private void btnEmailAbort1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailAbort1ActionPerformed
        // TODO add your handling code here:
        pskmaillogic.AbortButtonActionPerformed(evt);
    }//GEN-LAST:event_btnEmailAbort1ActionPerformed

    public void setStatus(String msg) {
        //pnlPSKmailTraffic.setTxtStatus(msg);
        pnlAPRS.setTxtBeaconComment(msg);
    }

    public String getStatus() {
        //return pnlPSKmailTraffic.getTxtStatus();
        return pnlAPRS.getTxtBeaconComment();
    }

    public void setLatitude(String msg) {
        pnlAPRS.setTxtLatitude(msg);
    }

    public String getLatitude() {
        return pnlAPRS.getTxtLatitude();
    }

    public void setLongitude(String msg) {
        pnlAPRS.setTxtLongitude(msg);
    }

    public String getLongitude() {
        return pnlAPRS.getTxtLongitude();
    }

    public void setChkBeacon(boolean state) {
        pnlAPRS.setCbBeaconTime("Auto 10");
    }

    public boolean getChkBeacon() {
        String dummy = pnlAPRS.getCbBeaconTime();
        if (dummy.equals("Manual")) {
            return false;
        } else {
            return true;
        }
    }

    public String getAPRSIcon() {
        return getAPRSIcon();
    }

    public void appendTextArea3(String msg) {
        pnlPSKmailTraffic.appendTxtTraffic(msg);
    }

    public void appendMainWindow(String msg) {
        //pnlPSKmailTraffic.appendJTextArea3(msg);
        txtAreaWx2.append(msg);
        //txtPosReport2.append(msg);
    }

    public void setDCDColor(Color color) {
        // need no implementation in our GUI
    }

    public void appendMSGWindow(String msg) {
        pnlPSKmailTraffic.appendJTextArea3(msg);
    }

    public void appendHeadersWindow(String msg) {
        //this.MailHeadersWindow2.append(msg);
        this.txtAreaWx2.append(msg);
    }

    public void appendFilesTextArea(String string) {
        txtAreaWx2.append(string);
    }

    public void setStatusLabel(String string) {
        pnlPSKmailTraffic.setTxtStatus(string);
    }

    public void setStatusLabelForeground(Color color) {
        pnlPSKmailTraffic.setStatusLabelForeground(color);
    }

    public void setFixAt(String instring) {
        // needs no implementation
        //txtFix.append("---" + instring + "---\n");
        if (!Main.lastKnownFixAt.equals(instring)) {
            Main.GPScounter = 0;
        }
        Main.lastKnownFixAt = instring;
    }

    public void setCourse(String instring) {
        pnlAPRS.setCourse(instring);
    }

    public String getCourse() {
        return pnlAPRS.getCourse();
    }

    public void setSpeed(String instring) {
        pnlAPRS.setSpeed(instring);
    }

    public String getSpeed() {
        return pnlAPRS.getSpeed();
    }

    public void setProgressBarValue(int n) {
        // needs no implementation
    }

    public void setProgressBarStringPainted(boolean b) {
        // needs no implementation
    }

    public void setClock(String instring) {
        pnlPSKmailTraffic.setTxtClock(instring);
    }

    public String getClock() {
        return pnlPSKmailTraffic.getTxtClock();
    }

    public void setConnectButtonText(String text) {
        // needs no implementation
        // no connect button there yet
    }

    public void setFileConnectButtonText(String text) {
        btnFilesConnect.setText(text);
        btnEmailConnect.setText(text);
        btnMBoxConnect.setText(text);

        //if (text.equals(java.util.ResourceBundle.getBundle("javapskmail/mainpskmailintermar").getString("CONNECTED_PSKMAIL_SERVER_ACTUAL."))) {
        if (text.equals(java.util.ResourceBundle.getBundle("javapskmail/mainpskmailintermar").getString("CONNECT"))) {
            btnFilesConnect.setBackground(new Color(204, 0, 0));
            btnEmailConnect.setBackground(new Color(204, 0, 0));
            btnMBoxConnect.setBackground(new Color(204, 0, 0));
        } else {
            btnFilesConnect.setBackground(new Color(0, 204, 0));
            btnEmailConnect.setBackground(new Color(0, 204, 0));
            btnMBoxConnect.setBackground(new Color(0, 204, 0));
        }
    }

    public String getBeaconPeriod() {
        String dummy = pnlAPRS.getCbBeaconTime();
        if (dummy.equals("Auto 10")) {
            return "10";
        }
        if (dummy.equals("Auto 30")) {
            return "30";
        }
        if (dummy.equals("Auto 60")) {
            return "60";
        }
        // no Beacon Period desired (chkBeacon deactivated)
        return "60";
    }

    public String getTxtMainEntry() {
        return pnlPSKmailTraffic.getTxtMainEntry();
    }

    public JButton getFileReadButton() {
        return btnReadFILE;
    }

    public void setFilesTextArea(String str) {
        this.txtAreaWx2.setText(str);
        //this.MailHeadersWindow2.setText(str);
    }

    public void setTxtMainEntry(String str) {
        pnlPSKmailTraffic.setTxtMainEntry(str);
    }

    public boolean getMnuMailScanning() {
        return true;
    }

    public void setMailHeadersWindow(String str) {
        //this.MailHeadersWindow2.setText(str);
        this.txtAreaWx2.setText(str);
    }

    public JButton getUpdateFileButton() {
        // return btnFilesUpdate;
        return new JButton("Dummy");
    }

    public void setAPRSIcon(String string) {
        //cboAPRSIcon.setSelectedItem(string);
        APRSIconsConf tempConf = new APRSIconsConf();
        Main.Icon = string;
        pnlAPRS.setAPRSIcon("");
//ImageIcon temp;
//        switch (string.charAt(0)) {
//            case 'y': temp = new ImageIcon("icons/pri_088.gif");
//                      break;
//            case '-': temp = new ImageIcon("icons/pri_012.gif");
//                      break;
//            case 'Y': temp = new ImageIcon("icons/pri_056.gif");
//                      break;
//            case 's': temp = new ImageIcon("icons/pri_082.gif");
//                      break;
//            case '>': temp = new ImageIcon("icons/pri_029.gif");
//                      break;
//            case 'U': temp = new ImageIcon("icons/pri_052.gif");
//                      break;
//            case ';': temp = new ImageIcon("icons/pri_026.gif");
//                      break;
//            case 'v': temp = new ImageIcon("icons/pri_085.gif");
//                      break;
//            default:  pnlAPRS.setAPRSIcon(new ImageIcon(""));
//                      pnlAPRS.setAPRSIcon(string);
//                      return;
//        }
        //System.out.println("---" + string + "---" + tempConf.getFileName(string));
        //temp = new ImageIcon(tempConf.getFileName(string));
        pnlAPRS.setAPRSIcon(tempConf.getImageIcon(string, 80, 80));
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void updatemodeset(modemmodeenum mymode) {
        Main.defaultmode = mymode;
    }

    public java.awt.Frame getParentFrame() {
        return this;
    }

    /**
     * Add a waypoint to the map
     */
    public void addWaypoint(String lat, String lon) {
    }

    /**
     * Paint the waypoints in the hash table
     */
    public void paintWaypoints() {
    }

    public void clearWaypoints() {
    }

    /*public void perform_ping() {
    myarq.set_txstatus(txstatus.TXPing);
    myarq.send_ping();
    }

    public void perform_beaconaction() {
    myarq.set_txstatus(txstatus.TXBeacon);
    myarq.send_beacon();
    }*/
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mainpskmailintermar().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel APRSLeft;
    private javax.swing.JPanel APRSLeftLower;
    private javax.swing.JPanel APRSLeftUpper;
    private javax.swing.JPanel APRSRight;
    private javax.swing.JSplitPane APRSSplitPane;
    private javax.swing.JPanel APRSbuttons;
    private javax.swing.JPanel APRStab;
    private javax.swing.JTabbedPane APRStabPane;
    private javax.swing.JPanel EmailButtons;
    private javax.swing.JPanel EmailLeft;
    private javax.swing.JPanel EmailLeftLower;
    private javax.swing.JScrollPane EmailLeftUpper2;
    private javax.swing.JPanel EmailRight;
    private javax.swing.JSplitPane EmailSplitPane;
    private javax.swing.JPanel Emailtab;
    private javax.swing.JPanel MBoxLeft;
    private javax.swing.JPanel MBoxLeftLower;
    private javax.swing.JScrollPane MBoxLeftUpper2;
    private javax.swing.JPanel MBoxRight;
    private javax.swing.JTextArea MailHeadersWindow2;
    private javax.swing.JPanel MboxButtons;
    private javax.swing.JSplitPane MboxSplitPane;
    private javax.swing.JPanel Mboxtab;
    private javax.swing.JPanel PosReportButtons;
    private javax.swing.JPanel PosReportLeft;
    private javax.swing.JPanel PosReportLeftLower;
    private javax.swing.JPanel PosReportLeftUpper;
    private javax.swing.JPanel PosReportMapButtons;
    private javax.swing.JSplitPane PosReportPane;
    private javax.swing.JPanel PosReportRight;
    private javax.swing.JPanel PosReporttab;
    private javax.swing.JPanel WXButtons;
    private javax.swing.JPanel WXLeft;
    private javax.swing.JPanel WXLeftLower;
    private javax.swing.JPanel WXRight;
    private javax.swing.JSplitPane WXSplitPane;
    private javax.swing.JPanel WXtab;
    private javax.swing.JScrollPane WxLeftUpper2;
    private javax.swing.JButton btlEmailClearQueue;
    private javax.swing.JButton btnCallFinder;
    private javax.swing.JButton btnClearMap;
    private javax.swing.JButton btnClearPosReportWindow;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteFileWindow;
    private javax.swing.JButton btnDeleteTEXT;
    private javax.swing.JButton btnEmailAbort;
    private javax.swing.JButton btnEmailAbort1;
    private javax.swing.JButton btnEmailClearHeaders;
    private javax.swing.JButton btnEmailConnect;
    private javax.swing.JButton btnEmailDelete;
    private javax.swing.JButton btnEmailInBox;
    private javax.swing.JButton btnEmailNew;
    private javax.swing.JButton btnEmailOutBox;
    private javax.swing.JButton btnEmailQTC;
    private javax.swing.JButton btnEmailRead;
    private javax.swing.JButton btnEmailSend;
    private javax.swing.JButton btnEmailUpdate;
    private javax.swing.JButton btnFilesAbort;
    private javax.swing.JButton btnFilesConnect;
    private javax.swing.JButton btnFilesDownload;
    private javax.swing.JButton btnFilesList;
    private javax.swing.JButton btnMBoxAbort;
    private javax.swing.JButton btnMBoxConnect;
    private javax.swing.JButton btnMBoxDelete;
    private javax.swing.JButton btnMBoxList;
    private javax.swing.JButton btnMBoxNew;
    private javax.swing.JButton btnMBoxRead;
    private javax.swing.JButton btnMBoxUpdate;
    private javax.swing.JButton btnMapTEXT;
    private javax.swing.JButton btnMboxInBox;
    private javax.swing.JButton btnMboxOutBox;
    private javax.swing.JButton btnMboxSend;
    private javax.swing.JButton btnMetarea;
    private javax.swing.JButton btnOperatorData;
    private javax.swing.JButton btnQuit;
    private javax.swing.JButton btnReadFILE;
    private javax.swing.JButton btnReadFile;
    private javax.swing.JButton btnSavePosReport;
    private javax.swing.JButton btnUpdateShipDB;
    private javax.swing.JButton btnWeatherUpdate;
    private javax.swing.JButton btnZoomGrid;
    private javax.swing.JComboBox cboMapChoice;
    private javax.swing.JComboBox cboModeFilter;
    private javax.swing.JComboBox cboSeaWeather;
    private javax.swing.JComboBox cboTimeMode;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblModeFilter;
    private javax.swing.JLabel lblStatusMapPanel;
    private javax.swing.JLabel lblTimeInterval;
    private javax.swing.JList lstWeatherData;
    private javax.swing.JPanel newPosReportMapPanel;
    private javax.swing.JPanel pnlPosReportMap;
    private javax.swing.JPanel pnlProgress;
    private javax.swing.JPanel pnlSeaWeather;
    private javax.swing.JTextArea txtAreaMbox2;
    private javax.swing.JTextArea txtAreaWx2;
    private javax.swing.JTextField txtCallFinder;
    private javax.swing.JTextField txtNr1;
    private javax.swing.JTextField txtNr2;
    private javax.swing.JTextField txtNr3;
    private javax.swing.JTextArea txtPosReport2;
    private javax.swing.JPanel txtStatusMapPanel;
    // End of variables declaration//GEN-END:variables
    private pskmailtrafficpanel pnlPSKmailTraffic;
    private fourbuttons pnlFourButtons;
    private OperatorDataDialog pnlOperatorData;
    private APRSpanel pnlAPRS;
    private PosReportMapPanel pnlPosReportMapPanel;

    public void setPosReportWindow(TreeMap<String, TreeMap<String, PosData>> pTree) {
        setPosReport("");
        if (pTree != null) {
            appendPosReportWindow(pTree);
        }
    }

    public void appendPosReportWindow(ArrayList<PosData> pList) {
        Iterator<PosData> iter1 = pList.iterator();

        while (iter1.hasNext()) {
            appendPosReport(iter1.next().toString() + "\n");
        }
    }

    public void appendPosReportWindow(TreeMap<String, TreeMap<String, PosData>> pTree) {
        Iterator<TreeMap<String, PosData>> iter1 = pTree.values().iterator();

        while (iter1.hasNext()) {
            Iterator<PosData> iter2 = iter1.next().values().iterator();
            while (iter2.hasNext()) {
                appendPosReport(iter2.next().toString() + "\n");
            }
        }
    }

    public void appendPosReport(String str) {
        txtPosReport2.append(str);
    }

    public void setPosReport(String str) {
        txtPosReport2.setText(str);
    }

    public String getPosReport() {
        return txtPosReport2.getText();
    }

    public void setSpnMinute(int i) {
        // I do not know the persone as I.
    }

    public String getSpnMinute() {
        // no implementation yet
        return "";
    }

    public void removeAllCboServer() {
        //throw new UnsupportedOperationException("Not supported yet.");
        pnlAPRS.getServerCbox().removeAllItems();
    }

    public void addCboServer(String server) {
        //throw new UnsupportedOperationException("Not supported yet.");
        pnlAPRS.getServerCbox().addItem(server);
    }

    public void setSelectedCboServer(String server) {
        //throw new UnsupportedOperationException("Not supported yet.");
        pnlAPRS.getServerCbox().setSelectedItem(server);
    }

    public String getSelectedCboServer() {
        //throw new UnsupportedOperationException("Not supported yet.");
        return (String) pnlAPRS.getServerCbox().getSelectedItem();
    }

    public void setSelectedCboAPRSIcon(String instring) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void appendFilesTxtArea(String instring) {
        this.txtAreaWx2.append(instring);
        //this.MailHeadersWindow2.append(instring);
    }

    public void setLabelStatus(String instring) {
        //throw new UnsupportedOperationException("Not supported yet.");
        this.pnlPSKmailTraffic.setStatusLabel(instring);
    }

    public void updatemailscanset(Integer what) {
        // empty so far
    }

    public void addServer(String server) {
        //throw new UnsupportedOperationException("Not supported yet.");
        pnlAPRS.getServerCbox().addItem(server);
    }

    public void setServer(String server) {
        pnlAPRS.getServerCbox().setSelectedItem(server);
    }

    public void setBoardWX(String str) {
        pnlAPRS.setBoardWX(str);
    }

    public String getBoardWX() {
        return pnlAPRS.getBoardWX();
    }

    public WXSetupDialog getWXDialog() {
        return pnlAPRS.getWXDialog();
    }

    public void setOperatorName(String str) {
        pnlAPRS.setOperatorName(str);
    }

    public String getOperatorName() {
        return pnlAPRS.getOperatorName();
    }

    public void setPicturePath(String str) {
        pnlAPRS.setPicturePath(str);
    }

    public String getPicturePath() {
        return pnlAPRS.getPicturePath();
    }

    public void setPictureName(String str) {
        pnlAPRS.setPictureName(str);
    }

    public String getPictureName() {
        return pnlAPRS.getPictureName();
    }

    public void setCallsign(String str) {
        pnlAPRS.setCallsign(str);
    }

    public JButton getPosReadButton() {
        return btnReadFile;
    }

    public JButton getTextReadButton() {
        return btnReadFILE;
    }

    public JButton getSYNOPReadButton() {
        return btnReadFILE;
    }

    public JButton getGRIBReadButton() {
        return btnReadFILE;
    }

    public void setLinkPeriod(int l) {
        pnlAPRS.setLinkPeriod(l);
    }

    public int getLinkPeriod() {
        return pnlAPRS.getLinkPeriod();
    }

    public void setLinkIndicator(boolean b) {
        pnlAPRS.setLinkIndicator(b);
    }

    public void setGPSIndicator(boolean b) {
        pnlAPRS.setGPSIndicator(b);
    }

    public void setLinkTime(String str) {
        pnlAPRS.setPosTime(str);
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        if (e.getSource().equals(cboMapChoice)) {
            mapInfFileScanner = new ScannerMapInfFiles(cboMapChoice);

            cboMapChoice.removeAllItems();

            Iterator<String> iter = mapInfFileScanner.getInfFileList().iterator();
            while (iter.hasNext()) {
                String name = iter.next();

                //System.out.println(name);

                String mapName = "";
                try {
                    mapName = (new ParseMapInfFile(new File(name + ".inf"))).getMapName();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(mainpskmailintermar.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(mainpskmailintermar.class.getName()).log(Level.SEVERE, null, ex);
                }

                cboMapChoice.addItem(name + ": " + mapName);
            }
        }
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        if (e.getSource().equals(cboMapChoice)) {
            String mapInfo = (String) cboMapChoice.getSelectedItem();
            Pattern p = Pattern.compile("(\\S+): .*");
            Matcher m = p.matcher(mapInfo);
            if (m.matches()) {
                mapInfo = m.group(1);
            }
            if (mainPosReport != null) {
                changePosReportMap(mapInfo + ".inf", mapInfo + ".gif", mainPosReport);
            } else {
                mainPosReport = new TreeMap<String, TreeMap<String, PosData>>();
                changePosReportMap(mapInfo + ".inf", mapInfo + ".gif", mainPosReport);
            }
            repaint();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cboSeaWeather)) {
            int weatherMode = cboSeaWeather.getSelectedIndex();
            nPanel.setSeaWeatherDraw(weatherMode);
            if (!Main.seaWeatherParser.isParsed()) {
                Main.seaWeatherParser.parseThis();
            }
            Main.seaWeatherParser.getInternalDatabase().writeFile(new File(Main.wxPath));
            if (weatherMode > 0) {
                TreeSet<String> tempSet = new TreeSet<String>(new WeekDayComparator(0));
                tempSet.addAll(Main.seaWeatherParser.getInternalDatabase().keySet());
                
                if (!tempSet.isEmpty()) {
                    pnlSeaWeather.setVisible(true);
                    pnlSeaWeather.repaint();

                    lstWeatherData.setListData(tempSet.toArray());
                    lstWeatherData.setSelectedIndex(0);

                    //SeaWeatherOptionPanel tempPanel = new SeaWeatherOptionPanel(this,
                    //        true,
                    //        tempSet);
                    //tempPanel.setLocationRelativeTo(null);
                    //tempPanel.setVisible(true);
                    //String timeStamp = tempPanel.getTimeStamp();

                    String timeStamp = (String) lstWeatherData.getSelectedValue();
                    // System.out.println("time stamp selected: " + timeStamp);
                    nPanel.setSeaWeatherTimeStamp(timeStamp);
                } else {
                    pnlSeaWeather.setVisible(false);
                    pnlSeaWeather.repaint();

                    weatherMode = 0;
                    cboSeaWeather.setSelectedIndex(0);
                    nPanel.setSeaWeatherTimeStamp("");
                }
            } else {
                pnlSeaWeather.setVisible(false);
                pnlSeaWeather.repaint();

                nPanel.setSeaWeatherTimeStamp("");
            }
            nPanel.repaint();
        }
    }

    public void popupMenuCanceled(PopupMenuEvent e) { }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource().equals(cboTimeMode)) {
            String timeMode = (String) cboTimeMode.getSelectedItem();
            if (timeMode.equals("infinite")) {
                nPanel.setTimeFilter(0);
                nPanel.repaint();
            } else {
                Pattern p = Pattern.compile("(\\d+) day[s]{0,1}");
                Matcher m = p.matcher(timeMode);
                if (m.matches()) {
                    //System.out.println(m.group(1));
                    nPanel.setTimeFilter(new Integer(m.group(1)));
                    nPanel.repaint();
                }
            }
        }
    }

    public fourbuttons getPanelFour() {
        return pnlFourButtons;
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(lstWeatherData)) {
            String timeStamp = (String) lstWeatherData.getSelectedValue();
            // System.out.println("time stamp selected: " + timeStamp);
            nPanel.setSeaWeatherTimeStamp(timeStamp);
            nPanel.repaint();
        }
    }

    public void setOperatingMode(String str) {
        pnlAPRS.setOperatingMode(str);
    }
}
