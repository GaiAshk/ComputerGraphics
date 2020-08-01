package edu.cg.models.Car;

import com.jogamp.opengl.GL2;

import edu.cg.algebra.Point;
import edu.cg.models.BoundingSphere;
import edu.cg.models.IRenderable;
import edu.cg.models.SkewedBox;

import java.util.LinkedList;
import java.util.List;

public class FrontBumber implements IRenderable {
	// TODO: Add fields as you like (and methods if you think they are necessary).
	private double bumperBoxDepthfactor = 1.75;//global variable for easier changes in multiple places
	private SkewedBox bumperBox = new SkewedBox(Specification.F_BUMPER_LENGTH,
			Specification.F_BUMPER_HEIGHT_1, Specification.F_BUMPER_HEIGHT_2,
			Specification.F_BUMPER_DEPTH * bumperBoxDepthfactor, Specification.F_BUMPER_DEPTH * 1.75);
	private SkewedBox bumperWingsBoxRight = new SkewedBox(Specification.F_BUMPER_LENGTH,
			Specification.F_BUMPER_WINGS_HEIGHT_1, Specification.F_BUMPER_WINGS_HEIGHT_2 * 1.5,
			Specification.F_BUMPER_WINGS_DEPTH, Specification.F_BUMPER_WINGS_DEPTH);
	private SkewedBox bumperWingsBoxLeft = new SkewedBox(Specification.F_BUMPER_LENGTH,
			Specification.F_BUMPER_WINGS_HEIGHT_1, Specification.F_BUMPER_WINGS_HEIGHT_2 * 1.5,
			Specification.F_BUMPER_WINGS_DEPTH, Specification.F_BUMPER_WINGS_DEPTH);
	//Front Lights of the car
	private BoundingSphere light = new BoundingSphere(0.025, new Point(0.0));

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
		//Bumper
		Materials.SetRedMetalMaterial(gl);
		bumperBox.render(gl);
		//Wing
		Materials.SetDarkRedMetalMaterial(gl);
		gl.glPushMatrix();
		// Right wing
		gl.glTranslated(0.0, 0.0,
				Specification.F_BUMPER_DEPTH * bumperBoxDepthfactor / 2.0
						- Specification.F_BUMPER_WINGS_DEPTH/ 2.0);
		bumperWingsBoxRight.render(gl);
		//Right Light 1
		light.setCenter(new Point(0.0));
		light.translateCenter(0.0, 0.05, 0.0);
		light.setSphereColore3d(1.0, 0.0, 0.0);
		light.render(gl);
		gl.glPopMatrix();

		// Left wing
		Materials.SetDarkRedMetalMaterial(gl);
		gl.glPushMatrix();
		gl.glTranslated(0.0, 0.0,
				-Specification.F_BUMPER_DEPTH * bumperBoxDepthfactor / 2.0
						+ Specification.F_BUMPER_WINGS_DEPTH / 2.0);
		bumperWingsBoxLeft.render(gl);
		//Left Light
		light.setCenter(new Point(0.0));
		light.translateCenter(0.0, 0.05, 0.0);
		light.render(gl);
		gl.glPopMatrix();
	}

	@Override
	public void init(GL2 gl) {
	}

	@Override
	public String toString() {
		return "FrontBumper";
	}

	public List<BoundingSphere> getBoundingSpheres() {
		LinkedList<BoundingSphere> res = new LinkedList<BoundingSphere>();

		// bumperBoxSphere - sphere bounding the baseBox
		List<BoundingSphere> bumperBoxSphereList = bumperBox.getBoundingSpheres();
		BoundingSphere bumperBoxSphere = bumperBoxSphereList.get(0);
		res.add(bumperBoxSphere);

		// bumperWingsBoxRightSphere - sphere bounding the baseBox
		List<BoundingSphere> bumperWingsBoxRightSphereList =
				bumperWingsBoxRight.getBoundingSpheres();
		BoundingSphere bumperWingsBoxRightSphere = bumperWingsBoxRightSphereList.get(0);
		bumperWingsBoxRightSphere.translateCenter(0.0, 0.0,
				Specification.F_BUMPER_DEPTH * bumperBoxDepthfactor / 2.0
						- Specification.F_BUMPER_WINGS_DEPTH/ 2.0);
		res.add(bumperWingsBoxRightSphere);

		// bumperWingsBoxLeftSphere - sphere bounding the baseBox
		List<BoundingSphere> bumperWingsBoxLeftSphereList =
				bumperWingsBoxLeft.getBoundingSpheres();
		BoundingSphere bumperWingsBoxLeftSphere = bumperWingsBoxLeftSphereList.get(0);
		bumperWingsBoxLeftSphere.translateCenter(0.0, 0.0,
				-Specification.F_BUMPER_DEPTH * bumperBoxDepthfactor / 2.0
						+ Specification.F_BUMPER_WINGS_DEPTH / 2.0);
		res.add(bumperWingsBoxLeftSphere);

		return res;
	}

}
