package edu.cg.algebra;

import java.nio.FloatBuffer;

public class Point {
	public float x, y, z;
	
	public Point(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point(double x, double y, double z) {
		this((float)x, (float)y, (float)z);
	}
	
	public Point(Point other) {
		this(other.x, other.y, other.z);
	}
	
	public Point(float val) {
		this(val, val, val);
	}
	
	public Point(double val) {
		this((float)val);
	}
	
	public Point() {
		this(0f);
	}
	
	public Point add(float t, Vec v) {
		return Ops.add(this, t, v);
	}
	
	public Point add(double t, Vec v) {
		return Ops.add(this, t, v);
	}
	
	public Point add(Vec v) {
		return Ops.add(this, v);
	}
	
	public Point mult(float a) {
		return Ops.mult(a, this);
	}
	
	public Point mult(Point p) {
		return Ops.mult(this, p);
	}
	
	public Point add(Point p) {
		return Ops.add(this, p);
	}
	
	public float dist(Point other) {
		return Ops.dist(this, other);
	}
	
	public float distSqr(Point other) {
		return Ops.distSqr(this, other);
	}
	
	public Vec sub(Point other) {
		return Ops.sub(this, other);
	}
	
	public Vec toVec() {
		return sub(new Point());
	}
	
	public FloatBuffer toGLVertex() {
		return FloatBuffer.wrap(toArray());
	}
	
	public float[] toArray() {
		return new float[] {x, y, z};
	}
	
	public boolean isFinite() {
		return Ops.isFinite(this);
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
