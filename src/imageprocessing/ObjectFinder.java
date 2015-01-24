package imageprocessing;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

/**
 * A class, using OpenCV, to identify objects in images from the camera.
 * 
 * @author osmidy
 *
 */
public class ObjectFinder {
    static {
        System.loadLibrary("interface");
    }

    /**
     * Find blocks in an image
     * 
     * @param image
     *            Mat object representing the image to examine
     * @return a List of the center pixels of blocks in the image
     */
    public synchronized List<int[]> findBlocks(Mat image) {
        List<int[]> blocks = new ArrayList<int[]>();
        LinkedList<int[]> queue = new LinkedList<int[]>();
        long pointer = image.getNativeObjAddr();
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                boolean cubeCenter = checkCube(pointer, x, y);
                if (cubeCenter) {
                    queue.add(new int[] { x, y });
                }
            }
        }
        int[] firstPixel = queue.poll();
        int totalX = firstPixel[0];
        int totalY = firstPixel[1];
        int count = 1;
        for (int i = 0; i < queue.size(); i++) {
            int[] pixel = queue.get(i);
            int[] nextPixel;
            if (i + 1 >= queue.size()) {
                nextPixel = pixel;
            } else {
                nextPixel = queue.get(i + 1);
            }
            int x = pixel[0];
            int y = pixel[1];
            int nextX = nextPixel[0];
            int nextY = nextPixel[1];
            if (Math.abs(nextX - x) <= 1 || Math.abs(nextY - y) <= 1) {
                totalX += x;
                totalY += y;
                count++;
            } else {
                totalX += x;
                totalY += y;
                count++;
                blocks.add(new int[] { totalX / count, totalY / count });
                totalX = 0;
                totalY = 0;
                count = 0;
            }
        }
        return blocks;
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
    private native boolean checkCube(long pointer, int x, int y);
}
