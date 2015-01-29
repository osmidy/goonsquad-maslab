package imageprocessing;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class ImageUtil {
    private static final int width = 160;
    private static final int height = 120;
    // Diagonal FOV is ~70; 56 horizontal, 42 vertical
    private static final double thetaX = 56;
    private static final double pixelsPerDegree = width / thetaX; // Horizontal
                                                                  // direction
    private static final double radiansToDegrees = 180 / Math.PI;
    private static final double radiansX = thetaX / radiansToDegrees;
    private static final double tanHalfX = Math.tan(thetaX / 2);
    private static final double focalLength = width * 0.5 / tanHalfX;
    private static List<int[]> redCenters = new ArrayList<int[]>();
    private static List<int[]> greenCenters = new ArrayList<int[]>();
    private static final Mat2Image rawImageConverter = new Mat2Image(BufferedImage.TYPE_3BYTE_BGR);
    private static final Mat2Image processedImageConverter = new Mat2Image(BufferedImage.TYPE_3BYTE_BGR);
    
    private ImageProcessor processor = new ImageProcessor();
    private Mat rawImage = new Mat();
    private Mat resizedImage = new Mat();
    private Mat processedImage = new Mat();
    private final VideoCapture camera = new VideoCapture();
    private final ObjectFinder finder = new ObjectFinder();
    
    private final Size imageSize = new Size(width, height);
    private final Size blurSize = new Size(5, 5);

    public static void main(String[] args) {
        ImageUtil ig = new ImageUtil();
        while (true) {ig.checkImage();}
    }
    
    public void checkImage() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load the OpenCV library
        camera.open(0);
        boolean guiOn = true;
        JLabel cameraPane = createWindow("Camera output", width, height, guiOn);
        JLabel opencvPane = createWindow("OpenCV output", width, height, guiOn);      


        // Set up structures for processing images
        
        // Scanner input = new Scanner(System.in);
        // String file = input.nextLine();

        long loopStart = System.currentTimeMillis();
        // Wait until the camera has a new frame
        for (int i = 0; i < 5; i++) {
            camera.grab();
        }
        camera.retrieve(rawImage);
        Imgproc.resize(rawImage, resizedImage, imageSize); // Halves resolution
        processor.process(resizedImage, processedImage, blurSize);
        finder.findCubes(processedImage);

        redCenters = this.setRedCenters(finder.getRedCubes());
        greenCenters = this.setGreenCenters(finder.getGreenCubes());

        
        for (int[] center : redCenters) {
            System.out.println("RED: " + Arrays.toString(center));
        }
        for (int[] x : greenCenters) {
            System.out.println("GREEN: " + Arrays.toString(x));
        }
        // Create GUI windows to display camera output and OpenCV output
        // Update the GUI windows
            if (guiOn) {
                updateWindow(cameraPane, resizedImage, rawImageConverter);
                updateWindow(opencvPane, processedImage, processedImageConverter);
            }
        long loopEnd = System.currentTimeMillis();
        System.out.println("Loop Time: " + ((loopEnd - loopStart)));
    }
    
    private synchronized List<int[]> setGreenCenters(List<int[]> greenCubes) {
        List<int[]> list = greenCubes;
        return list;
    }

    private synchronized List<int[]> setRedCenters(List<int[]> redCubes) {
        List<int[]> list = redCubes;
        return list;
    }

    public synchronized List<int[]> getRedCenters() {
        List<int[]> list = redCenters;
        return list;
    }
    
    public synchronized List<int[]> getGreenCenters() {
        List<int[]> list = greenCenters;
        return list;
    }

    /**
     * @param redCenters
     * @return Image representation of the cube
     */
    public synchronized ImageCube getClosestCube() {
        double closestDistance = Double.MAX_VALUE;
        int x = 0;
        int y = 0;
        double heading;
        for (int[] pixel : greenCenters) {
            y = pixel[1];
            double distance = 2129 * Math.pow(y, -0.876); // Equation from
                                                          // camera calibration
            if (distance < closestDistance) {
                closestDistance = distance;
                x = pixel[0];
                y = pixel[1];
            }
        }
        heading = getClosestCubeHeading(x);
        ImageCube cube = new ImageCube(x, y, closestDistance, heading);
        return new ImageCube(x, y, closestDistance, heading);
    }

    /**
     * @param x
     *            horizontal center coordinate of closest Cube
     * @return the heading, in degrees, of the cube
     */
    private synchronized double getClosestCubeHeading(int x) {
        double halfWidth = width * .5;
        double heading = Math.atan((x - halfWidth) / focalLength);
        return heading;
    }

    private static JLabel createWindow(String name, int width, int height, boolean guiOn) {
        JFrame imageFrame = new JFrame(name);
        imageFrame.setSize(width, height);
        imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel imagePane = new JLabel();
        imagePane.setLayout(new BorderLayout());
        imageFrame.setContentPane(imagePane);

        imageFrame.setVisible(guiOn);
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
