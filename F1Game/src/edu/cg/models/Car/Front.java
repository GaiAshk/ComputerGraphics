package edu.cg.models.Car;

import java.util.List;

import com.jogamp.opengl.GL2;
import edu.cg.algebra.Point;
import edu.cg.models.BoundingSphere;
import edu.cg.models.IIntersectable;
import edu.cg.models.IRenderable;

import static edu.cg.models.BoundingSphere.translateAndColorList;

public class Front implements IRenderable, IIntersectable {
	private FrontHood hood = new FrontHood();
	private PairOfWheels wheels = new PairOfWheels();
	private FrontBumber bumber = new FrontBumber();

	/**
	 * Destroy the model and free resources. This should be used
	 * to destroy textures (if any).
	 * @param gl GL context
	 */
	public void destroy(GL2 gl){
		// TODO: BoundingSphere is implementing IRenderable, this method must be implemented
	}

	@Override
	public void render(GL2 gl) {
		gl.glPushMatrix();
		// Render hood - Use Red Material.
		gl.glTranslated(-Specification.F_LENGTH / 2.0 + Specification.F_HOOD_LENGTH / 2.0, 0.0, 0.0);
		hood.render(gl);
		// Render the wheels.
		gl.glTranslated(Specification.F_HOOD_LENGTH / 2.0 - 1.25 * Specification.TIRE_RADIUS,
				0.5 * Specification.TIRE_RADIUS, 0.0);
		wheels.render(gl);
		gl.glPopMatrix();
		//Render front Bumper
		gl.glPushMatrix();
		gl.glTranslated(Specification.F_HOOD_LENGTH / 2.0, 0.0, 0.0);
		bumber.render(gl);
		gl.glPopMatrix();
	}

	@Override
	public void init(GL2 gl) { }

	@Override
	public BoundingSphere getBoundingSpheres() {
		//Find Center and Radius of bounding sphere
		Point sphereBoundCenter = new Point(0, Specification.F_HOOD_HEIGHT_2 /2, 0);
		double sphereBoundRadius =
				sphereBoundCenter.sub(new Point(-Specification.F_LENGTH / 2.0 +
						Specification.F_HOOD_LENGTH / 2.0 -(Specification.F_HOOD_LENGTH_1 +
						Specification.F_HOOD_LENGTH_2)/ 2.0 + Specification.F_HOOD_LENGTH_1 / 2.0 -
						Specification.F_HOOD_LENGTH_1/2,
						Specification.F_HOOD_HEIGHT_1,
						Specification.F_HOOD_DEPTH_1/2)).length();
		BoundingSphere s1 = new BoundingSphere(sphereBoundRadius, sphereBoundCenter);
		s1.setSphereColore3d(1,0,0);

		//Add children bounding spheres to Front
		// hoodSphere - sphere bounding bodyBase, add as children of Front
		List<BoundingSphere> hoodSphereList = hood.getBoundingSpheres();
		translateAndColorList(hoodSphereList, -Specification.F_LENGTH / 2.0 +
				Specification.F_HOOD_LENGTH / 2.0, 0.0, 0.0, 1, 0 ,0);
		s1.addList(hoodSphereList);

		// bumperSphere - sphere bounding bodyBase, add as children of Front
		List<BoundingSphere> bumperSphereList = bumber.getBoundingSpheres();
		translateAndColorList(bumperSphereList, Specification.F_HOOD_LENGTH / 2.0, 0.0, 0.0, 1,0,0);
		s1.addList(bumperSphereList);

		return s1;
	}

	@Override
	public String toString() {
		return "CarFront";
	}
}
