package edu.cg.models;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import edu.cg.algebra.Point;
import edu.cg.algebra.Vec;

public class SkewedBox implements IRenderable {
	private double length, height1, height2, depth1, depth2;
	private Texture texBox = null;
	private Texture texBillBoad = null;
	private boolean useTexture = false;
	private boolean useTexture2 = false;

	public SkewedBox(double length, double h1, double h2, double d1, double d2) {
		this.length = length;
		this.height1 = h1;
		this.height2 = h2;
		this.depth1 = d1;
		this.depth2 = d2;
	}

	public SkewedBox(double length, double h1, double h2, double d1, double d2, boolean useTexture) {
		this.length = length;
		this.height1 = h1;
		this.height2 = h2;
		this.depth1 = d1;
		this.depth2 = d2;
		this.useTexture2 = useTexture;
	}

	public SkewedBox(double length, boolean useTexture) {
		this.length = length;
		this.depth1 = length;
		this.depth2 = length;
		this.height1 = length;
		this.height2 = length;
		this.useTexture = useTexture;
	}

	@Override
	public void render(GL2 gl) {
		Vec normal = null;
		if (useTexture) {
			assert (texBox != null && gl != null);
			initTextureProperties(gl);
		}
		if (useTexture2) {
			assert (texBillBoad != null && gl != null);
			initTextureProperties2(gl);
		}
		// Front
		gl.glNormal3d(1.0, 0.0, 0.0);
		gl.glBegin(GL2.GL_QUADS);
		gl.glTexCoord2d(0.0, 0.0);
		gl.glVertex3d(length / 2.0, 0.0, depth2 / 2.0);
		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex3d(length / 2.0, 0.0, -depth2 / 2.0);
		gl.glTexCoord2d(1.0, 1.0);
		gl.glVertex3d(length / 2.0, height2, -depth2 / 2.0);
		gl.glTexCoord2d(1.0, 0.0);
		gl.glVertex3d(length / 2.0, height2, depth2 / 2.0);
		// Back
		gl.glNormal3d(-1.0, 0.0, 0.0);
		gl.glTexCoord2d(0.0, 0.0);
		gl.glVertex3d(-length / 2.0, 0.0, -depth1 / 2.0);
		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex3d(-length / 2.0, 0.0, depth1 / 2.0);
		gl.glTexCoord2d(1.0, 1.0);
		gl.glVertex3d(-length / 2.0, height1, depth1 / 2.0);
		gl.glTexCoord2d(1.0, 0.0);
		gl.glVertex3d(-length / 2.0, height1, -depth1 / 2.0);
		// Up
		normal = new Vec(height1 - height2, 1.0, 0.0).normalize();
		gl.glNormal3d(normal.x, normal.y, normal.z);
		gl.glTexCoord2d(0.0, 0.0);
		gl.glVertex3d(-length / 2.0, height1, depth1 / 2.0);
		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex3d(length / 2.0, height2, depth2 / 2.0);
		gl.glTexCoord2d(1.0, 1.0);
		gl.glVertex3d(length / 2.0, height2, -depth2 / 2.0);
		gl.glTexCoord2d(1.0, 0.0);
		gl.glVertex3d(-length / 2.0, height1, -depth1 / 2.0);
		// Bottom
		gl.glNormal3d(0.0, -1.0, 0.0);
		gl.glTexCoord2d(0.0, 0.0);
		gl.glVertex3d(-length / 2.0, 0.0, depth1 / 2.0);
		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex3d(-length / 2.0, 0.0, -depth1 / 2.0);
		gl.glTexCoord2d(1.0, 1.0);
		gl.glVertex3d(length / 2.0, 0.0, -depth2 / 2.0);
		gl.glTexCoord2d(1.0, 0.0);
		gl.glVertex3d(length / 2.0, 0.0, depth2 / 2.0);
		// Right
		normal = new Vec(depth1 - depth2, 0.0, 1.0).normalize();
		gl.glNormal3d(normal.x, 0.0, normal.z);
		gl.glTexCoord2d(0.0, 0.0);
		gl.glVertex3d(-length / 2.0, height1, depth1 / 2.0);
		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex3d(-length / 2.0, 0.0, depth1 / 2.0);
		gl.glTexCoord2d(1.0, 1.0);
		gl.glVertex3d(length / 2.0, 0.0, depth2 / 2.0);
		gl.glTexCoord2d(1.0, 0.0);
		gl.glVertex3d(length / 2.0, height2, depth2 / 2.0);
		// Left
		normal = new Vec(depth1 - depth2, 0.0, -1.0).normalize();
		gl.glNormal3d(normal.x, 0.0, normal.z);
		gl.glTexCoord2d(0.0, 0.0);
		gl.glVertex3d(-length / 2.0, 0.0, -depth1 / 2.0);
		gl.glTexCoord2d(0.0, 1.0);
		gl.glVertex3d(-length / 2.0, height1, -depth1 / 2.0);
		gl.glTexCoord2d(1.0, 1.0);
		gl.glVertex3d(length / 2.0, height2, -depth2 / 2.0);
		gl.glTexCoord2d(1.0, 0.0);
		gl.glVertex3d(length / 2.0, 0.0, -depth2 / 2.0);
		gl.glEnd();

		gl.glDisable(GL2.GL_TEXTURE_2D);
	}

	private void initTextureProperties(GL2 gl) {
		gl.glEnable(GL2.GL_TEXTURE_2D);
		texBox.bind(gl);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_LOD, 1);
	}

	private void initTextureProperties2(GL2 gl) {
		gl.glEnable(GL2.GL_TEXTURE_2D);
		texBillBoad.bind(gl);

		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
		gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_LOD, 1);
	}

	@Override
	public void init(GL2 gl) {
		if (useTexture) {
			try {
				texBox = TextureIO.newTexture(new File("Textures/WoodBoxTexture.jpg"), true);
			} catch (Exception e) {
				System.err.print("Unable to read texture : " + e.getMessage());
			}
		}
	}

	public void init2(GL2 gl) {
		if (useTexture2) {
			try {
				texBillBoad = TextureIO.newTexture(new File("Textures/BillBoadSign.png"), true);
			} catch (Exception e) {
				System.err.print("Unable to read texture : " + e.getMessage());
			}
		}
	}

	public void destroy(GL2 gl) {
		if (useTexture) {
			texBox.destroy(gl);
			texBox = null;
		}
	}

	@Override
	public String toString() {
		return "SkewedBox";
	}

	public List<BoundingSphere> getBoundingSpheres() {
		LinkedList<BoundingSphere> res = new LinkedList<BoundingSphere>();
		//center in the skewed box coordinate system is (0,(h1+h2)/2,0)
		Point sphereBoundCenter = new Point(0, (height1 + height2)/2,0);
		//there are two options for farthest point of the skewed box from its center
		double dis1 = sphereBoundCenter.sub(new Point(this.length/2, this.height1, this.depth1/2)).length();
		double dis2 = sphereBoundCenter.sub(new Point(this.length/2, this.height2, this.depth2/2)).length();
		// the radius is going to be the farthest point, max point
		double sphereBoundRadius = Math.max(dis1, dis2);
		BoundingSphere s1 = new BoundingSphere(sphereBoundRadius, sphereBoundCenter);
		res.add(s1);
		return res;
	}
}
