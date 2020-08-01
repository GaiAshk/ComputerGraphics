package edu.cg.models;

import java.util.List;

import com.jogamp.opengl.GL2;

public class Track implements IRenderable, IIntersectable {
	private TrackSegment currentTrackSegment = null;
	private TrackSegment nextTrackSegment = null;
	private double currentDifficulty = 0.2;
	private final double DIFFICULTY_DELTA = 0.025;
	private final double MAXIMUM_DIFFICULTY = 0.75;

	public Track() {
		// Initialize the track
		currentTrackSegment = new TrackSegment(currentDifficulty);
		nextTrackSegment = new TrackSegment(currentDifficulty + DIFFICULTY_DELTA);
	}

	@Override
	public void render(GL2 gl) {
		// Render the track by rendering the current and next segment.
		gl.glPushMatrix();
		currentTrackSegment.render(gl);
		gl.glTranslated(0.0, 0.0, -TrackSegment.TRACK_LENGTH);
		nextTrackSegment.render(gl);
		gl.glPopMatrix();
	}

	@Override
	public void init(GL2 gl) {
		// Initialize the track segments.
		// The init method for both segments will load the textures of the models.
		currentTrackSegment.init(gl);
		nextTrackSegment.init(gl);
	}

	@Override
	public void destroy(GL2 gl) {
		// Destroy the track segments.
		// This will destroy the textures of the track segments.
		// Note if this method is invoked, then you cannot longer render the track -
		// because the textures are not available.
		currentTrackSegment.destroy(gl);
		nextTrackSegment.destroy(gl);
		currentTrackSegment = nextTrackSegment = null;
	}

	public void changeTrack(GL2 gl) {
		// Change the current track by switching the current and next track.
		// - We provided an implementation, you can change it if you want.
		TrackSegment tmp = currentTrackSegment;
		currentTrackSegment = nextTrackSegment;
		currentDifficulty += DIFFICULTY_DELTA;
		currentDifficulty = Math.min(currentDifficulty, MAXIMUM_DIFFICULTY);
		tmp.setDifficulty(currentDifficulty + DIFFICULTY_DELTA);
		nextTrackSegment = tmp;
	}

	public List<BoundingSphere> getBoundingSpheresList() {
		return currentTrackSegment.getBoundingSpheresList();
	}

	@Override
	public BoundingSphere getBoundingSpheres() {return null;}	//dummy method

}
