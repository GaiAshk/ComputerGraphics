package edu.cg.scene.camera;

import edu.cg.algebra.Point;
import edu.cg.algebra.Vec;

public class PinholeCamera {
	private Point cameraPosition;
	private Point centerPoint;	//the Point coordinates of the center pixel in the image
	private Vec towardsVec;
	private Vec upVec;
	private Vec rightVec;
	private double distanceToPlain;
	private double resY;
	private double resX;
	private double viewAngle;
	private double ratio;	//the size of a pixel

	/**
	 * Initializes a pinhole camera model with default resolution 200X200 (RxXRy)
	 * and View Angle 90.
	 * 
	 * @param cameraPosition  - The position of the camera.
	 * @param towardsVec      - The towards vector of the camera (not necessarily
	 *                        normalized).
	 * @param upVec           - The up vector of the camera.
	 * @param distanceToPlain - The distance of the camera (position) to the center
	 *                        point of the image-plain.
	 * 
	 */
	public PinholeCamera(Point cameraPosition, Vec towardsVec, Vec upVec, double distanceToPlain) {
		this.cameraPosition = cameraPosition;
		this.towardsVec = towardsVec.normalize();
		this.distanceToPlain = distanceToPlain;

		//find rightVec and upVec
		this.rightVec = towardsVec.cross(upVec).normalize();
		this.upVec = rightVec.cross(towardsVec).normalize();

		//find center point
		this.centerPoint = cameraPosition.add(towardsVec.mult(distanceToPlain));

		// init default values
		this.resY = 200;
		this.resX = 200;
		this.viewAngle = 90;

		//find width of image and ration of square pixel
		double imgWidth = Math.tan(Math.toRadians(viewAngle/2.0))*distanceToPlain*2.0;

		this.ratio = imgWidth / resX;
	}

	public PinholeCamera(Point cameraPosition, Vec towardsVec, Vec upVec, double distanceToPlain,
						 double viewAngle) {
		this(cameraPosition, towardsVec, upVec, distanceToPlain);
		this.viewAngle = viewAngle;
	}

	/**
	 * Initializes the resolution and width of the image.
	 * 
	 * @param height    - the number of pixels in the y direction.
	 * @param width     - the number of pixels in the x direction.
	 * @param viewAngle - the view Angle.
	 */
	public void initResolution(int height, int width, double viewAngle) {
		this.resY = (double) height;
		this.resX = (double) width;
		this.viewAngle = viewAngle;

		//find width of image and ration of square pixel
		double imgWidth = Math.tan(Math.toRadians(viewAngle/2.0))*distanceToPlain*2.0;
		this.ratio = imgWidth / resX;
	}


	/**
	 * Transforms from pixel coordinates to the center point of the corresponding
	 * pixel in model coordinates.
	 * 
	 * @param x - the pixel index in the x direction.
	 * @param y - the pixel index in the y direction.
	 * @return the middle point of the pixel (x,y) in the model coordinates.
	 */
	public Point transform(int x, int y) {
		Vec right = rightVec.mult((x - Math.floor(resX/2)) * this.ratio);
		Vec up = upVec.mult((y - Math.floor(resY/2)) * this.ratio * -1);
		return centerPoint.add(right).add(up);
	}

	/**
	 * Returns the camera position
	 * 
	 * @return a new point representing the camera position.
	 */
	public Point getCameraPosition() {
		return new Point (this.cameraPosition.x, this.cameraPosition.y, this.cameraPosition.z);
	}

	//for debugging
	public double getResY(){return this.resY;}
	public double getResX(){return this.resX;}
	public double getViewAngle(){return this.viewAngle;}
	public double getRation(){return this.ratio;}
}
