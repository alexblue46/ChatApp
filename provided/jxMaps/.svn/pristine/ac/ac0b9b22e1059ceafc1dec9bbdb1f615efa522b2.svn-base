package provided.jxMaps.utils;

import com.teamdev.jxmaps.Map;

/**
 * An interface that supplies cases for distance-related operations.   This interface is akin to a
 * visitor who works with a purely virtualized host.
 * @author swong
 *
 */
public interface IDistanceCases {
	
	/**
	 * Case for when a distance is less than some reference value
	 * @param distance The distance value being compared.
	 * @param map The map the distance was measured on.
	 */
	void caseLessThan(double distance, Map map);
	
	/**
	 * Case for when a distance is greater than or equal to a reference value
	 * @param distance The distance value being compared.
	 * @param map The map the distance was measured on.
	 */
	void caseGreaterThanOrEqual(double distance, Map map);
	
}