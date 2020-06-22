package edu.cg;

import edu.cg.algebra.Point;
import edu.cg.algebra.Vec;
import edu.cg.scene.Scene;
import edu.cg.scene.lightSources.CutoffSpotlight;
import edu.cg.scene.lightSources.DirectionalLight;
import edu.cg.scene.lightSources.Light;
import edu.cg.scene.lightSources.PointLight;
import edu.cg.scene.objects.Dome;
import edu.cg.scene.objects.Material;
import edu.cg.scene.objects.Plain;
import edu.cg.scene.objects.Shape;
import edu.cg.scene.objects.Sphere;
import edu.cg.scene.objects.Surface;

public class Scenes {
	//single red ball
	public static Scene scene1() {
		Shape sphereShape1 = new Sphere(new Point(0.0), 1.0);
		Material sphereMat1 = new Material().initKa(new Vec(0.8, 0.05, 0.05)).initKd(new Vec(0.0)).initKs(new Vec(0.9))
				.initShininess(10).initIsTransparent(false).initRefractionIntensity(0.0);
		Surface boxSurface1 = new Surface(sphereShape1, sphereMat1);

		Light dirLight = new DirectionalLight(new Vec(-1.0, -1.0, -1.0), new Vec(0.9));

		return new Scene().initAmbient(new Vec(1.0))
				.initCamera(new Point(4, 4, 1.5), new Vec(-1.0, -1.0, -0.3), new Vec(0, 0, 1), 3)
				.addLightSource(dirLight).addSurface(boxSurface1).initName("scene1").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(3);
	}

	//pool table
	public static Scene scene2() {
		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene2").initAntiAliasingFactor(1)
				.initAmbient(new Vec(0.4))
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
		// Add Surfaces to the scene.
		// (1) A plain that represents the ground floor.
		Shape plainShape = new Plain(new Vec(0.0,1.0,0.0), new Point(0.0, -1.0, 0.0));
		Material plainMat = Material.getMetalMaterial();
		Surface plainSurface = new Surface(plainShape, plainMat);
		finalScene.addSurface(plainSurface);

		// (2) We will also add spheres to form a triangle shape (similar to a pool game).
		for (int depth = 0; depth < 4; depth++) {
			for(int width=-1*depth; width<=depth; width++) {
				Shape sphereShape = new Sphere(new Point((double)width, 0.0, -1.0*(double)depth), 0.5);
				Material sphereMat = Material.getRandomMaterial();
				Surface sphereSurface = new Surface(sphereShape, sphereMat);
				finalScene.addSurface(sphereSurface);
			}

		}
		// Add lighting condition:
		DirectionalLight directionalLight=new DirectionalLight(new Vec(0.5,-0.5,0.0),new Vec(0.7));
		finalScene.addLightSource(directionalLight);


		return finalScene;
	}

	//pool table with spotLight
	public static Scene scene3() {
		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene3").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
		// Add Surfaces to the scene.
		// (1) A plain that represents the ground floor.
		Shape plainShape = new Plain(new Vec(0.0,1.0,0.0), new Point(0.0, -1.0, 0.0));
		Material plainMat = Material.getMetalMaterial();
		Surface plainSurface = new Surface(plainShape, plainMat);
		finalScene.addSurface(plainSurface);

		// (2) We will also add spheres to form a triangle shape (similar to a pool game).
		for (int depth = 0; depth < 4; depth++) {
			for(int width=-1*depth; width<=depth; width++) {
				Shape sphereShape = new Sphere(new Point((double)width, 0.0, -1.0*(double)depth), 0.5);
				Material sphereMat = Material.getRandomMaterial();
				Surface sphereSurface = new Surface(sphereShape, sphereMat);
				finalScene.addSurface(sphereSurface);
			}

		}

		// Add light sources:
		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 45.0);
		cutoffSpotlight.initPosition(new Point(4.0, 4.0, -3.0));
		cutoffSpotlight.initIntensity(new Vec(1.0,0.6,0.6));
		finalScene.addLightSource(cutoffSpotlight);
		cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
		cutoffSpotlight.initPosition(new Point(-4.0, 4.0, -3.0));
		cutoffSpotlight.initIntensity(new Vec(0.6,1.0,0.6));
		finalScene.addLightSource(cutoffSpotlight);
		cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
		cutoffSpotlight.initPosition(new Point(0.0, 4.0, 0.0));
		cutoffSpotlight.initIntensity(new Vec(0.6,0.6,1.0));
		finalScene.addLightSource(cutoffSpotlight);
		DirectionalLight directionalLight=new DirectionalLight(new Vec(0.5,-0.5,0.0),new Vec(0.2));
		finalScene.addLightSource(directionalLight);

