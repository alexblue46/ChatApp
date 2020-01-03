package provided.jxMaps.utils.impl;

import java.util.function.Supplier;

import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;

import provided.jxMaps.utils.IDistanceCases;
import provided.jxMaps.utils.IJxMaps_Defs;
import provided.util.logic.ILogicalHost;
import provided.util.logic.ILogicalVisitor;

/**
 * An ILogicalHost that is true when the great circle distance between two entities is less than a boundary value.
 * @author swong
 *
 */
public class DistanceLogicalHost implements ILogicalHost {

	/**
	 * The supplier of the LatLng value for the first entity
	 */
	private Supplier<LatLng> getAFn;
	/**
	 * The supplier of the LatLng value for the second entity.
	 */
	private Supplier<LatLng> getBFn;
	/**
	 * The Earth's average radius in the same units as the boundary radius
	 */
	private double earthRadius;
	/**
	 * The radius that determines the difference between the true and false states of this host.
	 */
	private double boundaryRadius;

	/**
	 * Create an instance of the class with the given parameters.   The LatLng suppliers are called when 
	 * the execute method of this host is called so as to get the most up-to-date position data from 
	 * each entity.
	 * @param getAFn One supplier of a LatLng value, representing the first entity 
	 * @param getBFn Another supplier of a LatLng value, representing the second entity
	 * @param earthRadius The Earth's average radius in the same units as the boundary radius.
	 * @param boundaryRadius The boundary radius where if the distance between the two entities is less than this value, this host is in a true state.
	 */
	public DistanceLogicalHost(Supplier<LatLng> getAFn, Supplier<LatLng> getBFn, double earthRadius, double boundaryRadius) {
		this.getAFn = getAFn;
		this.getBFn = getBFn;
		this.earthRadius = earthRadius;
		this.boundaryRadius = boundaryRadius;
	}
	
	/**
	 * The latest calculated distance value.
	 */
	private double distance = -1.0;
	
	@Override
	public <R, P> R execute(ILogicalVisitor<R, P> vis, P param){
		LatLng latLngA = getAFn.get();
		LatLng latLngB = getBFn.get();
		System.out.println("[DistanceLogicalHost.execute()] latLngA = "+latLngA+", latLngB = "+latLngB);
		// safety check
		if(null==latLngA || null == latLngB) {
			System.err.println("[DistanceLogicalHost.execute()] Found null value! latLngA = "+latLngA+", latLngB = "+latLngB);
			throw new NullPointerException("[DistanceLogicalHost.execute()] null LatLng value generated:  latLngA = "+latLngA+", latLngB = "+latLngB);
		}
		else {
			// Great circle distance
			distance = IJxMaps_Defs.latLngDistance(latLngA, latLngB, earthRadius);
			System.out.println("[DistanceLogicalHost.execute()] distance = "+distance);
			return (distance < boundaryRadius) ? vis.trueCase(param) : vis.falseCase(param);
		}
	}

	/**
	 * Factory for an ILogicalVisitor that closes over this host and thus can supply both the map and distance
	 * values to the given IDistanceCases object.  The returned ILogicalVisitor must be used with this 
	 * DistanceLogicalHost instance because it tied to it.
	 * @param cases An IDistanceCases class that provides case methods
	 * @return An ILogicalVisitor that closes over this DistanceLogicalHost
	 */
	public ILogicalVisitor<Void, Map> makeLogicalVisitor(IDistanceCases cases) {
		return new ILogicalVisitor<Void, Map>() {

			@Override
			public Void trueCase(Map map) {
				cases.caseLessThan(distance, map);
				return null;
			}

			@Override
			public Void falseCase(Map map) {
				cases.caseGreaterThanOrEqual(distance, map);
				return null;
			}
			
		};
	}


}
