package provided.util.valueGenerator.impl;

import java.awt.geom.Point2D;

import provided.util.valueGenerator.IVectorUtil;

/**
 * Implementation of IVectorUtil
 * @author swong
 *
 */
public class VectorUtil implements IVectorUtil {
	
	/**
	 * Singleton instance
	 */
	public static final VectorUtil Singleton = new VectorUtil(); 
	
	/**
	 * Private constructor for singleton.
	 */
	private VectorUtil() {}

	@Override
	public void rotate(Point2D v, double angle) {
		double cosA = Math.cos(angle);
		double sinA = Math.sin(angle);
		v.setLocation(cosA * v.getX() - sinA * v.getY(), cosA * v.getY() + sinA * v.getX());
	}
	
	@Override
	public double angleBetween(Point2D v1, Point2D v2) {
		//  Get the vector lengths
		double v1_len = v1.distance(0.0, 0.0);
		double v2_len = v2.distance(0.0, 0.0);

		if (0.0 == v1_len || 0.0 == v2_len) { // safety check to prevent divide-by-zero below
			return 0.0;
		}

		// The cross-product length is product of each vector's length times sine of angle between
		double v1Xv2 = v1.getX() * v2.getY() - v1.getY() * v2.getX(); // calculate cross-product length 
		double angle = Math.asin(v1Xv2 / (v1_len * v2_len)); // extract the angle  
		if (Double.isNaN(angle)) {
			angle = 0.0; // safety check in case something went wrong with the arcsine calculation
		}
		return angle;
	}



}
