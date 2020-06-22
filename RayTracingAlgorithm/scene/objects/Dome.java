package edu.cg.scene.objects;

import edu.cg.algebra.*;

public class Dome extends Shape {
	private Sphere sphere;
	private Plain plain;

	public Dome() {
		sphere = new Sphere().initCenter(new Point(0, -0.5, -6));
		plain = new Plain(new Vec(-1, 0, -1), new Point(0, -0.5, -6));
	}

	public Dome(Point center, double radious, Vec plainDirection) {
		sphere = new Sphere(center, radious);
		plain = new Plain(plainDirection, center);
	}

	public String toString() {
		String endl = System.lineSeparator();
		return "Dome:" + endl + sphere + plain + endl;
	}

	/**
	 * A helper method that calculates the values of ax+by+cz+d for a point (x,y,z).
	 * This can be used to classify points with respect to the given plain.
	 *
	 * @param p - the source point.
	 * @return The value of distance form the Dome, depending on the side
	 */
	public double substitute(Point p){
		//find vector from center of sphere to point
		Vec toP = p.sub(this.sphere.getCenter());
		//if vector toP and normal to plain are in opposite directions
		if(toP.dot(plain.normal()) < 0){
			return plain.substitute(p);
		} else {
			return sphere.substitute(p);
		}
	}

	/**
	 * the methods get a ray and determines if the ray hits the Dome, if it does it returns the
	 * Hit Object of either a sphere or a plane, depending on the part of the Dome the ray hits.
	 * @param ray the specified ray
	 * @return Hit Object of either a sphere or a plane, depending on what the ray hits.
	 */
	public Hit intersect(Ray ray) {
		Hit sphereHit = sphere.intersect(ray);
		if(sphereHit == null) return null;		//if ray doesn't intersect sphere there is not hit

		Hit planeHit = plain.intersect(ray);		//find intersection with plane
		if(planeHit == null) {
		//ray is parallel to plane
			//this happens if the ray intersect with the sphere on the positive side of the
			// plane, so it intersects the Dome
			if(plain.substitute(ray.add(sphereHit.t())) > Ops.epsilon) {return sphereHit;}
			//happens if the ray intersect with the sphere on the negative side of the
			// plane, there is not intersection with the Dome
			else {return null;}
		}

		//if we got to this point the ray and the plane intersect
		double t = planeHit.t();
		Point intersectionOfRayPlane = ray.add(t);
		Vec sphereCenterToIntersection = intersectionOfRayPlane.sub(sphere.getCenter());
		double distanceOfIntersectionFromCenter = sphereCenterToIntersection.norm();

		double plainRayMagnitude = plain.substitute(ray.source());
		double plainHitMagnitude = plain.substitute(ray.add(sphereHit.t()));

		if(sphereHit.isWithinTheSurface()){
		// sphereHit is from inside the sphere, ray source is from inside the Dome

			//case 1: ray crosses dome without crossing the plane
			if(plainRayMagnitude > Ops.epsilon && plainHitMagnitude > Ops.epsilon){
				return sphereHit;

			//case 2: ray crosses dome and crosses the plane
			} else if (plainRayMagnitude > Ops.epsilon && distanceOfIntersectionFromCenter < sphere.getRadius()){
				return planeHit.setWithin();

			//case 3: ray enters dome from plane
			} else if(plainRayMagnitude <= Ops.epsilon) {
				return (plainHitMagnitude >= 0) ? plain.intersect(ray) : null;
			}

		} else {
		// ray source is from outside the Dome
			//case 1: ray hits the positive side of the sphere
			if(plainHitMagnitude >= 0.0D){
				return sphereHit.setIsWithin(false);

			//case 2: ray hits the plane
			} else if(distanceOfIntersectionFromCenter <= sphere.getRadius()) {
				return planeHit.setIsWithin(false);

			//this means not hit in Dome, the hit was in the negative side of the sphere
			} else if(plainHitMagnitude < 0) {
				return null;
			}
		}
		return null;
	}
}
