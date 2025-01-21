/*
 * mapmanager.java
 *
 * Copyright (C) 2009 Pär Crusefalk (SM0RWO)
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

import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author per
 */
public class mapmanager {

    JXMapKit mymap;     // The map object
    Waypoint userpos;   // Station position
    Set<Waypoint> waypoints = new HashSet<Waypoint>();  // Hash table for waypoints on the map
    private WaypointPainter painter;

    private void mapmanager() {
        // Initialize objects
        userpos = new Waypoint(0, 0);

    }

    /**
     * Give manager class a handle to the map object
     * @param map
     */
    public void setmapobject(JXMapKit map) {
        mymap = map;
    }

    /**
     * Returns a reference to the map object used
     * @return
     */
    public JXMapKit getmapobject() {
        return mymap;
    }

    /**
     * Check the latitude, range is -90..90
     * @param mylat
     * @return
     */
    private boolean checklat(Float mylat) {
        boolean myres = false;
        if (mylat >= -90 && mylat <= 90) {
            myres = true;
        }
        return myres;
    }

    /**
     * Check the longitude, range is -180..180
     * @param mylon
     * @return
     */
    private boolean checklon(Float mylon) {
        boolean myres = false;
        if (mylon >= -180 && mylon <= 180) {
            myres = true;
        }
        return myres;
    }

    /**
     * Add a waypoint to the hash table
     * @param lat
     * @param lon
     */
    public void addWaypoint(String lat, String lon) {
        Integer mylat = new Integer(lat);
        Integer mylon = new Integer(lon);
        waypoints.add(new Waypoint(mylat, mylon));
    }

    /**
     * Clear the waypoint hash table
     */
    public void clearWaypoints() {
        waypoints.clear();
    }

    public void paintWaypoints() {
        try {
            painter = new WaypointPainter();
            painter.setWaypoints(waypoints);
            painter.setRenderer(new WaypointRenderer() {

                public boolean paintWaypoint(Graphics2D g, JXMapViewer map, Waypoint wp) {
                    g.setColor(Color.RED);
                    g.drawLine(-5, -5, +5, +5);
                    g.drawLine(-5, +5, +5, -5);
                    return true;
                }
            });
            mymap.getMainMap().setOverlayPainter(painter);

        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
        }
    }

    public void centermappos(String lat, String lon) {
        mymap.setAddressLocation(new GeoPosition(41.881944, -87.627778));
    }

    /**
     * Update local stations position on map
     * @param lat
     * @param lon
     */
    public void updatemyposition(Float lat, Float lon) {
        try {
            userpos.setPosition(new GeoPosition(lat, lon));
        } catch (Exception ex) {
            // Hmmm, what to do here ;-)
        }
    }
}
