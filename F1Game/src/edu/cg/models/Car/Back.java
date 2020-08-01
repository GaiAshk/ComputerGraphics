package edu.cg.models.Car;

import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import edu.cg.algebra.Point;
import edu.cg.models.BoundingSphere;
import edu.cg.models.IIntersectable;
import edu.cg.models.IRenderable;
import edu.cg.models.SkewedBox;

import static edu.cg.models.BoundingSphere.translateAndColorList;

public class Back implements IRenderable, IIntersectable {
	private SkewedBox baseBox = new SkewedBox(Specification.B_BASE_LENGTH, Specification.B_BASE_HEIGHT,
			Specification.B_BASE_HEIGHT, Specification.B_BASE_DEPTH, Specification.B_BASE_DEPTH);
	private SkewedBox backBox = new SkewedBox(Specification.B_LENGTH, Specification.B_HEIGHT_1,
			Specification.B_HEIGHT_2, Specification.B_DEPTH_1, Specification.B_DEPTH_2);
	private PairOfWheels wheels = new PairOfWheels();
	private Spolier spoiler = new Spolier();

	/**
	 * Destroy the model and free resources. This should be used
	 * to destroy textures (if any).
	 * @param gl GL context
	 */
	public void destroy(GL2 gl){
		baseBox.destroy(gl);
		backBox.destroy(gl);
		wheels.destroy(gl);
		spoiler.destroy(gl);
	}

	@Override
	public void render(GL2 gl) {
		gl.glPushMatrix();
		Materials.SetBlackMetalMaterial(gl);
		gl.glTranslated(Specification.B_LENGTH / 2.0 - Specification.B_BASE_LENGTH / 2.0, 0.0, 0.0);
		baseBox.render(gl);
		Materials.SetRedMetalMaterial(gl);
		gl.glTranslated(-1.0 * (Specification.B_LENGTH / 2.0 - Specification.B_BASE_LENGTH / 2.0),
				Specification.B_BASE_HEIGHT, 0.0);
		backBox.render(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-Specification.B_LENGTH / 2.0 + Specification.TIRE_RADIUS, 0.5 * Specification.TIRE_RADIUS,
				0.0);
		wheels.render(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-Specification.B_LENGTH / 2.0 + 0.5 * Specification.S_LENGTH,
				0.5 * (Specification.B_HEIGHT_1 + Specification.B_HEIGHT_2), 0.0);
		spoiler.render(gl);
		//Exhaust Pipes
        GLU glu = new GLU();
        Materials.SetBlackMetalMaterial(gl);
        gl.glPushMatrix();
        gl.glTranslated(0, -0.07, 0);
        gl.glRotated(90.0,1,0,0);
        gl.glRotated(-90.0,0,1,0);
        GLUquadric quad = glu.gluNewQuadric();
        //Pipe 1
        gl.glPushMatrix();
        gl.glTranslated(0, 0.029, 0);       //remember rotation
        glu.gluCylinder(quad, Specification.B_EP_RADIUS_1,Specification.B_EP_RADIUS_2,
                Specification.B_EP_HEIGHT,10,10);
        gl.glPopMatrix();
        //Pipe 2
        gl.glPushMatrix();
        gl.glTranslated(0, -0.029, 0);   //remember rotation
        glu.gluCylinder(quad, Specification.B_EP_RADIUS_1,Specification.B_EP_RADIUS_2,
                Specification.B_EP_HEIGHT,10,10);
        gl.glPopMatrix();
        gl.glPopMatrix();
        glu.gluDeleteQuadric(quad);
		gl.glPopMatrix();
	}

	@Override
	public void init(GL2 gl) { }

	@Override
	public BoundingSphere getBoundingSpheres() {
		//Find Center and Radius of bounding sphere
		Point sphereBoundCenter = new Point(0, ((Specification.PAIR_OF_WHEELS_ROD_RADIUS/2) +
						(0.5 * (Specification.B_HEIGHT_1 + Specification.B_HEIGHT_2)) +
						(Specification.S_ROD_HIEGHT + Specification.S_WINGS_HEIGHT))/2,0);
		double sphereBoundRadius = sphereBoundCenter.sub(new Point(-Specification.B_LENGTH / 2.0,
						Specification.B_BASE_HEIGHT + Specification.B_HEIGHT_1 +
								Specification.S_ROD_HIEGHT + Specification.S_WINGS_HEIGHT,
						Specification.S_BASE_DEPTH/2 + Specification.S_WINGS_DEPTH)).length();
		BoundingSphere s1 = new BoundingSphere(sphereBoundRadius, sphereBoundCenter);
		s1.setSphereColore3d(0,0,1);

		//Add children bounding spheres to Back, translate and color correctly
		// baseBoxSphere - sphere bounding the baseBox
		List<BoundingSphere> baseBoxSphereList = baseBox.getBoundingSpheres();
		translateAndColorList(baseBoxSphereList, Specification.B_LENGTH / 2.0 - Specification.B_BASE_LENGTH / 2.0,
				Specification.B_BASE_HEIGHT/2, 0.0, 0,0,1);
		s1.addList(baseBoxSphereList);

		// backBoxSphere - sphere bounding the Back, translate and color correctly
		List<BoundingSphere> backBoxSphereList = backBox.getBoundingSpheres();
		translateAndColorList(backBoxSphereList, 0,(Specification.B_HEIGHT_1/2 +
				Specification.B_HEIGHT_2/2)/2,0, 0,0,1);
		s1.addList(backBoxSphereList);

		return s1;
	}
}
