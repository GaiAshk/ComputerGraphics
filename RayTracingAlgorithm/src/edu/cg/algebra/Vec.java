package edu.cg.algebra;

import java.awt.Color;

public class Vec {
	public double x, y, z;
	
	public Vec(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec(double val) {
		this(val, val, val);
	}
	
	public Vec(Vec other) {
		this(other.x, other.y, other.z);
	}
	
	public Vec() {
		this(0);
	}
	
	public double norm() {
		return Ops.norm(this);
	}
	
	public double normSqr() {
		return Ops.normSqr(this);
	}
	
	public double length() {
		return Ops.length(this);
	}
	
	public double lengthSqr() {
		return Ops.lengthSqr(this);
	}
	
	public Vec normalize() {
		return Ops.normalize(this);
	}
	
	public Vec neg() {
		return Ops.neg(this);
	}

	public double dot(Vec other) {
		return Ops.dot(this, other);
	}

	public Vec cross(Vec other) {
		return Ops.cross(this, other);
	}

	public Vec mult(double a) {
		return Ops.mult(a, this);
	}
	
	public Vec mult(Vec v) {
		return Ops.mult(this, v);
	}
	
	public Vec add(Vec v) {
		return Ops.add(this, v);
	}
	
	public boolean isFinite() {
		return Ops.isFinite(this);
	}
	
	public Color toColor() {
		return new Color(clip(x), clip(y), clip(z));
	}
	
	private static float clip(double val) {
		return (float)Math.min(1, Math.max(0, val));
	}
	
	public double[] asArray() {
		double ret[] = new double[3];
		ret[0] = x;
		ret[1] = y;
		ret[2] = z;
		return ret;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	
}
