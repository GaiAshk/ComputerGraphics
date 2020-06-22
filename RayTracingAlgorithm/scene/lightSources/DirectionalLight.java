package edu.cg.scene.lightSources;

import edu.cg.algebra.Point;
import edu.cg.algebra.Ray;
import edu.cg.algebra.Vec;
import edu.cg.scene.objects.Surface;

public class DirectionalLight extends Light {
	private Vec direction = new Vec(0, -1, -1);

	public DirectionalLight(Vec dirVec, Vec intensity) {
		this.direction = dirVec.normalize();
		this.intensity = intensity;
	}

	public DirectionalLight initDirection(Vec direction) {
		this.direction = direction.normalize();
		return this;
	}

	public String toString() {
		String endl = System.lineSeparator();
		return "Directional Light:" + endl + super.toString() + "Direction: " + direction + endl;
	}

	public DirectionalLight initIntensity(Vec intensity) {
		return (DirectionalLight) super.initIntensity(intensity);
	}

	public Ray rayToLight(Point fromPoint) {
		return new Ray(fromPoint, this.direction.neg());
	}

	public boolean isOccludedBy(Surface surface, Ray rayToLight) {
		return surface.intersect(rayToLight) != null;
	}

	public Vec intensity(Point hittingPoint, Ray rayToLight) {
		return this.intensity;
	}
}
