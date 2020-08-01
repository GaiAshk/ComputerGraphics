package edu.cg.models.Car;

import com.jogamp.opengl.GL2;

import edu.cg.algebra.Point;
import edu.cg.models.BoundingSphere;
import edu.cg.models.IIntersectable;
import edu.cg.models.IRenderable;
import edu.cg.models.SkewedBox;

import java.util.List;

import static edu.cg.models.BoundingSphere.translateAndColorList;

public class Center implements IRenderable, IIntersectable {

	private SkewedBox bodyBase = new SkewedBox(Specification.C_BASE_LENGTH, Specification.C_BASE_HEIGHT,
			Specification.C_BASE_HEIGHT, Specification.C_DEPTH, Specification.C_DEPTH);
	private SkewedBox backBox = new SkewedBox(Specification.C_BACK_LENGTH, Specification.C_BACK_HEIGHT_1,
			Specification.C_BACK_HEIGHT_2, Specification.C_BACK_DEPTH, Specification.C_BACK_DEPTH);
	private SkewedBox frontBox = new SkewedBox(Specification.C_FRONT_LENGTH, Specification.C_FRONT_HEIGHT_1,
			Specification.C_FRONT_HEIGHT_2, Specification.C_FRONT_DEPTH_1, Specification.C_FRONT_DEPTH_2);
	private SkewedBox sideBox = new SkewedBox(Specification.C_SIDE_LENGTH, Specification.C_SIDE_HEIGHT_1,
			Specification.C_SIDE_HEIGHT_2, Specification.C_SIDE_DEPTH_1, Specification.C_SIDE_DEPTH_2);

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
		Materials.SetBlackMetalMaterial(gl);
		bodyBase.render(gl);
		Materials.SetRedMetalMaterial(gl);
		gl.glTranslated(Specification.C_BASE_LENGTH / 2.0 - Specification.C_FRONT_LENGTH / 2.0,
				Specification.C_BASE_HEIGHT, 0.0);
		frontBox.render(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-Specification.C_BASE_LENGTH / 2.0 + Specification.C_FRONT_LENGTH / 2.0,
				Specification.C_BASE_HEIGHT, 0.0);
		gl.glRotated(180, 0.0, 1.0, 0.0);
		frontBox.render(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(0.0, Specification.C_BASE_HEIGHT,
				Specification.C_SIDE_LENGTH / 2 + Specification.C_FRONT_DEPTH_1 / 2.0);
		gl.glRotated(90, 0.0, 1.0, 0.0);
		sideBox.render(gl);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(0.0, Specification.C_BASE_HEIGHT,
				-Specification.C_SIDE_LENGTH / 2 - Specification.C_FRONT_DEPTH_1 / 2.0);
		gl.glRotated(-90, 0.0, 1.0, 0.0);
		sideBox.render(gl);
		gl.glPopMatrix();
		Materials.SetBlackMetalMaterial(gl);
		gl.glPushMatrix();
		gl.glTranslated(
				-Specification.C_BASE_LENGTH / 2.0 + Specification.C_FRONT_LENGTH + Specification.C_BACK_LENGTH / 2.0,
				Specification.C_BASE_HEIGHT, 0.0);
		backBox.render(gl);
		gl.glPopMatrix();
	}

	@Override
	public void init(GL2 gl) { }

	@Override
	public BoundingSphere getBoundingSpheres() {
		//Find center and radius of boundingSphere
		Point sphereBoundCenter = new Point(0, Specification.C_FRONT_HEIGHT_2, 0);
		double sphereBoundRadius =
				sphereBoundCenter.sub(new Point(Specification.C_SIDE_DEPTH_1/2, Specification.C_BASE_HEIGHT,
						Specification.C_DEPTH/2)).length();
		BoundingSphere s1 = new BoundingSphere(sphereBoundRadius, sphereBoundCenter);
		s1.setSphereColore3d(0,1,0);

		//Add children bounding Spheres to Center, no need to translate here
		// bodyBaseSphere - sphere bounding bodyBase
		List<BoundingSphere> bodyBaseSphereList = bodyBase.getBoundingSpheres();
		translateAndColorList(bodyBaseSphereList,0,0,0, 0,1,0);
		s1.addList(bodyBaseSphereList);

		// backBoxSphere - sphere bounding the back box
		List<BoundingSphere> backBoxSphereList = backBox.getBoundingSpheres();
		translateAndColorList(backBoxSphereList, -Specification.C_BASE_LENGTH / 2.0 + Specification.C_FRONT_LENGTH + Specification.C_BACK_LENGTH / 2.0,
				Specification.C_BASE_HEIGHT, 0.0, 0,1,0);
		s1.addList(backBoxSphereList);

		// frontBoxSphere - sphere bounding the front box
		List<BoundingSphere> frontBoxSphereList = frontBox.getBoundingSpheres();
		translateAndColorList(frontBoxSphereList, Specification.C_BASE_LENGTH / 2.0 - Specification.C_FRONT_LENGTH / 2.0,
				Specification.C_BASE_HEIGHT, 0.0, 0,1,0);
		s1.addList(frontBoxSphereList);

		// sideBoxSphere - sphere bounding the side box
		List<BoundingSphere> sideBoxSphereList = sideBox.getBoundingSpheres();
		translateAndColorList(sideBoxSphereList, 0.0, Specification.C_BASE_HEIGHT,
				Specification.C_SIDE_LENGTH / 2 + Specification.C_FRONT_DEPTH_1 / 2.0, 0,1,0);
		s1.addList(sideBoxSphereList);

		return s1;
	}
}
