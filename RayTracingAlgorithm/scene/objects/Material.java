package edu.cg.scene.objects;

import edu.cg.algebra.Vec;

public class Material {
	public Vec Ka = new Vec(0.1, 0.1, 0.1); // ambient coefficient
	public Vec Kd = new Vec(1, 1, 1); // diffuse coefficient
	public Vec Ks = new Vec(0.7, 0.7, 0.7); // specular coefficient
	public double reflectionIntensity = 0.3; // The reflection intensity
	public int shininess = 10; // shine factor - for specular calculation

	public boolean isTransparent = false;
	public double refractionIntensity = 0.3; // The refraction intensity
	public double refractionIndex = 1.5; // refraction index

	@Override
	public String toString() {
		String endl = System.lineSeparator();
		return "Ka: " + Ka + endl + "Kd1: " + Kd + endl + "Ks: " + Ks + endl + "Reflection Intensity: "
				+ reflectionIntensity + endl + "Shininess: " + shininess + endl + "isTransparent: " + isTransparent
				+ endl + "Refraction Intensity: " + refractionIntensity + endl + "Refraction Index: " + refractionIndex
				+ endl;
	}

	public Material initKa(Vec Ka) {
		this.Ka = Ka;
		return this;
	}

	public Material initKd(Vec Kd) {
		this.Kd = Kd;
		return this;
	}

	public Material initKs(Vec Ks) {
		this.Ks = Ks;
		return this;
	}

	public Material initReflectionIntensity(double reflectionIntensity) {
		this.reflectionIntensity = reflectionIntensity;
		return this;
	}

	public Material initShininess(int shininess) {
		this.shininess = shininess;
		return this;
	}

	public Material initRefractionIntensity(double refractionIntensity) {
		this.refractionIntensity = refractionIntensity;
		return this;
	}

	public Material initRefractionIndex(double refractionIndex) {
		this.refractionIndex = refractionIndex;
		return this;
	}

	public Material initIsTransparent(boolean isTransparent) {
		this.isTransparent = isTransparent;
		return this;
	}

	// Static methods for different materials:

	/**
	 * Creates a glass material
	 * 
	 * @param transparent - indicates whether the material is transparent
	 * @return an object representing a glass material.
	 */
	public static Material getGlassMaterial(boolean transparent) {
		Material mat = new Material();
		int shininess = 1 + (int) (Math.random() * ((15 - 1) + 1));
		mat.initKa(new Vec(0.1)).initKs(new Vec(0.1)).initShininess(shininess);
		mat.initKd(new Vec(0.1));
		mat.initReflectionIntensity(0.95);
		if (transparent) {
			mat.initIsTransparent(true).initRefractionIntensity(0.2).initReflectionIntensity(0.8);
		}
		return mat;
	}

	public static Material getMetalMaterial() {
		Material mat = new Material().initKa(new Vec(0.2)).initKd(new Vec(0.4)).initKs(new Vec(0.4))
				.initReflectionIntensity(0.2).initIsTransparent(false);
		return mat;
	}

	/**
	 * Generates a random material.
	 * 
	 * @return Returns a random material.
	 */
	public static Material getRandomMaterial() {
		boolean isTransparent = (Math.random() < 0.2 ? true : false);
		boolean isReflect = (Math.random() < 0.2 ? true : false);
		if (Math.random() < 0.1) {
			return getGlassMaterial(isTransparent);
		}
		// Lets pick a random color
		double rComponent = Math.random(), gComponent = Math.random(), bComponent = Math.random();
		// Lets pick a random refraction intensity
		double refractionIn = isTransparent ? Math.min(0.25+Math.random(), 1.0) : 0.0;
		// Lets pick a random reflection intensity
		double reflectionIn = isReflect ? Math.min(0.25+Math.random(), 1.0) : 0.0;
		// Set random specular reflection
		double specularIn =  Math.min(0.25+Math.random(), 1.0);
		double randomRefractionIndex = 1.33 +  Math.random();
		Material mat = new Material().initKa(new Vec(0.5*rComponent, 0.5*gComponent, 0.5*bComponent))
				.initKd(new Vec(rComponent, gComponent, bComponent)).initKs(new Vec(specularIn))
				.initReflectionIntensity(reflectionIn);
		if (isTransparent) {
			mat.initIsTransparent(true).initRefractionIndex(randomRefractionIndex)
					.initRefractionIntensity(refractionIn);
		}
		return mat;
	}
}
