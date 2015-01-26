package imageprocessing;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
    private static final int width = 320;
    private static final int height = 240;
    // Diagonal FOV is ~70; 56 horizontal, 42 vertical
    // Resized images have 320 horizontal pixels
    private static final double thetaX = 56;
    private static final double pixelsPerDegree = width / thetaX; // Horizontal
                                                                  // direction
    private static final double radiansToDegrees = 180 / Math.PI;
    private static final double radiansX = thetaX / radiansToDegrees;
    private static final double tanHalfX = Math.tan(thetaX / 2);
    private static final double focalLength = width * 0.5 / tanHalfX;

    /**
     * Main method
     */
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load the OpenCV library
        // Setup the camera
        VideoCapture camera = new VideoCapture();
        camera.open(0);

        // Create GUI windows to display camera output and OpenCV output
        JLabel cameraPane = createWindow("Camera output", width, height);
        JLabel opencvPane = createWindow("OpenCV output", width, height);

        // Set up structures for processing images
        ImageProcessor processor = new ImageProcessor();
        ObjectFinder finder = new ObjectFinder();
        // Scanner input = new Scanner(System.in);
        // String file = input.nextLine();
        Mat rawImage = new Mat();// Highgui.imread("C:/Users/George/Dropbox (MIT)/goonsquad-maslab/src/imageprocessing/test-images/center.jpg");//new
                                 // Mat(); // Highgui.imread("/home/osmidy/" +
                                 // file + ".jpg");
        Mat resizedImage = new Mat();
        Mat processedImage = new Mat();
        Mat2Image rawImageConverter = new Mat2Image(
                BufferedImage.TYPE_3BYTE_BGR);
        Mat2Image processedImageConverter = new Mat2Image(
                BufferedImage.TYPE_3BYTE_BGR);
        List<int[]> cubeCenters;

        while (true) {
            // Wait until the camera has a new frame
            while (!camera.read(rawImage)) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Imgproc.resize(rawImage, resizedImage, new Size(width, height)); // Halves
                                                                             // resolution
            processor.process(resizedImage, processedImage);
            cubeCenters = finder.findBlocks(processedImage);
            for (int[] center : cubeCenters) {
                System.out.println(Arrays.toString(center));
            }
            // Update the GUI windows
            updateWindow(cameraPane, resizedImage, rawImageConverter);
            updateWindow(opencvPane, processedImage, processedImageConverter);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * @param cubeCenters
     * @return center pixel of closest cube
     */
    public int[] findClosestCube(List<int[]> cubeCenters) {
        int[] closestCenter = cubeCenters.get(0);
        double closestDistance = Double.MAX_VALUE;
        for (int[] pixel : cubeCenters) {
            double y = pixel[1];
            double distance = 2129 * Math.pow(y, -0.876); // Equation from
                                                          // camera calibration
            if (distance < closestDistance) {
                closestDistance = distance;
                closestCenter = pixel;
            }
        }
        return closestCenter;
    }

    /**
     * @param center
     *            of closest Cube
     * @return the heading, in degrees, of the cube
     */
    public double getClosestCubeHeading(int[] center) {
        double x = center[0];
        double halfWidth = width * .5;
        double heading = Math.atan((x - halfWidth) / focalLength);
        return heading;
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
