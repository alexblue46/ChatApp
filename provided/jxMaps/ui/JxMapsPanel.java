/**
 * 
 */
package provided.jxMaps.ui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.teamdev.jxmaps.CircleOptions;
import com.teamdev.jxmaps.GeocoderCallback;
import com.teamdev.jxmaps.GeocoderRequest;
import com.teamdev.jxmaps.GeocoderResult;
import com.teamdev.jxmaps.GeocoderStatus;
import com.teamdev.jxmaps.InfoWindowOptions;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapEvent;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapViewOptions;

import com.teamdev.jxmaps.MarkerOptions;
import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.PlaceDetailsCallback;
import com.teamdev.jxmaps.PlaceDetailsRequest;
import com.teamdev.jxmaps.PlaceNearbySearchCallback;
import com.teamdev.jxmaps.PlaceRadarSearchCallback;
import com.teamdev.jxmaps.PlaceResult;
import com.teamdev.jxmaps.PlaceSearchPagination;
import com.teamdev.jxmaps.PlaceSearchRequest;
import com.teamdev.jxmaps.PlaceTextSearchCallback;
import com.teamdev.jxmaps.PlacesServiceStatus;
import com.teamdev.jxmaps.PolygonOptions;
import com.teamdev.jxmaps.PolylineOptions;
import com.teamdev.jxmaps.RadarSearchRequest;
import com.teamdev.jxmaps.RectangleOptions;
import com.teamdev.jxmaps.TextSearchRequest;
import com.teamdev.jxmaps.swing.MapView;

import provided.jxMaps.utils.IAction;
import provided.jxMaps.utils.IJxMapsComponentsFactory;
import provided.jxMaps.utils.IJxMaps_Defs;
import provided.jxMaps.utils.IMapNavigator;
import provided.jxMaps.utils.IPlace;
import provided.jxMaps.utils.enhanced.EnhancedCircle;
import provided.jxMaps.utils.enhanced.EnhancedInfoWindow;
import provided.jxMaps.utils.enhanced.EnhancedMarker;
import provided.jxMaps.utils.enhanced.EnhancedPolygon;
import provided.jxMaps.utils.enhanced.EnhancedPolyline;
import provided.jxMaps.utils.enhanced.EnhancedRectangle;
import provided.jxMaps.utils.impl.IPlace_Samples;



/**
 * An extension of com.teamdev.jxmaps.swing.MapView that adds functionality for better
 * decoupling, robustness, type safety and abstraction.
 * @author swong
 *
 */
public class JxMapsPanel extends MapView {

	/**
	 * For Serializable but don't do it!  ALWAYS instantiate GUI elements on the target machine!
	 */
	private static final long serialVersionUID = -7060486053506525669L;

	/**
	 * Timeout for waiting for map to become ready.
	 */
	private static final int GET_MAP_TIMEOUT = 30; 

	/**
	 * Latch to block the getMap() method until the map is ready.
	 */
	private CountDownLatch mapReadyLatch = new CountDownLatch(1);

	/**
	 * Constructs an instance using the given options
	 * Unless specified in the operation of the given mapInit function, 
	 * the map will default to a center = IJxMaps_Defs.WILLY and a 
	 * zoom = IJxMaps_Defs.ZOOM_RICE
	 * @param mapViewOptions The view options for the MapView superclass
	 * @param mapViewInit A BiConsumer&lt;Map, MapViewOptions&gt; that is used to initialize the map when it becomes ready and has status = MAP_STATUS_OK.  
	 * The given MapViewOptions are passed to this initializer as well.  These options have already been set and if the initializar wants, it can modify 
	 * the options and set them again.  Do NOT replace the MapViewOptions object as it contains the Google Maps API key! 
	 */
	public JxMapsPanel(MapViewOptions mapViewOptions, BiConsumer<Map, JxMapsPanel> mapViewInit) {
		super(mapViewOptions);
		init(mapViewInit);		
	}

