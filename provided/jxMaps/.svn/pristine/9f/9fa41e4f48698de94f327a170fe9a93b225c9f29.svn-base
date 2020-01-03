package provided.jxMaps.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.teamdev.jxmaps.LatLng;

/**
 * A collection of JxMaps-related definitions
 * @author swong
 *
 */
public interface IJxMaps_Defs {

	/**
	 * The approximate zoom level to see the Rice University campus
	 */
	public static final double ZOOM_RICE = 15.0;


	/**
	 * Nested interface to hold definitions for events.    These events types can technically
	 * be set on any MapObject (map, marker, etc) but note that some events are for 
	 * applicable to any MapObject while some only apply to maps.   That is, some event types 
	 * would never be triggered on some non-map MapObject types.   Also, the JxMaps system allows 
	 * non-intuitive and probably nonsensical types of events to be registered, e.g. both the 
	 * reasonable MapMouseEvents and the unreasonable MapEvents can be registered with a CLICK event.
	 * On the other hand, registering a MapMouseEvent with a CENTER_CHANGED event will result
	 * in that MapMouseEvent will cause an internal JxMaps null pointer exception.
	 * @author swong
	 *
	 */
	public static interface IEvent {	
	
		/**
		 * Event ID for an event generated when the animation of an element is changed
		 */
		public static final String ANIMATION_CHANGED = "animation_changed";

		/**
		 * Event ID for an event generated when the bounds of the map change
		 */
		public static final String BOUNDS_CHANGED = "bounds_changed";

		/**
		 * Event ID for an event generated when the center of the map changes
		 */
		public static final String CENTER_CHANGED = "center_changed";

		/**
		 * Event ID for an event generated when the (left) mouse button is clicked on the object
		 */
		public static final String CLICK = "click";

		/**
		 * Event ID for an event generated when the clickability of an object is changed
		 */
		public static final String CLICKABLE_CHANGED = "clickable_changed";

		/**
		 * Event ID for an event generated when a mouse click on the "x" icon on an object is used to close the object.
		 */
		public static final String CLOSE_CLICK = "closeclick";

		/**
		 * Event ID for an event generated when an object's content is changed.
		 */
		public static final String CONTENT_CHANGED = "content_changed";

		/**
		 * Event ID for an event generated when the mouse cursor is changed
		 */
		public static final String CURSOR_CHANGED = "cursor_changed";

		/**
		 * Event ID for an event generated when the (left) mouse button is double-clicked on the object
		 */
		public static final String DBL_CLICK = "dblclick";

		/**
		 * Event ID for an event generated when the HTML DOM (Document Object Model) is ready to display the map.
		 */
		public static final String DOM_READY = "domready";

		/**
		 * Event ID for an event generated when the a mouse drag of an object is performed
		 */
		public static final String DRAG = "drag";

		/**
		 * Event ID for an event generated when the mouse drag of an object ends
		 */
		public static final String DRAG_END = "dragend";

		/**
		 * Event ID for an event generated when the mouse drag of an object starts
		 */
		public static final String DRAG_START = "dragstart";

		/**
		 * Event ID for an event generated when the draggability of an object changes
		 */
		public static final String DRAGGABLE_CHANGED = "draggable_changed";

		/**
		 * Event ID for an event generated when the object's flat property is changed.
		 */
		public static final String FLAT_CHANGED = "flat_changed";

		/**
		 * Event ID for an event generated when a map's compass heading is changed.
		 */
		public static final String HEADING_CHANGED = "heading_changed";

		/**
		 * Event ID for an event generated when the icon associated with an object is changed
		 */
		public static final String ICON_CHANGED = "icon_changed";

		/**
		 * Event ID for an event generated when map becomes idle after being moved.
		 */
		public static final String IDLE = "idle";

		/**
		 * Event ID for an event generated when the map's type is changed.
		 */
		public static final String MAPTYPEID_CHANGED = "maptypeid_changed";

		/**
		 * Event ID for an event generated when the (left) mouse button is depressed.
		 */
		public static final String MOUSE_DOWN = "mousedown";

		/**
		 * Event ID for an event generated when the mouse is moved
		 */
		public static final String MOUSE_MOVE = "mousemove";

		/**
		 * Event ID for an event generated when a moves off of an object.
		 */
		public static final String MOUSE_OUT = "mouseout";

		/**
		 * Event ID for an event generated when the mouse is over an object.
		 */
		public static final String MOUSE_OVER = "mouseover";

		/**
		 * Event ID for an event generated when the (left) mouse button is released.
		 */
		public static final String MOUSE_UP = "mouseup";

		/**
		 * Event ID for an event generated when the object's position is changed
		 */
		public static final String POSITION_CHANGED = "position_changed";

		/**
		 * Event ID for an event generated when the projection of the map is changed.
		 */
		public static final String PROJECTION_CHANGED = "projection_changed";

