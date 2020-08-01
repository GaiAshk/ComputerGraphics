package edu.cg;

import java.awt.Component;
import java.util.List;

import javax.swing.JOptionPane;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import edu.cg.algebra.Point;
import edu.cg.algebra.Vec;
import edu.cg.models.BoundingSphere;
import edu.cg.models.Car.Specification;
import edu.cg.models.Track;
import edu.cg.models.TrackSegment;
import edu.cg.models.Car.F1Car;

/**
 * An OpenGL 3D Game.
 */
public class NeedForSpeed implements GLEventListener {
	private GameState gameState = null; // Tracks the car movement and orientation
	private F1Car car = null; // The F1 car we want to render
	private Vec carCameraTranslation = null; // The accumulated translation that should be applied on the car, camera
												// and light sources
	private Track gameTrack = null; // The game track we want to render
	private FPSAnimator ani; // This object is responsible to redraw the model with a constant FPS
	private Component glPanel; // The canvas we draw on.
	private boolean isModelInitialized = false; // Whether model.init() was called.
	private boolean isDayMode = true; // Indicates whether the lighting mode is day/night.
	private boolean isBirdseyeView = false; // Indicates whether the camera is looking from above on the scene or
											// looking towards the car direction.
	private Point carInitialPosition = new Point(0.0, 0.15, -7.0);	//Car initial position
	private Point cameraInitialPositionThirdPerson = new Point(0.0, 1.8, 0.0);	//Camera initial position
	private Point cameraInitialPositionBirdseye = new Point(0.0, 50.0, 0.0);	//Different camera settings
	private float[] carLight = {1.0f, 1.0f, 1.0f, 1.0f};	//Light colors
	//Lights positions Right spot light and left spot light
	private float[] rightLight = {(float) (Specification.F_BUMPER_DEPTH * Specification.bumperBoxDepthfactor / 2.0
			+ Specification.F_BUMPER_WINGS_DEPTH / 2.0), 0.1f, (float) (-Specification.F_LENGTH / 2.0 -
			Specification.C_BASE_LENGTH / 2.0 - Specification.F_HOOD_LENGTH / 2.0 - Specification.TIRE_RADIUS), 1.0f};
	private float[] leftLight = {(float) (-Specification.F_BUMPER_DEPTH * Specification.bumperBoxDepthfactor / 2.0
			- Specification.F_BUMPER_WINGS_DEPTH/ 2.0), 0.1f, (float) (-Specification.F_LENGTH / 2.0 -
			Specification.C_BASE_LENGTH / 2.0 - Specification.F_HOOD_LENGTH / 2.0 - Specification.TIRE_RADIUS), 1.0f};
	private double scaleFactor = 4.0;	//scale factor used to scale the car
	
	public NeedForSpeed(Component glPanel) {
		this.glPanel = glPanel;
		gameState = new GameState();
		gameTrack = new Track();
		carCameraTranslation = new Vec(0.0);
		car = new F1Car();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		if (!isModelInitialized) {
			initModel(gl);
		}
		if (isDayMode) {
			gl.glClearColor(0.52f, 0.824f, 1.0f, 1.0f);
		} else {
			gl.glClearColor(0.0f, 0.0f, 0.32f, 1.0f);
		}
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		// Step (1) Update the accumulated translation that needs to be applied on the car, camera and light sources.
		updateCarCameraTranslation(gl);
		// Step (2) Position the camera and setup its orientation
		setupCamera();
		// Step (3) setup the lights.
		setupLights(gl);
		// Step (4) render the car.
		renderCar(gl);
		// Step (5) render the track.
		renderTrack(gl);
		// Step (6) check collision. Note this has nothing to do with OpenGL.
		if (checkCollision()) {
			JOptionPane.showMessageDialog(this.glPanel, "Game is Over! Loser!");
			//is it possible to win in this game? yes stay in place forever, never lose
			this.gameState.resetGameState();
			this.carCameraTranslation = new Vec(0.0);
		}
	}

