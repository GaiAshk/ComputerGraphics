package edu.cg.scene.lightSources;

import edu.cg.algebra.Ops;
import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.objects.Surface;

public class CutoffSpotlight extends PointLight {
	private Vec direction;
	private double cutoffAngle;

	public CutoffSpotlight(Vec dirVec, double cutoffAngle) {
		super();
		this.direction = dirVec;
		this.cutoffAngle = cutoffAngle;
	}

	public CutoffSpotlight initDirection(Vec direction) {
		this.direction = direction;
		return this;
	}

	public CutoffSpotlight initCutoffAngle(double cutoffAngle) {
		this.cutoffAngle = cutoffAngle;
		return this;
	}

	public String toString() {
		String endl = System.lineSeparator();
		return "Spotlight: " + endl + description() + "Direction: " + direction + endl;
	}

	public CutoffSpotlight initPosition(Point position) {
		return (CutoffSpotlight) super.initPosition(position);
	}

	public CutoffSpotlight initIntensity(Vec intensity) {
		return (CutoffSpotlight) super.initIntensity(intensity);
	}

	public CutoffSpotlight initDecayFactors(double q, double l, double c) {
		return (CutoffSpotlight) super.initDecayFactors(q, l, c);
	}

	public boolean isOccludedBy(Surface surface, Ray rayToLight) {
		//if the light is outside of the spot lights angle, it is occluded, return true
		if (rayToLight.direction().normalize().neg().dot(direction.normalize()) < Ops.epsilon) {
			return true;
		//else it behaves like a regular pointLight
		} else {
			return super.isOccludedBy(surface, rayToLight);
		}
	}

	public Vec intensity(Point hittingPoint, Ray rayToLight) {
		Vec lightDirection = direction.normalize().neg();
		Vec rayDirection = rayToLight.direction().normalize();
		double cosGamma = lightDirection.dot(rayDirection);
		double gamma = Math.toDegrees(Math.acos(cosGamma));
		if (cosGamma < Ops.epsilon || gamma > cutoffAngle){
			return new Vec(0.0D);
		}
		return super.intensity(hittingPoint, rayToLight).mult(cosGamma);
	}
}
