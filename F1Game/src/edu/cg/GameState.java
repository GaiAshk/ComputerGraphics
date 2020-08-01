package edu.cg;

import java.util.Timer;
import java.util.TimerTask;


import edu.cg.algebra.Vec;
/**
 * This class is already implemented. Use it to track the car movement when the user interacts with the game.
 * @author MOAB and Gai + Yaniv, well we did mostly some deleting :)
 */
public class GameState {
	public enum SteeringState {
		LEFT, STRAIGHT, RIGHT
	}

	public enum AccelarationState {
		GAS, CRUISE, BREAKS
	}

	private SteeringState steeringState;
	private AccelarationState accelearationState;
	private double carVelocity;
	private Vec nextTranslation;
	private Timer timer;
	private final long TIMER_INTERVAL_MS = (long) (1000.0 / 120.0);
	private final double MAX_ROTATION = 20.0;
	private final double MAX_TRANSLATION_X = 5;
	private final double MAX_VELOCITY = 80.0; // m / sec
	private final double CAR_ACCELRATION = 20.0; // m / sec^2

	public GameState() {
		steeringState = SteeringState.STRAIGHT;
		accelearationState = AccelarationState.CRUISE;
		carVelocity = 0.0;
		nextTranslation = new Vec(0.0, 0.0, 0.0);
		timer = new Timer();
		timer.schedule(new UpdateTranslation(), 0, TIMER_INTERVAL_MS);
	}
	
	public synchronized void resetGameState() {
		steeringState = SteeringState.STRAIGHT;
		accelearationState = AccelarationState.CRUISE;
		carVelocity = 0.0;
		nextTranslation = new Vec(0.0, 0.0, 0.0);
	}

	private synchronized double getCarVelocity() {
		return carVelocity;
	}

	private synchronized void updateCarVelocity(double newVelocity) {
		carVelocity = Math.max(0.0, newVelocity);
		carVelocity = Math.min(MAX_VELOCITY, carVelocity);
	}

	private synchronized double getCarAccelaration() {
		switch (accelearationState) {
		case GAS:
			return CAR_ACCELRATION;
		case CRUISE:
			return -2.0 * CAR_ACCELRATION;
		case BREAKS:
			return -5.0 * CAR_ACCELRATION;
		}
		return 0.0;
	}

	/**
	 * @return The rotation of the car about its axis. The rotation value
	 * 		   depends on the steering state (RIGHT, LEFT or Straight).
	 */
	public synchronized double getCarRotation() {
		switch (steeringState) {
		case LEFT:
			return -MAX_ROTATION;
		case RIGHT:
			return MAX_ROTATION;
		case STRAIGHT:
			return 0.0;
		}
		return 0.0;
	}

	public synchronized void updateSteering(SteeringState newState) {
		this.steeringState = newState;
	}

	public synchronized void updateAccelaration(AccelarationState newState) { this.accelearationState = newState; }

	private synchronized void updateNextTranslation(Vec deltaTranslation) {
		this.nextTranslation = this.nextTranslation.add(deltaTranslation);
		this.nextTranslation.x = (float) Math.max(nextTranslation.x, -1.0 * MAX_TRANSLATION_X);
		this.nextTranslation.x = (float) Math.min(nextTranslation.x, MAX_TRANSLATION_X);
	}

	/**
	 * The method returns the offset by which the car moved, relative to the last time this method was invoked.
	 * So if the method was invoked at t1 and t2. And assume that car position at t1 is p.x, p.y and p.z. So the new 
	 * car position at t2 is p.x+d.x, p.y+d.y and p.z+d.z, where d is the returned value of getNextTranslation at t2.
	 * 
	 * @return The offset by which the car moved.
	 */
	public synchronized Vec getNextTranslation() {
		Vec retVal = new Vec(this.nextTranslation);
		this.nextTranslation = new Vec(0.0);
		return retVal;
	}

	public synchronized void setCarVelocity(double v) {
		this.carVelocity = v;
	}

	class UpdateTranslation extends TimerTask {

		@Override
		public void run() {
			double theta = (getCarRotation() * Math.PI) / 180.0;
			double cosTheta = Math.cos(theta);
			double sinTheta = Math.sin(theta);
			double currentCarVelocity = getCarVelocity();
			double currentCarAccelaration = getCarAccelaration();
			currentCarAccelaration += -.1 * Math.abs(sinTheta) * CAR_ACCELRATION;
			double dt = (double) TIMER_INTERVAL_MS / 1000.0; // move from msec -> sec
			double dr = (currentCarVelocity * dt + currentCarAccelaration * dt * dt);
			double dz = Math.min(0.0, -cosTheta * dr);
			double dx = sinTheta * dr;
			double newVelocity = Math.min(MAX_VELOCITY, currentCarVelocity + cosTheta * currentCarAccelaration * dt);
			updateCarVelocity(newVelocity);
			updateNextTranslation(new Vec(dx, 0.0, dz));
		}

	}
}
