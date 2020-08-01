package edu.cg.models;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import edu.cg.algebra.Point;
import edu.cg.models.Car.Materials;
import edu.cg.models.SkewedBox;

public class TrackSegment implements IRenderable, IIntersectable {
	// TODO: Some constants you can use
	public final static double ASPHALT_TEXTURE_WIDTH = 20.0;
	public final static double ASPHALT_TEXTURE_DEPTH = 10.0;
	public final static double GRASS_TEXTURE_WIDTH = 10.0;
	public final static double GRASS_TEXTURE_DEPTH = 10.0;
	public final static double CUTTING_GRASS_TEXTURE_WIDTH = 5.0;
	public final static double CUTTING_GRASS_TEXTURE_DEPTH = 5.0;
	public final static double CUTTING_GRASS_TEXTURE_LENGTH = 5.0;
	public final static double CRASHED_CAR_TEXTURE_WIDTH = 10.0;
	public final static double CRASHED_CAR_TEXTURE_DEPTH = 10.0;
	public final static double TRACK_LENGTH = 500.0;
	public final static double BOX_LENGTH = 1.5;
	private LinkedList<Point> boxesLocations; // Stores the boxes centroids (center points).
	private SkewedBox box = null; // Used to represent a wooden box.
	private SkewedBox board = null; // BillBoard sign
	private Texture texRoad, texGrass, texCuttingGrass, texCrashedCar, texBillBoadSign;

	public void setDifficulty(double difficulty) {
		// Set the difficulty of the track segment. Here you decide what are the boxes locations.
		// We provide a simple implementation. You can change it if you want. But if you do decide to use it,
		// then it is your responsibility to understand the logic behind it.
		// Note: In our implementation, the difficulty is the probability of a box to appear in the scene. 
		// We divide the scene into rows of boxes and we sample boxes according the difficulty probability.
		difficulty = Math.min(difficulty, 0.75);
		difficulty = Math.max(difficulty, 0.2);
		double numberOfLanes = 4.0;
		double deltaZ = 0.0;
		if (difficulty < 0.25) {
			deltaZ = 100.0;
		} else if (difficulty < 0.5) {
			deltaZ = 75.0;
		} else {
			deltaZ = 50.0;
		}
		boxesLocations = new LinkedList<Point>();
		for (double dz = deltaZ; dz < TRACK_LENGTH - BOX_LENGTH / 2.0; dz += deltaZ) {
			int cnt = 0; // Number of boxes sampled at each row.
			boolean flag = false;
			for (int i = 0; i < 12; i++) {
				double dx = -((double) numberOfLanes / 2.0) * ((ASPHALT_TEXTURE_WIDTH - 2.0) / numberOfLanes) + BOX_LENGTH / 2.0
						+ i * BOX_LENGTH;
				if (Math.random() < difficulty) {
					boxesLocations.add(new Point(dx, BOX_LENGTH / 2.0, -dz));
					cnt += 1;
				} else if (!flag) {// The first time we don't sample a box then we also don't sample the box next to. We want enough space for the car to pass through. 
					i += 1;
					flag = true;
				}
				if (cnt > difficulty * 10) {
					break;
				}
			}
		}
	}

	public TrackSegment(double difficulty) {
		box = new SkewedBox(BOX_LENGTH, true);
		board = new SkewedBox(14.0, 5.0, 5.0, 2.0, 2.0, true);
		setDifficulty(difficulty);
	}

	@Override
	public void render(GL2 gl) {
		//Render the track segment
		renderBoxes(gl);
		renderAsphalt(gl);
		renderGrass(gl);
//		renderCuttingGrass(gl);
//		renderFans(gl);
		renderCrashedCar(gl);
		renderBillBoard(gl);
	}

	private void renderBoxes(GL2 gl) {
		Materials.setWoodenBoxMaterial(gl);
		for (Point p : boxesLocations) {
			gl.glPushMatrix();
			gl.glTranslated(p.x, 0.0, p.z);
			box.render(gl);
			gl.glPopMatrix();
		}
	}

	private void renderAsphalt(GL2 gl) {
		Materials.setAsphaltMaterial(gl);
		gl.glPushMatrix();
		renderQuadraticTexture(gl, texRoad, ASPHALT_TEXTURE_WIDTH, ASPHALT_TEXTURE_DEPTH, 20, TRACK_LENGTH);
		gl.glPopMatrix();
	}

	private void renderGrass(GL2 gl) {
		Materials.setGreenMaterial(gl);
		double dx = ASPHALT_TEXTURE_WIDTH / 2.0 + GRASS_TEXTURE_WIDTH / 2.0;
		gl.glPushMatrix();
		gl.glTranslated(dx, 0.0, 0.0);
		renderQuadraticTexture(gl, texGrass, GRASS_TEXTURE_WIDTH, GRASS_TEXTURE_WIDTH, 2, TRACK_LENGTH);
		gl.glTranslated(-2.0 * dx, 0.0, 0.0);
		renderQuadraticTexture(gl, texGrass, GRASS_TEXTURE_WIDTH, GRASS_TEXTURE_WIDTH, 2, TRACK_LENGTH);
		gl.glPopMatrix();
	}

