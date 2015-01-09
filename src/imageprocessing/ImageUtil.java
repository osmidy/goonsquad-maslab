package imageprocessing;

/**
 * An interface providing tools to gather information from the images of a
 * CameraSensor.
 * 
 * @author osmidy
 *
 */
public interface ImageUtil {

    /**
     * Determines the color of the pixel
     * 
     * @param pixel
     *            Pixel in image matrix captured by CameraSensor
     * @return the color of the specified pixel (Red, Green, or Blue)
     */
    public int getPixelColor();

}
