/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PosReportMapPanel.java
 *
 * Created on 14.08.2009, 15:42:00
 */
package javapskmail;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author sebastian_pohl
 */
public class PosReportMapPanel extends javax.swing.JPanel
        implements MouseInputListener {

    // control variable for variable sea weather options
    private int seaWeatherDraw = 0;
    // control variable for the timestamp of the weather file
    private String seaWeatherTimeStamp = "";
    // activates overlay of ZoomGrid, only globus2.gif !!!
    private boolean overlayZoomGrid = false;
    // activates overlay of Metarea data
    private boolean overlayMetarea = false;
    // activates track plotting (drawing lines between consecutive pos
    // datas
    private boolean drawTracking = false;
    // activates board panel activity for this instance of the map panel
    private boolean iconsClickable = true;
    // state machine states
    private final static int MOUSE_BUTTON_1_PRESSEDDOWN = 1;
    private final static int MOUSE_BUTTON_1_RELEASED = 2;
    private int button_state = MOUSE_BUTTON_1_RELEASED;
    // time Filter to extract only time-valid Pos Data (0 = infinity)
    private int timeFilter = 0;
    // mode Filter (0 = none, 3 = all)
    private int modeFilter = 3;
    // map data files and images
    File mapInf = null;
    ImageIcon mapIcon = null;
    Image mapImage = null;
    File posFile = null;
    // parent Frame for dialog reference
    java.awt.Frame parentFrame = null;
    // list of Pos Data entries
    TreeMap<String, TreeMap<String, PosData>> posReport = null;
    // list of mouse over Pos Data entries (cursor with rectangle boundaries)
    private TreeMap<String, TreeMap<String, PosData>> mouseOverPosReport = null;
    private SeaWeatherDatabase seaWeatherDB = null;
    // frame database for location with weatherreports
    private TreeMap<String, Rectangle> locationDB = null;
    // frame database for metarea data icons
    private TreeMap<Integer, Rectangle> metareaDB = null;
    // contains the map ratio
    // to be determined by constructor or when you change the image
    double imageRatio = 1.0;
    double boundaryRatio = 1.0;
    int absolutesrcx1 = 1;
    int absolutesrcy1 = 1;
    int absolutesrcx2 = 1;
    int absolutesrcy2 = 1;
    // left-upper corner (x1,y1), right-lower corner (x2,y2) of visible part
    // of the image
    int srcx1 = 1;
    int srcy1 = 1;
    int srcx2 = 1;
    int srcy2 = 1;
    // the widht and height of the currently drawn picture in the panel
    int boundaryWidth = 1;
    int boundaryHeight = 1;
    // mouse position x,y coordinates
    int mouseX = 0;
    int mouseY = 0;
    // stored mouse positions , if button 1 was pressed
    int storedMouseX = 0;
    int storedMouseY = 0;
    // object to store Map Inf details
    ParseMapInfFile infFileParser;
    // object to store Pos File details
    ParsePosReportFile posFileParser;
    // posReportDialog object
    PosReportChooser posDataDialog;

    public void setSeaWeatherDraw(int i) {
        seaWeatherDraw = i;
    }

    public void setSeaWeatherTimeStamp(String str) {
        seaWeatherTimeStamp = str;
    }

    public void toggleOverlayZoomGrid() {
        overlayZoomGrid = !overlayZoomGrid;
        //iconsClickable = !b;
        //drawTracking = !b;
        repaint();
    }

    public void toggleOverlayMetarea() {
        overlayMetarea = !overlayMetarea;

        // System.out.println("Metarea toggled.");

        repaint();
    }

    public void setIconsClickable(boolean b) {
        iconsClickable = b;
    }

    public void setDrawTracking(boolean b) {
        drawTracking = b;
    }

    public TreeMap<String, PosData> lookUpCallSign(String str) {
        //TreeMap<String, PosData> tempList = new TreeMap<String, PosData>(new ReverseStringComparator());
        //PosData tempPos;
        //if ((tempPos = posReport.get(str)) != null)
        //    tempList.put(tempPos.getDate() + " " + tempPos.getTime(), tempPos);
        if (posReport.get(str) != null) {
            return posReport.get(str);
        } else {
            return null;
        }
    }

    /** Creates new form PosReportMapPanel */
    private PosReportMapPanel() {
        initComponents();

        addMouseMotionListener(this);
        addMouseListener(this);
    }

    /** Creates a new MapPanel */
    public PosReportMapPanel(java.awt.Frame frame, File mapInf, ImageIcon mapIcon) {
        this();

        this.parentFrame = frame;

        this.mapInf = mapInf;
        this.mapIcon = mapIcon;
        this.mapImage = mapIcon.getImage();

        this.absolutesrcx1 = 1;
        this.absolutesrcy1 = 1;
        this.absolutesrcx2 = mapIcon.getIconWidth();
        this.absolutesrcy2 = mapIcon.getIconHeight();

        this.srcx1 = 1;
        this.srcy1 = 1;
        this.srcx2 = mapIcon.getIconWidth();
        this.srcy2 = mapIcon.getIconHeight();

        this.imageRatio = (double) (srcy2 - srcy1 + 1) / (double) (srcx2 - srcx1 + 1);

        this.locationDB = new TreeMap<String, Rectangle>();

        this.metareaDB = new TreeMap<Integer, Rectangle>();

        try {
            // parse Map Inf file
            infFileParser = new ParseMapInfFile(this.mapInf);

            calculateInternals();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PosReportMapPanel(java.awt.Frame frame,
            File mapInf,
            ImageIcon mapIcon,
            File posFile,
            int mode) {
        this(frame, mapInf, mapIcon);

        this.posFile = posFile;

        try {
            // parse Pos Report file
            posFileParser = new ParsePosReportFile(this.posFile, mode);
            // instantiate posReport
            posReport = posFileParser.getPosReport();

            mouseOverPosReport = new TreeMap<String, TreeMap<String, PosData>>(new ReverseStringComparator());

            calculateInternals();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PosReportMapPanel(java.awt.Frame frame,
            File mapInf,
            ImageIcon mapIcon,
            TreeMap<String, TreeMap<String, PosData>> posReport) {
        this(frame, mapInf, mapIcon);

        this.posFile = null;
        this.posFileParser = null;

        try {
            this.posReport = posReport;

            mouseOverPosReport = new TreeMap<String, TreeMap<String, PosData>>(new ReverseStringComparator());

            calculateInternals();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setModeFilter(int i) {
        //System.out.println("reset filter");
        modeFilter = i;
    }

    public void setTimeFilter(int d) {
        // sets time filter to the number of days shown in the past
        // 0 = infinity
        timeFilter = d;
    }

    private boolean checkModeFilter(PosData p) {
        switch (modeFilter) {
            // nothing shown
            case 0:
                return false;
            // only show mode 1
            case 1:
                if (p.getOrigin() != 1) {
                    return false;
                }
                break;
            // only show mode 2
            case 2:
                if (p.getOrigin() != 2) {
                    return false;
                }
                break;
            // everything shown
            case 3:
                break;
            default:
                break;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = new Date();
        Date posDataDate = new Date();
        try {
            posDataDate = dateFormat.parse(p.getDate() + " " + p.getTime());
        } catch (ParseException ex) {
            return false;
        }

        //System.out.println((today.getTime() - posDataDate.getTime()) / 1000);

        // time in seconds from days
        if (timeFilter != 0) {
            int timeInSeconds = timeFilter * 86400;

            //System.out.println(timeInSeconds);

            if (timeInSeconds != 0) {
                return (((double) (today.getTime() - posDataDate.getTime()) / 1000) < timeInSeconds);
            }
        }

        return true;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        calculateMouseOverPosReport();
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        calculateMouseOverPosReport();

        repaint();
    }

    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    public void mousePressed(MouseEvent e) {
        //System.out.println(e.getModifiersEx());

        if (((e.getModifiersEx() & (java.awt.event.InputEvent.BUTTON1_DOWN_MASK
                | java.awt.event.InputEvent.BUTTON2_DOWN_MASK
                | java.awt.event.InputEvent.BUTTON3_DOWN_MASK))
                == java.awt.event.InputEvent.BUTTON1_DOWN_MASK)
                && (button_state == MOUSE_BUTTON_1_RELEASED)) {
            // change state
            button_state = MOUSE_BUTTON_1_PRESSEDDOWN;
            mouseMoved(e);
            // store current mouse position
            storedMouseX = mouseX;
            storedMouseY = mouseY;
        }

        if ((e.getModifiersEx() & (java.awt.event.InputEvent.BUTTON1_DOWN_MASK
                | java.awt.event.InputEvent.BUTTON2_DOWN_MASK
                | java.awt.event.InputEvent.BUTTON3_DOWN_MASK))
                == java.awt.event.InputEvent.BUTTON3_DOWN_MASK) {
            srcx1 = absolutesrcx1;
            srcx2 = absolutesrcx2;
            srcy1 = absolutesrcy1;
            srcy2 = absolutesrcy2;

            repaint();
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (((e.getModifiersEx()
                & (java.awt.event.InputEvent.BUTTON1_DOWN_MASK
                | java.awt.event.InputEvent.BUTTON2_DOWN_MASK
                | java.awt.event.InputEvent.BUTTON3_DOWN_MASK)) == 0)
                && (button_state == MOUSE_BUTTON_1_PRESSEDDOWN)) {
            // change state
            button_state = MOUSE_BUTTON_1_RELEASED;
            //System.out.println("Mouse released");
            mouseMoved(e);
            // store current mouse position
            if ((Math.abs(storedMouseX - mouseX) < 3) && (Math.abs(storedMouseY - mouseY) < 3)) {
                HashSet<String> tempSet = new HashSet<String>();

                if (iconsClickable) {
                    tempSet.addAll(mouseOverPosReport.keySet());
                }

                if (seaWeatherDraw > 0) {
                    Set<String> tempKeys = locationDB.keySet();
                    Object[] _keys = tempKeys.toArray();

                    for (int i = 0; i < _keys.length; i++) {
                        if (_keys[i].getClass().getName().equals("java.lang.String")) {
                            if (locationDB.get((String) _keys[i]).contains(mouseX, mouseY)) {
                                tempSet.add((String) _keys[i]);
                            }
                        }
                    }
                }

                if (overlayMetarea) {
                    Set<Integer> tempKeys = metareaDB.keySet();
                    Object[] _keys = tempKeys.toArray();

                    for (int i = 0; i < _keys.length; i++) {
                        if (_keys[i].getClass().getName().equals("java.lang.Integer")) {
                            if (metareaDB.get((Integer) _keys[i]).contains(mouseX, mouseY)) {
                                tempSet.add(_keys[i].toString());
                            }
                        }
                    }
                }

                if (!tempSet.isEmpty()) {
                    // if (!mouseOverPosReport.isEmpty() && iconsClickable) {

                    if (tempSet.size() > 1) {
                        posDataDialog = new PosReportChooser(
                                tempSet,
                                this.parentFrame, true);

                        posDataDialog.setLocationRelativeTo(null);

                        posDataDialog.setVisible(true);

                        String callSign;

                        if ((callSign = posDataDialog.getCallSign()) != null) {
                            if (mouseOverPosReport.containsKey(callSign)) {
                                BoardPanel boardPanel = new BoardPanel(callSign, mouseOverPosReport.get(callSign));

                                boardPanel.setLocationRelativeTo(null);

                                boardPanel.setVisible(true);
                            }
                            if (locationDB.containsKey(callSign)) {
                                WeatherPanel weatherPanel = new WeatherPanel(callSign,
                                        seaWeatherDB.getReportAsString(callSign));

                                weatherPanel.setLocationRelativeTo(null);

                                weatherPanel.setVisible(true);
                            }
                            try {
                                if (metareaDB.containsKey(new Integer(callSign))) {
                                    String temp = "";

                                    try {
                                        Scanner tempScan = new Scanner(new File(Main.metareaPath + "/metarea/metarea" + callSign + ".txt"));

                                        while (tempScan.hasNextLine()) {
                                            temp += tempScan.nextLine() + "\n";
                                        }

                                        MetareaPanel metareaPanel = new MetareaPanel(new Integer(callSign),
                                                temp);

                                        metareaPanel.setLocationRelativeTo(null);

                                        metareaPanel.setVisible(true);
                                    } catch (Exception exc) {
                                    }
                                }
                            } catch (NumberFormatException exc) {
                                // exc.printStackTrace();
                            }
                        }
                    } else {
                        Iterator<String> iter = tempSet.iterator();

                        if (iter.hasNext()) {
                            String callSign = iter.next();


                            if (mouseOverPosReport.containsKey(callSign)) {
                                //String callSign = mouseOverPosReport.firstKey();

                                BoardPanel boardPanel = new BoardPanel(callSign, mouseOverPosReport.get(callSign));

                                boardPanel.setLocationRelativeTo(null);

                                boardPanel.setVisible(true);
                            }
                            if (locationDB.containsKey(callSign)) {
                                WeatherPanel weatherPanel = new WeatherPanel(callSign,
                                        seaWeatherDB.getReportAsString(callSign));

                                weatherPanel.setLocationRelativeTo(null);

                                weatherPanel.setVisible(true);
                            }
                            try {
                                if (metareaDB.containsKey(new Integer(callSign))) {
                                    String temp = "";

                                    try {
                                        Scanner tempScan = new Scanner(new File(Main.metareaPath + "/metarea" + callSign + ".txt"));

                                        while (tempScan.hasNextLine()) {
                                            temp += tempScan.nextLine() + "\n";
                                        }

                                        MetareaPanel metareaPanel = new MetareaPanel(new Integer(callSign),
                                                temp);

                                        metareaPanel.setLocationRelativeTo(null);

                                        metareaPanel.setVisible(true);
                                    } catch (Exception exc) {
                                    }
                                }
                            } catch (NumberFormatException exc) {
                                //exc.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                //System.out.println(storedMouseX + "," + storedMouseY + " " + mouseX + "," + mouseY);

                int tempsrcx1 = min(storedMouseX, mouseX);
                tempsrcx1 = (int) (((double) tempsrcx1 / (double) boundaryWidth) * (srcx2 - srcx1) + srcx1);
                int tempsrcx2 = max(storedMouseX, mouseX);
                tempsrcx2 = (int) (((double) tempsrcx2 / (double) boundaryWidth) * (srcx2 - srcx1) + srcx1);
                int tempsrcy1 = min(storedMouseY, mouseY);
                tempsrcy1 = (int) (((double) tempsrcy1 / (double) boundaryHeight) * (srcy2 - srcy1) + srcy1);
                int tempsrcy2 = max(storedMouseY, mouseY);
                tempsrcy2 = (int) (((double) tempsrcy2 / (double) boundaryHeight) * (srcy2 - srcy1) + srcy1);

                srcx1 = tempsrcx1;
                srcx2 = tempsrcx2;
                srcy1 = tempsrcy1;
                srcy2 = tempsrcy2;

                //System.out.println(srcx1 + "," + srcx2 + " " + srcy1 + "," + srcy2);

                repaint();
            }
        }
    }

    private int min(int x, int y) {
        if (x < y) {
            return x;
        } else {
            return y;
        }
    }

    private int max(int x, int y) {
        if (x > y) {
            return x;
        } else {
            return y;
        }
    }

    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    public void mouseExited(MouseEvent e) {
        // do nothing
    }

    public TreeMap<String, TreeMap<String, PosData>> getPosReport() {
        return this.posReport;
    }

    public void addPosData(PosData posData) {
        TreeMap<String, PosData> tempList = new TreeMap<String, PosData>(new ReverseStringComparator());

        String tempCallSign = posData.getCallsign();
        String tempDate = posData.getDate() + " " + posData.getTime();

        if (posReport.get(tempCallSign) != null) {
            posReport.get(tempCallSign).put(tempDate, posData);
        } else {
            tempList.put(tempDate, posData);
            posReport.put(tempCallSign, tempList);
        }

        repaint();
    }

    public void addPosReport(ArrayList<PosData> posDataList) {
        Iterator<PosData> iter = posDataList.iterator();
        while (iter.hasNext()) {
            addPosData(iter.next());
        }
    }

    public void addPosReport(TreeMap<String, PosData> posDataList) {
        ArrayList<PosData> temp = (ArrayList<PosData>) posDataList.values();
        Iterator<PosData> iter = temp.iterator();
        while (iter.hasNext()) {
            addPosData(iter.next());
        }
    }

    public void setPosReport(TreeMap<String, TreeMap<String, PosData>> posDataList) {
        this.posReport = posDataList;
        // setting posReport new, invalidates current posFileParser
        posFileParser = null;

        repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    private void calculateInternals() {
        //System.out.println("height: " + getHeight() + "," + "width: " + getWidth());

        this.boundaryRatio = (double) getHeight() / (double) getWidth();

        this.imageRatio = (double) (srcy2 - srcy1 + 1) / (double) (srcx2 - srcx1 + 1);

        //System.out.println("boundary: " + boundaryRatio);

        if (imageRatio >= boundaryRatio) {
            boundaryWidth = (int) ((double) getHeight() / imageRatio);
            boundaryHeight = getHeight();
        } else {
            boundaryWidth = getWidth();
            boundaryHeight = (int) ((double) getWidth() * imageRatio);
        }
    }

    public MapCoordinate constructX(int x) {
        double relGetLeft = (double) (x) / (double) (boundaryWidth);
        relGetLeft = relGetLeft * (double) (srcx2 - srcx1) + srcx1;
        relGetLeft = (relGetLeft - absolutesrcx1) / (double) (absolutesrcx2 - absolutesrcx1);
        relGetLeft = relGetLeft * (double) (infFileParser.getRight().getSignedDecimal() - infFileParser.getLeft().getSignedDecimal());
        relGetLeft = relGetLeft + (double) (infFileParser.getLeft().getSignedDecimal());
        if (relGetLeft < 0.0) {
            return new MapCoordinate(Math.abs(relGetLeft), 'W');
        } else {
            return new MapCoordinate(Math.abs(relGetLeft), 'E');
        }
    }

    public MapCoordinate constructY(int y) {
        //System.out.println(boundaryHeight + "," + srcy2 + "," + srcy1 + "," + absolutesrcy2 + "," + absolutesrcy1 + ","
        //        + infFileParser.getLower().getSignedDecimal() + "," + infFileParser.getUpper().getSignedDecimal());

        double relGetLeft = (double) (y) / (double) (boundaryHeight);
        relGetLeft = (relGetLeft * (double) (srcy2 - srcy1)) + srcy1;
        relGetLeft = (relGetLeft - absolutesrcy1) / (double) (absolutesrcy2 - absolutesrcy1);
        relGetLeft = (1 - relGetLeft) * (double) (infFileParser.getUpper().getSignedDecimal() - infFileParser.getLower().getSignedDecimal());
        relGetLeft = relGetLeft + (double) (infFileParser.getLower().getSignedDecimal());
        if (relGetLeft < 0.0) {
            return new MapCoordinate(Math.abs(relGetLeft), 'S');
        } else {
            return new MapCoordinate(Math.abs(relGetLeft), 'N');
        }
    }

    // calculates X coordinate on the image according to the boundaries and
    // the coordinate given on earth
    private int getX(MapCoordinate c) {
        double relGetLeft = (double) (srcx1 - absolutesrcx1 + 1) / (double) (absolutesrcx2 - absolutesrcx1 + 1);
        relGetLeft = relGetLeft * (infFileParser.getRight().getSignedDecimal() - infFileParser.getLeft().getSignedDecimal());
        relGetLeft = relGetLeft + infFileParser.getLeft().getSignedDecimal();

        double relGetRight = (double) (srcx2 - absolutesrcx1 + 1) / (double) (absolutesrcx2 - absolutesrcx1 + 1);
        relGetRight = relGetRight * (infFileParser.getRight().getSignedDecimal() - infFileParser.getLeft().getSignedDecimal());
        relGetRight = relGetRight + infFileParser.getLeft().getSignedDecimal();

        double deltax1 = (c.getSignedDecimal() - relGetLeft);
        double deltax2 = (relGetRight - relGetLeft);
        //double deltax1 = (c.getSignedDecimal() - infFileParser.getLeft().getSignedDecimal());
        //double deltax2 = (infFileParser.getRight().getSignedDecimal() - infFileParser.getLeft().getSignedDecimal());
        return (int) ((deltax1 / deltax2) * boundaryWidth);
    }

    // calculates Y coordinate on the image according to the boundaries and
    // the coordinate given on earth
    private int getY(MapCoordinate c) {
        double relGetUpper = (double) (srcy1 - absolutesrcy1 + 1) / (double) (absolutesrcy2 - absolutesrcy1 + 1);
        relGetUpper = relGetUpper * (infFileParser.getLower().getSignedDecimal() - infFileParser.getUpper().getSignedDecimal());
        relGetUpper = relGetUpper + infFileParser.getUpper().getSignedDecimal();

        double relGetLower = (double) (srcy2 - absolutesrcy1 + 1) / (double) (absolutesrcy2 - absolutesrcy1);
        relGetLower = relGetLower * (infFileParser.getLower().getSignedDecimal() - infFileParser.getUpper().getSignedDecimal());
        relGetLower = relGetLower + infFileParser.getUpper().getSignedDecimal();

        double deltay1 = (c.getSignedDecimal() - relGetUpper);
        double deltay2 = (relGetLower - relGetUpper);
        //double deltay1 = (c.getSignedDecimal() - infFileParser.getUpper().getSignedDecimal());
        //double deltay2 = (infFileParser.getLower().getSignedDecimal() - infFileParser.getUpper().getSignedDecimal());
        return (int) ((deltay1 / deltay2) * boundaryHeight);
    }

    private void drawZoomGrid(Graphics g) {

        Color c = g.getColor();
        g.setColor(Color.YELLOW);

        //g.drawString("This is overlay mode", 2, 2);

        /*** drawing horizontal lines ***/
        g.drawLine(getX(new MapCoordinate(180.0, 'W')),
                getY(new MapCoordinate(45.0, 'N')),
                getX(new MapCoordinate(180.0, 'E')),
                getY(new MapCoordinate(45.0, 'N')));

        g.drawLine(getX(new MapCoordinate(180.0, 'W')),
                getY(new MapCoordinate(0.0, 'N')),
                getX(new MapCoordinate(180.0, 'E')),
                getY(new MapCoordinate(0.0, 'N')));

        g.drawLine(getX(new MapCoordinate(180.0, 'W')),
                getY(new MapCoordinate(45.0, 'S')),
                getX(new MapCoordinate(180.0, 'E')),
                getY(new MapCoordinate(45.0, 'S')));

        /*** drawing vertical lines ***/
        g.drawLine(getX(new MapCoordinate(135.0, 'W')),
                getY(new MapCoordinate(90.0, 'N')),
                getX(new MapCoordinate(135.0, 'W')),
                getY(new MapCoordinate(90.0, 'S')));

        g.drawLine(getX(new MapCoordinate(90.0, 'W')),
                getY(new MapCoordinate(90.0, 'N')),
                getX(new MapCoordinate(90.0, 'W')),
                getY(new MapCoordinate(90.0, 'S')));

        g.drawLine(getX(new MapCoordinate(45.0, 'W')),
                getY(new MapCoordinate(90.0, 'N')),
                getX(new MapCoordinate(45.0, 'W')),
                getY(new MapCoordinate(90.0, 'S')));

        g.drawLine(getX(new MapCoordinate(0.0, 'W')),
                getY(new MapCoordinate(90.0, 'N')),
                getX(new MapCoordinate(0.0, 'W')),
                getY(new MapCoordinate(90.0, 'S')));

        g.drawLine(getX(new MapCoordinate(135.0, 'E')),
                getY(new MapCoordinate(90.0, 'N')),
                getX(new MapCoordinate(135.0, 'E')),
                getY(new MapCoordinate(90.0, 'S')));

        g.drawLine(getX(new MapCoordinate(90.0, 'E')),
                getY(new MapCoordinate(90.0, 'N')),
                getX(new MapCoordinate(90.0, 'E')),
                getY(new MapCoordinate(90.0, 'S')));

        g.drawLine(getX(new MapCoordinate(45.0, 'E')),
                getY(new MapCoordinate(90.0, 'N')),
                getX(new MapCoordinate(45.0, 'E')),
                getY(new MapCoordinate(90.0, 'S')));

        Font saveFont = g.getFont();

        g.setFont(new Font("Serif", Font.BOLD, 18));

        g.drawString("1", getX(new MapCoordinate(157.5, 'W')),
                getY(new MapCoordinate(80.0, 'N')));

        g.drawString("2", getX(new MapCoordinate(112.5, 'W')),
                getY(new MapCoordinate(80.0, 'N')));

        g.drawString("3", getX(new MapCoordinate(67.5, 'W')),
                getY(new MapCoordinate(80.0, 'N')));

        g.drawString("4", getX(new MapCoordinate(22.5, 'W')),
                getY(new MapCoordinate(80.0, 'N')));

        g.drawString("8", getX(new MapCoordinate(157.5, 'E')),
                getY(new MapCoordinate(80.0, 'N')));

        g.drawString("7", getX(new MapCoordinate(112.5, 'E')),
                getY(new MapCoordinate(80.0, 'N')));

        g.drawString("6", getX(new MapCoordinate(67.5, 'E')),
                getY(new MapCoordinate(80.0, 'N')));

        g.drawString("5", getX(new MapCoordinate(22.5, 'E')),
                getY(new MapCoordinate(80.0, 'N')));

        g.drawString("A", getX(new MapCoordinate(175.0, 'W')),
                getY(new MapCoordinate(67.5, 'N')));

        g.drawString("B", getX(new MapCoordinate(175.0, 'W')),
                getY(new MapCoordinate(22.5, 'N')));

        g.drawString("C", getX(new MapCoordinate(175.0, 'W')),
                getY(new MapCoordinate(22.5, 'S')));

        g.drawString("D", getX(new MapCoordinate(175.0, 'W')),
                getY(new MapCoordinate(67.5, 'S')));

        g.setFont(saveFont);

        g.setColor(c);

    }

    private void drawSeaWeather(Graphics g) {

        if (!Main.seaWeatherParser.isParsed()) {
            Main.seaWeatherParser.parseThis();

            Main.seaWeatherParser.getInternalDatabase().writeFile(new File(Main.wxPath));
        }

        seaWeatherDB = Main.seaWeatherParser.getInternalDatabase();

        drawTheWeather(g, seaWeatherDraw);

    }

    private void drawMetarea(Graphics g) {
        int tempX = -1;
        int tempY = -1;

        metareaDB = new TreeMap<Integer, Rectangle>();

        int j = 0;

        for (int i = 0; i < 18; i++) {
            if (i < 16)
                j = i;
            else {
                if (i == 16) j = 7;
                if (i == 17) j = 2;
            }
            if (Main.metareaList.containsKey(i + 1)
                    && new File(Main.metareaPath + "/metarea" + (i + 1) + ".txt").exists()) {

                // System.out.println(i + "," + j);

                if ((constructX(1).getSignedDecimal() < Main.metareaList.get(i + 1).getLongitude().getSignedDecimal())// &&
                        && (constructX(boundaryWidth).getSignedDecimal() > Main.metareaList.get(i + 1).getLongitude().getSignedDecimal())
                        && ((constructY(1).getSignedDecimal() > Main.metareaList.get(i + 1).getLatitude().getSignedDecimal()))
                        && ((constructY(boundaryHeight).getSignedDecimal() < Main.metareaList.get(i + 1).getLatitude().getSignedDecimal()))) {

                    // System.out.println(Main.metareaList.get(i+1));

                    tempX = getX(Main.metareaList.get(i + 1).getLongitude());
                    tempY = getY(Main.metareaList.get(i + 1).getLatitude());

                    // System.out.println(tempX + "," + tempY);


                    g.drawImage((new ImageIcon("metarea/metarea" + (j + 1) + ".gif")).getImage(),
                            tempX - 15, tempY - 15, 30, 30, this);

                    if (!metareaDB.containsKey(i + 1)) {
                        metareaDB.put(i + 1, new Rectangle(tempX - 15, tempY - 15, 30, 30));
                    }
                }
            }
        }
    }

    private void drawTheWeather(Graphics g, int i) {

        if (seaWeatherTimeStamp.equals("")) {
            return;
        }

        SeaWeather tempWeather;

        int tempX = -1;
        int tempY = -1;

        // Set<String> setOfKeys = seaWeatherDB.keySet();

        TreeMap<String, SeaWeather> tempTree = seaWeatherDB.get(seaWeatherTimeStamp);

        Iterator<SeaWeather> iter = tempTree.values().iterator();

        locationDB = new TreeMap<String, Rectangle>();

        while (iter.hasNext()) {
            tempWeather = iter.next();

            if ((constructX(1).getSignedDecimal() < tempWeather.getLocationLong().getSignedDecimal())// &&
                    && (constructX(boundaryWidth).getSignedDecimal() > tempWeather.getLocationLong().getSignedDecimal())
                    && ((constructY(1).getSignedDecimal() > tempWeather.getLocationLat().getSignedDecimal()))
                    && ((constructY(boundaryHeight).getSignedDecimal() < tempWeather.getLocationLat().getSignedDecimal()))) {

                tempX = getX(tempWeather.getLocationLong());
                tempY = getY(tempWeather.getLocationLat());

                String fileName = "";

                String waveHeight = tempWeather.getWaveHeight();

                String windSpeed = tempWeather.getWindSpeed();

                Pattern p = Pattern.compile("(\\d+).*");
                Matcher m = p.matcher(windSpeed);
                if (m.matches()) {
                    windSpeed = m.group(1);
                } //else
                //break;

                if (windSpeed.length() < 2) {
                    windSpeed = "0" + windSpeed;
                }


                String gustSpeed = tempWeather.getGustSpeed();

                p = Pattern.compile("(\\d+).*");
                m = p.matcher(gustSpeed);
                if (m.matches()) {
                    gustSpeed = m.group(1);
                } //else
                //break;

                if ((gustSpeed.length() < 2) && (!gustSpeed.equals(""))) {
                    gustSpeed = "0" + gustSpeed;
                }

                String windDirection = tempWeather.getWindDirection();

                p = Pattern.compile("(\\w+).*");
                m = p.matcher(windDirection);

                if (m.matches()) {
                    windDirection = m.group(1);
                } //else
                //break;

                //System.out.println(windSpeed + "," + gustSpeed + "," + windDirection);

                switch (i) {
                    case 1:
                        fileName = "windspeeds/"
                                + windDirection + "_" + windSpeed + ".gif";
                        break;
                    case 2:
                        fileName = "windspeeds/"
                                + windDirection + "_" + gustSpeed + ".gif";
                        break;
                    case 3:
                        fileName = "waveheights/"
                                + waveHeight.replaceAll("\\.", "_") + "m.gif";
                        break;
                    default:
                        return;
                }

                g.drawImage((new ImageIcon(fileName)).getImage(),
                        tempX - 15, tempY - 15, 30, 30, this);

                if (!locationDB.containsKey(tempWeather.getLocationName())) {
                    locationDB.put(tempWeather.getLocationName(),
                            new Rectangle(tempX - 15, tempY - 15, 30, 30));
                }
            }
        }
    }

    private void drawPosReport(Graphics g, TreeMap<String, TreeMap<String, PosData>> posReport) {
        APRSIconsConf tempConf = null;

        tempConf = new APRSIconsConf();

        Iterator<TreeMap<String, PosData>> tempIter = posReport.values().iterator();
        PosData tempPos = null;
        while (tempIter.hasNext()) {

            TreeMap<String, PosData> tempTree = tempIter.next();
            Iterator<PosData> tempIter2 = tempTree.values().iterator();

            int tempX = -1;
            int tempY = -1;
            int tempPrioX = -1;
            int tempPrioY = -1;
            boolean prior = false;

            while (tempIter2.hasNext()) {

                if (checkModeFilter(tempPos = tempIter2.next())) {// && {

                    if ((constructX(1).getSignedDecimal() < tempPos.getLongitude().getSignedDecimal())// &&
                            && (constructX(boundaryWidth).getSignedDecimal() > tempPos.getLongitude().getSignedDecimal())
                            && ((constructY(1).getSignedDecimal() > tempPos.getLatitude().getSignedDecimal()))
                            && ((constructY(boundaryHeight).getSignedDecimal() < tempPos.getLatitude().getSignedDecimal()))) {
                        //(infFileParser.getUpper().getSignedDecimal() > tempPos.getLongitude().getSignedDecimal()) &&
                        //(infFileParser.getLower().getSignedDecimal() < tempPos.getLongitude().getSignedDecimal()))

                        //System.out.println(constructX(1).getSignedDecimal() + " " + 1 + " " +
                        //        constructX(boundaryWidth).getSignedDecimal() + " " + boundaryWidth + " " +
                        //        tempPos.getLongitude().getSignedDecimal());

                        tempX = getX(tempPos.getLongitude());
                        tempY = getY(tempPos.getLatitude());

                        //System.out.println(tempX + " " + tempY);

                        // calculate rectangle to contain the map information for this callsign
                        tempPos.setGuiRect(new Rectangle(tempX - 10, tempY - 10, 20, 20));

                        if (!prior && drawTracking) {
                            g.setColor(Color.RED);
                            g.fillRect(tempX - 10, tempY - 10, 20, 20);
                            g.setColor(Color.BLACK);
                        }

                        //g.drawImage((new ImageIcon("icons/pri_012.gif")).getImage(), tempX - 10, tempY - 10, 20, 20, this);
                        g.drawImage(tempConf.getImageIcon(tempPos.getAprs(), 20, 20).getImage(), tempX - 10, tempY - 10, 20, 20, this);



                        String overlay = null;
                        FontMetrics fm = g.getFontMetrics();

                        if ((overlay = tempConf.getOverlay(tempPos.getAprs())) != null) {
                            g.setColor(Color.YELLOW);
                            g.drawString(overlay,
                                    tempX - (fm.stringWidth(overlay) / 2),
                                    tempY + (fm.getAscent() / 2));
                        }

                        Font tempFont = g.getFont();
                        g.setFont(new Font("SansSerif", Font.PLAIN, 9));

                        g.setColor(Color.WHITE);

                        g.fillRect(tempX - 21,
                                tempY + 20 - fm.getAscent() + 1,
                                fm.stringWidth(tempPos.getCallsign()) + 2,
                                fm.getHeight() - fm.getDescent());

                        g.setColor(Color.BLACK);
                        g.drawString(tempPos.getCallsign(), tempX - 20, tempY + 20);

                        g.setFont(tempFont);

                    }
                }
                // if draw Tracking disabled, break while loop after first drawing
                if (!drawTracking) {
                    break;
                } // otherwise draw the lines eventually between consecutive pos data
                else {
                    if (prior) {
                        g.setColor(Color.WHITE);
                        g.drawLine(tempPrioX, tempPrioY, tempX, tempY);
                        g.setColor(Color.BLACK);
                    } else {
                        prior = true;
                    }
                    // new prior coordinates
                    tempPrioX = tempX;
                    tempPrioY = tempY;
                }
            }
        }
    }

    private void calculateMouseOverPosReport() {
        // reinitialise report variable
        mouseOverPosReport = new TreeMap<String, TreeMap<String, PosData>>(new ReverseStringComparator());

        // iterate over the whole report
        Iterator<TreeMap<String, PosData>> tempIter = posReport.values().iterator();
        TreeMap<String, PosData> tempPos = null;
        while (tempIter.hasNext()) {
            if (checkModeFilter((tempPos = tempIter.next()).firstEntry().getValue())) {
                // if current cursor position is within the rectangle, add
                // PosData to the report
                //try {
                if (tempPos.firstEntry().getValue().getGuiRect() != null) {
                    if (tempPos.firstEntry().getValue().getGuiRect().contains(mouseX, mouseY)
                            && (new Rectangle(1, 1, boundaryWidth, boundaryHeight)).contains(mouseX, mouseY)) {
                        mouseOverPosReport.put(tempPos.firstEntry().getValue().getCallsign(), tempPos);
                    }
                }
                //} catch (NullPointerException e) {
                //    System.out.println(tempPos.firstEntry().getValue().getGuiRect());
                //}
            }
        }

        //System.out.println(mouseOverPosReport.isEmpty());

        //System.out.println(mouseOverPosReport + "\n");
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        calculateInternals();

        // System.out.println(overlayZoomGrid);

        // drawing part of the picture, which should be visible
        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, boundaryWidth, boundaryHeight,
                    srcx1, srcy1, srcx2, srcy2, this);
        }

        // drawing pos Report, as it is available
        if (posReport != null) {
            drawPosReport(g, posReport);
        }

        if (seaWeatherDraw > 0) {
            drawSeaWeather(g);
        }

        if (overlayMetarea) {
            drawMetarea(g);
        }

        if (overlayZoomGrid) {
            // System.out.println("Drawing zoom grid");
            if (mapInf.getName().equals("physWorld.inf")) {
                drawZoomGrid(g);
            }
        }

        // drawing a white rectangle, if we want to zoom in
        if (button_state == MOUSE_BUTTON_1_PRESSEDDOWN) {
            //System.out.println(mouseX + "," + mouseY);
            int tempsrcx1 = min(storedMouseX, mouseX);
            int tempsrcx2 = max(storedMouseX, mouseX);
            int tempsrcy1 = min(storedMouseY, mouseY);
            int tempsrcy2 = max(storedMouseY, mouseY);
            g.setColor(java.awt.Color.WHITE);
            g.drawRect(tempsrcx1, tempsrcy1, tempsrcx2 - tempsrcx1, tempsrcy2 - tempsrcy1);
        }

        calculateMouseOverPosReport();


    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();

        JPanel testPanel = new PosReportMapPanel(frame, new File("physWorld.inf"), new ImageIcon("physWorld.gif"),
                new File("posreport2.txt"), 1);

        frame.add(testPanel);
        frame.setTitle("Demo");
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
