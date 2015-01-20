package imageprocessing;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class ImageUtil {
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
        Mat2Image processedImageConverter = new Mat2Image(BufferedImage.TYPE_3BYTE_BGR);
        
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
            processedImage = rawImage.clone();
            processor.process(processedImage); //, processedImage.data);
            
            // Update the GUI windows
            updateWindow(cameraPane, rawImage, rawImageConverter);
            //processor.process(rawImage);
            updateWindow(opencvPane, processedImage, processedImageConverter);
            
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) { }
        }
    }
    
    private static JLabel createWindow(String name, int width, int height) {    
        JFrame imageFrame = new JFrame(name);
        imageFrame.setSize(width, height);
        imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel imagePane = new JLabel();
        imagePane.setLayout(new BorderLayout());
        imageFrame.setContentPane(imagePane);
        
        imageFrame.setVisible(true);
        return imagePane;
    }
    
    private static void updateWindow(JLabel imagePane, Mat mat, Mat2Image converter) {
        int w = (int) (mat.size().width);
        int h = (int) (mat.size().height);
        if (imagePane.getWidth() != w || imagePane.getHeight() != h) {
            imagePane.setSize(w, h);
        }
        BufferedImage bufferedImage = converter.getImage(mat);
        imagePane.setIcon(new ImageIcon(bufferedImage));
    }
    
    
    
    

    /**
     * Determines the color of the pixel
     * 
     * @param pixel
     *            Pixel in image matrix captured by CameraSensor
     * @return the color of the specified pixel (Red, Green, or Blue)
     */
    // public int getPixelColor();
}
