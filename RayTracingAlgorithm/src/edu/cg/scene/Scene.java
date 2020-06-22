package edu.cg.scene;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.cg.Logger;
import edu.cg.algebra.*;
import edu.cg.scene.camera.PinholeCamera;
import edu.cg.scene.lightSources.Light;
import edu.cg.scene.objects.Surface;

public class Scene {
	private String name = "scene";
	private int maxRecursionLevel = 1;
	private int antiAliasingFactor = 1; // gets the values of 1, 2 and 3
	private boolean renderRefarctions = false;
	private boolean renderReflections = false;

	private PinholeCamera camera;
	private Vec ambient = new Vec(1, 1, 1); // white
	private Vec backgroundColor = new Vec(0, 0.5, 1); // blue sky
	private List<Light> lightSources = new LinkedList<>();
	private List<Surface> surfaces = new LinkedList<>();

	//my fields
	private transient int imgWidth;
	private transient int imgHeight;

	// MARK: initializers
	public Scene initCamera(Point eyePoistion, Vec towardsVec, Vec upVec, double distanceToPlain) {
		this.camera = new PinholeCamera(eyePoistion, towardsVec, upVec, distanceToPlain);
		return this;
	}

	public Scene initCamera(Point eyePoistion, Vec towardsVec, Vec upVec, double distanceToPlain,
							double viewAngle) {
		this.camera = new PinholeCamera(eyePoistion, towardsVec, upVec, distanceToPlain, viewAngle);
		return this;
	}

	public Scene initAmbient(Vec ambient) {
		this.ambient = ambient;
		return this;
	}

