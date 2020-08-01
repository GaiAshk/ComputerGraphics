package edu.cg.algebra;

public class Mat3x3 {
	private final Vec col1, col2, col3;
	
	public Mat3x3(Vec col1, Vec col2, Vec col3) {
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
	}
	
	public Vec col1() {
		return new Vec(col1);
	}
	
	public Vec col2() {
		return new Vec(col2);
	}
	
	public Vec col3() {
		return new Vec(col3);
	}
	
	public Vec row1() {
		return new Vec(col1.x, col2.x, col3.x);
	}
	
	public Vec row2() {
		return new Vec(col1.y, col2.y, col3.y);
	}
	
	public Vec row3() {
		return  new Vec(col1.z, col2.z, col3.z);
	}

	public Mat3x3 transpose() {
		return new Mat3x3(row1(), row2(), row3());
	}
	
	public Vec mult(Vec v) {
		float x = row1().dot(v);
		float y = row2().dot(v);
		float z = row3().dot(v);
		return new Vec(x, y, z);
	}
}
