package computervision;

/**
 * An interface providing methods for retrieving and processing data received
 * from physical sensors.
 * @author osmidy
 *
 */
public interface Sensor {
    
    /**
     * Finds the distance to the object detected by this Sensor.
     * 
     * @return the distance from this Sensor to the object in meters
     */
    public double distanceToObject();
    
    /**
     * Finds the distance to the closest object detected by this Sensor.
     * 
     * @return the distance from this Sensor to the closest object in meters
     */
    public double distanceToClosestObject();
}
