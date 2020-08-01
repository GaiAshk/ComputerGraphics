package edu.cg.algebra;

//import ex3.UnimplementedMethodException;

public class Ops {
	public static final float epsilon = 1e-5f;
	public static final float infinity = 1e8f;
	
	public static float dot(Vec u, Vec v) {
		return u.x*v.x + u.y*v.y + u.z*v.z;
	}
	
	public static Vec cross(Vec u, Vec v) {
		return new Vec((u.y*v.z - u.z*v.y), (u.z*v.x - u.x*v.z), (u.x*v.y - u.y*v.x));
	}
	
	public static Vec mult(float a, Vec v) {
		return mult(new Vec(a), v);
	}
	
	public static Vec mult(double a, Vec v) {
		return mult((float)a, v);
	}
	
	public static Vec mult(Vec u, Vec v) {
		return new Vec(u.x*v.x, u.y*v.y, u.z*v.z);
	}
	
	public static Point mult(float a, Point p) {
		return mult(new Point(a), p);
	}
	
	public static Point mult(double a, Point p) {
		return mult((float)a, p);
	}
	
	public static Point mult(Point p1, Point p2) {
		return new Point(p1.x*p2.x, p1.y*p2.y, p1.z*p2.z);
	}
	
	public static float normSqr(Vec v) {
		return dot(v, v);
	}
	
	public static float norm(Vec v) {
		return (float)Math.sqrt(normSqr(v));
	}
	
	public static float lengthSqr(Vec v) {
		return normSqr(v);
	}
	
	public static float length(Vec v) {
		return norm(v);
	}
	
	public static float dist(Point p1, Point p2) {
		return length(sub(p1, p2));
	}
	
	public static float distSqr(Point p1, Point p2) {
		return lengthSqr(sub(p1, p2));
	}
	
	public static Vec normalize(Vec v) {
		return mult(1f/norm(v), v);
	}
	
	public static Vec neg(Vec v) {
		return mult(-1f, v);
	}
	
	public static Vec add(Vec u, Vec v) {
		return new Vec(u.x+v.x, u.y+v.y, u.z+v.z);
	}
	
	public static Point add(Point p, Vec v) {
		return new Point(p.x+v.x, p.y+v.y, p.z+v.z);
	}
	
	public static Point add(Point p1, Point p2) {
		return new Point(p1.x+p2.x, p1.y+p2.y, p1.z+p2.z);
	}
	
	public static Point add(Point p, float t, Vec v) {
		//returns p + tv;
		return add(p, mult(t, v));
	}
	
	public static Point add(Point p, double t, Vec v) {
		//returns p + tv;
		return add(p, (float)t, v);
	}
	
	public static Vec sub(Point p1, Point p2) {
		return new Vec(p1.x-p2.x, p1.y-p2.y, p1.z-p2.z);
	}
	
	public static boolean isFinite(Vec v) {
		return Float.isFinite(v.x) & Float.isFinite(v.y) & Float.isFinite(v.z);
	}

	public static boolean isFinite(Point p) {
		return Float.isFinite(p.x) & Float.isFinite(p.y) & Float.isFinite(p.z);
	}
	
	public static Vec reflect(Vec u, Vec normal) {
		return add(u, mult(-2*dot(u, normal), normal));
	}
}