	/**
	 * @return Checks if the car intersects the one of the boxes on the track.
	 */
	private boolean checkCollision() {
		List<BoundingSphere> trackBoundingSpheres = gameTrack.getBoundingSpheresList();
		BoundingSphere carBoundingSphere = car.getBoundingSpheres();	//F1Car bounding sphere

		carBoundingSphere.rotateToGameMode();	//Bounding sphere rotation
		carBoundingSphere.scaleToGameMode(this.scaleFactor); //Bounding sphere scaling
		//Translate bounding sphere to current position in the frame
		carBoundingSphere.translateCenterToChildrenAndMe(carInitialPosition);
		carBoundingSphere.translateCenterToChildrenAndMe(carCameraTranslation.x,
				carCameraTranslation.y, carCameraTranslation.z);

		//check if the carBoundingSphere intersects with one of the boxes
		for (BoundingSphere boxSphere : trackBoundingSpheres){
			//check intersection with cars outer sphere and boxes
			if(carBoundingSphere.checkIntersection(boxSphere)){
				//check collision with inner spheres
				for(BoundingSphere innerSphere : carBoundingSphere.children){
					if(innerSphere.checkIntersection(boxSphere))return true;
				}
			}
		}
		return false;
	}

	private void updateCarCameraTranslation(GL2 gl) {
		// Update the car and camera translation values (not the ModelView-Matrix).
		// - Always keep track of the car offset relative to the starting
		// point.
		// - Change the track segments here.
		Vec ret = gameState.getNextTranslation();
		carCameraTranslation = carCameraTranslation.add(ret);
		double dx = Math.max(carCameraTranslation.x, -TrackSegment.ASPHALT_TEXTURE_DEPTH / 2.0 - 2);
		carCameraTranslation.x = (float) Math.min(dx, TrackSegment.ASPHALT_TEXTURE_DEPTH / 2.0 + 2);
		if (Math.abs(carCameraTranslation.z) >= TrackSegment.TRACK_LENGTH + 10.0) {
			carCameraTranslation.z = -(float) (Math.abs(carCameraTranslation.z) % TrackSegment.TRACK_LENGTH);
			gameTrack.changeTrack(gl);
		}
	}

	private void setupCamera() {
		GLU glu = new GLU();
		if (isBirdseyeView) {
			glu.gluLookAt(this.cameraInitialPositionBirdseye.x + (double)(this.carCameraTranslation.x),
					this.cameraInitialPositionBirdseye.y + (double)(this.carCameraTranslation.y),
					this.cameraInitialPositionBirdseye.z + (double)(this.carCameraTranslation.z),
					0.0 + (double)(this.carCameraTranslation.x),
					1.5 + (double)(this.carCameraTranslation.y),
					-30.0 + (double)(this.carCameraTranslation.z), 0.0, 0, -1);
		} else {
			glu.gluLookAt(cameraInitialPositionThirdPerson.x + (double)(this.carCameraTranslation.x),
					cameraInitialPositionThirdPerson.y + (double)(this.carCameraTranslation.y),
					cameraInitialPositionThirdPerson.z + (double)(this.carCameraTranslation.z),
					0.0 + (double)(this.carCameraTranslation.x),
					1.5 + (double)(this.carCameraTranslation.y),
					-5.0 + (double)(this.carCameraTranslation.z), 0.0, 0.7, -0.3);
		}
	}

	private void setupLights(GL2 gl) {
		if (isDayMode) {
			//switch-off night lighting
			gl.glDisable(GLLightingFunc.GL_LIGHT0);
			gl.glDisable(GLLightingFunc.GL_LIGHT1);
			setupDayMode(gl, GLLightingFunc.GL_LIGHT0);
		} else {
			//switch-off day light
			gl.glDisable(GLLightingFunc.GL_LIGHT0);
			// in night mode spotLight of the car are one, in the exact position of the light on the car
			setupNightMode(gl);
		}
	}

	private void setupDayMode(GL2 gl, int light) {
		// Uses one directional light source:
		float[] sunIntensity = {1.0f, 1.0f, 1.0f, 1.0f};
		//Giving a small x value to position for some side shading
		float[] position = {0.01f, 1.0f, 1.0f, 0.0f};
		gl.glLightfv(light, GLLightingFunc.GL_AMBIENT, new float[]{0.5f, 0.5f, 0.5f, 1.0f}, 0);
		gl.glLightfv(light, GLLightingFunc.GL_SPECULAR, sunIntensity, 0);
		gl.glLightfv(light, GLLightingFunc.GL_DIFFUSE, sunIntensity, 0);
		gl.glLightfv(light, GLLightingFunc.GL_POSITION, position, 0);
		gl.glEnable(light);
	}

