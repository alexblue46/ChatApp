package provided.jxMaps.utils;

import com.teamdev.jxmaps.CircleOptions;
import com.teamdev.jxmaps.InfoWindowOptions;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MarkerOptions;
import com.teamdev.jxmaps.PolygonOptions;
import com.teamdev.jxmaps.PolylineOptions;
import com.teamdev.jxmaps.RectangleOptions;

import provided.jxMaps.utils.enhanced.EnhancedCircle;
import provided.jxMaps.utils.enhanced.EnhancedInfoWindow;
import provided.jxMaps.utils.enhanced.EnhancedMarker;
import provided.jxMaps.utils.enhanced.EnhancedPolygon;
import provided.jxMaps.utils.enhanced.EnhancedPolyline;
import provided.jxMaps.utils.enhanced.EnhancedRectangle;

/**
 * A factory that encapsulates the generation of entities that bound to 
 * an associated Map object.   DO NOT INSTANTIATE OR IMPLEMENT THIS FACTORY YOURSELF!
 * This factory MUST be created by a JxMapsPanel to properly tie to and manage the 
 * associated Map object.  Note that when created by a JxMapsPanel, an IJxMapsComponentsFactory
 * instance-specific state so creating multiple instances of  IJxMapsComponentsFactory is not
 * a problem as they will all close over the same map. 
 * @author swong
 *
 */
public interface IJxMapsComponentsFactory {
	/**
	 * Instantiate a new EnhancedMarker instance tied to the associated Map. 
	 * @param options Options for the new marker.  Use null for default options (visible=false).
	 * @return A new EnhancedMarker instance
	 */
	public EnhancedMarker makeMarker(MarkerOptions options);
	
	/**
	 * Instantiate a new EnhancedInfoWindow tied to the associated Map
	 * @param options Options for the new info window.  Use null for default options.
	 * @return A new EnhancedInfoWindow instance
	 */
	public EnhancedInfoWindow makeInfoWindow(InfoWindowOptions options);
	
	/**
	 * Instantiate a new EnhancedCircle object tied to the associated Map.
	 * @param options Options for the new EnhancedCircle.  Use null for the default options (visible=false)
	 * Note: By default, a new CircleOptions object has visible=true so be sure that the visibility 
	 * is as desired when supplying a non-null CircleOptions.
	 * @return A new EnhancedCircle instance
	 */
	public EnhancedCircle makeCircle(CircleOptions options);

	/**
	 * Instantiate a new EnhancedRectangle object tied to the associated Map.
	 * @param options Options for the new EnhancedRectangle.  Use null for the default options (visible=false)
	 * Note: By default, a new RectangleOptions object has visible=true so be sure that the visibility 
	 * is as desired when supplying a non-null RectangleOptions.
	 * @return A new EnhancedRectangle instance
	 */
	public EnhancedRectangle makeRectangle(RectangleOptions options);
	
	/**
	 * Instantiate a new EnhancedPolygon object tied to the associated Map.  
	 * @param options Options for the new EnhancedPolygon.  Use null for the default options (visible=false)
	 * Note: By default, a new PolygonOptions object has visible=true so be sure that the visibility 
	 * is as desired when supplying a non-null PolygonOptions.
	 * @return A new EnhancedPolygon instance
	 */
	public EnhancedPolygon makePolygon(PolygonOptions options); 

	/**
	 * Instantiate a new EnhancedPolyline object tied to the associated Map.  
	 * @param options Options for the new EnhancedPolyline.  Use null for default options (visible=false).
	 * Note: By default, a new PolylineOptions object has visible=true so be sure that the visibility 
	 * is as desired when supplying a non-null PolylineOptions.
	 * @return A new EnhancedPolyline instance
	 */
	public EnhancedPolyline makePolyline(PolylineOptions options); 
	
	/**
	 * Make the associated IMapNavigator object. 
	 * Individual IMapNavigator instances hold no instance-specific state, 
	 * so multiple navigators associated with the same Map are safe to use.
	 * @return A navigator instance tied to the associated Map.
	 */
	public IMapNavigator makeNavigator();
	
	/**
	 * Get the associated Map object.   
	 * WARNING:  This method will block until the Map object is ready!
	 * @return The associate Map object
	 */
	public Map getMap();



}
