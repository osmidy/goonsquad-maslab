package imageprocessing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Mat;

/**
 * A class, using OpenCV, to identify objects in images from the camera.
 * 
 * @author osmidy
 *
 */
public class ObjectFinder {
	// For these, will travel to closest pixel/distance
	private final LinkedList<int[]> wallQueue = new LinkedList<int[]>();
	private final LinkedList<int[]> dropzoneQueue = new LinkedList<int[]>();
	private int[] wallPixel = new int[1];
	private int[] dropzonePixel = new int[1];
	
    private final LinkedList<int[]> redQueue = new LinkedList<int[]>();
    private final LinkedList<int[]> greenQueue = new LinkedList<int[]>();
    private final List<int[]> redCubes = new ArrayList<int[]>();
    private final List<int[]> greenCubes = new ArrayList<int[]>();
    
    // Hacky things to make color separation work
    private final int notObject = 33;
    private final int redCenter = 44;
    private final int greenCenter = 55;
    private final int wall = 66;
    private final int dropzone = 77;

    static {
        System.loadLibrary("interface");
    }
    
    /**
     * Finds all the things
     */
    public synchronized void findObjects(Mat image) {
    	this.getObjectTypes(image);
    	this.findCubes();
    	this.findClosestWall();
    	this.findDropzone();
    }

	/**
     * Find blocks in an image
     * 
     * @return a List of the center pixels of blocks in the image
     */
    public synchronized void getObjectTypes(Mat image) {
        // Need Lists empty every time a new frame is processed
    	wallQueue.clear();
    	dropzoneQueue.clear();
        redQueue.clear();
        greenQueue.clear();
        redCubes.clear();
        greenCubes.clear();
        
        int rows = image.rows();
        int cols = image.cols();
        long pointer = image.getNativeObjAddr();
        for (int y = 0; y < rows; y += 5) {
            for (int x = 0; x < cols; x += 5) {
                int objectType = getObjectType(pointer, x, y);
                if (objectType == redCenter) {
                    redQueue.add(new int[] {x,y});
                } else if (objectType == greenCenter) {
                    greenQueue.add(new int[] {x, y});
                } else if (objectType == wall) {
                	wallQueue.add(new int[] {x, y});
                } else if (objectType == dropzone) {
                	dropzoneQueue.add(new int[] {x, y});
                }
            }
        }
    }

	private void findClosestWall() {
		double closestDistance = Double.MAX_VALUE;
		double distance;
		int y;
		for (int[] pixel : wallQueue) {
			y = pixel[1];
			distance = 2129 * Math.pow(y, -0.876);
			if (distance < closestDistance) {
				closestDistance = distance;
				wallPixel = pixel;
			}
		}
	}
	
	private void findDropzone() {
		double closestDistance = Double.MAX_VALUE;
		double distance;
		int y;
		for (int[] pixel : dropzoneQueue) {
			y = pixel[1];
			distance = 2129 * Math.pow(y, -0.876);
			if (distance < closestDistance) {
				closestDistance = distance;
				dropzonePixel = pixel;
			}
		}
		
	}
    
    private synchronized void findCubes() {
        if (!redQueue.isEmpty()) {
            int[] firstPixel = redQueue.poll();
            int totalX = firstPixel[0];
            int totalY = firstPixel[1];
            int count = 1;
            int length = redQueue.size();
            for (int i = 0; i < length; i++) {
                int[] currentPixel = redQueue.get(i);
                int[] nextPixel;
                if (i + 1 == length) {
                    nextPixel = currentPixel;
                } else {
                    nextPixel = redQueue.get(i + 1);
                }
                int currentX = currentPixel[0];
                int currentY = currentPixel[1];
                int nextX = nextPixel[0];
                int nextY = nextPixel[1];
                totalX += currentX;
                totalY += currentY;
                count++;
                if ((Math.abs(nextX - currentX)) > 10
                        && (Math.abs(nextY - currentY) > 10) || i + 1 == length) {
                    redCubes.add(new int[] { totalX / count, totalY / count });
                    totalX = 0;
                    totalY = 0;
                    count = 0;
                }
            }
        }
        
        if (!greenQueue.isEmpty()) {
            int[] firstPixel = greenQueue.poll();
            int totalX = firstPixel[0];
            int totalY = firstPixel[1];
            int count = 1;
            int length = greenQueue.size();
            for (int i = 0; i < length; i++) {
                int[] currentPixel = greenQueue.get(i);
                int[] nextPixel;
                if (i + 1 == length) {
                    nextPixel = currentPixel;
                } else {
                    nextPixel = greenQueue.get(i + 1);
                }
                int currentX = currentPixel[0];
                int currentY = currentPixel[1];
                int nextX = nextPixel[0];
                int nextY = nextPixel[1];
                totalX += currentX;
                totalY += currentY;
                count++;
                if ((Math.abs(nextX - currentX)) > 10
                        && (Math.abs(nextY - currentY) > 10) || i + 1 == length) {
                    greenCubes.add(new int[] { totalX / count, totalY / count });
                    totalX = 0;
                    totalY = 0;
                    count = 0;
                }
            }
        }
    }
    
    public synchronized int[] getWallPixel() {
    	return this.wallPixel;
    }
    
    public synchronized int[] getDropzonePixel() {
    	return this.dropzonePixel;
    }
    public synchronized List<int[]> getRedCubes() {
        return this.redCubes;
    }
    
    public synchronized List<int[]> getGreenCubes() {
        return this.greenCubes;
    }

    /**
     * Verifies that given coordinate is the center of a desired cube
     * 
     * @param x
     *            horizontal center coordinate
     * @param y
     *            vertical center coordinate
     * @return boolean value of result of operation
     */
    private native int getObjectType(long pointer, int x, int y);
}
