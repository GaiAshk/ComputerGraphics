//package edu.cg.models;
//
//import com.jogamp.opengl.GL2;
//import com.jogamp.opengl.glu.GLU;
//import com.jogamp.opengl.glu.GLUquadric;
//
//import edu.cg.algebra.Point;
//
//public class BoundingSphere implements IRenderable {
//	// TODO Add your implementation
//}

package edu.cg.models;

import com.jogamp.opengl.GL2;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import edu.cg.algebra.Point;

import java.util.LinkedList;
import java.util.List;

public class BoundingSphere implements IRenderable {
    private double radius = 0.0;
    private Point center;
    private double color[];
    public List<BoundingSphere> children;		//this makes the BoundingSphere a Tree

    public BoundingSphere(double radius, Point center) {
        color = new double[3];
        this.setRadius(radius);
        this.setCenter(new Point(center.x, center.y, center.z));
        this.children = new LinkedList<>();
    }

    public void setSphereColore3d(double r, double g, double b) {
        this.color[0] = r;
        this.color[1] = g;
        this.color[2] = b;
    }

    /**
     * Destroy the model and free resources. This should be used
     * to destroy textures (if any).
     * @param gl GL context
     */
    public void destroy(GL2 gl){
        // TODO: BoundingSphere is implementing IRenderable, this method must be implemented
    }


    /**
     * Given a sphere s - check if this sphere and the given sphere intersect.
     * @return true if the spheres intersects, and false otherwise
     */
    public boolean checkIntersection(BoundingSphere s) {
        //distance between this sphere and sphere s
        double distance = center.sub(s.getCenter()).length();
        //if collided with s then current distance 'distance' has to be less the sum of radius
        if(distance <= radius + s.getRadius())return true;
        return false;
    }

    public void translateCenter(double dx, double dy, double dz) {
        this.setCenter(new Point(center.x + dx, center.y + dy, center.z + dz));
    }
    // a more elegant method to translate all the BS's children, and this BS too
    public void translateCenterToChildrenAndMe(double dx, double dy, double dz) {
        this.translateCenter(dx, dy, dz);
        for (int i = 0; i < this.children.size(); i++) {
            children.get(i).translateCenter(dx, dy, dz);
        }
    }

    public void translateCenterToChildrenAndMe(Point other) {
        this.translateCenter(other.x, other.y, other.z);
        for (int i = 0; i < this.children.size(); i++) {
            children.get(i).translateCenter(other.x, other.y, other.z);
        }
    }

    //this applyies the spheres rotation to the game mode
    public void rotateToGameMode() {
        Point oldCenter2 = this.getCenter();
        Point newCenter2 = new Point(oldCenter2.z, oldCenter2.y, -oldCenter2.x);
        this.setCenter(newCenter2);
        for (int i = 0; i < this.children.size(); i++) {
            Point oldCenter = children.get(i).getCenter();
            Point newCenter = new Point(oldCenter.z, oldCenter.y, -oldCenter.x);
            children.get(i).setCenter(newCenter);
        }
    }

    //scaling to game mode is by a factor of 4
    public void scaleToGameMode(double scaleFactor) {
        //fix spheres position due to change by scaling
        Point carCenter = children.get(1).getCenter();
        Point carFrontCurrent = children.get(0).getCenter();
        Point carBackCurrent = children.get(2).getCenter();

        //Find new Z coordinate of car, x and y are obvious
        double carFrontNewZ = carFrontCurrent.z + 3*(carFrontCurrent.z - carCenter.z);
        double carBackNewZ = carBackCurrent.z - 3*(carCenter.z - carBackCurrent.z);

        //change Front and Back position due to scaling uniformly by scaleFactor
        children.get(0).setCenter(carFrontCurrent.x*scaleFactor, carFrontCurrent.y*scaleFactor,
                carFrontNewZ);
        children.get(2).setCenter(carBackCurrent.x*scaleFactor, carBackCurrent.y*scaleFactor,
                carBackNewZ);

        //scale radius of all the spheres
        this.setRadius(scaleFactor * this.getRadius());
        for (int i = 0; i < this.children.size(); i++) {
            children.get(i).setRadius(scaleFactor * children.get(i).getRadius());
        }
    }

    @Override
    public void render(GL2 gl) {
        gl.glColor3d(this.color[0], this.color[1], this.color[2]);
        GLU glu = new GLU();
        gl.glPushMatrix();
        gl.glTranslated(center.x, center.y, center.z);
        GLUquadric quad = glu.gluNewQuadric();
        glu.gluSphere(quad, this.radius, 10, 10);
        gl.glPopMatrix();
        glu.gluDeleteQuadric(quad);
        //this is rendering all the bounding spheres in the tree
        if(!this.isLeaf()){
            for(BoundingSphere sphere : children){
                if(sphere == null) break;
                sphere.render(gl);
            }
        }
    }

    @Override
    public void init(GL2 gl) {}

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public void setCenter(double x, double y, double z){setCenter(new Point(x, y, z));}

    //checks if this bounding sphere is a leaf in the bounding sphere tree
    public boolean isLeaf(){
        return (this.children.size() == 0);
    }
    //adds a bounding sphere as a child to this bounding sphere
    public void addChild(BoundingSphere s){
        this.children.add(s);
    }
    //adds a list of bounding spheres to be children of this bounding sphere
    public void addList(List<BoundingSphere> list){
        for (int i = 0; i < list.size(); i++) {
            this.addChild(list.get(i));
        }
    }
    //translates and colors a list of bounding spheres, much more elegant
    public static void translateAndColorList(List<BoundingSphere> list, double dx, double dy, double dz, double r, double g, double b) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).translateCenter(dx, dy, dz);
            list.get(i).setSphereColore3d(r, g, b);
        }
    }
}

