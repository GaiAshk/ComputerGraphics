package edu.cg.scene.lightSources;

import edu.cg.algebra.Hit;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.objects.Surface;

/**
 * TODO: This class represents a point light and is already implemented. Read
 * carefully the implementation because you will need it in order to implement
 * the CutoffSpotlight light source. Note: You may ignore our implementation and
 * implement the CutoffSpotlight class without using this implementation.
 * 
 */
public class PointLight extends Light {
	protected Point position;

	// Decay factors:
	protected double kq = 0.01;
	protected double kl = 0.1;
	protected double kc = 1;

	protected String description() {
		String endl = System.lineSeparator();
		return "Intensity: " + intensity + endl + "Position: " + position + endl + "Decay factors: kq = " + kq
				+ ", kl = " + kl + ", kc = " + kc + endl;
	}

	public String toString() {
		String endl = System.lineSeparator();
		return "Point Light:" + endl + description();
	}

	public PointLight initIntensity(Vec intensity) {
		return (PointLight) super.initIntensity(intensity);
	}

	public PointLight initPosition(Point position) {
		this.position = position;
		return this;
	}

	public PointLight initDecayFactors(double kq, double kl, double kc) {
		this.kq = kq;
		this.kl = kl;
		this.kc = kc;
		return this;
	}

	public Ray rayToLight(Point fromPoint) {
		return new Ray(fromPoint, position);
	}

	public boolean isOccludedBy(Surface surface, Ray rayToLight) {
		Hit hit = surface.intersect(rayToLight);
		if (hit == null) return false;
		Point source = rayToLight.source();
		Point hittingPoint = rayToLight.getHittingPoint(hit);
		return source.distSqr(position) > source.distSqr(hittingPoint);
	}

	public Vec intensity(Point hittingPoint, Ray rayToLight) {
		double dist = hittingPoint.dist(position);
		double decay = kc + (kl + kq * dist) * dist;
		return intensity.mult(1 / decay);
	}
}
