package edu.cg.scene.objects;

import edu.cg.algebra.Hit;
import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;

/** 
 * This class is already implemented and it represents a 3D plain. 
 * You can use this code for reference.
 *
 */
public class Plain extends Shape {
	// implicit form of a plain: ax + by + cz + d = 0;
	private double a, b, c, d;

	private transient Vec normal = null;

	// Plain Constructors.
	public Plain(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public Plain(Vec normal, Point p0) {
		this(normal.x, normal.y, normal.z, -normal.dot(p0.toVec()));
	}

	public Plain() {
		this(new Vec(0, 1, 0), new Point(0, -1, 0));
	}

	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Plain: a = " + a + ", b = " + b + ", c = " + c + ", d = " + d + endl;
	}

	public synchronized Vec normal() {
		if (normal == null)
			normal = new Vec(a, b, c).normalize();
		return normal;
	}

	/**
	 * A helper method that returns a plain normal such that the angle between the
	 * ray direction and the normal is acute.
	 * 
	 * @param ray - the source ray.
	 * @return A plain normal.
	 */
	public Vec normal(Ray ray) {
		return ray.direction().dot(normal()) < 0 ? normal() : normal().neg();
	}

	/**
	 * A helper method that calculates the values of ax+by+cz+d for a point (x,y,z).
	 * This can be used to classify points with respect to the given plain.
	 * 
	 * @param p - the source point.
	 * @return The value ax+by+cz+d
	 */
	public double substitute(Point p) {
		Vec abc = new Vec(a, b, c);
		return abc.dot(p.toVec()) + d;
	}

	/**
	 * Returns the ray intersection with the plain if exists, and null otherwise.
	 */
	public Hit intersect(Ray ray) {
		Vec abc = new Vec(a, b, c);

		double t = -substitute(ray.source()) / ray.direction().dot(abc);
		return t > Ops.epsilon & t < Ops.infinity ? new Hit(t, normal(ray)) : null;
	}
}
