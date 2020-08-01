package edu.cg.models.Car;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import edu.cg.algebra.Point;
import edu.cg.models.BoundingSphere;
import edu.cg.models.IRenderable;

import java.util.LinkedList;
import java.util.List;

public class Wheel implements IRenderable {

	/**
	 * Destroy the model and free resources. This should be used
	 * to destroy textures (if any).
	 * @param gl GL context
	 */
	public void destroy(GL2 gl){}

	@Override
	public void render(GL2 gl) {
		GLU glu = new GLU();
		GLUquadric quad = glu.gluNewQuadric();
		// Render Tire:
		Materials.setMaterialTire(gl);
		gl.glPushMatrix();
		gl.glTranslated(0, 0, -1.0 * Specification.TIRE_DEPTH / 2.0);
		glu.gluCylinder(quad, Specification.TIRE_RADIUS, Specification.TIRE_RADIUS, Specification.TIRE_DEPTH, 20, 1);
		gl.glRotated(180.0, 1.0, 0.0, 0.0);
		glu.gluDisk(quad, 0.8 * Specification.TIRE_RADIUS, Specification.TIRE_RADIUS, 20, 1);
		gl.glRotated(180.0, 1.0, 0.0, 0.0);
		gl.glTranslated(0, 0, Specification.TIRE_DEPTH);
		glu.gluDisk(quad, 0.8 * Specification.TIRE_RADIUS, Specification.TIRE_RADIUS, 20, 1);
		// Render Rims:
		Materials.setMaterialRims(gl);
		glu.gluDisk(quad, 0.0, 0.8 * Specification.TIRE_RADIUS, 20, 1);
		gl.glTranslated(0.0,0.0, -1*Specification.TIRE_DEPTH);
		gl.glRotated(180.0, 1.0, 0.0, 0.0);
		glu.gluDisk(quad, 0.0, 0.8 * Specification.TIRE_RADIUS, 20, 1);
		
		gl.glPopMatrix();
		glu.gluDeleteQuadric(quad);
	}

	@Override
	public void init(GL2 gl) {
	}
	
	@Override
	public String toString() {
		return "Wheel";
	}

//	public List<BoundingSphere> getBoundingSpheres() {
//		LinkedList<BoundingSphere> res = new LinkedList<BoundingSphere>();
//		//center in the wheel coordinate system is (0,0,0)
//		Point sphereBoundCenter = new Point();
//		// the radius the distance to farthest point in the cylinder
//		double sphereBoundRadius =
//				sphereBoundCenter.sub(new Point(Specification.TIRE_RADIUS, 0, Specification.TIRE_DEPTH/2)).length();
//		BoundingSphere s1 = new BoundingSphere(sphereBoundRadius, sphereBoundCenter);
//		s1.setSphereColore3d(0,0,1);
//		res.add(s1);
//		return res;
//	}
}
