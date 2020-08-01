package edu.cg.models;

import com.jogamp.opengl.GL2;

/**
 * A renderable model
 * 
 */
public interface IRenderable {
	
	/**
	 * Render the model
	 * 
	 * @param gl
	 *            GL context
	 */
	public void render(GL2 gl);

	/**
	 * Initialize the model. 
	 * Use this to initialize textures in the model.
	 * 
	 * @param gl
	 *            GL context
	 */
	public void init(GL2 gl);
	
	/**
	 * Destroy the model and free resources. This should be used
	 * to destroy textures (if any).
	 * @param gl
	 * 			  GL context
	 */			  
	public void destroy(GL2 gl);
	

}
