package sensors;

import imageprocessing.ImageUtil;

/**
 * A class providing methods for data retrieved from a physical camera. This
 * class provides implementations of the Sensor interface.
 * 
 * @author osmidy
 *
 */
public class CameraSensor implements Sensor {
    private final ImageUtil imageUtil;
    
    public CameraSensor(ImageUtil imageUtil) {
        this.imageUtil = imageUtil;
    }

    
    // Will give distance of closest object in image
    @Override
    public double distanceToObject() {
        // TODO Auto-generated method stub
        return imageUtil.getClosestCube().getDistance();
    }
}
