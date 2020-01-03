package provided.jxMaps.utils;

import java.util.function.BiConsumer;

import com.teamdev.jxmaps.GeocoderRequest;
import com.teamdev.jxmaps.GeocoderResult;
import com.teamdev.jxmaps.GeocoderStatus;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapEvent;
import com.teamdev.jxmaps.MapMouseEvent;

import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.PlaceDetailsRequest;
import com.teamdev.jxmaps.PlaceResult;
import com.teamdev.jxmaps.PlaceSearchRequest;
import com.teamdev.jxmaps.PlacesServiceStatus;
import com.teamdev.jxmaps.RadarSearchRequest;
import com.teamdev.jxmaps.TextSearchRequest;



/**
 * An encapsulation of the movement and event-based actions
 * of an associated map.  DO NOT INSTANTIATE OR IMPLEMENT THIS FACTORY YOURSELF!
 * This factory MUST be created by a IJxMapsComponentFactory to properly tie to 
 * and manage the associated Map object. 
 * @author swong
 *
 */
public interface IMapNavigator {
	
	/**
	 * Get the LatLng of the current center of the map; 
	 * @return The LatLng of the center of the map.
	 */
	public LatLng getCenter() ;
	
	
	/**
	 * Get the current zoom level of the map;
	 * @return The zoom level of the map
	 */
	public double getZoom();

	/**
	 * Get the Map to which this navigator is associated.
	 * WARNING: This method will block  until the map is ready! 
	 * @return The associated map
	 */
	public Map getMap();
	
	/**
	 * Move the center of the map to the given LatLng 
	 * @param loc The LatLng of the new center of the map
	 */
	public void moveTo(LatLng loc);
	/**
	 * Move the center of the map to the LatLng in the
	 * given IPlace 
	 * @param place The place of the new center of the map
	 */
	public void moveTo(IPlace place);
	
	/**
	 * Add a map event to the map 
	 * @param eventID  The desired event type, selected from the constants in IJxMaps_Defs.IEvent.  DO NOT USE "MAGIC" values!
	 * @param handlerFn The action to perform when the event is invoked.  The map is given to this function upon invocation. 
	 * @return A new MapEvent instance that encapsulates the event ID and handlerFn.  This MapEvent can be used to remove the installed event. 
	 */
	public MapEvent addMapEvent(String eventID, IAction<Map> handlerFn);
	
	/**
	 * Remove a map event from the map
	 * @param mapEvt The MapEvent to remove.  This should be the MapEvent returned by addMapEvent().
	 */
	public void removeMapEvent(MapEvent mapEvt);
	
	/**
	 * Add a map mouse event to the map 
	 * @param eventID  The desired event type, selected from the constants in IJxMaps_Defs.IEvent.  DO NOT USE "MAGIC" values!
	 * @param handlerFn The action to perform when the event is invoked.  The MouseEvent is given to this function upon invocation.  
	 * Note that the map is available via MouseEvent.getMap().  
	 * @return A new MapMouseEvent instance that encapsulates the event ID and handlerFn.  This MapMouseEvent can be used to remove the installed event. 
	 */
	public MapMouseEvent addMapMouseEvent(String eventID, IAction<MouseEvent> handlerFn);
	
	/**
	 * Remove a map mouse event from the map
	 * @param mapMouseEvt The MapEvent to remove.  This should be the MapMouseEvent returned by addMapMouseEvent().
	 */
	public void removeMapMouseEvent(MapMouseEvent mapMouseEvt);
	
	/**
	 * Perform the Geocoding service request as defined by the given GeocodeRequest object.  The geocoder 
	 * results are processed by the onCompleteHandler when the request is complete.
	 * @param request The GeocodeRequest object that one wished to be serviced.
	 * @param onCompleteHandler A BiConsumer&lt;GeocoderResult[], GeocoderStatus&gt; that takes the results of the geocoder request servicing and processes them.
	 */
	void serviceGeoCoderRequest(GeocoderRequest request, BiConsumer<GeocoderResult[], GeocoderStatus> onCompleteHandler);

	
	/**
	 * Perform an asynchronous search of nearby places using the given request object.  The results are
	 * processed by the given onCompleteHandler.   When invoked, the onCompleteHandler's  
	 * parameters are an array of result objects and a status object.<br>
	 * NOTE: JxMaps does not currently support pagination of results, so only the first page 
	 * of results is ever returned. DO NOT ATTEMPT SEARCHES WHERE MORE THAN ABOUT A DOZEN 
	 * RESULTS ARE EXPECTED!
	 * @param request  The desired search request 
	 * @param onCompleteHandler A handler function to process any returned results.
	 */
	void servicePlacesSearch(PlaceSearchRequest request,
			BiConsumer<PlaceResult[], PlacesServiceStatus> onCompleteHandler);

	/**
	 * Perform an asynchronous search of nearby places using the given request object.  The results are
	 * processed by the given onCompleteHandler.   When invoked, the onCompleteHandler's  
	 * parameters are an array of result objects and a status object.<br>
	 * NOTE: JxMaps does not currently support pagination of results, so only the first page 
	 * of results is ever returned. DO NOT ATTEMPT SEARCHES WHERE MORE THAN ABOUT A DOZEN 
	 * RESULTS ARE EXPECTED!
	 * @param request  The desired search request 
	 * @param onCompleteHandler A handler function to process any returned results.
	 */
	void servicePlacesSearch(RadarSearchRequest request,
			BiConsumer<PlaceResult[], PlacesServiceStatus> onCompleteHandler);

	/**
	 * Perform an asynchronous search of nearby places using the given request object.  The results are
	 * processed by the given onCompleteHandler.   When invoked, the onCompleteHandler's  
	 * parameters are an array of result objects and a status object.<br>
	 * NOTE: JxMaps does not currently support pagination of results, so only the first page 
	 * of results is ever returned. DO NOT ATTEMPT SEARCHES WHERE MORE THAN ABOUT A DOZEN 
	 * RESULTS ARE EXPECTED!
	 * @param request  The desired search request 
	 * @param onCompleteHandler A handler function to process any returned results.
	 */
	void servicePlacesSearch(TextSearchRequest request,
			BiConsumer<PlaceResult[], PlacesServiceStatus> onCompleteHandler);

	/**
	 * Perform an asynchronous search for the details of a given place using the given request object.  
	 * The results are processed by the given onCompleteHandler.   When invoked, the onCompleteHandler's  
	 * parameters are an array of result objects and a status object.<br>
	 * NOTE: Since only one place is being searched, the results array will have at most one element.
	 * @param request  The desired search request 
	 * @param onCompleteHandler A handler function to process any returned results.
	 */
	void servicePlacesSearch(PlaceDetailsRequest request,
			BiConsumer<PlaceResult[], PlacesServiceStatus> onCompleteHandler);



}
