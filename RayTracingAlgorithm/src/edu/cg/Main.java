package edu.cg;

import java.io.File;

import edu.cg.menu.MenuWindow;

public class Main {
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		File scenesDir = new File("scenes");
		if(!scenesDir.exists() && !scenesDir.mkdir())
			throw new RuntimeException("Couldn't create the folder scenes...");
		
		File imagesOutputDir = new File("outputs");
		if(!imagesOutputDir.exists() && !imagesOutputDir.mkdir())
			throw new RuntimeException("Couldn't create the folder outputs...");
		
		MenuWindow mw = new MenuWindow();
		mw.setVisible(true);
	}
}
