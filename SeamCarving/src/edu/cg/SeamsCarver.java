package edu.cg;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SeamsCarver extends ImageProcessor {

	// MARK: An inner interface for functional programming.
	@FunctionalInterface
	interface ResizeOperation {
		BufferedImage resize();
	}

	// MARK: Fields
	private int numOfSeams;
	private ResizeOperation resizeOp;
	boolean[][] imageMask;

	// TODO: Add some additional fields
	private ArrayList<ArrayList<Integer>> originalImageGrayScale;//gray scale matrix representing
	// the value of each gray pixel I(x,y) in an ArrayList of int at location (x, y)
	private ArrayList<ArrayList<Integer>> pixelEnergy;
	public ArrayList<ArrayList<Long>> costMatrix;
	public ArrayList<ArrayList<Integer>> indexMatrix; //to remember the indexes of original image
	private ArrayList<ArrayList<Integer>> indexMatrixCopy; //for increase operation
	private ArrayList<ArrayList<Integer>> seamsPath;	//paths of all the seams removed

	public SeamsCarver(Logger logger, BufferedImage workingImage, int outWidth, RGBWeights rgbWeights,
					   boolean[][] imageMask) {
		super((s) -> logger.log("Seam carving: " + s), workingImage, rgbWeights, outWidth, workingImage.getHeight());

		numOfSeams = Math.abs(outWidth - inWidth);
		this.imageMask = imageMask;
		if (inWidth < 2 | inHeight < 2)
			throw new RuntimeException("Can not apply seam carving: workingImage is too small");

		if (numOfSeams > inWidth / 2)
			throw new RuntimeException("Can not apply seam carving: too many seams...");

		// Setting resizeOp by with the appropriate method reference
		if (outWidth > inWidth)
			resizeOp = this::increaseImageWidth;
		else if (outWidth < inWidth)
			resizeOp = this::reduceImageWidth;
		else
			resizeOp = this::duplicateWorkingImage;

		//initialize the saemsPath ArrayList that holds all the paths of the removed seams
		this.seamsPath = new ArrayList<>();
		//grayScale copy of working image (to compute the pixelEnergyInit)
		this.originalImageGrayScale = new ArrayList<>();
		this.greyScaleIntegers();
		//compute the pixelEnergyInit matrix, costMatrix matrix and indexMartix
		//initialize the ArrayLists
		this.pixelEnergy = new ArrayList<>();
		this.costMatrix = new ArrayList<>();
		this.indexMatrix = new ArrayList<>();
		this.indexMatrixCopy = new ArrayList<>();

		//create indexMatrix and indexMatrixCopy (for increase op)
		forEach((y, x) -> {
			//if x==0 then add new (inner) ArrayList to the main ArrayList
			if(x == 0) {
				indexMatrix.add(new ArrayList<>());
				indexMatrixCopy.add(new ArrayList<>());
			}
			//copy the x value to the index matrix
			indexMatrix.get(y).add(x);
			indexMatrixCopy.get(y).add(x);
		});

		//create the first pixelEnergy matrix (this matrix will be updated after we remove a
        // seam) and the first costMatrix
		forEach((y, x) -> {
			//if x==0 then add new (inner) ArrayList to the main ArrayList
			if(x == 0) {
				pixelEnergy.add(new ArrayList<>());
				costMatrix.add(new ArrayList<>());
			}
			//compute first the pixelEnergyInit at (x, y)
			int currentPixelEnergy = pixelEnergyInit(x, y);
			this.pixelEnergy.get(y).add(currentPixelEnergy);
			//now based on the pixelEnergyInit at (x, y) and the above row compute the costMatrix
			this.costMatrix.get(y).add(minCost(x, y) + currentPixelEnergy);
		});
		this.logger.log("preliminary calculations were ended.");
	}

	public BufferedImage resize() {
		return resizeOp.resize();
	}

	private BufferedImage reduceImageWidth() {
		logger.log("Preparing to reduce Images width operation...");
		//initialize an empty output sized image
		BufferedImage ans = newEmptyOutputSizedImage();
		//find the best k seams, remove each one and add all paths to seamsPath
		for (int i = 0; i < numOfSeams; i++) {
			//find best seam and add it to seamsPath ArrayList
			findBestSeamPath();
			//remove seam and update matrices
			if(i < numOfSeams - 1 ) updateCostMatrix();
		}
		//loop over the original image and remove the deleted pixels
		setForEachOutputParameters();
		forEach((y, x) -> {
			int originalXIndex = indexMatrix.get(y).get(x);
			// get pixels color from original image
			Color c = new Color(workingImage.getRGB(originalXIndex, y));
			ans.setRGB(x, y, c.getRGB());   // set the pixel in reduced image
		});
		logger.log("Images width reducing is done!");
		return ans;
	}

	private BufferedImage increaseImageWidth() {
		logger.log("Preparing to increase Images width operation...");
		//initialize an empty output sized image
		BufferedImage ans = newEmptyOutputSizedImage();
		//find the best k seams, remove each one and add all paths to seamsPath
		for (int i = 0; i < numOfSeams; i++) {
			findBestSeamPath(); //find best seam and add it to seamsPath ArrayList
            if(i < numOfSeams - 1)updateCostMatrix();   //remove seam and update matrices
		}
		//update the indexMatrixCopy for increase operation
		//this means to duplicate the indexes of the removed pixels
		for (int i = 0; i < seamsPath.size(); i++) {
			for (int j = 0; j < seamsPath.get(i).size(); j++) {
				//insert another index where copying should be done
				int removedXIndex = seamsPath.get(i).get(j);
				//the position of removedXIndex in indexMatrixCopy can differ by k the most
				for (int k = 0; k <= i; k++) {
					// the duplication will happen to the right of the original pixel
					if(removedXIndex == indexMatrixCopy.get(j).get(removedXIndex + k)){
						indexMatrixCopy.get(j).add(removedXIndex + k + 1, removedXIndex);
						break;
					}
					//else just increase k
				}
			}
		}
		//loop over the original image and duplicate the deleted pixels
		setForEachOutputParameters();
		forEach((y, x) -> {
			int originalXIndex = indexMatrixCopy.get(y).get(x);
			// get pixels color from original
			Color c = new Color(workingImage.getRGB(originalXIndex, y));
			ans.setRGB(x, y, c.getRGB());
		});
		logger.log("Images width increase is done!");
		return ans;
	}

	/**
	 * this function colors numOfSeams seams in the specified seamColorRGB, numberOfSeams is the
	 * difference between inWidth and outWidth requested by the user.
	 * @param seamColorRGB - the color the seam will be colored
	 * @return ans - a BufferedImage of the working image after the seams were colored
	 */
	public BufferedImage showSeams(int seamColorRGB) {
		logger.log("Preparing to show seams operation...");
		//duplicate Working image
		BufferedImage ans = duplicateWorkingImage();
		//find the best k seams, remove each one and add all paths to seamsPath
		for (int i = 0; i < numOfSeams; i++) {
			//find best seam and add it to seamsPath ArrayList
			findBestSeamPath();
			//remove seam and update matrices
			if(i < numOfSeams-1) updateCostMatrix();
		}

		// loop over all the saved paths and mark them with seam color
		for (int j = 0; j < seamsPath.size(); j++) {
			ArrayList<Integer> path = seamsPath.get(j);
			for (int i = 0; i < path.size() ; i++) {
				int xOriginal = path.get(i);
				ans.setRGB(xOriginal, i, seamColorRGB);
			}
		}
		logger.log("show seams is done!");
		return ans;
	}

	public boolean[][] getMaskAfterSeamCarving() {
		// This method should return the mask of the resize image after seam carving.
		// Meaning, after applying Seam Carving on the input image,
		// getMaskAfterSeamCarving() will return a mask, with the same dimensions as the
		// resized image, where the mask values match the original mask values for the
		// corresponding pixels.
		// HINT: Once you remove (replicate) the chosen seams from the input image, you
		// need to also remove (replicate) the matching entries from the mask as well.
		if(outWidth == inWidth) return imageMask;
		boolean[][] mask = new boolean[outHeight][outWidth];
		setForEachOutputParameters();
		if (outWidth > inWidth){	//increase op
			forEach((y, x) -> {
				int originalXIndex = indexMatrixCopy.get(y).get(x);
				mask[y][x] = imageMask[y][originalXIndex];
			});
		} else {	//decrease op
			forEach((y, x) -> {
				int originalXIndex = indexMatrix.get(y).get(x);
				mask[y][x] = imageMask[y][originalXIndex];
			});
		}
		return mask;
	}

	/**
	 * this function computes the path of the optimal seam starting from the minimum value at the
	 * bottom of the cost matrix all the way to the top. this path is then added to seamsPath.
	 * path - an ArrayList of Integers each ArrayList entry has the x coordinate of the removed seam
	 * from the original picture, the index in the arrayList is the y coordinate
	 */
	public void findBestSeamPath(){
		ArrayList<Integer> path = new ArrayList<>();
		ArrayList<Integer> currentIndexPath = new ArrayList<>();
		int height = workingImage.getHeight()-1;

		int minXIndexCurrent = getMinIndexInCostMatrix();	//the x index of minVal in the bottom row

		// this will be that last index in the final path
		path.add(0, this.indexMatrix.get(height).get(minXIndexCurrent));
		currentIndexPath.add(0, minXIndexCurrent);

		//loop on each row in the picture from bottom to top
		int xOriginalAbove = 0;    //the x index that the optimal path came from in original image
		for (int i = height; i > 0; i--) {
			int xOriginal = path.get(0);

			long currentCostMatrixValue = costMatrix.get(i).get(minXIndexCurrent);
			int currentPixelEnergy = pixelEnergy.get(i).get(minXIndexCurrent);

			//find the xAbove index out of three options above
			if (currentCostMatrixValue == currentPixelEnergy +
                    costCenterandUp(minXIndexCurrent, i) + c_V(minXIndexCurrent, i)) {
                xOriginalAbove = this.indexMatrix.get(i-1).get(minXIndexCurrent);
                // minXIndex = minXIndex, stays the same
			} else if ( currentCostMatrixValue == currentPixelEnergy +
                    costRightandUp(minXIndexCurrent, i) + c_R(minXIndexCurrent, i)) {
                xOriginalAbove = this.indexMatrix.get(i-1).get(minXIndexCurrent + 1);
                minXIndexCurrent = minXIndexCurrent + 1;
			} else {
				xOriginalAbove = this.indexMatrix.get(i-1).get(minXIndexCurrent - 1);
				minXIndexCurrent = minXIndexCurrent -1;
			}
			//add the xAbove index at the first place of the ArrayList
			path.add(0, xOriginalAbove);
			currentIndexPath.add(0, minXIndexCurrent);

			//update the removed seams in imageMask in the original x coordinate
			imageMask[i][xOriginal] = false;
		}
        //update the removed seams in imageMask in the original x coordinate
        imageMask[0][xOriginalAbove] = false;

		//after we found the seam we are removing, remove corresponding coordinates from
        // indexMatrix and pixelEnergy
        for (int i = 0; i <= height; i++) {
            int xCurrent = currentIndexPath.get(i);
            indexMatrix.get(i).remove(xCurrent);
            pixelEnergy.get(i).remove(xCurrent);
        }
		//add this path to the seamsPath ArrayList
		this.seamsPath.add(path);
		//update pixelEnergy around the deleted seam
		updatePixelEnergyAroundSeam(currentIndexPath);
	}

	/**
	 * this function updates the pixelEnergy matrix around the removed seam, no need to create
     * the pixel energy again, better time efficiency
	 * @param pathCurrent - the path removed with the current indexes
	 */
	public void updatePixelEnergyAroundSeam(ArrayList<Integer> pathCurrent) {
		int energy1, energy2;
		for (int i = 0; i < pathCurrent.size(); i++) {
			int xCurrent = pathCurrent.get(i);
			if(xCurrent == 0) {
				energy2 = E1PixelEnergy(xCurrent, i, xCurrent+1, i) + E2PixelEnergy(xCurrent, i,
						xCurrent, i+1) + E3PixelEnergy(xCurrent, i);
				pixelEnergy.get(i).set(xCurrent, energy2);	//update pixel energy
			} else if (xCurrent < workingImage.getWidth() - seamsPath.size()){
				energy1 = E1PixelEnergy(xCurrent-1, i, xCurrent, i) + E2PixelEnergy(xCurrent-1, i,
						xCurrent-1, i+1) + E3PixelEnergy(xCurrent-1, i);
				pixelEnergy.get(i).set(xCurrent-1,energy1);	//update pixel energy
				energy2 = E1PixelEnergy(xCurrent, i, xCurrent+1, i) + E2PixelEnergy(xCurrent, i,
						xCurrent, i+1) + E3PixelEnergy(xCurrent, i);
				pixelEnergy.get(i).set(xCurrent, energy2);	//update pixel energy
			} else {
				energy1 = E1PixelEnergy(xCurrent-1, i, xCurrent-2, i) + E2PixelEnergy(xCurrent-1, i,
						xCurrent-1, i+1) + E3PixelEnergy(xCurrent-1, i);
				pixelEnergy.get(i).set(xCurrent-1,energy1);	//update pixel energy
			}
		}
	}

	/**
	 * this function creates the costMatrix, it is computed again from zero.
	 */
	public void updateCostMatrix(){
		int currentWidth = workingImage.getWidth() - this.seamsPath.size();
		//create new costMatrix from scratch
		costMatrix = null;
		this.costMatrix = new ArrayList<>();
		//loop on all the rows of the image
		for (int i = 0; i < workingImage.getHeight(); i++) {
			//fill in the the entries row by row
			for (int x = 0; x < currentWidth; x++) {
				//if j==0 then add new (inner) ArrayList to the main ArrayList
				if(x == 0) {
					costMatrix.add(new ArrayList<>());
				}
				//compute the costMatrix, minCostFast has better performance so it is why we used
                // it here, we left the other functions (costUp, right, left, c_L, c_V, ... for
                // simplicity in other cases
				this.costMatrix.get(i).add(minCostFast(x, i) + pixelEnergy.get(i).get(x));
			}
		}
	}

	/**
	 * a faster version of the minCost function, this function runs a lot so we made it faster,
     * and for simplicity we left the other functions
	 * @param x - x coordinate of the current sized image
	 * @param y - y coordinate of the current sized image
	 * @return - the minCost
	 */
	public long minCostFast(int x, int y){
		if(y == 0) return 0;
		//costLeftandUp
		long costLeft = (x == 0)? (long) Integer.MAX_VALUE : this.costMatrix.get(y-1).get(x-1);
		//costRightandUp
		long costRight = (x == workingImage.getWidth() - seamsPath.size()-1)? (long) Integer.MAX_VALUE :
				this.costMatrix.get(y-1).get(x+1);
		//costCenterandUp
		long costUp = this.costMatrix.get(y-1).get(x);

		int cL; int cR; int cV;
		if(x == 0 || x == pixelEnergy.get(0).size()-1){
			cL = 0; cR=0; cV=0;
		} else {
			//C_V
			int c1 = originalImageGrayScale.get(y).get(indexMatrix.get(y).get(x+1));
			int c2 = (x+1 < workingImage.getWidth() - seamsPath.size() - 1)?
					originalImageGrayScale.get(y).get(indexMatrix.get(y).get(x-1)) :
					originalImageGrayScale.get(y).get(indexMatrix.get(y).get(x));
			cV = Math.abs(c1 - c2);

			//C_L
			int c3 = originalImageGrayScale.get(y-1).get(indexMatrix.get(y-1).get(x));
			int c4 = (y-1 < workingImage.getHeight() - 1)?
				originalImageGrayScale.get(y).get(indexMatrix.get(y).get(x-1)) :
				originalImageGrayScale.get(y-2).get(indexMatrix.get(y-2).get(x));
			cL = Math.abs(c3 - c4) + cV;

			//C_R
			int c5 = originalImageGrayScale.get(y).get(indexMatrix.get(y).get(x+1));
			int c6 = (y < workingImage.getHeight() - 1)?
				originalImageGrayScale.get(y-1).get(indexMatrix.get(y-1).get(x)) :
				originalImageGrayScale.get(y-1).get(indexMatrix.get(y-1).get(x+1));
			cR = Math.abs(c5 - c6) + cV;
		}
		long min = Math.min((costLeft + cL), (costRight + cR));
		return Math.min(min, (costUp+cV));
	}

	/**
	 * this function computes the pixel energy at pixel (x, y) in the initial matrix
	 * @param x - the x coordinate of the pixel
	 * @param y - the y coordinate of the pixel
	 * @return the full pixel energy E1+E2+E3
	 */
	public int pixelEnergyInit(int x, int y){
		return E1PixelEnergy(x, y, x+1, y) + E2PixelEnergy(x, y, x, y+1) + E3PixelEnergy(x, y);
	}

	/**
	 * this function computes the derivative in the x direction
	 * @return result - the derivative in the x direction of the pixel
	 */
	public int E1PixelEnergy(int x1, int y1, int x2, int y2) {
		int c1 = originalImageGrayScale.get(y1).get(indexMatrix.get(y1).get(x1));
		int c2 = (x1 < workingImage.getWidth() - seamsPath.size() - 1)?
				originalImageGrayScale.get(y2).get(indexMatrix.get(y2).get(x2)) :
				originalImageGrayScale.get(y1).get(indexMatrix.get(y1).get(x1-1));
		return Math.abs(c1 - c2);
	}

	/**
	 * this function computes the derivative in the y direction
	 * @return result - the derivative in the y direction of the pixel
	 */
	public int E2PixelEnergy(int x1, int y1, int x2, int y2) {
		int c1 = originalImageGrayScale.get(y1).get(indexMatrix.get(y1).get(x1));
		int c2 = (y1 < workingImage.getHeight() - 1)?
				originalImageGrayScale.get(y2).get(indexMatrix.get(y2).get(x2)) :
				originalImageGrayScale.get(y1-1).get(indexMatrix.get(y1-1).get(x1));
		return Math.abs(c1 - c2);
	}

	/**
	 * this function checks if the mask is True or False in pixel (x,y)
	 * if it is on return MIN_VALUE, if not return 0;
	 * @param x - the x coordinate of the pixel
	 * @param y - the y coordinate of the pixel
	 * @return if imageMask[y][x] is true MIN_VALUE else 0
	 */
	public int E3PixelEnergy(int x, int y) {
		return (imageMask[y][indexMatrix.get(y).get(x)])? Integer.MIN_VALUE : 0;
	}

	/**
	 * this function returns the minimum value from the three possible ways to reach this
	 * position (x, y)
	 * @param x - the x coordinate of the pixel
	 * @param y - the y coordinate of the pixel
	 * @return minCost - the minimum value from the C_values (see 3 functions below this)
	 */
	public long minCost(int x, int y) {
		if(y == 0) return 0;
		long min = Math.min((costLeftandUp(x, y)+c_L(x, y)), (costRightandUp(x, y)+c_R(x, y)));
		return Math.min(min, (costCenterandUp(x, y))+c_V(x, y));
	}

	/**
	 * this function returns the value of the costMatrix in position (x-1, y-1)
	 * if one of the indexes is out of bounds return 0 or MAX_VALUE (for x)
	 * @param x - the x coordinate of the pixel
	 * @param y - the y coordinate of the pixel
	 * @return costLeftandUp - the value of the costMatrix in position (x-1, y-1)
	 */
	public long costLeftandUp(int x, int y) {
		if (y == 0) return 0;
		return (x == 0)? (long) Integer.MAX_VALUE : this.costMatrix.get(y-1).get(x-1);
	}

	/**
	 * this function returns the value of the costMatrix in position (x+1, y-1)
	 * if one of the indexes is out of bounds return 0 or MAX_VALUE (for x)
	 * @param x - the x coordinate of the pixel
	 * @param y - the y coordinate of the pixel
	 * @return costRightandUp - the value of the costMatrix in position (x+1, y-1)
	 */
	public long costRightandUp(int x, int y) {
		if(y == 0) return 0;
		return (x == workingImage.getWidth() - seamsPath.size()-1)? (long) Integer.MAX_VALUE :
				this.costMatrix.get(y-1).get(x+1);
	}

	/**
	 * this function returns the value of the costMatrix in position (x, y-1)
	 * if one of the indexes is out of bounds return 0;
	 * @param x - the x coordinate of the pixel
	 * @param y - the y coordinate of the pixel
	 * @return costCenterandUp - the value of the costMatrix in position (x, y-1)
	 */
	public long costCenterandUp(int x, int y) {
		return (y == 0)? 0 : this.costMatrix.get(y-1).get(x);
	}

	/**
	 * this function returns the value of the two edges that we added to the picture after
	 * deleting the a seam, in this case the seams path comes to this pixel from left and above
	 * this is called forward difference.
	 * @param x - the x coordinate of the pixel
	 * @param y - the y coordinate of the pixel
	 * @return c_L - the energy added to the image after we will delete the seam
	 */
	public int c_L(int x, int y) {
		if(x == 0 || y == 0 || x == pixelEnergy.get(0).size()-1) return 0;
		return E2PixelEnergy(x, y-1, x-1, y) + E1PixelEnergy(x-1, y, x+1, y);
	}

	/**
	 * this function returns the value of the two edges that we added to the picture after
	 * deleting the a seam, in this case the seams path comes to this pixel from right and above
	 * this is called forward difference.
	 * @param x - the x coordinate of the pixel
	 * @param y - the y coordinate of the pixel
	 * @return c_R - the energy added to the image after we will delete the seam
	 */
	public int c_R(int x, int y) {
		if(x == 0 || y == 0 || x == pixelEnergy.get(0).size()-1) return 0;
		return E2PixelEnergy(x+1, y, x, y-1) + E1PixelEnergy(x+1, y, x-1, y);
	}

	/**
	 * this function returns the value of the two edges that we added to the picture after
	 * deleting the a seam, in this case the seams path comes to this pixel from above only
	 * this is called forward difference.
	 * @param x - the x coordinate of the pixel
	 * @param y - the y coordinate of the pixel
	 * @return c_V - the energy added to the image after we will delete the seam
	 */
	public int c_V(int x, int y) {
		if(y == 0 || x == 0 || x == pixelEnergy.get(0).size()-1) return 0;
		return E1PixelEnergy(x+1, y, x-1, y);
	}

	/**
	 * this function create a gray scale copy of the image, but instead of pixels we use integers
	 * in a gray scale pixel all the values are the same, so here we use only one int for it.
	 * the grayScale copy it put in two ArrayLists originalImageGrayScale and dynamicImageGrayScale.
	 */
	public void greyScaleIntegers() {
		//this is here because in EX1.jar file the weights effect the seam carving
		int r = rgbWeights.redWeight;
		int g = rgbWeights.greenWeight;
		int b = rgbWeights.blueWeight;
		//sum of all the weights given by the client
		int sumOfWeights = r + g + b;

		forEach((y, x) -> {
			if(x == 0){
				originalImageGrayScale.add(new ArrayList<>());
			}
			Color c = new Color(workingImage.getRGB(x, y));
			int red = r * c.getRed();
			int green = g * c.getGreen();
			int blue = b * c.getBlue();
			// grayScale is weighted average of the pixels RGB
			int grayScale = (red + green + blue) / sumOfWeights;
			// set the integer representing the pixel in the
			originalImageGrayScale.get(y).add(grayScale);
		});
	}

	//returns the minIndex in the bottom row of the costMatrix
	public int getMinIndexInCostMatrix(){
		int height = workingImage.getHeight()-1;
		int width = workingImage.getWidth() - seamsPath.size();
		long minVal = costMatrix.get(height).get(0);
		int minXIndex = 0;	//the x index of minVal in the bottom row
		long val;
		for (int i = 0; i < width; i++) {
			val = costMatrix.get(height).get(i);
			if(val < minVal){
				minVal = val;
				minXIndex = i;
			}
		}
		return minXIndex;
	}
}
