package edu.cg.models;

import java.util.List;


public interface IIntersectable {
	/** 
	 * 
	 * @return
	 */
//	this was the original method, I changed the implementation to a tree
//	public List<BoundingSphere> getBoundingSpheres();

	public BoundingSphere getBoundingSpheres();
	

}
