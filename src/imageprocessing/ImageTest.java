package imageprocessing;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;

public class ImageTest {
    public static void main(String[] args) {
        ImageReturn("C:/Users/George/Dropbox (MIT)/goonsquad-maslab/src/imageprocessing/test-images/Logitech Webcam/Picture 5.jpg");       
    }
    
    public static void ImageReturn(String filename) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Highgui.imread(filename); 
        Mat scaledImg = new Mat();
        int count = 0;
        Imgproc.resize(img,scaledImg ,new Size(320, 240)); 
        for (int row = 134 ; row < 178; row += 2) {
            for (int col = 0 ; col < 474; col += 2) {
                // if (Math.pow(row-221,2) + Math.pow(col-343, 2) < Math.pow(43, 2)) {
                // count++;
                System.out.println(scaledImg.get(row/2,col/2)[2]);
                // System.out.println("Count:" + count);
                // }
            }
        }
             
    }
} 