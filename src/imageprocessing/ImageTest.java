package imageprocessing;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;

public class ImageTest {
    public static void main(String[] args) {
        ImageReturn("C:/Users/George/Dropbox (MIT)/goonsquad-maslab/src/imageprocessing/test-images/test.jpg");       
    }
    
    public static void ImageReturn(String filename) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Highgui.imread(filename); 
        // Mat scaledImg = new Mat();
        int count = 0;
        // Imgproc.resize(img,scaledImg ,new Size(320, 240)); 
        for (int row = 229 ; row < 260; row++) {
            for (int col = 396 ; col < 492; col++) {
                // if (!(((row > 304) && (row < 356)) && ((col > 630) && (col < 668)))) {
                count++;
                System.out.println(img.get(row,col)[2]);
                // System.out.println("Count:" + count);
                // }
            }
        }
             
    }
} 