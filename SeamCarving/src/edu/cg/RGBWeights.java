package edu.cg;

public class RGBWeights {
	public final int redWeight;
	public final int greenWeight;
	public final int blueWeight;
	public final int weightsAmount;
	public final int maxWeight;
	
	public RGBWeights(int redWeight, int greenWeight, int blueWeight) {
		this.redWeight = redWeight;
		this.greenWeight = greenWeight;
		this.blueWeight = blueWeight;
		weightsAmount = redWeight + greenWeight + blueWeight;
		maxWeight = Math.max(redWeight, Math.max(greenWeight, blueWeight));
	}
}
