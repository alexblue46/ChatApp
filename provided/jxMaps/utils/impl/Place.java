package provided.jxMaps.utils.impl;

import com.teamdev.jxmaps.LatLng;

import provided.jxMaps.utils.IPlace;

/**
 * Simple implementation of the IPlace interface.
 * @author swong
 *
 */
public class Place implements IPlace {
	/**
	 * For serialization
	 */
	private static final long serialVersionUID = -8507330511577055401L;
	
	/**
	 * The name of the place
	 */
	private String name;
	
	/**
	 * The latitude of the place
	 */
	private double lat;
	/**
	 * The longitude of the place
	 */
	private double lng;
	/**
	 * The default zoom level when viewing the place
	 */
	private double zoom;

	/**
	 * Constructor for the class that takes individual latitude and longitude coordinates.
	 * @param name The name of the place
	 * @param lat The latitude of the place
	 * @param lng The longitude of the place
	 * @param zoom The default zoom level when viewing the place
	 */
	public Place(String name, double lat, double lng, double zoom) {
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.zoom = zoom;
	}
	
	/**
	 * Constructor for the class that takes a LatLng instead of individual coordinates.  
	 * Since LatLng is not Serializable, the individual latitude and longitude values
	 * will be stored and the noWrap setting of the LatLng will be lost.
	 * @param name The name of the place
	 * @param latLng The latitude and longitude of the place
	 * @param zoom The default zoom level when viewing the place
	 */
	public Place(String name, LatLng latLng, double zoom) {
		this(name, latLng.getLat(), latLng.getLng(), zoom);
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public double getLat() {
		return this.lat;
	}
	
	@Override
	public double getLng() {
		return this.lng;
	}
	
	
	@Override
	public LatLng getLatLng() {
		return new LatLng(this.lat, this.lng);
	}

	@Override
	public double getZoom() {
		return this.zoom;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	
}