		return finalScene;
	}

	//two domes
	public static Scene scene4() {
		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene4").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
		// Add Surfaces to the scene.

		// (2) Add two domes to make it look like we split a sphere in half.
		Shape domeShape = new Dome(new Point(2.0, 0.0, -10.0), 5.0, new Vec(1.0, 0.0, 0.0));
		Material domeMat = Material.getRandomMaterial();
		Surface domeSurface = new Surface(domeShape, domeMat);
		finalScene.addSurface(domeSurface);

		domeShape = new Dome(new Point(-2.0, 0.0, -10.0), 5.0, new Vec(-1.0, 0.0, 0.0));
		domeSurface = new Surface(domeShape, domeMat);
		finalScene.addSurface(domeSurface);

		// Add light sources:
		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 75.0);
		cutoffSpotlight.initPosition(new Point(0.0, 6.0, -10.0));
		cutoffSpotlight.initIntensity(new Vec(.5,0.5,0.5));
		finalScene.addLightSource(cutoffSpotlight);

		return finalScene;
	}

	//point light test with reflection
	public static Scene scene5() {
		Shape plane = new Plain(new Vec(0.0,1.0,0.0), new Point(0.0, -1.0, 0.0));
		Material planeMat =
				new Material().initKa(new Vec(0.8, 0.05, 0.05)).initKd(new Vec(0.4)).initKs(new Vec(1))
				.initShininess(100).initIsTransparent(false).initRefractionIntensity(0.0);

		Surface boxSurface1 = new Surface(plane, planeMat);

		Light dirLight = new PointLight().initDecayFactors(0,0,1)
				.initIntensity(new Vec(1,1,1))
				.initPosition(new Point (2,2,2));


		return new Scene().initAmbient(new Vec(1.0))
				.initCamera(new Point(4, 4, 1.5), new Vec(-1.0, -1.0, -0.3), new Vec(0, 0, 1), 3)
				.addLightSource(dirLight).addSurface(boxSurface1).initName("scene5")
				.initRenderRefarctions(false).initRenderReflections(false).initMaxRecursionLevel(1);
	}

	//two domes with shadow test
	public static Scene scene6() {
		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene6").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
		// Add Surfaces to the scene.

		// (2) Add two domes to make it look like we split a sphere in half.
		Shape domeShape = new Dome(new Point(2.0, 0.0, -10.0), 5.0, new Vec(1.0, 0.0, 0.0));
		Material domeMat = Material.getRandomMaterial();
		Surface domeSurface = new Surface(domeShape, domeMat);
		finalScene.addSurface(domeSurface);

		domeShape = new Dome(new Point(-2.0, 0.0, -10.0), 5.0, new Vec(-1.0, 0.0, 0.0));
		domeSurface = new Surface(domeShape, domeMat);
		finalScene.addSurface(domeSurface);

		Shape sphere = new Sphere(new Point(0, 0, -10), 1.0);
		Material sphereMat = Material.getMetalMaterial();
		Surface metalSphere = new Surface(sphere, sphereMat);
		finalScene.addSurface(metalSphere);

		// Add light sources:
		PointLight light = new PointLight().initPosition(new Point(-8, 2, 0))
				.initIntensity(new Vec(1)).initDecayFactors(0,0,1);
		finalScene.addLightSource(light);

		return finalScene;
	}

	//Sphere test the pyramid
	public static Scene scene7() {
		int pyramidHeight = 6, sphereHeight=1,sphereWidth=1,sphereDepth=1;
		//init Scene
		Scene pyramidScence = new Scene().initName("Scene7").initAmbient(new Vec(0.73))
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(5);

		// Init camera position and setup
		Point cameraPosition = new Point(10,10, 4);
		Vec towardsVec = new Vec(-1.0, -1.0, -0.04);
		Vec upVec = new Vec(0.0, 0.0, 1.0);
		double distanceFromPlain = 2.0;
		pyramidScence.initCamera(cameraPosition,towardsVec, upVec, distanceFromPlain, 110);

		// Add some light sources to the Scene
		Light dirLight = new DirectionalLight(new Vec(-0.5, -0.5, -1.0), new Vec(0.5));
		pyramidScence.addLightSource(dirLight);

		double sphereRadius = 0.5;
		for (int currentHeight = 0; currentHeight < pyramidHeight;currentHeight++) {
			int numOfSpheres = pyramidHeight - currentHeight;
			int offsetX = currentHeight*sphereWidth;
			int offsetY = currentHeight*sphereDepth;
			for (int i = 0; i <numOfSpheres;i++) {
				for (int j = 0; j<numOfSpheres;j++) {
					Shape sphereShape = new Sphere(
							new Point(offsetX+i*sphereWidth, offsetY+j*sphereDepth, sphereHeight*currentHeight),
							sphereRadius);
					Material sphereMat = Material.getRandomMaterial();
					Surface sphereSurface = new Surface(sphereShape, sphereMat);
					pyramidScence.addSurface(sphereSurface);
				}
			}

		}

		Plain scenePlain = new Plain(new Vec(0.0, 0.0, 1.0), new Point(0.0, 0.0, 0.0));
		Material plainMat = Material.getGlassMaterial(false).initReflectionIntensity(0.4);
		Surface plainSurface = new Surface(scenePlain, plainMat);
		pyramidScence.addSurface(plainSurface);
		return pyramidScence;
	}

	//Spheres from above
	public static Scene scene8() {
		Shape sphereShape1 = new Sphere(new Point(0.5, 0.5, 0.5), 0.5);
		Material sphereMat1 = Material.getRandomMaterial();
		Surface sphereSurface1 = new Surface(sphereShape1, sphereMat1);

		Shape sphereShape2 = new Sphere(new Point(-0.5, 0.5, 0.5), 0.5);
		Material sphereMat2 = Material.getRandomMaterial();
		Surface sphereSurface2 = new Surface(sphereShape2, sphereMat2);

		Shape sphereShape3 = new Sphere(new Point(0.5, -0.5, 0.5), 0.5);
		Material sphereMat3 = Material.getRandomMaterial();
		Surface sphereSurface3 = new Surface(sphereShape3, sphereMat3);

		Shape sphereShape4 = new Sphere(new Point(-0.5, -0.5, 0.5), 0.5);
		Material sphereMat4 = Material.getRandomMaterial();
		Surface sphereSurface4 = new Surface(sphereShape4, sphereMat4);

		Light dirLight = new DirectionalLight(new Vec(0.0, 0.0, -1.0), new Vec(0.7));

		return new Scene().initAmbient(new Vec(1.0))
				.initCamera(new Point(0.0, 0.0, 2.0), new Vec(0.0, 0.0 , -1.0),
						new Vec(1.0, 1.0, 0.0), 1.0)
				.addLightSource(dirLight).addSurface(sphereSurface1).addSurface(sphereSurface2)
				.addSurface(sphereSurface3).addSurface(sphereSurface4).initName("scene8").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
	}

	//Dome tests
	public static Scene scene9() {
		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene8").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
		// Add Surfaces to the scene.
		// (1) A plain that represents the ground floor.
		Shape plainShape = new Plain(new Vec(0.0,1.0,0.0), new Point(0.0, -1.0, 0.0));
		Material plainMat = Material.getMetalMaterial();
		Surface plainSurface = new Surface(plainShape, plainMat);
		finalScene.addSurface(plainSurface);

		// (2) We will also add spheres to form a triangle shape (similar to a pool game).
		for (int depth = 0; depth < 4; depth++) {
			for(int width=-1*depth; width<=depth; width++) {
				Shape sphereShape = new Dome(new Point((double)width, 0.0, -1.0*(double)depth),
						0.5, new Vec(0, -1, 0));
				Material sphereMat = Material.getRandomMaterial();
				Surface sphereSurface = new Surface(sphereShape, sphereMat);
				finalScene.addSurface(sphereSurface);
			}

		}

		// Add light sources:
		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 45.0);
		cutoffSpotlight.initPosition(new Point(4.0, 4.0, -3.0));
		cutoffSpotlight.initIntensity(new Vec(1.0,0.6,0.6));
		finalScene.addLightSource(cutoffSpotlight);
		cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
		cutoffSpotlight.initPosition(new Point(-4.0, 4.0, -3.0));
		cutoffSpotlight.initIntensity(new Vec(0.6,1.0,0.6));
		finalScene.addLightSource(cutoffSpotlight);
		cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
		cutoffSpotlight.initPosition(new Point(0.0, 4.0, 0.0));
		cutoffSpotlight.initIntensity(new Vec(0.6,0.6,1.0));
		finalScene.addLightSource(cutoffSpotlight);
		DirectionalLight directionalLight=new DirectionalLight(new Vec(0.5,-0.5,0.0),new Vec(0.2));
		finalScene.addLightSource(directionalLight);

		return finalScene;
	}

	//reflecting test and refracting test
	public static Scene scene10() {
		// Define basic properties of the scene
		Scene finalScene = new Scene().initAmbient(new Vec(1.0))
				.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
						/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
						/* Up vector = */new Vec(0.0, 1.0, 0.0),
						/*Distance to plain =*/ 2.0)
				.initName("scene10").initAntiAliasingFactor(1)
				.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(6);
		// Add Surfaces to the scene.
		// (1) A plain that represents the ground floor.
		Shape plainShape = new Plain(new Vec(0.0,1.0,0.0), new Point(0.0, -1.0, 0.0));
		Material plainMat = Material.getMetalMaterial();
		Surface plainSurface = new Surface(plainShape, plainMat);
		finalScene.addSurface(plainSurface);

		// (2) We will also add spheres to form a triangle shape (similar to a pool game).
		for (int depth = 0; depth < 3; depth++) {
			Shape sphereShape = new Sphere(new Point(1, 0, -depth*5), 1);
			Material sphereMat;
			if(depth == 0){
				sphereMat = Material.getGlassMaterial(true);
			} else if (depth == 1){
				sphereMat = Material.getMetalMaterial();
			} else {
				sphereMat = Material.getRandomMaterial();
			}

			Surface sphereSurface = new Surface(sphereShape, sphereMat);
			finalScene.addSurface(sphereSurface);
		}

		// Add light sources:
		CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 45.0);
		cutoffSpotlight.initPosition(new Point(4.0, 4.0, -3.0));
		cutoffSpotlight.initIntensity(new Vec(1.0,0.6,0.6));
		finalScene.addLightSource(cutoffSpotlight);
		cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
		cutoffSpotlight.initPosition(new Point(-4.0, 4.0, -3.0));
		cutoffSpotlight.initIntensity(new Vec(0.6,1.0,0.6));
		finalScene.addLightSource(cutoffSpotlight);
		cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
		cutoffSpotlight.initPosition(new Point(0.0, 4.0, 0.0));
		cutoffSpotlight.initIntensity(new Vec(0.6,0.6,1.0));
		finalScene.addLightSource(cutoffSpotlight);
		DirectionalLight directionalLight=new DirectionalLight(new Vec(0.5,-0.5,0.0),new Vec(0.2));
		finalScene.addLightSource(directionalLight);

		return finalScene;
	}

	//refracting test
	public static Scene scene11() {
			// Define basic properties of the scene
			Scene finalScene = new Scene().initAmbient(new Vec(1.0))
					.initCamera(/* Camera Position = */new Point(0.0, 2.0, 6.0),
							/* Towards Vector = */ new Vec(0.0, -0.1 ,-1.0),
							/* Up vector = */new Vec(0.0, 1.0, 0.0),
							/*Distance to plain =*/ 2.0)
					.initName("scene11").initAntiAliasingFactor(1)
					.initRenderRefarctions(true).initRenderReflections(true).initMaxRecursionLevel(5);
			// Add Surfaces to the scene.
			// (1) A plain that represents the ground floor.
			Shape plainShape = new Plain(new Vec(0.0,1.0,0.0), new Point(0.0, -1.0, 0.0));
			Material plainMat = Material.getMetalMaterial();
			Surface plainSurface = new Surface(plainShape, plainMat);
			finalScene.addSurface(plainSurface);

			//create a transparent material
			Material transparent = new Material().initKa(new Vec(0.1)).initKd(new Vec(0.1))
					.initKs(new Vec(0.1)).initRefractionIndex(0.97).initIsTransparent(true)
					.initReflectionIntensity(0.8).initShininess(10);

			// (2) We will also add spheres to form a line one behind each other
			for (int depth = 0; depth < 3; depth++) {
				Shape sphereShape = new Sphere(new Point(1, 0, -depth*5), 1);
				Material sphereMat;
				if(depth == 0){
					sphereMat = transparent;
				} else if (depth == 1){
					sphereMat = Material.getGlassMaterial(false);
				} else {
					sphereMat = Material.getRandomMaterial();
				}

				Surface sphereSurface = new Surface(sphereShape, sphereMat);
				finalScene.addSurface(sphereSurface);
			}

			// Add light sources:
			CutoffSpotlight cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 45.0);
			cutoffSpotlight.initPosition(new Point(4.0, 4.0, -3.0));
			cutoffSpotlight.initIntensity(new Vec(1.0,0.6,0.6));
			finalScene.addLightSource(cutoffSpotlight);
			cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
			cutoffSpotlight.initPosition(new Point(-4.0, 4.0, -3.0));
			cutoffSpotlight.initIntensity(new Vec(0.6,1.0,0.6));
			finalScene.addLightSource(cutoffSpotlight);
			cutoffSpotlight = new CutoffSpotlight(new Vec(0.0, -1.0, 0.0), 30.0);
			cutoffSpotlight.initPosition(new Point(0.0, 4.0, 0.0));
			cutoffSpotlight.initIntensity(new Vec(0.6,0.6,1.0));
			finalScene.addLightSource(cutoffSpotlight);
			DirectionalLight directionalLight=new DirectionalLight(new Vec(0.5,-0.5,0.0),new Vec(0.2));
			finalScene.addLightSource(directionalLight);

			return finalScene;
		}

}
