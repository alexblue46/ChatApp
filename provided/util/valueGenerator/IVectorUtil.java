package provided.util.valueGenerator;

import java.awt.geom.Point2D;

/**
 * Utilities for working with 2-D vectors.   
 * @author swong
 *
 */
public interface IVectorUtil {
	
	/**
	 * Utility method to rotate the given vector by the given angle
	 * the input vector is mutated.  Works with both Points and Point2D.Doubles.
	 * @param v  A 2-D vector
	 * @param angle Angle to rotate by in radians
	 */
	void rotate(Point2D v, double angle) ;
	
	/**
	 * Utility method to get the signed angle between the two 2-D vectors.
	 * Works with both Points and Point2D.Doubles.
	 * @param v1  vector #1
	 * @param v2  vector #2
	 * @return the angle from v1 to v2
	 */
	double angleBetween(Point2D v1, Point2D v2);	

}
