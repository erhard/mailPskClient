/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * BoardPanel.java
 *
 * Created on 16.09.2009, 15:02:03
 */

package javapskmail;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author sebastian_pohl
 */
public class BoardPanel extends javax.swing.JFrame {

    private String callSign = null;
    private TreeMap<String, PosData> posReport = null;

    private class BoardPanelTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int rowIndex,
                int vColIndex) {

            if ((rowIndex + 1) % 2 == 0) {
                //setBackground(Color.LIGHT_GRAY);
                setBackground(new Color(202,232,255));
            } else {
                setBackground(Color.WHITE);
            }

            return super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, rowIndex, vColIndex);

        }

        @Override
        public void validate() {}
        @Override
        public void revalidate() {}
        @Override
        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
        @Override
	public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}

    }

    //private class BoardTable extends JTable {
    private void setPreferredColumnWidths(JTable table, double[] percentages) {

        Dimension tableDim = table.getPreferredSize();

        //System.out.println(tableDim.width);

        double total = 0;
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            total += percentages[i];
        }

        for (int i = 0; i < table.getColumnModel().getColumnCount() - 1; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            //column.setPreferredWidth((int) (tableDim.width * (percentages[i] / total)));
            //column.setMinWidth((int) (tableDim.width * (percentages[i] / total)));
            //column.setMaxWidth((int) (tableDim.width * (percentages[i] / total)));
            column.setMinWidth(120);
            column.setMaxWidth(120);
        }

    }

    //}

    private Vector buildVector(PosData p) {
        Vector rowVector = new Vector();

        // rowVector.add(p.getCallsign());
        rowVector.add(p.getDate() + " " + p.getTime());
        rowVector.add(p.getLatitude().getGeodesic());
        rowVector.add(p.getLongitude().getGeodesic());
        if (p.isOptionalFlag()) {
            rowVector.add(p.getOptional());
        } else
            rowVector.add("");

        return rowVector;
    }
    
    private void buildTable(TreeMap<String, TreeMap<String, PosData>> tempReport) {
        DefaultTableModel tempModel = new DefaultTableModel();

        // build the columnHeader
        Vector columnHeaderVector = new Vector();
        // columnHeaderVector.add("Callsign");
        columnHeaderVector.add(java.util.ResourceBundle.getBundle("javapskmail/BoardPanel").getString("DATE/TIME"));
        columnHeaderVector.add(java.util.ResourceBundle.getBundle("javapskmail/BoardPanel").getString("LATITUDE"));
        columnHeaderVector.add(java.util.ResourceBundle.getBundle("javapskmail/BoardPanel").getString("LONGITUDE"));
        columnHeaderVector.add(java.util.ResourceBundle.getBundle("javapskmail/BoardPanel").getString("STATUS_COMMENT"));

        // build row Vectors
        Vector rowVector = new Vector();

        Iterator<String> iterOuter = tempReport.keySet().iterator();

        // outer loop for callsigns (normally only one, this is just redundancy)
        while (iterOuter.hasNext()) {
            // get report for the iterated key
            TreeMap<String, PosData> tempTree = tempReport.get(iterOuter.next());

            Iterator<String> iterInner = tempTree.keySet().iterator();

            // inner loop for date/time stamps of the outer callsign
            while (iterInner.hasNext()) {

                PosData p = tempTree.get(iterInner.next());

                rowVector.add(buildVector(p));

            }
        }

        tempModel.setDataVector(rowVector, columnHeaderVector);
        tblTrackReport.setModel(tempModel);
    }

    /** Creates new form BoardPanel */
    public BoardPanel(String callSign,
            TreeMap<String, PosData> posReport) {
        this.callSign = callSign;
        this.posReport = posReport;

        initComponents();

        setTitle(java.util.ResourceBundle.getBundle("javapskmail/BoardPanel").getString("YACHT-INFO_FOR_") + callSign);

        java.util.TreeMap<String, TreeMap<String, PosData>> tempReport =
                new TreeMap<String, TreeMap<String, PosData>>();
        tempReport.put(callSign, posReport);

        // build table of track report here
        tblTrackReport.setDefaultRenderer(Object.class, new BoardPanelTableCellRenderer());
        buildTable(tempReport);

        
        /* ---------------------- */

        /* ---- GPS Panel ------- */
        PosData last = posReport.get(posReport.firstKey());
        lblPosGPS.setText(last.getLatitude().getGeodesic() + "   " + last.getLongitude().getGeodesic());
        lblDateTimeGPS.setText(last.getDate() + "   " + last.getTime());

        lblBoardTime.setText(last.getBoardTime());
        lblBoardDate.setText(last.getBoardDate());
        /* ---------------------- */

        // build map for track report here
        PosReportMapPanel mPanel = new PosReportMapPanel(this,
                new File("physWorld.inf"),
                new ImageIcon("physWorld.gif"), tempReport);

        mPanel.setIconsClickable(false);
        mPanel.setDrawTracking(true);
        mPanel.setBackground(new Color(247,245,240));
        /* ------------------------------- */

        pnlMap.add(mPanel);
        try {
            /* ---------------------- */
            /* get ship info */
            File shipDB = new File(Main.ShipDBPath);

            ShipData s = null;

            lblYachtInfo.setText(java.util.ResourceBundle.getBundle("javapskmail/BoardPanel").getString("YACHT-INFO_FOR_") + callSign);

            s = (new ParseShipDataFile(shipDB)).getDataBase().get(callSign);

            if (s != null) {

                lblSkipper.setText(s.getName());
                lblCountry.setText(s.getCountry());
                lblShip.setText(s.getShip());
                lblRemark.setText(s.getRemark());
                lblCrew.setText(s.getCrew());

                // System.out.println(s.getWebsitephoto());

                ImageShipDataFile datafile = new ImageShipDataFile(shipDB,
                        s.getWebsitephoto().substring(2));

                BufferedImage shipImage = datafile.getShipImage();

                ImagePanel tempPanel = new ImagePanel(new ImageIcon(shipImage));
                tempPanel.setBackground(new Color(247, 245, 240));
                pnlShipPhoto.add(tempPanel);
            }

        } catch (IOException ex) {
            System.out.println("Ship DB not found\n");
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            System.out.println("Something screwed");
            ex.printStackTrace();
        }

        setPreferredColumnWidths(tblTrackReport, new double[]{0.2, 0.2, 0.2, 0.4});

        /* ---------------------- */
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private class ImagePanel extends JPanel {

        private ImageIcon icon;

        public ImagePanel(ImageIcon i) {
            icon = i;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            int tempWidth = 0;
            int tempHeight = 0;

            // System.out.println(this.getWidth() + " " + this.getHeight());

            double iconRatio = (double) icon.getIconWidth() / (double) icon.getIconHeight();

            if ((int) ((double) this.getHeight() * iconRatio) > this.getWidth()) {
                tempWidth = this.getWidth();
                tempHeight = (int) ((double) this.getWidth() / iconRatio);
            } else {
                tempHeight = this.getHeight();
                tempWidth = (int) ((double) this.getHeight() * iconRatio);
            }

            g.drawImage(icon.getImage(), 0, 0, tempWidth, tempHeight, this);
        }

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

        pnlShip = new javax.swing.JPanel();
        pnlShipInfo = new javax.swing.JPanel();
        lblYachtInfo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblSkipper = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblCountry = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblShip = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblRemark = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblCrew = new javax.swing.JLabel();
        pnlShipPhoto = new javax.swing.JPanel();
        pnlMap = new javax.swing.JPanel();
        pnlTime = new javax.swing.JPanel();
        pnlInlayer = new javax.swing.JPanel();
        pnlGPS = new javax.swing.JPanel();
        lblPos2 = new javax.swing.JLabel();
        lblPosGPS = new javax.swing.JLabel();
        pnllDateTime = new javax.swing.JPanel();
        lblDate2 = new javax.swing.JLabel();
        lblDateTimeGPS = new javax.swing.JLabel();
        pnlBoardTime = new javax.swing.JPanel();
        lblBoardTime1 = new javax.swing.JLabel();
        lblBoardTime = new javax.swing.JLabel();
        pnlBoardDate = new javax.swing.JPanel();
        lblBoardDate1 = new javax.swing.JLabel();
        lblBoardDate = new javax.swing.JLabel();
        pnlTracking = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTrackReport = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(960, 560));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlShip.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlShip.setMaximumSize(new java.awt.Dimension(480, 320));
        pnlShip.setMinimumSize(new java.awt.Dimension(480, 320));
        pnlShip.setPreferredSize(new java.awt.Dimension(480, 320));
        pnlShip.setLayout(new java.awt.GridBagLayout());

        pnlShipInfo.setBackground(new java.awt.Color(247, 245, 240));
        pnlShipInfo.setMinimumSize(new java.awt.Dimension(240, 250));
        pnlShipInfo.setPreferredSize(new java.awt.Dimension(240, 250));
        pnlShipInfo.setLayout(new java.awt.GridBagLayout());

        lblYachtInfo.setFont(new java.awt.Font("Tahoma", 1, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlShipInfo.add(lblYachtInfo, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("javapskmail/BoardPanel"); // NOI18N
        jLabel1.setText(bundle.getString("BoardPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.16;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlShipInfo.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.16;
        pnlShipInfo.add(lblSkipper, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel3.setText(bundle.getString("BoardPanel.jLabel3.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.16;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlShipInfo.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.16;
        pnlShipInfo.add(lblCountry, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel5.setText(bundle.getString("BoardPanel.jLabel5.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.16;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlShipInfo.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.16;
        pnlShipInfo.add(lblShip, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel7.setText(bundle.getString("BoardPanel.jLabel7.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.16;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlShipInfo.add(jLabel7, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.16;
        pnlShipInfo.add(lblRemark, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel9.setText(bundle.getString("BoardPanel.jLabel9.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 0.16;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        pnlShipInfo.add(jLabel9, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 0.16;
        pnlShipInfo.add(lblCrew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.6;
        pnlShip.add(pnlShipInfo, gridBagConstraints);

        pnlShipPhoto.setBackground(new java.awt.Color(247, 245, 240));
        pnlShipPhoto.setMinimumSize(new java.awt.Dimension(240, 250));
        pnlShipPhoto.setPreferredSize(new java.awt.Dimension(240, 250));
        pnlShipPhoto.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.6;
        pnlShip.add(pnlShipPhoto, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(pnlShip, gridBagConstraints);

        pnlMap.setBackground(new java.awt.Color(247, 245, 240));
        pnlMap.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlMap.setMaximumSize(new java.awt.Dimension(480, 320));
        pnlMap.setMinimumSize(new java.awt.Dimension(480, 320));
        pnlMap.setPreferredSize(new java.awt.Dimension(480, 320));
        pnlMap.setLayout(new java.awt.GridLayout(1, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.weighty = 0.5;
        getContentPane().add(pnlMap, gridBagConstraints);

        pnlTime.setBackground(new java.awt.Color(202, 232, 255));
        pnlTime.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlTime.setPreferredSize(new java.awt.Dimension(960, 60));
        pnlTime.setLayout(new java.awt.GridLayout(1, 1));

        pnlInlayer.setLayout(new java.awt.GridBagLayout());

        pnlGPS.setBackground(new java.awt.Color(202, 232, 255));
        pnlGPS.setLayout(new java.awt.GridBagLayout());

        lblPos2.setBackground(new java.awt.Color(202, 232, 255));
        lblPos2.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblPos2.setText(bundle.getString("BoardPanel.lblPos2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlGPS.add(lblPos2, gridBagConstraints);

        lblPosGPS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPosGPS.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlGPS.add(lblPosGPS, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlInlayer.add(pnlGPS, gridBagConstraints);

        pnllDateTime.setBackground(new java.awt.Color(202, 232, 255));
        pnllDateTime.setLayout(new java.awt.GridBagLayout());

        lblDate2.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblDate2.setText(bundle.getString("BoardPanel.lblDate2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnllDateTime.add(lblDate2, gridBagConstraints);

        lblDateTimeGPS.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnllDateTime.add(lblDateTimeGPS, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlInlayer.add(pnllDateTime, gridBagConstraints);

        pnlBoardTime.setBackground(new java.awt.Color(202, 232, 255));
        pnlBoardTime.setLayout(new java.awt.GridBagLayout());

        lblBoardTime1.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblBoardTime1.setText(bundle.getString("BoardPanel.lblBoardTime1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlBoardTime.add(lblBoardTime1, gridBagConstraints);

        lblBoardTime.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlBoardTime.add(lblBoardTime, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlInlayer.add(pnlBoardTime, gridBagConstraints);

        pnlBoardDate.setBackground(new java.awt.Color(202, 232, 255));
        pnlBoardDate.setLayout(new java.awt.GridBagLayout());

        lblBoardDate1.setFont(new java.awt.Font("Tahoma", 1, 12));
        lblBoardDate1.setText(bundle.getString("BoardPanel.lblBoardDate1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlBoardDate.add(lblBoardDate1, gridBagConstraints);

        lblBoardDate.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlBoardDate.add(lblBoardDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        pnlInlayer.add(pnlBoardDate, gridBagConstraints);

        pnlTime.add(pnlInlayer);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.05;
        getContentPane().add(pnlTime, gridBagConstraints);

        pnlTracking.setBackground(new java.awt.Color(247, 245, 240));
        pnlTracking.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        pnlTracking.setPreferredSize(new java.awt.Dimension(960, 180));
        pnlTracking.setLayout(new java.awt.GridLayout(1, 0));

        tblTrackReport.setBackground(new java.awt.Color(247, 245, 240));
        tblTrackReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CallSign", "Date/Time", "Latitude", "Longitude", "Status Comment"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblTrackReport);

        pnlTracking.add(jScrollPane3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.4;
        getContentPane().add(pnlTracking, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BoardPanel(null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblBoardDate;
    private javax.swing.JLabel lblBoardDate1;
    private javax.swing.JLabel lblBoardTime;
    private javax.swing.JLabel lblBoardTime1;
    private javax.swing.JLabel lblCountry;
    private javax.swing.JLabel lblCrew;
    private javax.swing.JLabel lblDate2;
    private javax.swing.JLabel lblDateTimeGPS;
    private javax.swing.JLabel lblPos2;
    private javax.swing.JLabel lblPosGPS;
    private javax.swing.JLabel lblRemark;
    private javax.swing.JLabel lblShip;
    private javax.swing.JLabel lblSkipper;
    private javax.swing.JLabel lblYachtInfo;
    private javax.swing.JPanel pnlBoardDate;
    private javax.swing.JPanel pnlBoardTime;
    private javax.swing.JPanel pnlGPS;
    private javax.swing.JPanel pnlInlayer;
    private javax.swing.JPanel pnlMap;
    private javax.swing.JPanel pnlShip;
    private javax.swing.JPanel pnlShipInfo;
    private javax.swing.JPanel pnlShipPhoto;
    private javax.swing.JPanel pnlTime;
    private javax.swing.JPanel pnlTracking;
    private javax.swing.JPanel pnllDateTime;
    private javax.swing.JTable tblTrackReport;
    // End of variables declaration//GEN-END:variables

}
