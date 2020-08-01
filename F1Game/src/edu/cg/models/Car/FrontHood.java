package edu.cg.models.Car;

import com.jogamp.opengl.GL2;

import edu.cg.models.BoundingSphere;
import edu.cg.models.IRenderable;
import edu.cg.models.SkewedBox;

import java.util.LinkedList;
import java.util.List;

public class FrontHood implements IRenderable {
	private SkewedBox hoodBox1 = new SkewedBox(Specification.F_HOOD_LENGTH_1, Specification.F_HOOD_HEIGHT_1,
			Specification.F_HOOD_HEIGHT_2, Specification.F_HOOD_DEPTH_1,
			Specification.F_HOOD_DEPTH_2);
	private SkewedBox hoodBox2 = new SkewedBox(Specification.F_HOOD_LENGTH_2, Specification.F_HOOD_HEIGHT_2,
			Specification.F_BUMPER_HEIGHT_1, Specification.F_HOOD_DEPTH_2, Specification.F_HOOD_DEPTH_3);

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
		double hoodLength = Specification.F_HOOD_LENGTH_1 + Specification.F_HOOD_LENGTH_2;
		// Render hood - Use Red Material.
		Materials.SetRedMetalMaterial(gl);
		gl.glTranslated(-hoodLength / 2.0 + Specification.F_HOOD_LENGTH_1 / 2.0, 0.0, 0.0);
		hoodBox1.render(gl);
		gl.glTranslated(Specification.F_HOOD_LENGTH_1 / 2.0 + Specification.F_HOOD_LENGTH_2 / 2.0, 0.0, 0.0);
		hoodBox2.render(gl);
		gl.glPopMatrix();
	}

	@Override
	public void init(GL2 gl) {
	}
	
	@Override
	public String toString() {
		return "FrontHood";
	}

	public List<BoundingSphere> getBoundingSpheres() {
		LinkedList<BoundingSphere> res = new LinkedList<BoundingSphere>();
		double hoodLength = Specification.F_HOOD_LENGTH_1 + Specification.F_HOOD_LENGTH_2;

		// hoodBox1Sphere - sphere bounding the hoodBox1
		List<BoundingSphere> hoodBox1SphereList = hoodBox1.getBoundingSpheres();
		BoundingSphere hoodBox1Sphere = hoodBox1SphereList.get(0);
		hoodBox1Sphere.translateCenter(-hoodLength / 2.0 + Specification.F_HOOD_LENGTH_1 / 2.0, 0.0, 0.0);
		res.add(hoodBox1Sphere);

		// hoodBox2Sphere - sphere bounding the hoodBox2
		List<BoundingSphere> hoodBox2SphereList = hoodBox2.getBoundingSpheres();
		BoundingSphere hoodBox2Sphere = hoodBox2SphereList.get(0);
		hoodBox2Sphere.translateCenter(Specification.F_HOOD_LENGTH_1 / 2.0 + Specification.F_HOOD_LENGTH_2 / 2.0, 0.0, 0.0);
		res.add(hoodBox2Sphere);

		return res;
	}

}