	/**
	 * Initialize the map panel
	 * @param mapViewInit  The BiConsumer used to add additional initialization behavior.
	 */
	private void init(BiConsumer<Map, JxMapsPanel> mapViewInit) {
		setOnMapReadyHandler(new MapReadyHandler() {
			@Override
			public void onMapReady(MapStatus status) {
				System.out.println("[JxMapsPanel.init()] onMapReady called");
				if (status == MapStatus.MAP_STATUS_OK) {

					Map map = JxMapsPanel.super.getMap(); // Use the superclass getMap() to avoid deadlock with overridden version
					// Map will not display without a center and a zoom
					IPlace willy =IPlace_Samples.SAMPLE_PLACES_DICT.get("WILLY"); 
					map.setCenter(willy.getLatLng());	 // Make sure the map has a center
					map.setZoom(willy.getZoom()); // Make sure that the map has a zoom
					mapReadyLatch.countDown(); // Do this here so setting the default center and zoom don't invoke events.
					System.out.println("[JxMapsPanel.init()] Calling supplied mapInit function..."); 
					mapViewInit.accept(map, JxMapsPanel.this);

				}
				else {
					System.err.println("[JxMapsPanel.init()] Map status NOT OK in MapReadyHandler.onMapReady() invocation. MapStatus = "+status);
				}
			}
		});

	}

	/**
	 * Override of MapView.getMap() to always block until the map is ready.
	 * If the map is not ready in GET_MAP_TIMEOUT seconds, an InterruptedException will be thrown.
	 * @return the associated Map object, which is ready and with an MAP_STATUS_OK status.
	 */
	@Override 
	public Map getMap() {
		try {
			if(!mapReadyLatch.await(GET_MAP_TIMEOUT, TimeUnit.SECONDS)) {
				throw new InterruptedException(GET_MAP_TIMEOUT+"second timeout exceeded while waiting for map to become ready!");
			}
		} catch (InterruptedException e) {
			System.err.println("[JxMapsPanel.getMap()] Exception while waiting for map to be ready: "+e);
			e.printStackTrace();
		}
		return super.getMap();
	}

	/**
	 * Start the map panel
	 */
	public void start() {
		this.setVisible(true);
	}

	/**
	 * The installed map event handlers for each eventID.  This is needed because 
	 * even though Map.addEventListener() properly handles multiple MapEvents registered to 
	 * the same eventID, the Map.removeEventListener() method doesn't do anything in version 1.3.1
	 */
	private java.util.Map<String, Set<MapEvent>> mapEventsDict = new HashMap<String, Set<MapEvent>>();

	/**
	 * The installed map mouse event handlers for each eventID.  This is needed because 
	 * even though Map.addEventListener() properly handles multiple MapMouseEvents registered to 
	 * the same eventID, the Map.removeEventListener() method doesn't do anything in version 1.3.1
	 */	
	private java.util.Map<String, Set<MapMouseEvent>> mapMouseEventsDict = new HashMap<String, Set<MapMouseEvent>>();


