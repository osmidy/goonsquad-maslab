package computervision;

/**
 * An interface providing methods for retrieving and processing data received
 * from physical sensors.
 * 
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
     * @return the pin number for input to this Sensor
     */
    public int getInputPin();

    /**
     * @return the pin number for output from this Sensor
     */
    public int getOutputPin();
    
    /**
     * @return the type of interface used by the input pin of this Sensor
     */
    public String getInputInterface();
    
    /**
     * @return the type of interface used by the output pin of this Sensor
     */
    public String getOutputInterface();

}
