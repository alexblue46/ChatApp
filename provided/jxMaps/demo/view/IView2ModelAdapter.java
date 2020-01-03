package provided.jxMaps.demo.view;

/**
 * The adapter from the JxMaps demo view to the model
 * @author swong
 *
 * @param <TPlacesItem>  The type of object on the places drop list on the view
 */
public interface IView2ModelAdapter<TPlacesItem> {

	/**
	 * Add a marker to the map
	 */
	void addMarkerAt();

	/**
	 * Move to the place designated by the given place object
	 * @param place  The object drop list that represents a particular place on the map
	 */
	void addMoveTo(TPlacesItem place);

	/**
	 * Ask the model to geocode the given location
	 * @param location The location to geocode
	 */
	void geocodeLocation(String location);

	/**
	 * Create a polygon using a stored list of LatLngs
	 */
	void makePolygon();

	/**
	 * Create a polyline using a stored list of LatLngs
	 */
	void makePolyline();
	
	/**
	 * Clear the list of LatLngs used to make a polygons and polylines
	 */
	void clearPolygonPoints();

	/**
	 * Search nearby the center of the map with the given parameters
	 * @param placeType The type of place to search for
	 * @param radius The radius (meters) to search within
	 */
	void searchNearby(String placeType, String radius);



}