		/**
		 * Event ID for an event generated when an object is resized.
		 */
		public static final String RESIZE = "resize";

		/**
		 * Event ID for an event generated when the right mouse button is clicked on the object.
		 */
		public static final String RIGHT_CLICK = "rightclick";

		/**
		 * Event ID for an event generated when the object's shape is changed.
		 */
		public static final String SHAPE_CHANGED = "shape_changed";

		/**
		 * Event ID for an event generated when the map tiles are loaded.
		 */
		public static final String TILES_LOADED = "tilesloaded";

		/**
		 * Event ID for an event generated when the map's tilt is changed.
		 */
		public static final String TILT_CHANGED = "tilt_changed";

		/**
		 * Event ID for an event generated when the object's title is changed.
		 */
		public static final String TITLE_CHANGED = "title_changed";

		/**
		 * Event ID for an event generated when the object's visibility is changed
		 */
		public static final String VISIBLE_CHANGED = "visible_changed";

		/**
		 * Event ID for an event generated when z-index (bottom-top displayed position) is changed.
		 */
		public static final String ZINDEX_CHANGED = "zindex_changed";

		/**
		 * Event ID for an event generated when the map's zoom level is changed.
		 */
		public static final String ZOOM_CHANGED = "zoom_changed";

		

		/**
		 * The set of event IDs that are allowed for MapEvents
		 */
		public static final Set<String> ALLOWED_MAPEVENT_IDS = Collections.unmodifiableSet(new HashSet<String>() {
			/**
			 * For Serializable
			 */
			private static final long serialVersionUID = -5268798275932265060L;

			{
				this.add(BOUNDS_CHANGED);
				this.add(CENTER_CHANGED);
				this.add(HEADING_CHANGED);
				this.add(IDLE);
				this.add(MAPTYPEID_CHANGED);
				this.add(PROJECTION_CHANGED);
				this.add(RESIZE);
				this.add(TILES_LOADED);
				this.add(TILT_CHANGED);
				this.add(ZOOM_CHANGED);
			}
		}); 

		/**
		 * The set of event IDs that are allowed for MapMouseEvents
		 */
		public static final Set<String> ALLOWED_MAPMOUSEEVENT_IDS = Collections.unmodifiableSet(new HashSet<String>() {
			/**
			 * For Serializable
			 */
			private static final long serialVersionUID = -1443971294212934421L;

			{
				this.add(CLICK);
				this.add(DBL_CLICK);
				this.add(DRAG);
				this.add(DRAG_END);
				this.add(DRAG_START);
				this.add(MOUSE_MOVE);
				this.add(MOUSE_OUT);
				this.add(MOUSE_OVER);
				this.add(RIGHT_CLICK);
			}
		}); 
		
		
	}	

	/**
	 * The earth's average radius in meters
	 */
	public static final double EARTH_RADIUS_METERS = 6371008.7714;

	/**
	 * The earth's average radius in feet
	 */
	public static final double EARTH_RADIUS_FEET = EARTH_RADIUS_METERS*3.2808399;

	/**
	 * The earth's average radius in kilometers
	 */
	public static final double EARTH_RADIUS_KM = EARTH_RADIUS_METERS/1000;

	/**
	 * The earth's average radius in miles
	 */
	public static final double EARTH_RADIUS_MILE = EARTH_RADIUS_FEET/5280;

	/**
	 * Convert the given value from degrees to radians
	 * @param degrees A value in degrees
	 * @return The equivalent value in radians.
	 */
	public static double degreesToRadians(double degrees) {
		return Math.PI*degrees/180;
	}

	/**
	 * Calculate the great circle distance between two LatLng's using the 
	 * "haversine formula" for greater accuracy.
	 * The units of the results depend on the units of the given earth's radius.
	 * Reference: http://www.movable-type.co.uk/scripts/latlong.html 
	 * @param latLngA	The first LatLng value
	 * @param latLngB	The second LatLng value
	 * @param earthRadius The mean earth radius in the desired units.
	 * @return  The great circle distance in the units of the given earthRadius for a sphere of that radius.
	 */
	public static double latLngDistance(LatLng latLngA, LatLng latLngB, double earthRadius) {

		// Do all calculations in radians
		double latA = degreesToRadians(latLngA.getLat());
		double lngA = degreesToRadians(latLngA.getLng());

		double latB = degreesToRadians(latLngB.getLat());
		double lngB = degreesToRadians(latLngB.getLng());

		// The square of the chord length
		double chord2 = Math.pow(Math.sin((latB-latA)/2), 2) 
				+ Math.cos(latA)*Math.cos(latB) * Math.pow(Math.sin((lngB-lngA)/2), 2);

		// the angular distance in radians
		double unitCircleDistance = 2*Math.atan2(Math.sqrt(chord2), Math.sqrt(1.0-chord2));

		return earthRadius*unitCircleDistance;

	}
}
