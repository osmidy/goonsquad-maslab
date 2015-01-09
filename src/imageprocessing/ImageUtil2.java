package imageprocessing;

import ImageProcessor;
import Mat2Image;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

/**
 * An class providing tools to gather information from the images of a
 * CameraSensor.
 * 
 * @author osmidy
 *
 */
public class ImageUtil2 {
    
    /**
     * Main method
     */
    public static void main (String args[]) {
        // Load the OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Setup the camera
        VideoCapture camera = new VideoCapture();
        camera.open(0);
        
        // Create GUI windows to display camera output and OpenCV output
        int width = (int) (camera.get(Highgui.CV_CAP_PROP_FRAME_WIDTH));
        int height = (int) (camera.get(Highgui.CV_CAP_PROP_FRAME_HEIGHT));
        JLabel cameraPane = createWindow("Camera output", width, height);
        JLabel opencvPane = createWindow("OpenCV output", width, height);

        // Set up structures for processing images
        ImageProcessor processor = new ImageProcessor();
        Mat rawImage = new Mat();
        Mat processedImage = new Mat();
        Mat2Image rawImageConverter = new Mat2Image(BufferedImage.TYPE_3BYTE_BGR);
        Mat2Image processedImageConverter = new Mat2Image(BufferedImage.TYPE_BYTE_GRAY);
        
        while (true) {
            // Wait until the camera has a new frame
            while (!camera.read(rawImage)) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            // Process the image however you like
            processor.process(rawImage, processedImage);
            
            // Update the GUI windows
            updateWindow(cameraPane, rawImage, rawImageConverter);
            updateWindow(opencvPane, processedImage, processedImageConverter);
            
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) { }
        }
    }
        
    
    
    
    

    /**
     * Determines the color of the pixel
     * 
     * @param pixel
     *            Pixel in image matrix captured by CameraSensor
     * @return the color of the specified pixel (Red, Green, or Blue)
     */
    public int getPixelColor();

}
