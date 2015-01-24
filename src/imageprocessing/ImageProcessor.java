package imageprocessing;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ImageProcessor {
    private static final int numBuffers = 1;
    private List<Mat> buffers = null;
    
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.loadLibrary("interface");
    }
    
    public ImageProcessor() {
        // Fill "buffers" with however many intermediate images that you will need
        // to get from "rawImage" to "processedImage" in the "process" method
        buffers = new ArrayList<Mat>();
        for (int i = 0; i < numBuffers; i++) {
            buffers.add(new Mat(new Size(), CvType.CV_8UC3));
            buffers.add(new Mat(new Size(), CvType.CV_8UC1));
            buffers.add(new Mat(new Size(), CvType.CV_8UC1));
            buffers.add(new Mat(new Size(), CvType.CV_8UC1));
        }
    }

    // Input: an image from the camera
    // Output: the OpenCV-processed image
    
    // (In practice it's a little different:
    //  the output image will be for your visual reference,
    //  but you will mainly want to output a list of the locations of detected objects.)
    public void process(Mat resizedImage, Mat processedImage) {
        Imgproc.GaussianBlur(resizedImage, processedImage, new Size(5, 5), 1.0);
        long pointer = processedImage.getNativeObjAddr();
        process(pointer);
    }
    
    private native void process(long imagePointer);
}
