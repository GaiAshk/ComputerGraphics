package edu.cg.scene.objects;

import edu.cg.algebra.Hit;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;

public interface Intersectable {
	/**
	 * Checks if the ray hits the object. If the ray hits the object,
	 * then the hit point is returned. 
	 * 
	 * NOTE: 
	 * The implementation should also indicate whether the ray is within the object or not.
	 * 
	 * @param ray the specified ray
	 * @return The hit point of the ray with the object if exist and null otherwise. 
	 */
	public Hit intersect(Ray ray);

	/**
	 * A helper method that calculates the values of ax+by+cz+d for a point (x,y,z).
	 * This can be used to classify points with respect to the given plain.
	 *
	 * @param p - the source point.
	 * @return The value ax+by+cz+d
	 */
//	public double substitute(Point p);

	/**
	 * A helper method that returns a plain normal such that the angle between the
	 * ray direction and the normal is acute.
	 *
	 * @param ray - the source ray.
	 * @return A plain normal.
	 */
//	public Vec normal(Ray ray);
}
