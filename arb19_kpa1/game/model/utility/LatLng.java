package arb19_kpa1.game.model.utility;

import java.io.Serializable;

import provided.jxMaps.utils.IJxMaps_Defs;

/**
 * Represents lattitude and longitude but serializable
 *
 */
public class LatLng implements Serializable {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 7699814811556413090L;
	/**
	 * Latitude
	 */
	private final double lat;
	/**
	 * Longitude
	 */
	private final double lng;

	/**
	 * Creates a new obj
	 * @param lat the latitude
	 * @param lng the longitude
	 */
	public LatLng(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}

	/**
	 * Distance between this and another latlng
	 * @param other the other object
	 * @return the distance
	 */
	public double distance(LatLng other) {
		return IJxMaps_Defs.latLngDistance(getJxMapsEquiv(), other.getJxMapsEquiv(), IJxMaps_Defs.EARTH_RADIUS_MILE);
	}

	/**
	 * Get lattitude
	 * @return the lattitude
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Get longitude
	 * @return longitude
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * Get the jxmaps version of this class
	 * @return a com.teamdev.jxmaps.LatLng object
	 */
	public com.teamdev.jxmaps.LatLng getJxMapsEquiv() {
		return new com.teamdev.jxmaps.LatLng(lat, lng);
	}

	@Override
	public String toString() {
		return "LatLng [lat=" + lat + ", lng=" + lng + "]";
	}
	
}
