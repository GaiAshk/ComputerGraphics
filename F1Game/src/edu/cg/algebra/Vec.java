package edu.cg.algebra;

import java.nio.FloatBuffer;

public class Vec {
	public float x, y, z;
	
	public Vec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec(double x, double y, double z) {
		this((float)x, (float)y, (float)z);
	}
	
	public Vec(float val) {
		this(val, val, val);
	}
	
	public Vec(double val) {
		this((float)val);
	}
	
	public Vec(Vec other) {
		this(other.x, other.y, other.z);
	}
	
	public Vec() {
		this(0f);
	}
	
	public float norm() {
		return Ops.norm(this);
	}
	
	public float normSqr() {
		return Ops.normSqr(this);
	}
	
	public float length() {
		return Ops.length(this);
	}
	
	public float lengthSqr() {
		return Ops.lengthSqr(this);
	}
	
	public Vec normalize() {
		return Ops.normalize(this);
	}
	
	public Vec neg() {
		return Ops.neg(this);
	}

	public float dot(Vec other) {
		return Ops.dot(this, other);
	}

	public Vec cross(Vec other) {
		return Ops.cross(this, other);
	}

	public Vec mult(float a) {
		return Ops.mult(a, this);
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
	
	public FloatBuffer toGLColor() {
		return FloatBuffer.wrap(clip().toArray());
	}
	
	public float[] toArray() {
		return new float[] {x, y, z};
	}
	
	public Vec clip() {
		return new Vec(clip(x), clip(y), clip(z));
	}
	
	private static float clip(float val) {
		return Math.min(1, Math.max(0, val));
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	
}
