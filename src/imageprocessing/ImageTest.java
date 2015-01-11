package imageprocessing;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class ImageTest {
    public static void main(String[] args) {
        String file;
        File f = new File("/home/osmidy/Pictures/wall.jpg");
        if (f.exists()) {
            System.out.println("HERE!");
            file = f.getName();
            System.out.println(file);
        }
        ImageReturn("/home/osmidy/Pictures/wall.jpg");
    }
    
    public static void ImageReturn(String file) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat img = Highgui.imread(file);
        System.out.println(img.cols());
        System.out.println(img.rows());
        System.out.println(img.get(1, 1));
    }
}
