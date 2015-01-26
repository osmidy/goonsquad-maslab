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
    private final Mat image;
    private final LinkedList<int[]> redQueue = new LinkedList<int[]>();
    private final LinkedList<int[]> greenQueue = new LinkedList<int[]>();
    private final List<int[]> redCubes = new ArrayList<int[]>();
    private final List<int[]> greenCubes = new ArrayList<int[]>();
    
    // Hacky things to make color separation work
    private final int notCenter = 33;
    private final int redCenter = 44;
    private final int greenCenter = 55;

    static {
        System.loadLibrary("interface");
    }

    public ObjectFinder(Mat image) {
        this.image = image;
        this.findCubes();
    }

    /**
     * Find blocks in an image
     * 
     * @return a List of the center pixels of blocks in the image
     */
    public synchronized void findCubes() {
        int rows = image.rows();
        int cols = image.cols();
        System.out.println("JAVA: " + notCenter + ", " + redCenter + ", " + greenCenter);
        long pointer = image.getNativeObjAddr();
        for (int y = 0; y < rows; y += 5) {
            for (int x = 0; x < cols; x += 5) {
                int cubeCenter = checkCube(pointer, x, y);
//                System.out.println(cubeCenter);
                if (cubeCenter == redCenter) {
                    redQueue.add(new int[] {x,y});
                }
                if (cubeCenter == greenCenter) {
                    greenQueue.add(new int[] {x, y});
                }
            }
        }

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
                if ((Math.abs(nextX - currentX)) > 15
                        && (Math.abs(nextY - currentY) > 15) || i + 1 == length) {
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
                if ((Math.abs(nextX - currentX)) > 15
                        && (Math.abs(nextY - currentY) > 15) || i + 1 == length) {
                    greenCubes.add(new int[] { totalX / count, totalY / count });
                    totalX = 0;
                    totalY = 0;
                    count = 0;
                }
            }
        }
        
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
    private native int checkCube(long pointer, int x, int y);
}
