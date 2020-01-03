package provided.jxMaps.utils;

import java.io.Serializable;

import com.teamdev.jxmaps.LatLng;

/**
 * Represents a named location on a map with an associated default zoom level when viewing it.
 * Implementation note:  Since the LatLng class is not Serializable, then a concrete IPlace
 * must store the latitude and longitude of the place, not a LatLng object.
 * @author swong
 *
 */
public interface IPlace extends Serializable {
	
	/**
	 * Get the name of the place
	 * @return The name of the place
	 */
	public String getName();
	/**
	 * Get the latitude of the place
	 * @return The latitude of the place
	 */
	double getLat();
	/**
	 * Get the longitude of the place
	 * @return The longitude of the place
	 */
	double getLng();
	
	/**
	 * Convenience method to get the latitude and longitude of the place 
	 * in the form of a LatLng object.   Note that a new LatLng instance is
	 * returned with a default noWrap setting.
	 * @return A new LatLng instance with this place's latitude and longitude.
	 */
	public LatLng getLatLng();
	
	/**
	 * Get the default zoom when viewing the place
	 * @return The default zoom level for this place
	 */
	public double getZoom();
}