	//a small robot car that maintains the track by cutting the grass
	private void renderCuttingGrass(GL2 gl) {
		Materials.setGreenMaterial(gl);
		double dx = ASPHALT_TEXTURE_WIDTH / 2.0 + GRASS_TEXTURE_WIDTH / 2.0;
		gl.glPushMatrix();
		gl.glTranslated(dx, 0.1, 0.0);
		renderQuadraticTexture(gl, texCuttingGrass, CUTTING_GRASS_TEXTURE_WIDTH,
				CUTTING_GRASS_TEXTURE_DEPTH, 2,
				CUTTING_GRASS_TEXTURE_LENGTH);
		gl.glTranslated(-2.0 * dx, 0.1, TRACK_LENGTH/2.0);
		renderQuadraticTexture(gl, texCuttingGrass, CUTTING_GRASS_TEXTURE_WIDTH, CUTTING_GRASS_TEXTURE_DEPTH, 2, CUTTING_GRASS_TEXTURE_LENGTH);
		gl.glPopMatrix();
	}

	private void renderCrashedCar(GL2 gl) {
		Materials.setRegular(gl);
		double dx = ASPHALT_TEXTURE_WIDTH / 2.0 + GRASS_TEXTURE_WIDTH/2;
		gl.glPushMatrix();
		gl.glTranslated(dx, 0.1, 0.0);
		renderQuadraticTexture(gl, texCrashedCar, CRASHED_CAR_TEXTURE_WIDTH,
				CRASHED_CAR_TEXTURE_DEPTH, 2, CRASHED_CAR_TEXTURE_WIDTH);
		gl.glTranslated(-2.0 * dx, 0.1, TRACK_LENGTH/2.0);
		renderQuadraticTexture(gl, texCrashedCar, CRASHED_CAR_TEXTURE_WIDTH,
				CRASHED_CAR_TEXTURE_WIDTH, 2, CRASHED_CAR_TEXTURE_WIDTH);
		gl.glPopMatrix();
	}

	private void renderBillBoard (GL2 gl) {
		Materials.SetBlackMetalMaterial(gl);
		double dx = ASPHALT_TEXTURE_WIDTH / 2.0 + GRASS_TEXTURE_WIDTH/2;
		SkewedBox base = new SkewedBox(2.0, 10.0, 10.0, 2.0, 2.0);
		//render board
		gl.glPushMatrix();
		gl.glTranslated(dx, 0.1, -50.0);
		base.render(gl);
		gl.glTranslated(0, 10.0, 0.0);
		Materials.setSignMaterial(gl);
		board.render(gl);
		gl.glPopMatrix();
	}

	private void renderQuadraticTexture(GL2 gl, Texture tex, double quadWidth, double quadDepth, int split,
			double totalDepth) {
		gl.glEnable(GL2.GL_TEXTURE_2D);
		tex.bind(gl);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_LOD, 1);

		gl.glColor3d(1.0, 0.0, 0.0);
		GLU glu = new GLU();
		GLUquadric quad = glu.gluNewQuadric();
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glNormal3d(.0, 1.0, 0.0);
		double d = 1.0 / ((double) split);
		double dz = (double) quadDepth / (double) (split);
		double dx = (double) quadWidth / (double) (split);
		for (double tz = 0.0; tz < totalDepth; tz += quadDepth) {
			for (double i = 0.0; i < (double) split; i += 1.0) {
				gl.glBegin(GL2.GL_TRIANGLE_STRIP);
				for (double j = 0.0; j <= (double) split; j += 1.0) {
					// (b)
					gl.glTexCoord2d(j * d, (i + 1.0) * d);
					gl.glVertex3d(-quadWidth / 2.0 + j * dx, 0.0, -tz - (i + 1) * dz);
					// (a)
					gl.glTexCoord2d(j * d, i * d);
					gl.glVertex3d(-quadWidth / 2.0 + j * dx, 0.0, -tz - i * dz);
				}
				gl.glEnd();
			}
		}
		glu.gluDeleteQuadric(quad);
		gl.glDisable(GL2.GL_TEXTURE_2D);
	}

	@Override
	public void init(GL2 gl) {
		//Initialize textures.
		box.init(gl);
		board.init2(gl);
		try {
			texRoad = TextureIO.newTexture(new File("Textures/RoadTexture.jpg"), true);
			texGrass = TextureIO.newTexture(new File("Textures/GrassTexture.jpg"), true);
			texCuttingGrass = TextureIO.newTexture(new File("Textures/CuttingGrass.jpg"), true);
			texCrashedCar = TextureIO.newTexture(new File("Textures/CrashedCar.jpg"), true);
		} catch (Exception e) {
			System.err.print("Unable to read texture : " + e.getMessage());
		}
	}

	public void destroy(GL2 gl) {
		// destroy textures.
		texRoad.destroy(gl);
		texGrass.destroy(gl);
		texCuttingGrass.destroy(gl);
		box.destroy(gl);
		texBillBoadSign.destroy(gl);
		texCrashedCar.destroy(gl);
		System.out.println("in destroy - this message is for debugging purposes");
	}

	public List<BoundingSphere> getBoundingSpheresList() {
		// Return bounding spheres of the wooden boxes.
		List<BoundingSphere> res = new LinkedList<BoundingSphere>();
		for (Point p : boxesLocations) {
			res.add(new BoundingSphere(BOX_LENGTH/2.0,new Point(p.x,BOX_LENGTH/2.0,p.z)));
		}
		return res;
	}

	@Override
	public BoundingSphere getBoundingSpheres(){ return null; }	//dummy method

}