	/**
	 * Get a factory for map components that is tied to this map.
	 * @return a factory tied to this map
	 */
	public IJxMapsComponentsFactory getComponentsFactory() {
		return new IJxMapsComponentsFactory() {

			@Override
			public EnhancedMarker makeMarker(MarkerOptions options) {
				if(null == options) {
					options = new MarkerOptions();
					options.setVisible(false);
				}
				EnhancedMarker marker = new EnhancedMarker(JxMapsPanel.this.getMap());
				marker.setOptions(options);
				return marker;
			}

			@Override
			public EnhancedInfoWindow makeInfoWindow(InfoWindowOptions options) {
				if(null==options) {
					options = new InfoWindowOptions();
				}
				EnhancedInfoWindow result = new EnhancedInfoWindow(JxMapsPanel.this.getMap());
				result.setOptions(options);
				return result;

			}
			
			@Override
			public EnhancedCircle makeCircle(CircleOptions options) {	
				if(null==options) {
					options = new CircleOptions();
					options.setVisible(false);
				}			
				EnhancedCircle result = new EnhancedCircle(JxMapsPanel.this.getMap());
				result.setOptions(options);
				return result;
			}

			@Override
			public EnhancedRectangle makeRectangle(RectangleOptions options) {	
				if(null==options) {
					options = new RectangleOptions();
					options.setVisible(false);
				}			
				EnhancedRectangle result = new EnhancedRectangle(JxMapsPanel.this.getMap());
				result.setOptions(options);
				return result;
			}
			
			@Override
			public EnhancedPolygon makePolygon(PolygonOptions options) {			
				if(null==options) {
					options = new PolygonOptions();
					options.setVisible(false);
				}
//				// The following code is to fix the bug in JxMaps where PolygonOptions.setPaths() results in an
//				// IllegalArgument (unsupported parameter) exception
//				LatLng[][] paths = null;
//				try {
//					// save the paths if it they were set
//					paths = options.getPaths(); // will throw a null pointer exception if setPaths() was never called.
//					System.out.println("[JxMapsPanel.IJxMapsComponentsFactory.makePolygon()] paths = "+paths);
//					if(null != paths){
//						options.setPaths(new LatLng[][] {}); // clear the paths in the options so that no exceptions are thrown
//					}
//				}
//				catch(NullPointerException e) {
//					// this is normal if options.setPaths() was never called.
//					System.out.println("[JxMapsPanel.IJxMapsComponentsFactory.makePolygon()] NORMAL null pointer exception if PolygonOptons.setPaths() was never called: "+e);
//				}
				
				EnhancedPolygon result = new EnhancedPolygon(JxMapsPanel.this.getMap());

				result.setOptions(options); // set the options

//				if(null != paths) {  // install the paths if they were originally set in the options
//					result.setPaths(paths);  // set the paths here 
//				}
				return result;
			}

			@Override
			public EnhancedPolyline makePolyline(PolylineOptions options) {			
				if(null==options) {
					options = new PolylineOptions();
					options.setVisible(false);
				}
				
				EnhancedPolyline result = new EnhancedPolyline(JxMapsPanel.this.getMap());
				result.setOptions(options);
				return result;
			}
			

			@Override
			public IMapNavigator makeNavigator() {
				return new IMapNavigator() {
					
					@Override
					public Map getMap() {
						return JxMapsPanel.this.getMap();
					}

					@Override
					public MapEvent addMapEvent(String eventID, IAction<Map> handlerFn) {
						if(!IJxMaps_Defs.IEvent.ALLOWED_MAPEVENT_IDS.contains(eventID)) {
							throw new IllegalArgumentException("[INavigator.addMapEvent()] The given event, \""+eventID+"\", is not on the allowed list of IDs for MapEvents! See IJxMaps_Defs.IEvent.ALLOWED_MAPEVENT_IDS");
						}
						MapEvent mapEvt = new MapEvent() {
							{
								this.setEventId(eventID);
							}
							@Override
							public void onEvent() {
								handlerFn.accept(JxMapsPanel.this.getMap());

							}
						};

						// Map.addEventListener() is capable of adding multiple events for the same eventID.
						//						getMap().addEventListener(eventID, mapEvt); // This should be all that is needed but the Map.removeEventListener() method doesn't do anything in version 1.3.1

						// The following is to get around the incorrect multiple event handling of Map
						if(mapEventsDict.containsKey(eventID)) {
							mapEventsDict.get(eventID).add(mapEvt);
						}
						else {
							mapEventsDict.computeIfAbsent(eventID, (key)->{ return new HashSet<MapEvent>();}).add(mapEvt);

							JxMapsPanel.this.getMap().addEventListener(eventID, new MapEvent() {
								@Override
								public void onEvent() {
									// JxMaps's event handling does not catch and print exceptions during event invocation!
									try {
										mapEventsDict.get(eventID).forEach((mapEvent)->{
											// Catch individual exceptions to enable the system to keep running if any given event handler has problems.
											try {
												mapEvent.onEvent();
											} catch(Exception e) {
												System.err.println("[JxMapsPanel.IMapNavigator] An exception occured during the invocation of a map event (event ID = "+eventID+", event = "+mapEvent+"): "+e);
												e.printStackTrace();											
											}
										});
									} catch(Exception e) {
										System.err.println("[JxMapsPanel.IMapNavigator] An exception occured during the invocation of the set of map events: "+e);
										e.printStackTrace();
									}
								}
							});
						}

						System.out.println("[JxMapsPanel.INavigator.addMapEvent()] Added MapEvent: "+mapEvt);
						return mapEvt;
					}

					@Override 
					public void removeMapEvent(MapEvent mapEvt) {
						//						getMap().removeEventListener(mapEvt);  // This should be all that is needed but this method doesn't do anything in version 1.3.1

						// The following is to get around the incorrect multiple event handling of Map
						if(mapEventsDict.getOrDefault(mapEvt.getEventId(), new HashSet<MapEvent>()).remove(mapEvt)) {
							System.out.println("[JxMapsPanel.INavigator.removeMapEvent()] Removed MapEvent: "+mapEvt);
						}
						else {
							System.err.println("[JxMapsPanel.INavigator.removeMapEvent()] MapEvent not found: "+mapEvt );
						}
					}

					
					@Override
					public MapMouseEvent addMapMouseEvent(String eventID, IAction<MouseEvent> handlerFn) {
						if(!IJxMaps_Defs.IEvent.ALLOWED_MAPMOUSEEVENT_IDS.contains(eventID)) {
							throw new IllegalArgumentException("[INavigator.addMapMouseEvent()] The given event, \""+eventID+"\", is not on the allowed list of IDs for MapMouseEvents! See IJxMaps_Defs.IEvent.ALLOWED_MAPMOUSEEVENT_IDS");
						}
						MapMouseEvent mapMouseEvt = new MapMouseEvent() {
							{
								this.setEventId(eventID);
							}
							@Override
							public void onEvent(MouseEvent mouseEvent) {
								handlerFn.accept(mouseEvent);

							}
						};

						// Map.addEventListener() is capable of adding multiple events for the same eventID.
						// getMap().addEventListener(eventID, mapMouseEvt); // This should be all that is needed but the Map.removeEventListener() method doesn't do anything in version 1.3.1

						// The following is to get around the incorrect multiple event handling of Map
						if(mapMouseEventsDict.containsKey(eventID)) {
							mapMouseEventsDict.get(eventID).add(mapMouseEvt);
						}
						else {
							mapMouseEventsDict.computeIfAbsent(eventID, (key)->{ return new HashSet<MapMouseEvent>();}).add(mapMouseEvt);

							JxMapsPanel.this.getMap().addEventListener(eventID, new MapMouseEvent() {
								@Override
								public void onEvent(MouseEvent mouseEvent) {
									// JxMaps's event handling does not catch and print exceptions during event invocation!
									try {
										mapMouseEventsDict.get(eventID).forEach((mapMouseEvent)->{
											// Catch individual exceptions to enable the system to keep running if any given event handler has problems.
											try {
												mapMouseEvent.onEvent(mouseEvent);
											} catch(Exception e) {
												System.err.println("[JxMapsPanel.IMapNavigator] An exception occured during the invocation of a map mouse event (event ID = "+eventID+", mouse event = "+mapMouseEvent+"): "+e);
												e.printStackTrace();											
											}
										});
									} catch(Exception e) {
										System.err.println("[JxMapsPanel.IMapNavigator] An exception occured during the invocation of the set of map mouse events: "+e);
										e.printStackTrace();
									}
								}
							});
						}

						System.out.println("[JxMapsPanel.INavigator.addMapMouseEvent()] Added MapMouseEvent: "+mapMouseEvt);
						return mapMouseEvt;
					}
					
					@Override 
					public void removeMapMouseEvent(MapMouseEvent mapMouseEvt) {
						//	getMap().removeEventListener(mapMouseEvt);  // This should be all that is needed but this method doesn't do anything in version 1.3.1

						// The following is to get around the incorrect multiple event handling of Map
						if(mapMouseEventsDict.getOrDefault(mapMouseEvt.getEventId(), new HashSet<MapMouseEvent>()).remove(mapMouseEvt)) {
							System.out.println("[JxMapsPanel.INavigator.removeMapMouseEvent()] Removed MapMouseEvent: "+mapMouseEvt);
						}
						else {
							System.err.println("[JxMapsPanel.INavigator.removeMapMouseEvent()] MapMouseEvent not found: "+mapMouseEvt );
						}
					}
					
					@Override
					public void moveTo(LatLng loc) {
						JxMapsPanel.this.getMap().setCenter(loc);
					}

					@Override
					public void moveTo(IPlace place) {
						moveTo(place.getLatLng());
						JxMapsPanel.this.getMap().setZoom(place.getZoom());

					}

					@Override
					public void serviceGeoCoderRequest(GeocoderRequest request, BiConsumer<GeocoderResult[], GeocoderStatus> onCompleteHandler) {
						getServices().getGeocoder().geocode(request, new GeocoderCallback(JxMapsPanel.this.getMap()) {
				            @Override
				            public void onComplete(GeocoderResult[] results, GeocoderStatus status) {
				            	System.out.println("[JxMapsPanel.IMapNavigator.serviceGeoCoderRequest] Results for request address = \""+request.getAddress()+"\": (Status = "+status+") # of results = "+results.length);
				            	onCompleteHandler.accept(results, status);
				            }
						});
					}
					
					@Override
					public void servicePlacesSearch(PlaceSearchRequest request, BiConsumer<PlaceResult[], PlacesServiceStatus> onCompleteHandler) {
						System.out.println("[JxMapsPanel.IMapNavigator.servicePlacesSearch] Submitting PlaceSearchRequest request = "+request);
						getServices().getPlacesService().nearbySearch(request, new PlaceNearbySearchCallback(getMap()) {
							@Override
							public void onComplete(PlaceResult[] results, PlacesServiceStatus status, PlaceSearchPagination pagination) {
				            	System.out.println("[JxMapsPanel.serviceGeoCoderRequest] Results for PlaceSearchRequest types = \""+Arrays.toString(request.getTypes())+"\": (Status = "+status+") # of results = "+results.length+", pagination = "+pagination);
				            	// JxMaps 1.3.1 does not pass a usable pagination object
				            	onCompleteHandler.accept(results, status);
							}	
						});
					}
					
					@Override
					public void servicePlacesSearch(RadarSearchRequest request, BiConsumer<PlaceResult[], PlacesServiceStatus> onCompleteHandler) {
						getServices().getPlacesService().radarSearch(request, new PlaceRadarSearchCallback(getMap()) {
							@Override
							public void onComplete(PlaceResult[] results, PlacesServiceStatus status) {

				            	System.out.println("[JxMapsPanel.IMapNavigator.servicePlacesSearch] Results for request types = \""+Arrays.toString(request.getTypes())+"\": (Status = "+status+") # of results = "+results.length);
				            	onCompleteHandler.accept(results, status);
							}
	
						});
					}
					
					@Override
					public void servicePlacesSearch(TextSearchRequest request, BiConsumer<PlaceResult[], PlacesServiceStatus> onCompleteHandler) {
						getServices().getPlacesService().textSearch(request, new PlaceTextSearchCallback(getMap()) {
							@Override
							public void onComplete(PlaceResult[] results, PlacesServiceStatus status, PlaceSearchPagination pagination) {
				            	System.out.println("[JxMapsPanel.IMapNavigator.servicePlacesSearch] Results for TextSearchRequest request types = \""+request.getType()+"\": (Status = "+status+") # of results = "+results.length);
				            	// JxMaps 1.3.1 does not pass a usable pagination object
				            	onCompleteHandler.accept(results, status);
							}

	
						});
					}
					
					@Override
					public void servicePlacesSearch(PlaceDetailsRequest request, BiConsumer<PlaceResult[], PlacesServiceStatus> onCompleteHandler) {
						getServices().getPlacesService().getDetails(request, new PlaceDetailsCallback(getMap()) {
							@Override
							public void onComplete(PlaceResult result, PlacesServiceStatus status) {

				            	System.out.println("[JxMapsPanel.IMapNavigator.servicePlacesSearch] Results for PlaceDetailsRequest PlaceID = \""+request.getPlaceId()+"\": (Status = "+status+") # of result = "+result);
				            	onCompleteHandler.accept(new PlaceResult[] {result}, status);
							}
						});
					}

					@Override
					public LatLng getCenter() {
						return getMap().getCenter();
					}
					

					@Override
					public double getZoom() {
						return getMap().getZoom();
					}
				};
			}
			
			

			@Override
			public Map getMap() {
				return JxMapsPanel.this.getMap();
			}


		};
	}


