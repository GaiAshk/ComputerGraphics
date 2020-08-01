package edu.cg;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import edu.cg.GameState.AccelarationState;
import edu.cg.GameState.SteeringState;

public class Main {
	static Point prevMouse;
	static int currentModel;
	static Frame frame;
	static NeedForSpeed game;
	/**
	 * Create frame, canvas and viewer, and load the first model.
	 * 
	 * @param args No arguments
	 */
	public static void main(String[] args) {
		frame = new JFrame();

		// General OpenGL init
		GLProfile.initSingleton();
		GLProfile glp = GLProfile.get(GLProfile.GL2);
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setSampleBuffers(true);
		caps.setNumSamples(4);
		final GLJPanel canvas = new GLJPanel(caps);

		frame.setSize(500, 500);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);

		// Create viewer and initialize with first model
		game = new NeedForSpeed(canvas);

		// Add event listeners
		canvas.addGLEventListener(game);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(1);
			}
		});

		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				GameState gameState = game.getGameState();

				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					gameState.updateAccelaration(AccelarationState.GAS);
					break;
				case KeyEvent.VK_DOWN:
					gameState.updateAccelaration(AccelarationState.BREAKS);
					break;
				case KeyEvent.VK_LEFT:
					gameState.updateSteering(SteeringState.LEFT);
					break;
				case KeyEvent.VK_RIGHT:
					gameState.updateSteering(SteeringState.RIGHT);
					break;
				case KeyEvent.VK_L:
					game.toggleNightMode();
					frame.repaint();
					break;
				case KeyEvent.VK_V:
					game.changeViewMode();
					frame.repaint();
					frame.setBounds(frame.getBounds());
					break;
				default:
					break;
				}
				super.keyPressed(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				GameState gameState = game.getGameState();

				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
				case KeyEvent.VK_DOWN:
					gameState.updateAccelaration(AccelarationState.CRUISE);
					break;
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
					gameState.updateSteering(SteeringState.STRAIGHT);
					break;
				default:
					break;
				}
				super.keyPressed(e);
			}
			@Override
			public void keyTyped(KeyEvent e) {
				// canvas.repaint();
				super.keyTyped(e);
			}
		});

		// Show frame
		canvas.setFocusable(true);
		canvas.requestFocus();
		frame.setVisible(true);
		frame.setTitle("Exercise 6 Gai & Yaniv");
		canvas.repaint();
	}
}
