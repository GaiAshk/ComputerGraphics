package edu.cg.algebra;

public class Ray {
	private final Point source;
	private final Vec direction;
	
	public Ray(Point source, Vec direction) {
		this.source = source;
		this.direction = direction.normalize();
	}
	
	public Ray(Point p0, Point p1) {
		this(p0, p1.sub(p0).normalize());
	}
	
	public Point source() {
		return source;
	}
	
	public Vec direction() {
		return direction;
	}
	
	public Point add(double t) {
		// returns: p0 + t*direction
		return source.add(t, direction);
	}
	
	public Point getHittingPoint(Hit hit) {
		return add(hit.t());
	}
	
	public Ray inverse() {
		return new Ray(source, direction.neg());
	}
	
}
