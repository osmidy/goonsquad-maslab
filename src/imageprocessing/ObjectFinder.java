package imageprocessing;

import java.util.ArrayList;
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
    public List<double[]> findBlocks(Mat image) {
        List<double[]> blocks = new ArrayList<double[]>();
        long pointer = image.getNativeObjAddr();
        for (int y = 0; y < image.rows(); y++) {
            for (int x = 0; x < image.cols(); x++) {
                boolean cubeCenter = checkCube(pointer, x, y);
                if (cubeCenter) {
                    blocks.add(new double[] { x, y });
                }
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
