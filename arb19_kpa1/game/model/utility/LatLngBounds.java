package arb19_kpa1.game.model.utility;

import java.io.Serializable;

/**
 * LatLngBounds but Serializable
 *
 */
public class LatLngBounds implements Serializable{

	/**
	 * UID
	 */
	private static final long serialVersionUID = -7668348826945861772L;
	/**
	 * corner1
	 */
	private LatLng sw;
	/**
	 * Corner2
	 */
	private LatLng ne;

	/**
	 * Creates new bounds
	 * @param sw the sw corner
	 * @param ne the ne corner
	 */
	public LatLngBounds(LatLng sw, LatLng ne)  {
		this.sw = sw;
		this.ne = ne;
	}
	
	/**
	 * Gets the com.teamdev.jxmaps.LatLngBounds object
	 * @return corresponding object
	 */
	public com.teamdev.jxmaps.LatLngBounds getJxMapsEquiv(){
		return new com.teamdev.jxmaps.LatLngBounds(sw.getJxMapsEquiv(), ne.getJxMapsEquiv());
	}
	
	/**
	 * REturns whether it contains a LatLng object
	 * @param pos the object
	 * @return containment
	 */
	public boolean contains(LatLng pos) {
		return getJxMapsEquiv().contains(pos.getJxMapsEquiv());
	}

}