	public Scene initBackgroundColor(Vec backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public Scene addLightSource(Light lightSource) {
		lightSources.add(lightSource);
		return this;
	}

	public Scene addSurface(Surface surface) {
		surfaces.add(surface);
		return this;
	}

	public Scene initMaxRecursionLevel(int maxRecursionLevel) {
		this.maxRecursionLevel = maxRecursionLevel;
		return this;
	}

	public Scene initAntiAliasingFactor(int antiAliasingFactor) {
		this.antiAliasingFactor = antiAliasingFactor;
		return this;
	}

	public Scene initName(String name) {
		this.name = name;
		return this;
	}

	public Scene initRenderRefarctions(boolean renderRefarctions) {
		this.renderRefarctions = renderRefarctions;
		return this;
	}

	public Scene initRenderReflections(boolean renderReflections) {
		this.renderReflections = renderReflections;
		return this;
	}

	// MARK: getters
	public String getName() {
		return name;
	}

	public int getFactor() {
		return antiAliasingFactor;
	}

	public int getMaxRecursionLevel() {
		return maxRecursionLevel;
	}

	public boolean getRenderRefarctions() {
		return renderRefarctions;
	}

	public boolean getRenderReflections() {
		return renderReflections;
	}

	public String toString() {
		String endl = System.lineSeparator();
		return "Camera: " + camera + endl + "Ambient: " + ambient + endl + "Background Color: " + backgroundColor + endl
				+ "Max recursion level: " + maxRecursionLevel + endl + "Anti aliasing factor: " + antiAliasingFactor
				+ endl + "Light sources:" + endl + lightSources + endl + "Surfaces:" + endl + surfaces;
	}

	private transient ExecutorService executor = null;
	private transient Logger logger = null;

	private void initSomeFields(int imgWidth, int imgHeight, Logger logger) {
		this.logger = logger;
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
	}

	public BufferedImage render(int imgWidth, int imgHeight, double viewAngle, Logger logger)
			throws InterruptedException, ExecutionException, IllegalArgumentException {

		initSomeFields(imgWidth, imgHeight, logger);

		BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		camera.initResolution(imgHeight, imgWidth, viewAngle);
		int nThreads = Runtime.getRuntime().availableProcessors();
		nThreads = nThreads < 2 ? 2 : nThreads;
		//int nThreads = 1; 	//debug
		this.logger.log("Intitialize executor. Using " + nThreads + " threads to render " + name);
		executor = Executors.newFixedThreadPool(nThreads);

		@SuppressWarnings("unchecked")
		Future<Color>[][] futures = (Future<Color>[][]) (new Future[imgHeight][imgWidth]);

		this.logger.log("Starting to shoot " + (imgHeight * imgWidth * antiAliasingFactor * antiAliasingFactor)
				+ " rays over " + name);

		for (int y = 0; y < imgHeight; ++y)
			for (int x = 0; x < imgWidth; ++x)
				futures[y][x] = calcColor(x, y);

		this.logger.log("Done shooting rays.");
		this.logger.log("Wating for results...");

		for (int y = 0; y < imgHeight; ++y)
			for (int x = 0; x < imgWidth; ++x) {
				Color color = futures[y][x].get();
				img.setRGB(x, y, color.getRGB());
			}

		executor.shutdown();

		this.logger.log("Ray tracing of " + name + " has been completed.");

		executor = null;
		this.logger = null;

		return img;
	}

	private Future<Color> calcColor(int x, int y) {
		return executor.submit(() -> {
			//super sampling!
			Point centerPoint = camera.transform(x, y);
			Vec color = new Vec(); Ray ray;
			if(antiAliasingFactor == 1) {
				ray = new Ray(camera.getCameraPosition(), centerPoint);
				color = calcColor(ray, 0);
				return color.toColor();
			}

			//ration of pixel size / antiAliasingFactor
			double ration = camera.getRation() / (double) antiAliasingFactor;
			//top and right point in the pixel
			Point rightUpPoint = new Point(centerPoint.x + ration, centerPoint.y + ration,
					centerPoint.z);

			for (int i = 0; i < antiAliasingFactor; i++) {
				for (int j = 0; j < antiAliasingFactor; j++) {
					//point is the ration this sample needs to move
					Point point = new Point((double) i * ration , (double) -j * ration, 0);
					Point newP = Ops.add(rightUpPoint, point);	//the final point in the pixel

					ray = new Ray(camera.getCameraPosition(), newP);
					color = color.add(calcColor(ray, 0));
				}
			}
			//remember to divide by the samplingSize, if not image will be very bright
			double samplingSize = (double) antiAliasingFactor * antiAliasingFactor;
			return color.mult(1 / samplingSize).toColor();
		});
	}

	private Vec calcColor(Ray ray, int recusionLevel) {
		// This is the recursive method in RayTracing.

		//base case of recursion, return a new empty Vec representing color
		if(recusionLevel >= this.maxRecursionLevel) return new Vec();

		//// **  **  find closest intersection **  **  ////
		Hit hitPoint = findClosestIntersection(ray);
		if (hitPoint == null) return this.backgroundColor;

		////  **  **  finding the color recursively  **  **  ////
        Surface surface = hitPoint.getSurface();
        Vec normalToSurface = hitPoint.getNormalToSurface();
        Point hittingPoint = ray.getHittingPoint(hitPoint);

        Vec color = getPhongColor(surface, normalToSurface, hittingPoint, ray);

		//reflections
		if(renderReflections){
			//find the ray reflecting from the current hitting point
			Vec reflectedVec = Ops.reflect(ray.direction(), normalToSurface);
			Ray reflectedRay = new Ray(hittingPoint, reflectedVec);

			//calculate the color of this new ray recursively, with the reflectionIntensity weight
			color = color.add(calcColor(reflectedRay, recusionLevel+1)
					.mult(surface.reflectionIntensity()));
		}

		//refractions
			if(renderRefarctions){
			if(surface.isTransparent()){
				//find the ray refracting from the current hitting point
				Vec refractedVec = Ops.refract(ray.direction(), normalToSurface,
						surface.n1(hitPoint), surface.n2(hitPoint));
				Ray refractedRay = new Ray(hittingPoint, refractedVec);

				//calculate the color of this new ray recursively, with the refractionIntensity weight
				color = color.add(calcColor(refractedRay, recusionLevel+1)
						.mult(surface.refractionIntensity()));
			}
		}
		return color;
	}

	/**
	 * this method returns the closest intersection out of the scene surfaces
	 * @param ray - a ray from some point (either camera or object)
	 * @return Hit - a hit object if an intersection was found or null if no intersection
	 */
	private Hit findClosestIntersection(Ray ray){
		//loop on all objects in scene
		double t1 = Double.MAX_VALUE;
		Hit closestHit = null;
		for (int i = 0; i < surfaces.size(); i++) {
			Hit hit = surfaces.get(i).intersect(ray);
			if(hit != null){
				double t2 = hit.t();
				if(t1 > t2 && t2 > 0){
					t1 = t2;
					closestHit = hit;
				}
			}
		}
		return closestHit;
	}

    /**
     * this function calculates the Phong method colors of the object hit, taking into
     * consideration ambient, diffused and specular lighting
     * @param surface - the surface hit by the ray from camera
     * @param normalToSurface - normal to the surface hit, for diffused and specular calculations
     * @param hittingPoint - Point the ray hit on the surface
     * @param rayFromCamera - the ray from camera
     * @return a vector representing the color of a pixel
     */
	private Vec getPhongColor(Surface surface, Vec normalToSurface, Point hittingPoint, Ray rayFromCamera){

		Vec color = this.ambient.mult(surface.Ka());	// ambient calculation

        // diffused and specular calculations
        for (int i = 0; i < lightSources.size(); i++) {
            Light light = lightSources.get(i);
            Ray rayToLight = light.rayToLight(hittingPoint);

            //check if light is shadowed by one of the surfaces
            if(!isShadowed(light, rayToLight)){
                Vec lightIntensity = light.intensity(hittingPoint, rayToLight);

                //diffuse calculation
                color = color.add(lightIntensity.mult(surface.Kd())
                        .mult(Math.max(rayToLight.direction().dot(normalToSurface), 0)));
                //very annoying bug!, cos(theta) can be less then 0, so added color should be 0

                //specular calculation
                Vec lHat = Ops.reflect(rayToLight.direction().neg(), normalToSurface);
                Vec vecToViwer = rayFromCamera.direction().neg();
                double cos_a = lHat.dot(vecToViwer);
                color = color.add( (cos_a <= 0)? new Vec(0):lightIntensity.mult(surface.Ks())
						.mult(Math.pow(cos_a, surface.shininess())));
            }
        }
        return color;
    }

    /**
     * this function checks if one of the surfaces in the scene blocks the light to the hit
     * @param light
     * @param rayToLight - a ray from the hitting point to the the light source
     * @return
     */
    private Boolean isShadowed(Light light, Ray rayToLight){
        for (int i = 0; i < surfaces.size(); i++) {
            if(light.isOccludedBy(surfaces.get(i), rayToLight)) return true;
        }
        return false;
    }
}