	/**
	 * Static factory to produce JxMapsPanels with a given MapViewOptions and a
	 * function to initialize the map and JxMapsPanel if necessary. If the map
	 * initializer function does not explicitly do so, the map will use the
	 * JxMapsPanel's default center and zoom values. THIS FACTORY DOES *NOT* SET THE
	 * GOOGLE API KEY! MapViewOptions.setApiKey(googleApiKey) must be called BEFORE
	 * using this factory! This factory provides the maximum JxMapsPanel
	 * instantiation capability at the expense of a more complicated setup.The
	 * mapViewInit initializer should NOT pass the JxMapsPanel instance on to the
	 * model as it is a view component only! Passing the Map reference to the model
	 * is allowable however.
	 */
	public static final BiFunction<MapViewOptions, BiConsumer<Map, JxMapsPanel>, JxMapsPanel> OPTIONS_FACTORY = (mapViewOptions, mapViewInit) -> {
		return new JxMapsPanel(mapViewOptions, mapViewInit);
	};	
	
	/**
	 * Static factory to produce JxMapsPanels with a given Google Maps API key and a
	 * function to initialize the map and JxMapsPanel if necessary. If the map
	 * initializer function does not explicitly do so, the map will use the
	 * JxMapsPanel's default center and zoom values. This factory provides a medium
	 * level JxMapsPanel instantiation capability with some flexibility and
	 * relatively simple invocation.   The mapViewInit initializer should NOT pass the 
	 * JxMapsPanel instance on to the model as it is a view component only!   Passing
	 * the Map reference to the model is allowable however.
	 */
	public static final BiFunction<String, BiConsumer<Map, JxMapsPanel>, JxMapsPanel> FACTORY = (googleApiKey, mapViewInit) -> {
		MapViewOptions mapViewOptions = new MapViewOptions();
		mapViewOptions.importPlaces();
		mapViewOptions.setApiKey(googleApiKey);
		return OPTIONS_FACTORY.apply(mapViewOptions, mapViewInit);
	};

	/**
	 * Static factory to produce JxMapsPanels with a given Google Maps API key and
	 * a no-op mapViewInit function which will set the map to JxMapsPanel's default center 
	 * and zoom values.   This factory provides the simplest but most restrictive
	 * JxMapsPanel instantiation capability.
	 */
	public static final Function<String, JxMapsPanel> DEFAULT_FACTORY = (googleApiKey) -> {
		return FACTORY.apply(googleApiKey, (map, mapPanel)->{
			System.out.println("[JxMapsPanel.DEFAULT_FACTORY] No-op mapInit.");
		});
	};
}
