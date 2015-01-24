package imageprocessing;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class ImageUtil {
    /**
     * Main method
     */
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load the OpenCV library

        int width = 320;
        int height = 240;
        // Diagonal FOV is ~70; 56 horizontal, 42 vertical
        // Resized images have 320 horizontal pixels
        double thetaX = 56;
        double pixelsPerDegree = width / thetaX; // Horizontal direction
        double radiansToDegrees = 180 / Math.PI;
        double radiansX = thetaX / radiansToDegrees;
        double arctanHalfX = Math.atan(thetaX / 2);
        double focalLength = width * .5 / arctanHalfX;

        // Setup the camera
        VideoCapture camera = new VideoCapture();
        camera.open(0);

        // Create GUI windows to display camera output and OpenCV output
        JLabel cameraPane = createWindow("Camera output", width, height);
        JLabel opencvPane = createWindow("OpenCV output", width, height);

        // Set up structures for processing images
        ImageProcessor processor = new ImageProcessor();
        ObjectFinder finder = new ObjectFinder();
        Mat rawImage = Highgui.imread("/home/osmidy/center.jpg");// new Mat();
        Mat resizedImage = new Mat();
        Mat processedImage = new Mat();
        Mat2Image rawImageConverter = new Mat2Image(
                BufferedImage.TYPE_3BYTE_BGR);
        Mat2Image processedImageConverter = new Mat2Image(
                BufferedImage.TYPE_3BYTE_BGR);

        // while (true) {
        // // Wait until the camera has a new frame
        // while (!camera.read(rawImage)) {
        // try {
        // Thread.sleep(1);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
        // }

        Imgproc.resize(rawImage, resizedImage, new Size(width, height)); // Halves
                                                                         // resolution
        processor.process(resizedImage, processedImage);

        // Find objects in processedImage
        // 1. check pixels
        // 2. check surrounding pixels for color/object match (flood fill?)
        // 3. if match, get take pixel(center), convert to location for robot to
        // travel to

        List<int[]> blocks = finder.findBlocks(processedImage);
        for (int[] block : blocks) {
            System.out.println(Arrays.toString(block));
        }

        // Update the GUI windows
        updateWindow(cameraPane, resizedImage, rawImageConverter);
        updateWindow(opencvPane, processedImage, processedImageConverter);

        // try {
        // Thread.sleep(10);
        // } catch (InterruptedException e) {
        // }
        // }
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

    private static void updateWindow(JLabel imagePane, Mat mat,
            Mat2Image converter) {
        int w = (int) (mat.size().width);
        int h = (int) (mat.size().height);
        if (imagePane.getWidth() != w || imagePane.getHeight() != h) {
            imagePane.setSize(w, h);
        }
        BufferedImage bufferedImage = converter.getImage(mat);
        imagePane.setIcon(new ImageIcon(bufferedImage));
    }
}
