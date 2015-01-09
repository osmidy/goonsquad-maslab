package computervision;

import fieldobject.FieldObject;

/**
 * A class providing methods for data retrieved from a physical Camera. This
 * class provides implementations of the Sensor interface.
 * 
 * @author osmidy
 *
 */
public class CameraSensor implements Sensor {
    // TODO:  use 320x240 resolutions
    // TODO:  perspective projection falls off like 1/r (vertically)

    @Override
    public double distanceToObject() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    /**
     * Determines the material type of a received image.
     * @param TBD, some type of image
     * @return a FieldObject matching the profile of the detected object
     */
    public FieldObject processMaterial() {
        return null;
        
    }

}
