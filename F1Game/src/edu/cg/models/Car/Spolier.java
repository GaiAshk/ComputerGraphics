package edu.cg.models.Car;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import edu.cg.models.IRenderable;
import edu.cg.models.SkewedBox;

public class Spolier implements IRenderable {
	private static final SkewedBox spoilerBase = new SkewedBox(Specification.S_LENGTH, Specification.S_BASE_HEIGHT, Specification.S_BASE_HEIGHT, Specification.S_BASE_DEPTH, Specification.S_BASE_DEPTH);
	private static final SkewedBox spoilerWings = new SkewedBox(Specification.S_LENGTH, Specification.S_WINGS_HEIGHT, 4.0*Specification.S_BASE_HEIGHT, Specification.S_WINGS_DEPTH, Specification.S_WINGS_DEPTH);

	/**
	 * Destroy the model and free resources. This should be used
	 * to destroy textures (if any).
	 * @param gl GL context
	 */
	public void destroy(GL2 gl){
		spoilerBase.destroy(gl);
		spoilerWings.destroy(gl);
	}

	@Override
	public void render(GL2 gl) {
		Materials.SetDarkGreyMetalMaterial(gl);
		GLU glu = new GLU();
		GLUquadric quad = glu.gluNewQuadric();
		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, Specification.S_ROD_RADIUS+Specification.S_RODS_DISTANCE/2.0);
		gl.glRotated(-90.0, 1.0, 0.0, 0.0);
		glu.gluCylinder(quad, Specification.S_ROD_RADIUS, Specification.S_ROD_RADIUS, Specification.S_ROD_HIEGHT, 20, 2);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, -Specification.S_ROD_RADIUS-Specification.S_RODS_DISTANCE/2.0);
		gl.glRotated(-90.0, 1.0, 0.0, 0.0);
		glu.gluCylinder(quad, Specification.S_ROD_RADIUS, Specification.S_ROD_RADIUS, Specification.S_ROD_HIEGHT, 20, 2);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(0.0, Specification.S_ROD_HIEGHT, 0.0);
		Materials.SetDarkRedMetalMaterial(gl);
		spoilerBase.render(gl);
		gl.glPushMatrix();
		gl.glTranslated(0.0, 3.0*Specification.S_BASE_HEIGHT, 0.0);
		spoilerBase.render(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0, 0.5*Specification.S_BASE_DEPTH + 0.5*Specification.S_WINGS_DEPTH);
		Materials.SetRedMetalMaterial(gl);
		spoilerWings.render(gl);
		gl.glPopMatrix();
		gl.glTranslated(0.0, 0.0, -0.5*Specification.S_BASE_DEPTH - 0.5*Specification.S_WINGS_DEPTH);
		spoilerWings.render(gl);
		gl.glPopMatrix();
		glu.gluDeleteQuadric(quad);
	}

	@Override
	public void init(GL2 gl) {
	}
	
	@Override
	public String toString() {
		return "Spoiler";
	}
}
