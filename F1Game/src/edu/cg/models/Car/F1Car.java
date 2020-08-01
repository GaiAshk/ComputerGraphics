package edu.cg.models.Car;

import com.jogamp.opengl.*;

import edu.cg.algebra.Point;
import edu.cg.models.BoundingSphere;
import edu.cg.models.IIntersectable;
import edu.cg.models.IRenderable;

/**
 * A F1 Racing Car.
 *
 */
public class F1Car implements IRenderable, IIntersectable {
	Center carCenter = new Center();
	Back carBack = new Back();
	Front carFront = new Front();

    /**
     * Destroy the model and free resources. This should be used
     * to destroy textures (if any).
     * @param gl GL context
     */
    public void destroy(GL2 gl){
		carFront.destroy(gl);
		carCenter.destroy(gl);
		carBack.destroy(gl);
    }

	@Override
	public void render(GL2 gl) {
		carCenter.render(gl);
		gl.glPushMatrix();
		gl.glTranslated(-Specification.B_LENGTH / 2.0 - Specification.C_BASE_LENGTH / 2.0, 0.0, 0.0);
		carBack.render(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(Specification.F_LENGTH / 2.0 + Specification.C_BASE_LENGTH / 2.0, 0.0, 0.0);
		carFront.render(gl);
		gl.glPopMatrix();
	}

	@Override
	public String toString() {
		return "F1Car";
	}

	@Override
	public void init(GL2 gl) {}

	@Override
	public BoundingSphere getBoundingSpheres() {
		// s1 - sphere bounding the whole car, find center and radius of s1
		Point sphereBoundCenter = new Point(0, /*y value is the average height of car */
				((Specification.TIRE_RADIUS/2) +
						Specification.B_BASE_HEIGHT + Specification.B_HEIGHT_1  +
						(Specification.S_ROD_HIEGHT + Specification.S_WINGS_HEIGHT))/2, 0);
		double sphereBoundRadius =
				sphereBoundCenter.sub(new Point(Specification.F_LENGTH / 2.0 +
						Specification.C_BASE_LENGTH / 2.0 + Specification.F_HOOD_LENGTH / 2.0 +
						Specification.F_BUMPER_LENGTH/2
						, 0, Specification.F_BUMPER_DEPTH * 1.75 / 2)).length();
		BoundingSphere s1 = new BoundingSphere(sphereBoundRadius, sphereBoundCenter);
		s1.setSphereColore3d(0,0,0);

		//Add children to s1, s1's children will have there children as well, tree hierarchy
		// s2 - sphere bounding the car front, with all of his children
		BoundingSphere s2 = carFront.getBoundingSpheres();		//get bounding sphere of front
		s2.translateCenterToChildrenAndMe(Specification.F_LENGTH / 2.0 + Specification.C_BASE_LENGTH / 2.0,
				0.0, 0.0);		//translate all the children in front to match F1Car coordinates
		s1.addChild(s2);		//add front as children of F1Car

		// s3 - sphere bounding the car center
		BoundingSphere s3 = carCenter.getBoundingSpheres();		//get bounding sphere of center
		s3.setSphereColore3d(0,1,0);
		s1.addChild(s3);		//add center as children of F1Car

		// s4 - sphere bounding the car back
		BoundingSphere s4 = carBack.getBoundingSpheres();		//get bounding sphere of back
		s4.translateCenterToChildrenAndMe(-Specification.B_LENGTH / 2.0 -
				Specification.C_BASE_LENGTH / 2.0, 0.0, 0.0);
		s1.addChild(s4);		//add front as children of F1Car

		return s1;
	}
}
