package edu.cg.scene.objects;

import edu.cg.algebra.*;

public class Sphere extends Shape {
	private Point center;
	private double radius;
	
	public Sphere(Point center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public Sphere() {
		this(new Point(0, -0.5, -6), 0.5);
	}
	
	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Sphere:" + endl + 
				"Center: " + center + endl +
				"Radius: " + radius + endl;
	}
	
	public Sphere initCenter(Point center) {
		this.center = center;
		return this;
	}
	
	public Sphere initRadius(double radius) {
		this.radius = radius;
		return this;
	}

	public Point getCenter(){return this.center;}

	public double getRadius(){return this.radius;}

	/**
	 * A helper method that calculates the values of ax+by+cz+d for a point (x,y,z).
	 * This can be used to classify points with respect to the given plain.
	 *
	 * @param p - the source point.
	 * @return The value (x-a)^2+(y-b)^2+(z-c)^2-(r^2), if 0 p is on the sphere
	 */
	public double substitute(Point p) {
		double x =  this.center.x - p.x;
		x *= x;
		double y =  this.center.y - p.y;
		y *= y;
		double z =  this.center.z - p.z;
		z *= z;
		return x+y+z-(this.radius*this.radius);
	}

	//need to remember that t>0 can return values smaller than 0
	public Hit intersect(Ray ray) {
		double t; boolean isIn = false;

		//we need to solve quadratic equation ax^2 + bx +c = 0 (where a=1)
		double b = ray.direction().mult(2).dot(ray.source().sub(this.center));
		double c = this.substitute(ray.source());
		double root = Math.sqrt((b*b) - 4.0D*c);
		if(Double.isNaN(root)) return null;

		//find zeros (t's) that satisfy answer
		double t1 = ((-1)*b + root) / (2.0D);
		double t2 = ((-1)*b - root) / (2.0D);	//remember t2 is always smaller then t1

		//if both t's are smaller then zero, not intersection
		if(t1 < Ops.epsilon && t2 < Ops.epsilon){ return null;}

		//else there is at lest one hit
		if(t1 < Ops.epsilon){
			//this should not happen
			System.out.println("bug in Sphere intersect");
			t = t2;
		} else if(t2 < Ops.epsilon){
			t = t1;
		} else {
			t = Math.min(t1, t2);
		}
		//if the smaller intersection (or only one) is infinity, return null
		if(t > Ops.infinity) return null;
		//calculate normal to surface
		Point hittedPoint = ray.add(t);
		Vec normalToSurface = (hittedPoint.sub(this.center)).normalize();

		//check if ray is coming from with in the sphere
		if(substitute(ray.source()) < Ops.epsilon && substitute(ray.add(t)) < Ops.epsilon) isIn = true;

		return (new Hit(t, normalToSurface)).setIsWithin(isIn);
	}


}