	private void setupNightMode(GL2 gl) {
		// Moon light
		gl.glLightModelfv(GL2ES1.GL_LIGHT_MODEL_AMBIENT, new float[]{0.7f, 0.7f, 0.7f, 1.0f}, 0);
		// SpotLight from cars lights, in exact position of lights on the car
		gl.glPushMatrix();
		gl.glTranslated(carInitialPosition.x + (double)(this.carCameraTranslation.x),
				carInitialPosition.y + (double)(this.carCameraTranslation.y),
				carInitialPosition.z + (double)(this.carCameraTranslation.z));
		gl.glRotated(-gameState.getCarRotation(), 0.0, 1.0, 0.0);
		gl.glScaled(scaleFactor, scaleFactor, scaleFactor);
		setupCarLight(gl, GLLightingFunc.GL_LIGHT0, rightLight);
		setupCarLight(gl, GLLightingFunc.GL_LIGHT1, leftLight);
		gl.glPopMatrix();
	}

	private void setupCarLight(GL2 gl, int light, float[] position) {

		gl.glLightfv(light, GLLightingFunc.GL_DIFFUSE, carLight, 0);
		gl.glLightfv(light, GLLightingFunc.GL_SPECULAR, carLight, 0);
		gl.glLightfv(light, GLLightingFunc.GL_POSITION, position, 0);
		//light direction is in the towards direction -z
		gl.glLightfv(light, GLLightingFunc.GL_SPOT_DIRECTION, new float[]{0.0f, 0.0f, -1.0f}, 0);
		gl.glLightf(light, GLLightingFunc.GL_SPOT_CUTOFF, 70.0f);
		gl.glEnable(light);
	}

	private void renderTrack(GL2 gl) {
		// * Note: the track is not translated. It should be fixed.
		gl.glPushMatrix();
		gameTrack.render(gl);
		gl.glPopMatrix();
	}

	private void renderCar(GL2 gl) {
		gl.glPushMatrix();
		gl.glTranslated(carInitialPosition.x + (double)(this.carCameraTranslation.x),
				carInitialPosition.y + (double)(this.carCameraTranslation.y),
				carInitialPosition.z + (double)(this.carCameraTranslation.z));
		gl.glRotated(-gameState.getCarRotation(), 0.0, 1.0, 0.0);
		gl.glRotated(90.0, 0.0, 1.0, 0.0);
		gl.glScaled(scaleFactor, scaleFactor, scaleFactor);
		car.render(gl);
		gl.glPopMatrix();
	}

	public GameState getGameState() { return gameState; }

	@Override
	public void dispose(GLAutoDrawable drawable) { }

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		// Initialize display callback timer
		ani = new FPSAnimator(30, true);
		ani.add(drawable);
		glPanel.repaint();
		initModel(gl);
		ani.start();
	}

	public void initModel(GL2 gl) {
		gl.glCullFace(GL2.GL_BACK);
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glEnable(GL2.GL_NORMALIZE);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_SMOOTH);
		car.init(gl);
		gameTrack.init(gl);
		isModelInitialized = true;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(gl.GL_PROJECTION);	// Changes to Projection matrix
		gl.glLoadIdentity();				//reset matrix to Identity
		GLU glu = new GLU();
		glu.gluPerspective(60.0, width / (double)(height), 2.0, 500.0);
	}

	/**
	 * Start redrawing the scene with 30 FPS
	 */
	public void startAnimation() {
		if (!ani.isAnimating()) ani.start();
	}

	/**
	 * Stop redrawing the scene with 30 FPS
	 */
	public void stopAnimation() {
		if (ani.isAnimating()) ani.stop();
	}

	public void toggleNightMode() { isDayMode = !isDayMode; }

	public void changeViewMode() { isBirdseyeView = !isBirdseyeView; }
}
